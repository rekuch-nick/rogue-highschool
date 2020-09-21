package com.rekuchn.model;

import com.rekuchn.creature.Creature;

import java.awt.*;

public class Status {

    public String trait;
    public Color color;
    public int time;
    public boolean decayEachTurn;
    public boolean decayEachLevel;
    public Creature creature;

    public Status(String trait, Color color, int time, boolean decayEachTurn, boolean decayEachLevel, Creature creature) {
        this.trait = trait;
        this.color = color;
        this.time = time;
        this.decayEachTurn = decayEachTurn;
        this.decayEachLevel = decayEachLevel;
        this.creature = creature;
    }
}
