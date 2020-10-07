package com.rekuchn.model;

import java.awt.Color;
import com.rekuchn.AsciiPanel;
import com.rekuchn.creature.Creature;

public enum Tile {
    WATER((char)247, AsciiPanel.brightCyan),
    LAVA((char)247, AsciiPanel.brightYellow),
    SPIKE('^', AsciiPanel.brightWhite),
    LAVA_BLOCK((char)244, AsciiPanel.brightYellow),
    GROW('.', AsciiPanel.brightYellow),
    BUSH((char)5, AsciiPanel.green),
    FLOOR((char)250, AsciiPanel.yellow),
    GRASS((char)250, AsciiPanel.green),
    GRASS2(',', AsciiPanel.green),
    GRASS3(';', AsciiPanel.green),
    GRASS4('\'', AsciiPanel.green),
    LONG_GRASS('"', AsciiPanel.green),
    FUNGUS('"', AsciiPanel.yellow),
    STAIRS((char)240, AsciiPanel.brightYellow),
    START_FLOOR((char)250, AsciiPanel.yellow),
    WALL((char)177, AsciiPanel.yellow),
    DOOR((char)8, AsciiPanel.yellow),
    LOCKED_DOOR('+', AsciiPanel.black),
    OPEN_DOOR((char)250, AsciiPanel.yellow),
    EVENT_DOOR((char)236, AsciiPanel.black),
    NODE('.', Color.magenta),
    COUNTED_NODE('.', Color.magenta),
    FIRE_4('^', AsciiPanel.brightYellow),
    FIRE_3('^', AsciiPanel.brightYellow),
    FIRE_2('^', AsciiPanel.brightYellow),
    FIRE_1('^', AsciiPanel.brightYellow),


    GREASE((char)171, AsciiPanel.yellow),

    WALL_SWITCH('#', AsciiPanel.yellow),

    STATUE((char)12, AsciiPanel.black),
    SHRINE((char)241, AsciiPanel.brightCyan),
    USED_SHRINE((char)241, Color.darkGray),

    LEVER_UP((char)24, AsciiPanel.cyanMid),
    LEVER_DOWN((char)25, AsciiPanel.cyan),

    WEB((char)176, AsciiPanel.brightWhite),
    VINE((char)244, AsciiPanel.brightGreen),

    BLOCK((char)254, Color.gray),

    W1((char)186, AsciiPanel.white),
    W2((char)205, AsciiPanel.white),
    W3((char)186, AsciiPanel.white),
    W4((char)205, AsciiPanel.white),

    W12((char)200, AsciiPanel.white),
    W23((char)201, AsciiPanel.white),
    W34((char)187, AsciiPanel.white),
    W41((char)188, AsciiPanel.white),
    W13((char)186, AsciiPanel.white),
    W24((char)205, AsciiPanel.white),

    W123((char)204, AsciiPanel.white),
    W234((char)203, AsciiPanel.white),
    W341((char)185, AsciiPanel.white),
    W412((char)202, AsciiPanel.white),

    W1234((char)206, AsciiPanel.white),

    BRICK_WALL((char)178, AsciiPanel.white),



    BOUNDS('x', AsciiPanel.brightBlack);


    private char glyph;
    public char glyph() { return glyph; }

    private Color color;
    public Color color() { return color; }


    Tile(char glyph, Color color){
        this.glyph = glyph;
        this.color = color;
    }

    public boolean canSeeThrough(){
        if(isWall()){ return false; }
        if(this == BUSH){ return false; }
        if(this == LAVA_BLOCK){ return false; }
        if(this == VINE){ return false; }


        return true;
    }

    public boolean canEnter(Creature creature){
        if(creature == null){ return canEnter(); }
        if(creature.hasTrait("Flying")){
            if(this == WATER){ return true; }
            if(this == LAVA){ return true; }
            if(this == SPIKE){ return true; }
        }

        // TODO: put logic here for monsters willing to enter dangers spaces
        if(!creature.isPlayer){ // && !is_lava_immune
            if(this == LAVA){ return false; }
            if(this == FIRE_1){ return false; }
            if(this == FIRE_2){ return false; }
            if(this == FIRE_3){ return false; }
            if(this == FIRE_4){ return false; }
            if(this == SPIKE){ return false; }
        }

        return canEnter();
    }

    public boolean canEnter(){
        if(this == BOUNDS){ return false; }
        if(this == FLOOR){ return true; }
        if(this == WALL_SWITCH){ return true; }
        if(this == GRASS){ return true; }
        if(this == GRASS2){ return true; }
        if(this == GRASS3){ return true; }
        if(this == GRASS4){ return true; }
        if(this == LONG_GRASS){ return true; }
        if(this == FUNGUS){ return true; }
        if(this == WATER){ return true; }
        if(this == LAVA){ return true; }
        if(this == START_FLOOR){ return true; }
        if(this == OPEN_DOOR){ return true; }
        if(this == STAIRS){ return true; }
        if(this == NODE){ return true; }
        if(this == SHRINE){ return true; }
        if(this == USED_SHRINE){ return true; }
        if(this == WEB){ return true; }
        if(this == VINE){ return true; }
        if(this == FIRE_1){ return true; }
        if(this == FIRE_2){ return true; }
        if(this == FIRE_3){ return true; }
        if(this == FIRE_4){ return true; }
        if(this == LEVER_UP){ return true; }
        if(this == LEVER_DOWN){ return true; }
        if(this == SPIKE){ return true; }
        if(this == GREASE){ return true; }

        return false;
    }

