package com.cs2340.interstellarmerchant.model.repository.save_state;

import android.util.Log;

import com.cs2340.interstellarmerchant.model.player.Player;
import com.cs2340.interstellarmerchant.model.universe.Universe;
import com.cs2340.interstellarmerchant.model.universe.time.TimeController;
import com.google.gson.Gson;

import java.util.Date;
import java.util.Random;

/**
 * Used to hold saves for the game
 */
public class SaveState {
    private static final int RANDOM_NUMBER_FOR_SAVE = 1000;
    /**
     * Auto generates a name for the save
     * @param player - the player
     * @param universe - the universe
     * @param timeController - the time controller
     * @return the SaveState object that holds the parameters--given an autogenerated name
     */
    public static SaveState createSaveWithNoName(Player player, Universe universe,
                                                 TimeController timeController) {
        Random rand = new Random();
        String name = "Save-" + rand.nextInt(RANDOM_NUMBER_FOR_SAVE);
        return new SaveState(player, universe, timeController, name);
    }

    /**
     * Creates a save from a json string
     * @param json - the json string
     * @return - the save within the json string
     */
    public static SaveState saveJSONFactory(String json) {
        SaveState output = null;
        Gson gson = new Gson();
        output = gson.fromJson(json, SaveState.class);

        return output;
    }

    private Date lastModified;
    private final Player player;
    private final String name;
    private final TimeController timeController;
    private final Universe universe;

    /**
     * Generates a SaveState
     * @param player - the player
     * @param universe - the universe
     * @param timeController - the time controller
     * @param name - the save's name
     */
    public SaveState(Player player, Universe universe, TimeController timeController, String name) {
        this.player = player;
        this.universe = universe;
        this.timeController = timeController;
        this.name = name;
        this.updateLastModified();
    }

    /**
     * getter for last modfied
     * @return the date it was last modified
     */
    public Date getLastModified() {
        return (Date) lastModified.clone();
    }

    /**
     * getter for player
     * @return the player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * getter for name
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * getter for time controller
     * @return the time controller
     */
    public TimeController getTimeController() {
        return timeController;
    }

    /**
     * Calls after deserialized on the deserialized interface objects
     */
    public void callAfterDeserialized() {
        universe.afterDeserialized();
        player.afterDeserializedSpecialized(universe);
        player.afterDeserialized();
    }

    /**
     * getter for the universe
     * @return the universe
     */
    public Universe getUniverse() {
        return universe;
    }

    /**
     * Gets the save state as a json string
     * @return the save state as a json string
     */
    public String getSerialization() {
        Gson gson = new Gson();
        return gson.toJson(this, SaveState.class);
    }

    /**
     * Updates the last modified lastModified with the current lastModified
     */
    private void updateLastModified() {
        this.lastModified = new Date();
    }



}