package com.cs2340.interstellarmerchant.model.universe.events

interface Event {
    /**
     * Indicates whether the event should be destroyed or not from whatever is holding it.
     * In other words, has the event expired
     *
     * @return true if the event has expired. false otherwise
     */
    fun eventExpired(): Boolean
}