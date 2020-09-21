package com.rekuchn.spell;


import com.rekuchn.AsciiPanel;
import com.rekuchn.creature.Creature;
import com.rekuchn.model.ObjectFactory;
import com.rekuchn.model.Status;

public class SummonMonster implements Action {

    String monster;
    int summonTime;
    int number;

    public SummonMonster(String monster) {
        super();
        this.monster = monster;
        this.summonTime = 200;
        this.number = 1;
    }

    public SummonMonster(String monster, int summonTime, int number) {
        super();
        this.monster = monster;
        this.summonTime = summonTime;
        this.number = number;
    }

    @Override
    public void resolve(Creature creature, int x, int y) {

        ObjectFactory cf = new ObjectFactory(creature.world());

        for(int i=0; i<number; i++) {
            Creature spawn = cf.newCreatureAt(monster, creature.x, creature.y);
            spawn.hostile = creature.hostile;
            spawn.summonTime = this.summonTime;
        }
        if(number == 1) {
            creature.doAction("create a %s", monster);
        } else {
            creature.doAction("create some %ss", monster);
        }
    }

    @Override
    public String aimType() { return "Self"; }
}
