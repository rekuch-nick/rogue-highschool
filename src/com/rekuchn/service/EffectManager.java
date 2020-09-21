package com.rekuchn.service;

import com.rekuchn.AsciiPanel;
import com.rekuchn.World;
import com.rekuchn.model.Effect;

import java.awt.*;

public class EffectManager {



    public static void splash(int x, int y, World world){
        for(int xx=x-1; xx<=x+1; xx++){
            for(int yy=y-1; yy<=y+1; yy++){
                world.effects().add(new Effect(xx, yy, AsciiPanel.brightCyan, AsciiPanel.blue, 0, 0, 1, 1, 15, 3));
            }
        }
    }

    public static void slip(int x, int y, World world){
        world.effects().add(new Effect(x, y, AsciiPanel.black, AsciiPanel.yellow, 4));
    }

    public static void hurt(int x, int y, World world){
        world.effects().add(new Effect(x, y, AsciiPanel.brightYellow, AsciiPanel.red, 4));
    }

    public static void hilight(int x, int y, World world, Color c){
        world.effects().add(new Effect(x, y, AsciiPanel.black, c, 6));
    }

    public static void spark(int x, int y, World world){
        world.effects().add(new Effect(x, y, AsciiPanel.brightMagenta, AsciiPanel.brightYellow, 1, 0, 0, 0, 1000));
        world.effects().add(new Effect(x, y, AsciiPanel.brightMagenta, AsciiPanel.brightYellow, -1, 0, 0, 0, 1000));
        world.effects().add(new Effect(x, y, AsciiPanel.brightMagenta, AsciiPanel.brightYellow, 0, 1, 0, 0, 1000));
        world.effects().add(new Effect(x, y, AsciiPanel.brightMagenta, AsciiPanel.brightYellow, 0, -1, 0, 0, 1000));
    }
}
