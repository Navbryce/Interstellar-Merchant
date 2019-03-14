package com.cs2340.interstellarmerchant.model.universe.planet

import com.cs2340.interstellarmerchant.model.universe.events.planet_events.PlanetEventType
import com.cs2340.interstellarmerchant.model.universe.market.Economy
import com.cs2340.interstellarmerchant.model.universe.market.items.Item
import com.cs2340.interstellarmerchant.model.universe.market.items.Order
import com.cs2340.interstellarmerchant.model.universe.market.items.OrderStatus
import com.cs2340.interstellarmerchant.model.universe.planet_attributes.Resource
import com.cs2340.interstellarmerchant.model.universe.planet_attributes.Tech
import com.cs2340.interstellarmerchant.utilities.logd
import java.io.Serializable
import java.util.*
import java.util.stream.Collectors
import kotlin.math.roundToInt

/**
 * Used to determine various values for the planet's market based on attributes of the planet
 * @param planet - the host planet
 */
class PlanetEconomy(private val tech: Tech, private val resource: Resource,
                    private val currentEventTypes: Set<PlanetEventType>): Economy, Serializable {
    override fun canBuyItems(order: Order?): OrderStatus {
        var output: OrderStatus
        if (order!!.minSellTech!! > this.tech) {
            output = OrderStatus.NOT_ENOUGH_TECH
            logd("not enough tech to sell")

        } else {
            output = OrderStatus.SUCCESS
        }
        return output
    }

    /**
     * Whether the host economy can buy the economy. ONLY market should use (the class)
     */
    override fun canBuyItem(item: Item?, quantity: Int): OrderStatus {
        var orderStatus: OrderStatus
        if (item!!.sellTechLevel > this.tech) {
            orderStatus = OrderStatus.NOT_ENOUGH_TECH
        } else {
            orderStatus = OrderStatus.SUCCESS
        }
        return orderStatus
    }

    /**
     * ONLY market should use (the class)
     */
    override fun filterItems(potentialItems: List<Item>): MutableList<Item>? {
        val random = Random()
        val chanceInStore = 80
        return potentialItems
                .stream()
                .filter { item: Item ->
                    var output = item.produceTechLevel <= this.tech
                    output = output && random.nextInt(100) < chanceInStore

                    return@filter output
                }
                .collect(Collectors.toList())
    }

    /**
     * ONLY market should use (the class)
     */
    override fun calculateQuantity(item: Item?): Int {
        val random = Random()
        var factor: Int
        var signFactor: Int
        var minVariance: Int
        var maxVariance: Int
        if (item!!.idealTechLevel == this.tech) { // increase quantity if ideal tech level
            minVariance = 50
            maxVariance = 150
            signFactor = 1
        } else {
            signFactor = if (random.nextBoolean()) -1 else 1
            minVariance = 10
            maxVariance = 90
        }

        factor = random.nextInt(maxVariance - minVariance) + minVariance
        val netVariance = signFactor * ((factor.toDouble()/100.0) * item.baseQuantity).roundToInt()
        return item.baseQuantity + netVariance
    }

    /**
     * Used for calculating the price of items in its economy
     * Only market should use (the class)
     */
    override fun calculatePrice(item: Item?): Int {
        var price: Int
        if (item != null) {
            price = item.base

            // account for tech level difference
            price += (this.tech.ordinal - item.sellTechLevel.ordinal) * item.priceIncreasePerTech

            val random = Random()
            var factor: Int
            when {
                item.decreaseResource == this.resource -> {
                    // decrease the price of the item based on predefined allowed amount of variance
                    factor =
                            -1 * (random.nextInt(Planet.decreaseEventVar.second
                                    - Planet.decreaseEventVar.first)
                                    + Planet.decreaseEventVar.first)
                }
                currentEventTypes.contains(item.increaseEventType) -> {
                    // increase the price based on event
                    factor =
                            1 * (random.nextInt(Planet.increaseEventVar.second
                                    - Planet.increaseEventVar.first)
                                    + Planet.decreaseEventVar.first)
                } else -> {
                // determine if the variance increases the price
                factor = if (random.nextBoolean()) 1 else -1
                factor *= random.nextInt(item.variance)
            }
            }

            val netVariance = ((factor.toDouble()/100.0) * price).roundToInt()
            price += netVariance
        } else {
            price = -1
        }
        return price
    }

    override fun getEconomyName(): String {
        return "Economy"
    }
}