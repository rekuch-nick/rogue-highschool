package com.rekuchn.service;

import com.rekuchn.AsciiPanel;
import com.rekuchn.creature.Creature;
import com.rekuchn.spell.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PowerManager {

    public static void update(Creature player){
        player.spellsKnown = new ArrayList<>();


        if(player.hasTrait("Earth Conjurer")){
            know(player, new Spell("Stoning", new RockThrow(), 5, player));
        }
        if(player.hasTrait("Thinker")){
            know(player, new Spell("Mind Reading", new GainTelepathy(), 10, player));
            know(player, new Spell("Warp", new RandomTeleport(), 3, player));
        }
        if(player.hasTrait("Berserker")){
            know(player, new Spell("Rage", new GainMight(), 5, player));
        }
        if(player.hasTrait("Arachnophilia")){
            know(player, new Spell("Web", new CreateWebs(), 7, player));
        }





        // spells from items

        if(player.hasTrait("Pyromage")){
            know(player, new Spell("Fire Bolt", new FireBolt(), 5, player));
        }
        if(player.hasTrait("Cast Fire Bolt")){
            know(player, new Spell("Fire Bolt", new FireBolt(), 5, player));
        }

        if(player.hasTrait("Cast Gust")){
            know(player, new Spell("Gust", new Gust(), 2, player));
        }

        if(player.hasTrait("Cast Stone Skin")){
            know(player, new Spell("Stone Skin", new GainStoneSkin(), 10, player));
        }

        if(player.hasTrait("Cast Radiate")){
            know(player, new Spell("Radiate", new Radiation(), 25, player));
        }




        cleanQuickBar(player);
    }



    private static void know(Creature player, Spell spell){
        for(Spell s : player.spellsKnown){
            if(s.getName().equals(spell.getName())){ return; }
        }
        player.spellsKnown.add(spell);

        if(emptySlotOnQuickBar(player) != -1){
            if(!hasOnQuickBar(player, spell.getName())){
                player.spells[emptySlotOnQuickBar(player)] = spell;
            }
        }
    }

    private static void cleanQuickBar(Creature player){
        for(int i=0; i<10; i++){
            if(player.spells[i] == null){ continue; }
            String spellName = player.spells[i].getName();
            boolean okay = false;
            for(Spell spell : player.spellsKnown){
                if(spellName.equals(spell.getName())){
                    okay = true;
                    break;
                }
            }
            if(!okay){
                player.spells[i] = null;
            }
        }
    }

    public static boolean hasOnQuickBar(Creature player, String name){
        for(int i=0; i<10; i++){
            if(player.spells[i] == null){ continue; }
            if(player.spells[i].getName().equals(name)){
                return true;
            }
        }
        return false;
    }

    public static int emptySlotOnQuickBar(Creature player){
        for(int i=1; i<10; i++){
            if(player.spells[i] == null){ return i; }
        }
        if(player.spells[0] == null){ return 0; }
        return -1;
    }

    public static String getDescription(String name){
        switch (name){
            case "Earth Conjurer":
                return "Attuned to the minerals of the deep, you can summon stones to throw.";
            case "Thinker":
                return "Hear the thoughts of others and be able to teleport to random locations.";
            case "Berserker":
                return "Have the ability to enter a rage, temporarily doubling your Might";
            case "Sports Hero":
                return "Years of practice throwing and catching balls gives you +10 to Evade";
            case "Heartthrob":
                return "Waifus can't resist you. Gain hearts more easily.";
            case "A+ in chemistry":
                return "Potions you use have a 50% chance to not be consumed.";
            case "Ring Master":
                return "You can wear up to ten rings instead of just two.";
            case "Arachnophilia":
                return "You can create and move freely across spiderwebs.";

        }

        return "...";
    }

    public static Color getColor(String name){
        switch(name) {
            case "Earth Conjurer": return AsciiPanel.brown;
            case "Thinker": return AsciiPanel.cyan;
            case "Berserker": return AsciiPanel.red;
            case "Sports Hero": return AsciiPanel.green;
            case "Heartthrob": return AsciiPanel.pink;
            case "A+ in chemistry": return AsciiPanel.brightMagenta;
            case "Ring Master": return AsciiPanel.brightYellow;
            case "Arachnophilia": return AsciiPanel.grey;
        }


        return AsciiPanel.brightWhite;
    }


    public static List<String> getRandomPowerList(Random random){
        List<String> list = new ArrayList<>();

        int tries = 0;
        while(tries < 1000000){
            tries ++;

            int r = random.nextInt(8) + 1;
            String s = "";
            if(r == 1){ s = "Earth Conjurer"; }
            if(r == 2){ s = "Thinker"; }
            if(r == 3){ s = "Berserker"; }
            if(r == 4){ s = "Sports Hero"; }
            if(r == 5){ s = "Heartthrob"; }
            if(r == 6){ s = "A+ in chemistry"; }
            if(r == 7){ s = "Ring Master"; }
            if(r == 8){ s = "Arachnophilia"; }


            if(!list.contains(s)){
                list.add(s);
            }

        }

        return list;
    }
}
