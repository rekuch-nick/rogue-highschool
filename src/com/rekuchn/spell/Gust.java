package com.rekuchn.spell;


import com.rekuchn.AsciiPanel;
import com.rekuchn.World;
import com.rekuchn.creature.Creature;
import com.rekuchn.model.Effect;
import com.rekuchn.service.Cords;
import com.rekuchn.service.Line;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Gust implements Action {

    @Override
    public void resolve(Creature creature, int x, int y) {
        World world = creature.world();


        System.out.println();

        for(Cords p : world.getLineBetween(creature.x, creature.y, x, y)){


            if(p.x == creature.x && p.y == creature.y){ continue; }
            world.effects().add(new Effect(p.x, p.y, AsciiPanel.brightYellow, AsciiPanel.grey, 6, 1));

            Creature c = world.creature(p.x, p.y);
            if(c != null){

                int tx = p.x; int ty = p.y;
                int dis = 5;

                int sx = creature.x;
                int sy = creature.y;

                c.stunTime ++;

                for(int i=0; i<dis; i++){
                    if(sx < tx){
                        world.effects().add(new Effect(c.x, c.y, AsciiPanel.brightYellow, AsciiPanel.brightGrey,  6, 1));
                        c.moveBy(1, 0, true); i++;

                    }
                    if(sx > tx){
                        world.effects().add(new Effect(c.x, c.y, AsciiPanel.brightYellow, AsciiPanel.brightGrey,  6, 1));
                        c.moveBy(-1, 0, true); i++;

                    }

                    if(sy < ty){
                        world.effects().add(new Effect(c.x, c.y, AsciiPanel.brightYellow, AsciiPanel.brightGrey,  6, 1));
                        c.moveBy(0, 1, true); i++;

                    }
                    if(sy > ty){
                        world.effects().add(new Effect(c.x, c.y, AsciiPanel.brightYellow, AsciiPanel.brightGrey,  6, 1));
                        c.moveBy(0, -1, true); i++;

                    }
                }
            }
        }

    }

    @Override
    public String aimType() { return "Line"; }
}






