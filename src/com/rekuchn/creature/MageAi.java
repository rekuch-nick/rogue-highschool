package com.rekuchn.creature;


import com.rekuchn.AsciiPanel;
import com.rekuchn.model.Effect;
import com.rekuchn.service.Cords;
import com.rekuchn.spell.Spell;

import java.util.List;

public class MageAi extends CreatureAi {

    List<Spell> spells;
    Creature player;
    int castChance;

    public MageAi(Creature creature, List<Spell> spells, int castChance) {
        super(creature);
        this.spells = spells;
        player = creature.world().player();
        this.castChance = castChance;
    }

    public void onUpdate(){

        Spell spell = spells.get(creature.world().random.nextInt(spells.size()));
        Creature target = null;
        boolean canCast = creature.world().random.nextInt(100) < castChance;
        if(spell.cost() > creature.mp){ canCast = false; }
        if(canCast && spell.getAction().aimType().equals("Line")){
            target = creature.clearShotAt(creature.visionRadius);
            if(target == null){ canCast = false; }
        }

        if(canCast && spell.getAction().aimType().equals("Single Target")){
            target = creature.clearShotAt(creature.visionRadius);
            if(target == null){ canCast = false; }
        }

        if(canCast && !creature.canSee(creature.world().player().x, creature.world().player().y)){ canCast = false; }


        if(creature.hasTrait("Mute")){ canCast = false; }

        if(canCast){

            int xAim = target == null ? creature.x : target.x;
            int yAim = target == null ? creature.y : target.y;

            creature.mp -= spell.cost();
            if(spell.getName().equals("Needle")){
                creature.doAction(AsciiPanel.yellow, "throw a needle", spell.getName());
            } else if (spell.getName().equals("Rage")) {
                creature.doAction(AsciiPanel.yellow, "goe into a rage", spell.getName());
            } else {
                creature.doAction(AsciiPanel.yellow, "cast %s", spell.getName());
            }
            spell.getAction().resolve(creature, xAim, yAim);

            return;
        }

        Creature c = adjacentFoe();
        if(c != null){
            int x = c.x - creature.x;
            int y = c.y - creature.y;
            creature.moveBy(x, y);
            return;
        }

        if(creature.mp >= 10 && canSee(player.x, player.y)){
            backStep();
        }


        if(canSee(player.x, player.y)){
            pathTo(player.x, player.y);
        } else {
            wander();
        }





    }

}
