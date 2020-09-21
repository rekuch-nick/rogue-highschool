package com.rekuchn.spell;

import com.rekuchn.creature.Creature;

public interface Action {


    public void resolve(Creature creature, int x, int y);

    public String aimType();

}
