package com.rekuchn.spell;


import com.rekuchn.AsciiPanel;
import com.rekuchn.World;
import com.rekuchn.creature.Creature;
import com.rekuchn.model.Effect;
import com.rekuchn.model.ItemList;
import com.rekuchn.model.ObjectFactory;
import com.rekuchn.service.Cords;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RangedAttack implements Action {

    List<String> tags;

    public RangedAttack() {
        this.tags = new ArrayList<>();
    }

    public RangedAttack(String ... t) {
        this.tags = new ArrayList<>();
        this.tags.addAll(Arrays.asList(t));
    }

    @Override
    public void resolve(Creature creature, int x, int y) {
        World world = creature.world();



        for(Cords p : world.getLineBetween(creature.x, creature.y, x, y)){
            if(p.x == creature.x && p.y == creature.y){ continue; }
            world.effects().add(new Effect(p.x, p.y, AsciiPanel.brightBlack, AsciiPanel.black, 0, 0, 0, 0, 4));

            if(creature.world().creature(p.x, p.y) != null){
                if(tags.size() == 0){
                    creature.attack(world.creature(p.x, p.y));
                } else if (tags.size() == 1){
                    creature.attack(world.creature(p.x, p.y), tags.get(0));
                } else if (tags.size() == 2){
                    creature.attack(world.creature(p.x, p.y), tags.get(0), tags.get(1));
                } else if (tags.size() >= 3){
                    creature.attack(world.creature(p.x, p.y), tags.get(0), tags.get(1), tags.get(2));
                }


                world.effects().add(new Effect(p.x, p.y, AsciiPanel.brightWhite, AsciiPanel.red, 0, 0, 0, 0, 4, 1));
            }
        }



    }

    @Override
    public String aimType() { return "Line"; }
}








/*
            item.action = (creature, aimType) -> {
                setWorld(creature.world());

                for(Cords p : world.getLineBetween(creature.x, creature.y, Global.xCursor, Global.yCursor)){
                    if(p.x == creature.x && p.y == creature.y){ continue; }
                    world.effects().add(new Effect(p.x, p.y, AsciiPanel.brightBlack, AsciiPanel.black, 0, 0, 0, 0, 4));
                    if(creature.world().creature(p.x, p.y) != null){
                        creature.unarmedAttack(world.creature(p.x, p.y));
                        world.effects().add(new Effect(p.x, p.y, AsciiPanel.brightWhite, AsciiPanel.red, 0, 0, 0, 0, 4, 1));
                    }
                }
                newItemAt(newItem("Rock"), Global.xCursor, Global.yCursor);
                creature.consumeItem(creature.inventory().get(creature.readyItem));

            };
            */
