package com.rekuchn.creature;

import com.rekuchn.model.ObjectFactory;

public class JellyAi extends CreatureAi {

    Creature player;
    int lastHp;

    public JellyAi(Creature creature) {

        super(creature);
        player = null;
        for(Creature c : creature.world().creatures){
            if(c.isPlayer){ player = c; break; }
        }

        lastHp = creature.hp;

        creature.hasBlood = false;
        creature.hasMind = false;
        creature.hasEyes = false;
    }

    public void onUpdate(){



        int tries = 10000;
        int range = 1;
        if(lastHp > creature.hp && creature.hp / 2 > 1){
            tries = 0;
            creature.hp = creature.hp / 2;
            lastHp = creature.hp;
        }
        while(tries < 1000) {
            tries ++;
            if(tries > 100){ range = 2; }
            if(tries > 300){ range = 3; }
            if(tries > 600){ range = 4; }


            int x = creature.x + creature.world().random.nextInt((range * 2) + 1) - range;
            int y = creature.y + creature.world().random.nextInt((range * 2) + 1) - range;

            if(x < 1 || y < 1 || x >= creature.world().width() - 1 || y >= creature.world().height() - 1){ continue; }
            if(!creature.world().tile(x, y).canEnter()){ continue; }
            if(creature.world().creature(x, y) != null){ continue; }

            creature.doAction("split in two");

            Creature spawn = new ObjectFactory(creature.world()).newJelly(creature.hp, creature);
            spawn.x = x; spawn.y = y;
            break;

        }


        Creature c = adjacentFoe();
        if(c != null){
            int x = c.x - creature.x;
            int y = c.y - creature.y;
            creature.moveBy(x, y);
            return;
        }


        if(canSee(player.x, player.y)){
            pathTo(player.x, player.y);
        } else {
            wander();
        }


    }





}
