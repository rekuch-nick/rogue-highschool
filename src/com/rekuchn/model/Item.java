package com.rekuchn.model;

import com.rekuchn.AsciiPanel;
import com.rekuchn.spell.Action;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Item {

    private String name;
    private char glyph;
    private Color color;
    private String description;

    public int bonus;

    public Action action;
    public String aimType;

    public List<String> traits;

    public Tile connectedTile;

    private int slot;
    public int slot(){ return slot; }

    public int armor;
    public int armorCheck;

    public int charges;
    /*
    public Item(String name, char glyph, Color color, String description, int bonus) {
        this.name = name;
        this.glyph = glyph;
        this.color = color;
        this.description = description;
        this.slot = -1;
        this.traits = new ArrayList<>();
        this.action = null;
        this.aimType = "Line";
        connectedTile = null;
        charges = 1;
        this.bonus = bonus;
        armor = 0; armorCheck = 0;
    }
     */


    public Item(String name, char glyph, Color color, String description) {
        this.name = name;
        this.glyph = glyph;
        this.color = color;
        this.description = description;
        this.slot = -1;
        this.traits = new ArrayList<>();
        this.action = null;
        this.aimType = "Line";
        connectedTile = null;
        charges = 1;
        this.bonus = 0;
        armor = 0; armorCheck = 0;
    }

    public Item(String name, char glyph, Color color, String description, int slot, String ... traits) {
        this.name = name;
        this.glyph = glyph;
        this.color = color;
        this.description = description;

        this.slot = slot;
        this.bonus = 0;

        this.traits = new ArrayList<String>();
        for(String t : traits){
            this.traits.add(t);
        }

        this.action = null;
        this.aimType = "Line";
        connectedTile = null;
        charges = 1;
        armor = 0; armorCheck = 0;
    }

    public Item(String name, char glyph, Color color, int slot, int bonus, String ... traits) {
        this.name = name;
        this.glyph = glyph;
        this.color = color;
        this.description = "";

        this.slot = slot;
        this.bonus = bonus;

        this.traits = new ArrayList<String>();
        for(String t : traits){
            this.traits.add(t);
        }

        this.action = null;
        this.aimType = "Line";
        connectedTile = null;
        charges = 1;
        armor = 0; armorCheck = 0;
    }



    public String name() {
        String n = name;
        //prefix
        if(traits.contains("Light Armor")){ n = "Light " + n; }
        if(traits.contains("Hide Armor")){ n = "Hide " + n; }
        if(traits.contains("Chain Armor")){ n = "Chain " + n; }
        if(traits.contains("Mail Armor")){ n = "Mail " + n; }
        if(traits.contains("Splint Armor")){ n = "Splint " + n; }
        if(traits.contains("Plate Armor")){ n = "Plate " + n; }


        if( traits.contains("Light Armor") ||
                traits.contains("Hide Armor") ||
                traits.contains("Chain Armor") ||
                traits.contains("Mail Armor") ||
                traits.contains("Splint Armor") ||
                traits.contains("Plate Armor") ||
                traits.contains("Sword") ||
                traits.contains("Axe") ||
                traits.contains("Polearm") ||
                traits.contains("Mace") ||
                traits.contains("Rapid")){

            if(traits.contains("Speedy")){ n += " of Speed"; }
            if(traits.contains("Power")){ n += " of Power"; }
            if(traits.contains("Slowing")){ n += " of Sloth"; }


            if(traits.contains("Striking")){ n = "Powerful " + n; }
            if(traits.contains("Rending")){ n = "Rending " + n; }
            if(traits.contains("Poison Strike")){ n = "Poison " + n; }







        }





        //plus
        if(bonus > 0){ n = "+" + bonus + " " + n; }


        if(name.startsWith("Wand")){ n += " [" + charges + "]"; }

        return n;
    }
    public char glyph() { return glyph; }
    public Color color() { return color; }
    public String description(){ return description; }

    public void setColor(Color color) { this.color = color; }
}
