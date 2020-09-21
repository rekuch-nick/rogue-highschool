package com.rekuchn.spell;


import com.rekuchn.creature.Creature;
import com.rekuchn.model.ObjectFactory;
import com.rekuchn.model.Tile;

public class CreateWebs implements Action {

    String monster;

    public CreateWebs() {
        super();
    }

    @Override
    public void resolve(Creature creature, int x, int y) {

        for(int xx=x-3; xx<=x+3; xx++){ for(int yy=y-3; yy<=y+3; yy++){

            if(xx < 1 || yy < 1 || xx >= creature.world().width() - 1 || yy >= creature.world().height() - 1 ){ continue; }
            if(creature.world().tile(xx, yy).canOverWrite()){
                creature.world().setTile(xx, yy, Tile.WEB);
            }
        }}
        //ObjectFactory cf = new ObjectFactory(creature.world());
        //Creature spawn = cf.newCreatureAt(monster, x, y);
        creature.doAction("create a spiderweb");
    }

    @Override
    public String aimType() { return "Single Target"; }
}
