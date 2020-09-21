package com.rekuchn.creature;


import com.rekuchn.AsciiPanel;
import com.rekuchn.World;

import java.awt.*;

public enum Waifu {
    W001("Aimi", "001", "001", "001", "001", "001", "001", AsciiPanel.brightMagenta, 0),
    W002("Chihiro", "002", "002", "001", "001", "001", "001", AsciiPanel.brightMagenta, 1),
    W003("Emiko", "003", "003", "001", "001", "001", "001", AsciiPanel.brightMagenta, 2),
    W004("Fumiko", "004", "004", "001", "001", "001", "001", AsciiPanel.brightMagenta, 3),
    W005("Hachi", "005", "005", "001", "001", "001", "001", AsciiPanel.brightMagenta, 4),
    W006("Hitomi", "006", "006", "001", "001", "001", "001", AsciiPanel.brightMagenta, 5),
    W007("Kaida", "007", "007", "001", "001", "001", "001", AsciiPanel.brightMagenta, 6),
    W008("Kimiko", "008", "008", "001", "001", "001", "001", AsciiPanel.brightMagenta, 7),
    W009("Manami", "009", "009", "001", "001", "001", "001", AsciiPanel.brightMagenta, 8),
    W010("Natsumi", "010", "010", "001", "001", "001", "001", AsciiPanel.brightMagenta, 9),
    W011("Reiki", "011", "011", "001", "001", "001", "001", AsciiPanel.brightMagenta, 10),
    W012("Yumi", "012", "012", "001", "001", "001", "001", AsciiPanel.brightMagenta, 11);

    public static Waifu getWaifuByName(String name){
        if(name.equals("Aimi")){ return W001; }
        if(name.equals("Chihiro")){ return W002; }
        if(name.equals("Emiko")){ return W003; }
        if(name.equals("Fumiko")){ return W004; }
        if(name.equals("Hachi")){ return W005; }
        if(name.equals("Hitomi")){ return W006; }
        if(name.equals("Kaida")){ return W007; }
        if(name.equals("Kimiko")){ return W008; }
        if(name.equals("Manami")){ return W009; }
        if(name.equals("Natsumi")){ return W010; }
        if(name.equals("Reiki")){ return W011; }
        if(name.equals("Yumi")){ return W012; }
        return null;
    }


    Waifu(String name, String textFile, String image01, String image02, String image03, String image04, String image05, Color color, int id) {
        this.id = id;
        this.name = name;
        this.textFile = textFile;
        this.image01 = image01;
        this.image02 = image02;
        this.image03 = image03;
        this.image04 = image04;
        this.image05 = image05;
        this.color = color;
    }

    private String name;

    private String textFile;
    private String image01;
    private String image02;
    private String image03;
    private String image04;
    private String image05;
    private Color color;
    public int id;

    private String description;


    public static Waifu newWaifuName(World world){
        int tries = 0;
        boolean okay = false;
        Waifu waifu = null;
        while(!okay) {
            tries ++;
            waifu = randomWaifu(world);
            okay = true;
            for (Creature c : world.creatures) {
                if(c.isWaifu){
                    if(c.name == waifu.getName()){
                        okay = false; break;
                    }
                }
            }
            if(tries > 10000){ return null; }
        }

        return waifu;
    }


    private static Waifu randomWaifu(World world){
        int r = world.random.nextInt(Waifu.values().length);
        return Waifu.values()[r];
    }


    public String getName() {
        return name;
    }

    public String getTextFile() {
        return textFile;
    }

    public String getImage01() {
        return image01;
    }

    public String getImage02() {
        return image02;
    }

    public String getImage03() {
        return image03;
    }

    public String getImage04() {
        return image04;
    }

    public String getImage05() {
        return image05;
    }
}
