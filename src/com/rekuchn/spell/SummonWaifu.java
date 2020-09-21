package com.rekuchn.spell;


import com.rekuchn.World;
import com.rekuchn.creature.Creature;
import com.rekuchn.model.ObjectFactory;
import com.rekuchn.service.EffectManager;

public class SummonWaifu implements Action {


    public SummonWaifu() {
        super();
    }

    @Override
    public void resolve(Creature creature, int x, int y) {
        World world = creature.world();
        ObjectFactory cf = new ObjectFactory(creature.world());
        Creature c = cf.newWaifu();
        if(c == null){
            creature.doAction("call out to anyone");
            return;
        }

        boolean ok = true;

        if(world.putCreatureInRangeWide(c, creature.x, creature.y, 8) == null){
            if (world.putCreatureInRangeWide(c, creature.x, creature.y, 16) == null) {
                ok = false;
            }
        }

        if(ok) {
            EffectManager.splash(c.x, c.y, world);
            creature.doAction("call out to %s", c.name);
        } else {
            creature.doAction("call out to anyone at all");
        }
    }

    @Override
    public String aimType() { return "Self"; }
}
