package com.rekuchn.screen;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


import com.rekuchn.AsciiPanel;
import com.rekuchn.Global;
import com.rekuchn.service.Draw;

import javax.imageio.ImageIO;
import javax.swing.*;

public class StartScreen implements Screen {

    public AsciiPanel terminal;
    public Screen subscreen;
    public Screen getSubscreen(){ return subscreen; }
    private boolean active;


    public StartScreen(AsciiPanel terminal) {
        this.active = true;
        this.terminal = terminal;

        //Global.animate = false;
    }

    public void displayOutput(AsciiPanel terminal) {
        if(!active){ return; }

        if(Global.animate) {
            Draw.html("title", terminal, 5, 3);
            terminal.writeCenter("rogue highschool", 1);
            terminal.writeCenter("-- press [enter] to start --", 40);



            Global.animate = false;
        }


    }

    public Screen respondToUserInput(KeyEvent key) {
        if(key.getKeyCode() == KeyEvent.VK_ENTER){
            Global.animate = true;
            active = false;
            return new PlayScreen(terminal, null);
        } else {
            if(key.getKeyCode() == KeyEvent.VK_UP){
                Global.seed ++;
                terminal.write("->" + Global.seed + "          ", 1, 38);
            }
            if(key.getKeyCode() == KeyEvent.VK_DOWN){
                Global.seed --;
                terminal.write("->" + Global.seed + "          ", 1, 38);
            }

            return this;
        }


        //return key.getKeyCode() == KeyEvent.VK_ENTER ? new PlayScreen(terminal) : this;
    }

    @Override
    public Screen respondToUserClick(MouseEvent e) {
        return this;
    }

    public void screenTask(){}
}


