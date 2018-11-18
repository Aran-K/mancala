package com.mancala.persistence.model;

import org.springframework.context.annotation.PropertySource;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Arrays;

/**
 * Created by aran on 15/07/17.
 */

@Entity
@PropertySource("classpath:application.properties")
public class Player {

    @Id
    private String name;
    private int homePit;
    private int[] pits;
    private boolean active;

    private Player(){}

    public Player(String name, int stoneCount, int boardLength, boolean active) {
        this.name = name;
        homePit = 0;
        this.active = active;
        pits = new int[boardLength];
        Arrays.fill(pits, stoneCount);
    }

    /*
    Standard getters
     */
    public int getHomePitCount() {
        return homePit;
    }

    public int[] getPits() {
        return pits;
    }

    public String getName() {
        return name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
    /*
            Game logic methods below
             */
    public int getPitCount(int pit) {
        return pits[pit];
    }

    /**
     * Sets pit to new count
     *
     * @param pit   Pit number 0-6
     * @param count New count of stones in pit
     */
    public void setPitCount(int pit, int count) {
        pits[pit] = count;
    }

    /**
     * Increments pit count by one
     *
     * @param pit
     */
    public void incrementPitCount(int pit) {
        pits[pit]++;
    }

    /**
     * Adds stones to home pit
     *
     * @param count number of stones to add
     */
    public void addStonesToStorePit(int count) {
        homePit += count;
    }
}
