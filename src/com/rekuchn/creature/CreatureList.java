package com.rekuchn.creature;

import com.rekuchn.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.rekuchn.Global.level;

public class CreatureList {

    static CreatureString[] localMobs;

    public static CreatureString randomCreatureString(Random random){
        int r = random.nextInt(4);
        return localMobs[r];
    }


    public static void setRandomCreatureList(Random random){
        localMobs = new CreatureString[4];


        //if(true){ return new CreatureString("Ogre Mage", 4); }

        List<CreatureString>  mobs= new ArrayList<>();

        // _ _ _ _ _ summoner

        mobs.add(new CreatureString("Bat", 1));
        mobs.add(new CreatureString("Bat", 1));





        mobs.add(new CreatureString("Bat", 1)); // level 3
        mobs.add(new CreatureString("Rat Matron", 5));
        mobs.add(new CreatureString("Mole Man", 3));
        //mobs.add(new CreatureString("Rat", 2));
        mobs.add(new CreatureString("Fungus", 1));
        mobs.add(new CreatureString("Grey Ogre", 3));
        mobs.add(new CreatureString("Wolf", 2));

        mobs.add(new CreatureString("Lynx", 3));

        mobs.add(new CreatureString("Snake", 2));
        mobs.add(new CreatureString("Frog", 2));
        mobs.add(new CreatureString("Salamander", 3));
        mobs.add(new CreatureString("Slime", 3));
        mobs.add(new CreatureString("Fenrir", 2));
        mobs.add(new CreatureString("Goblin", 4));

        mobs.add(new CreatureString("Stalker", 5));
        mobs.add(new CreatureString("Stalker", 4));


        mobs.add(new CreatureString("Rat", 1));
        mobs.add(new CreatureString("Jelly", 4));
        mobs.add(new CreatureString("Gob Striker", 3));
        mobs.add(new CreatureString("Ogre", 4));
        mobs.add(new CreatureString("Ogre Mage", 5));
        mobs.add(new CreatureString("Wolf", 1));
        mobs.add(new CreatureString("Skirmisher", 4));
        mobs.add(new CreatureString("Gob Pyro", 5));



        for(int i=0; i<4; i++) {

            //int r = random.nextInt(6) + (level / 2);
            int r = random.nextInt(6) + level - 1;

            if (r < mobs.size()) {
                boolean ok = true;
                for(int j=0; j<i; j++){
                    if(localMobs[j] == mobs.get(r)){ ok = false; break; }
                }
                if(ok) {
                    localMobs[i] = mobs.get(r);
                } else {
                    i --;
                }
            } else {
                localMobs[i] = new CreatureString("Ogre Mage", 3);
            }
        }


    }

    public static CreatureString randomGoblin(Random random){
        int r = random.nextInt(4) + 1;

        if(r == 1){ return new CreatureString("Gob Striker", 4); }
        if(r == 2){ return new CreatureString("Gob Pyro", 5); }

        return new CreatureString("Goblin", 3);
    }

    public static CreatureString randomLongWolf(Random random){
        int r = random.nextInt(4) + 1;

        //if(r == 1){ return new CreatureString("Skirmisher", 4); }
        //if(r == 2){ return new CreatureString("Pyromage", 5); }

        return new CreatureString("Fenrir", 3);
    }

    public static String appropriateOgre(){
        if(level > 22){ return "Ogre Mage"; }
        if(level > 12){ return "Ogre"; }
        return "Grey Ogre";
    }

    public static String appropriateFish(){

        return "Carp";
    }







}