    public boolean canHaveItem(){
        if(this == BOUNDS){ return false; }
        if(this == FLOOR){ return true; }
        if(this == GRASS){ return true; }
        if(this == GRASS2){ return true; }
        if(this == GRASS3){ return true; }
        if(this == GRASS4){ return true; }
        if(this == WATER){ return true; }
        if(this == OPEN_DOOR){ return true; }
        return false;
    }

    // used for fancy wall attachments
    public boolean isWall(){
        if(this == WALL){ return true; }
        if(this == W1){ return true; }
        if(this == W2){ return true; }
        if(this == W3){ return true; }
        if(this == W4){ return true; }
        if(this == W12){ return true; }
        if(this == W23){ return true; }
        if(this == W34){ return true; }
        if(this == W41){ return true; }
        if(this == W123){ return true; }
        if(this == W234){ return true; }
        if(this == W341){ return true; }
        if(this == W412){ return true; }
        if(this == W24){ return true; }
        if(this == W13){ return true; }
        if(this == W1234){ return true; }
        if(this == DOOR){ return true; }
        if(this == LOCKED_DOOR){ return true; }
        if(this == EVENT_DOOR){ return true; }
        if(this == BRICK_WALL){ return true; }

        return false;
    }

    public boolean canOverWrite(){
        if(this == FLOOR){ return true; }
        if(this == START_FLOOR){ return true; }
        if(this == GRASS){ return true; }
        if(this == GRASS2){ return true; }
        if(this == GRASS3){ return true; }
        if(this == GRASS4){ return true; }
        if(this == WEB){ return true; }
        if(this == VINE){ return true; }
        if(this == FIRE_1){ return true; }
        if(this == FIRE_2){ return true; }
        if(this == FIRE_3){ return true; }
        if(this == FIRE_4){ return true; }
        if(this == SPIKE){ return true; }
        if(this == GREASE){ return true; }
        if(this == OPEN_DOOR){ return true; }
        if(this == LONG_GRASS){ return true; }
        if(this == FUNGUS){ return true; }
        return false;
    }

    public boolean canBurn(){
        if(this == GRASS){ return true; }
        if(this == GRASS2){ return true; }
        if(this == GRASS3){ return true; }
        if(this == GRASS4){ return true; }
        if(this == WEB){ return true; }
        if(this == VINE){ return true; }
        if(this == GREASE){ return true; }
        if(this == LONG_GRASS){ return true; }
        if(this == FUNGUS){ return true; }
        return false;
    }

    public boolean isDoor(){
        if(this == DOOR){ return true; }
        if(this == LOCKED_DOOR){ return true; }
        if(this == EVENT_DOOR){ return true; }
        return false;
    }

    public String description() {
        //if(this == FLOOR){ return "You see the floor."; }
        if(this == WEB){ return "Spiderwebs have a 50% chance to prevent movement and reduce evade to zero."; }

        if(this == STATUE){ return "You see a statue."; }

        if(this == DOOR){ return "You see a door."; }
        if(this == LOCKED_DOOR){ return "You see a locked door."; }
        if(this == EVENT_DOOR){ return "It isn't clear what is keeping this door sealed."; }
        if(this == STAIRS){ return "These stairs lead to the next level."; }

        if(this == LAVA){ return "Touching this will cause a lot of fire damage."; }
        if(this == LAVA_BLOCK){ return "Lava will pour out of here until the level is filled."; }

        if(this == LEVER_UP){ return "The lever is up."; }
        if(this == LEVER_DOWN){ return "The lever is down."; }

        if(this == WALL_SWITCH){ return "You see a  pressure panel."; }

        if(this == SPIKE){ return "Stepping on spikes would hurt."; }
        if(this == GREASE){ return "Slippery grease might make you fall prone."; }

        if(this == LONG_GRASS){ return "Tall grass may contain bugs."; }
        if(this == FUNGUS){ return "Brittle mushrooms will crunch under your feet."; }

        return "";
    }

    public Color bgColor(){
        if(this == LOCKED_DOOR){ return AsciiPanel.yellow; }
        if(this == EVENT_DOOR){ return AsciiPanel.yellow; }
        if(this == STATUE){ return Color.darkGray; }
        if(this == LAVA_BLOCK){ return AsciiPanel.brightRed; }
        return AsciiPanel.black;
    }

    public Color descriptionColor() {

        return AsciiPanel.white;
    }


}
