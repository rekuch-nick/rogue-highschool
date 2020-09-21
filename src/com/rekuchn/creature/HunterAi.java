package com.rekuchn.creature;

import com.rekuchn.service.Pathing;

public class HunterAi extends CreatureAi {

    Creature player;

    public HunterAi(Creature creature) {

        super(creature);
        player = null;
        for(Creature c : creature.world().creatures){
            if(c.isPlayer){ player = c; break; }
        }
    }

    public void onUpdate(){

        Creature c = adjacentFoe();
        if(c != null){
            int x = c.x - creature.x;
            int y = c.y - creature.y;
            creature.moveBy(x, y);
            return;
        }

        for(Creature cre : creature.world().creatures){
            if(cre.hostile != creature.hostile){
                if(canSee(cre.x, cre.y)){
                    pathTo(cre.x, cre.y);
                    return;
                }
            }
        }


        if(canSee(player.x, player.y)){
            pathTo(player.x, player.y);
        } else {
            if(!creature.hostile && creature.world().random.nextBoolean()){
                pathTo(player.x, player.y);
                return;
            }

            wander();
        }


    }





}
