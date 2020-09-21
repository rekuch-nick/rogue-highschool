package com.rekuchn.creature;

public class WaifuAi extends CreatureAi {

    public WaifuAi(Creature creature) {

        super(creature);
        creature.hostile = false;
        creature.passive = true;
        creature.isWaifu = true;


        //String name = "Yumi";
        //creature.name = name;

    }

    public void onUpdate(){


    }

    public String getDescription(String name) {
        return creature.name + " is dressed in her school uniform.";
    }

}
