package com.rekuchn.creature;

import com.rekuchn.service.Pathing;

public class LongHunterAi extends CreatureAi {

    Creature player;
    int trackCD;

    public LongHunterAi(Creature creature) {

        super(creature);
        player = null;
        for(Creature c : creature.world().creatures){
            if(c.isPlayer){ player = c; break; }
        }

        trackCD = creature.world().random.nextInt(10) + 1;
    }

    public void onUpdate(){

        if(canSee(player.x, player.y)){ trackCD = 0; }

        if(trackCD > 0){
            trackCD --;
            wander();
            return;
        }

        Creature c = adjacentFoe();
        if(c != null){
            int x = c.x - creature.x;
            int y = c.y - creature.y;
            creature.moveBy(x, y);
            return;
        }

        Pathing.avoidCreatures = false;
        if(Pathing.find(creature.x, creature.y, player.x, player.y, creature.world()) != null){
            pathTo(player.x, player.y);
        } else {
            wander();
            trackCD = creature.world().random.nextInt(15) + 3;
        }


    }





}
