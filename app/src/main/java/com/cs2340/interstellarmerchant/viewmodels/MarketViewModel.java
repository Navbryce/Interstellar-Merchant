package com.cs2340.interstellarmerchant.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;
import android.util.Log;

import com.cs2340.interstellarmerchant.model.player.Player;
import com.cs2340.interstellarmerchant.model.universe.market.Market;
import com.cs2340.interstellarmerchant.model.universe.market.items.Item;
import com.cs2340.interstellarmerchant.model.universe.market.items.Order;
import com.cs2340.interstellarmerchant.model.universe.planet.Planet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


import java.util.Set;

import static android.content.ContentValues.TAG;

public class MarketViewModel extends AndroidViewModel {

    private Planet planet;
    public Market market;

    //market items for buying
    public Map marketItems;
    public ArrayList<Item> buyItemArray;


    //cargo items for selling
    public Map shipItems;
    public ArrayList<Item> sellItemArray;



    public MarketViewModel(@NonNull Application application) {
        super(application);
        planet = (Planet) Player.getInstance().getCurrentLocation();
        market = planet.getMarket();

        marketItems = market.getInventoryClone();
        Set<Item> buyKeys = marketItems.keySet();
        buyItemArray = new ArrayList<>();
        for (Item key : buyKeys) {
            buyItemArray.add(key);
        }

        shipItems = Player.getInstance().getShip().getInventoryClone();
        Set<Item> sellKeys = shipItems.keySet();
        sellItemArray = new ArrayList<>();
        for (Item key : sellKeys) {
            sellItemArray.add(key);
        }

    }

    public String getMarketItem(int i) {
        return buyItemArray.get(i).toString();
    }

    public String getMarketItemPrice(int i) {
        Item item = buyItemArray.get(i);
        return "$" + market.getItemBuyPrice(item);
    }


    //gets the item to sell at index i from market
    public String getShipItem(int i) {
        return sellItemArray.get(i).toString();
    }

    //gets the selling price from market
    public String getShipItemSellPrice(int i) {
        Item item = sellItemArray.get(i);
        //return "$" + market.getItemSellPrice(item);
        return "yolo";

    }

    public void buyItem(int amount, int i) {
        Item item = buyItemArray.get(i);
        Log.d(TAG, "the view model buyItem method amount: " + amount);
        Map<Item, Integer> map = new HashMap<>();
        map.put(item, amount);
        market.buyItems(new Order(map), Player.getInstance());
    }

    public void sellItem(int amount, int i) {
        Item item = sellItemArray.get(i);
        Log.d(TAG, "the view model sell method amount: " + amount);
        Map<Item, Integer> map = new HashMap<>();
        map.put(item, amount);
        market.sellItems(new Order(map), Player.getInstance());
    }


}