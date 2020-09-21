package com.rekuchn.spell;

import com.rekuchn.World;
import com.rekuchn.creature.Creature;
import com.rekuchn.service.Pathing;

public class RandomTeleport implements Action {

    @Override
    public void resolve(Creature creature, int xx, int yy) {
            World world = creature.world();

            int tries = 0;
            do {
                tries ++;

                int x = world.random.nextInt(world.width());
                int y = world.random.nextInt(world.height());

                if(Math.abs(creature.x - x) + Math.abs(creature.y - y) < 10){ continue; }

                if(world.tile(x, y).canEnter() && world.creature(x, y) == null){
                    Pathing.avoidCreatures = false;
                    Pathing.ignoreDoors = true;

                    if(Pathing.find(creature.x, creature.y, x, y, world) != null){
                        creature.ai.onEnter(x, y, world.tile(x, y));
                        creature.doAction("blink to another spot");
                        return;
                    }


                }




            } while(tries < 1000);




    }

    @Override
    public String aimType() { return "Self"; }
}
