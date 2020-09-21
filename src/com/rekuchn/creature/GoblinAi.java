package com.rekuchn.creature;

public class GoblinAi extends CreatureAi {

    Creature player;
    int xMem;
    int yMem;
    boolean rememberPlayer;

    public GoblinAi(Creature creature) {
        super(creature);

        rememberPlayer = false;

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


        if(canSee(player.x, player.y)){
            rememberPlayer = true;
            xMem = player.x; yMem = player.y;
            pathTo(player.x, player.y);
        } else if (rememberPlayer) {
            pathTo(xMem, yMem);
            if(Math.abs(creature.x - xMem) + Math.abs(creature.y - yMem) <= 1) {
                rememberPlayer = false;
            }
        } else {
            wander();
        }


    }





}
