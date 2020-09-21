package com.rekuchn.spell;

import com.rekuchn.creature.Creature;

public class Spell {

    private String name;
    private Action action;
    private int cost;
    private Creature owner;

    public Spell(String name, Action action, int cost, Creature owner) {
        this.name = name;
        this.action = action;
        this.cost = cost;
        this.owner = owner;
    }

    public void cast(){
        owner.mp -= cost();
    }

    public boolean canCast(){
        if(owner.mp >= cost()){
            return true;
        }
        return  false;
    }

    public String getNam(){
        if(name.length() > 9) {
            return name.substring(0, 9);
        }
        return name;
    }

    public int cost() {
        return cost;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Action getAction() { return action; }
    public void setAction(Action action) { this.action = action; }
    public int getCost() { return cost; }
    public void setCost(int cost) { this.cost = cost; }
}
