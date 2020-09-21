package com.rekuchn.creature;


import com.rekuchn.AsciiPanel;
import com.rekuchn.model.Effect;
import com.rekuchn.service.Cords;

public class SpitterAi extends CreatureAi {

    public SpitterAi(Creature creature) {
        super(creature);
    }

    public void onUpdate(){


        Creature c = adjacentFoe();
        if(c != null){
            int x = c.x - creature.x;
            int y = c.y - creature.y;
            creature.moveBy(x, y);
            return;
        }

        c = creature.clearShotAt(5);

        if(creature.hasTrait("Mute")){ c = null; }

        if(c != null && creature.world().random.nextBoolean()){

            for(Cords p : creature.world().getLineBetween(creature.x, creature.y, c.x, c.y)){
                if(p.x == creature.x && p.y == creature.y){ continue; }
                //if(p.x == c.x && p.y == c.y){ continue; }
                creature.world().effects().add(new Effect(p.x, p.y, AsciiPanel.brightYellow, AsciiPanel.brightRed, 0, 0, 0, 0, 4));
                creature.world().burnTile(p.x, p.y);
            }
            //world.effects().add(new Effect(c.x, c.y, AsciiPanel.black, AsciiPanel.brightRed, 0, 0, 0, 0, 4, 1));

            creature.attack(c, "Magic", "Fire");
        }



        wander();

    }

}
