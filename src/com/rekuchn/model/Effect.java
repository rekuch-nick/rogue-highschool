package com.rekuchn.model;


import com.rekuchn.AsciiPanel;
import com.rekuchn.World;

import java.awt.*;

public class Effect {

    public int x, y;
    private Color color;
    private Color bgColor;
    private int sx, sy, moveCD, maxMoveCD;
    private int time;
    private int glyphSet;
    private char glyph;

    public Effect(int x, int y, Color color, Color bgColor, int time) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.bgColor = bgColor;
        this.sx = 0;
        this.sy = 0;
        this.moveCD = 0;
        this.maxMoveCD = 0;
        this.time = time;
        this.glyphSet = 0;
        this.glyph = '*';
    }

    public Effect(int x, int y, Color color, Color bgColor, int time, int glyphSet) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.bgColor = bgColor;
        this.sx = 0;
        this.sy = 0;
        this.moveCD = 0;
        this.maxMoveCD = 0;
        this.time = time;
        this.glyphSet = glyphSet;
        this.glyph = '*';
    }

    public Effect(int x, int y, Color color, Color bgColor, int sx, int sy, int moveCD, int maxMoveCD, int time, int glyphSet) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.bgColor = bgColor;
        this.sx = sx;
        this.sy = sy;
        this.moveCD = moveCD;
        this.maxMoveCD = maxMoveCD;
        this.time = time;
        this.glyphSet = glyphSet;
        this.glyph = '*';
    }

    public Effect(int x, int y, Color color, Color bgColor, int sx, int sy, int moveCD, int maxMoveCD, int time) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.bgColor = bgColor;
        this.sx = sx;
        this.sy = sy;
        this.moveCD = moveCD;
        this.maxMoveCD = maxMoveCD;
        this.time = time;
        this.glyphSet = 0;
        this.glyph = '*';
    }

    public void render(AsciiPanel terminal, World world, boolean onPlayer){

        if(sx != 0 || sy != 0){
            moveCD --;
            if(moveCD < 1){
                moveCD = maxMoveCD;
                x += sx;
                y += sy;
                if(x < 0 || y < 0 || x >= world.width() || y >= world.height()){ time= 0; }
            }
        }


        time --;
        if(time < 1){
            world.remove(this);
            return;
        }


        glyph = setGlyph();
        if(onPlayer){ glyph = (char)189; }

        terminal.write(glyph, x, y, color, bgColor);

    }

    private char setGlyph(){

        if(glyphSet == 1){
            switch(glyph){
                case '/': return '-';
                case '-': return '\\';
                case '\\': return '|';
                case '|': return '/';
            }
            return '/';
        }

        if(glyphSet == 3){
            switch(glyph){
                case '?': return (char)168;
                case (char)168: return '?';
            }
            return (char)168;
        }

        switch(glyph){
            case '*': return '!';
            case '!': return '#';
            case '#': return '$';
            case '$': return '%';
            case '%': return '^';
            case '^': return '&';
            case '&': return '*';
        }

        return '*';
    }
}
