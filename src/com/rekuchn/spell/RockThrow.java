package com.rekuchn.spell;


import com.rekuchn.AsciiPanel;
import com.rekuchn.Global;
import com.rekuchn.World;
import com.rekuchn.creature.Creature;
import com.rekuchn.model.ItemList;
import com.rekuchn.model.ObjectFactory;
import com.rekuchn.model.Effect;
import com.rekuchn.service.Cords;

public class RockThrow implements Action {

    @Override
    public void resolve(Creature creature, int x, int y) {
        World world = creature.world();

        int lastX = creature.x; int lastY = creature.y;

        for(Cords p : world.getLineBetween(creature.x, creature.y, x, y)){
            if(p.x == creature.x && p.y == creature.y){ continue; }
            world.effects().add(new Effect(p.x, p.y, AsciiPanel.brightBlack, AsciiPanel.black, 0, 0, 0, 0, 4));
            lastX = p.x; lastY = p.y;
            if(creature.world().creature(p.x, p.y) != null){
                creature.attack(world.creature(p.x, p.y), "Unarmed");
                world.effects().add(new Effect(p.x, p.y, AsciiPanel.brightWhite, AsciiPanel.red, 0, 0, 0, 0, 4, 1));
            }
        }

        ObjectFactory cf = new ObjectFactory(world);

        cf.newItemAt(ItemList.get("Rock"), lastX, lastY);

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
