package com.rekuchn.spell;


import com.rekuchn.AsciiPanel;
import com.rekuchn.Global;
import com.rekuchn.World;
import com.rekuchn.creature.Creature;
import com.rekuchn.model.Effect;
import com.rekuchn.model.ItemList;
import com.rekuchn.model.ObjectFactory;
import com.rekuchn.service.Cords;

public class FireBolt implements Action {

    @Override
    public void resolve(Creature creature, int x, int y) {
        World world = creature.world();


        for(Cords p : world.getLineBetween(creature.x, creature.y, x, y)){
            if(p.x == creature.x && p.y == creature.y){ continue; }
            world.effects().add(new Effect(p.x, p.y, AsciiPanel.brightYellow, AsciiPanel.red, 0, 0, 0, 0, 6));
            world.burnTile(p.x, p.y);
            if(creature.world().creature(p.x, p.y) != null){
                creature.attack(world.creature(p.x, p.y), "Magic", "Fire", "Accurate");
                world.effects().add(new Effect(p.x, p.y, AsciiPanel.brightWhite, AsciiPanel.red, 0, 0, 0, 0, 6));
            }
        }





    }

    @Override
    public String aimType() { return "Line"; }
}






