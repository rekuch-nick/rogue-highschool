package com.rekuchn.creature;


import com.rekuchn.AsciiPanel;
import com.rekuchn.spell.Spell;

import java.util.List;

public class BattleMageAi extends CreatureAi {

    List<Spell> spells;
    Creature player;
    int castChance;
    int xMem;
    int yMem;
    boolean rememberPlayer;

    public BattleMageAi(Creature creature, List<Spell> spells, int castChance) {
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
            creature.doAction(AsciiPanel.yellow, "cast %s", spell.getName());
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
