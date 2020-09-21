package com.rekuchn.spell;

import com.rekuchn.World;
import com.rekuchn.creature.Creature;

public class ScrambleMemory implements Action {


    @Override
    public void resolve(Creature creature, int x, int y) {
        World world = creature.world();

            for(int xx=0; xx<world.width(); xx++){
                for(int yy=0; yy<world.height(); yy++){
                    char c = world.memoryTiles()[xx][yy];
                    if(c != ' ' && !world.player().canSee(xx, yy)){
                        if(world.random.nextBoolean() || world.random.nextBoolean()){ continue; }
                        world.memoryTiles()[xx][yy] = (char)(world.random.nextInt(200) + 55);
                    }
                }
            }
            creature.doAction("drain some memory");
    }

    @Override
    public String aimType() { return "Self"; }
}
