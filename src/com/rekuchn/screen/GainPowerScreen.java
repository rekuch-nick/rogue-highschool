package com.rekuchn.screen;

import com.rekuchn.AsciiPanel;
import com.rekuchn.Global;
import com.rekuchn.World;
import com.rekuchn.creature.Creature;
import com.rekuchn.model.Item;
import com.rekuchn.service.Cords;
import com.rekuchn.service.Line;
import com.rekuchn.service.PowerManager;
import com.rekuchn.spell.Action;
import com.rekuchn.spell.Spell;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import static java.awt.event.KeyEvent.*;

public class GainPowerScreen implements Screen {

    public AsciiPanel terminal;
    public Screen subscreen;
    public Creature player;

    public World world;

    private String[] choices;


    public GainPowerScreen(AsciiPanel terminal, Creature player, World world) {
        this.terminal = terminal;
        this.player = player;
        this.world = world;

        choices = new String[4];

        int n = 0;
        for(int i=0; i<4; i++){
            while(player.hasPower(Global.powerList.get(n))){ n ++; }
            choices[i] = Global.powerList.get(n);
            n ++;
        }

        //choices[0] = Global.powerList.get(0);
        //choices[1] = Global.powerList.get(1);
        //choices[2] = Global.powerList.get(2);
        //choices[3] = Global.powerList.get(3);

        Global.powerList.remove(choices[3]);
        Global.powerList.remove(choices[2]);
        Global.powerList.remove(choices[1]);
        Global.powerList.remove(choices[0]);

        Global.powerList.add(choices[0]);
        Global.powerList.add(choices[1]);
        Global.powerList.add(choices[2]);
        Global.powerList.add(choices[3]);

    }


    @Override
    public Screen getSubscreen() { return this.subscreen; }

    @Override
    public void displayOutput(AsciiPanel terminal) {

        for(int i=0; i<200; i++) {
            int x = world.random.nextInt(world.width() - 1);
            int y = world.random.nextInt(10);

            String s = world.random.nextBoolean() ? "+" : " ";

            terminal.write(s, x, y);
            terminal.write(s, x, 47 - y);
        }

        Color conCol = world.random.nextBoolean() ? AsciiPanel.brightWhite : AsciiPanel.brightCyan;

        terminal.writeCenter("  CONGRATULATIONS HERO: YOU MAY CHOOSE A NEW POWER  ", 5, conCol);



        int yy = 10;
        for(int i=0; i<4; i++){
            String ss = choices[i];
            Color col = PowerManager.getColor(ss);
            String desc = PowerManager.getDescription(ss);
            printText((i+1) + ":  " + ss + ": " + desc, 5, 135, yy, col);
            yy = terminal.getCursorY() + 2;

        }






    }

    @Override
    public Screen respondToUserInput(KeyEvent key) {
        if(key.getKeyCode() == VK_1){
            player.powers.add(choices[0]);
            PowerManager.update(player);
            return null;
        }
        if(key.getKeyCode() == VK_2){
            player.powers.add(choices[1]);
            PowerManager.update(player);
            return null;
        }
        if(key.getKeyCode() == VK_3){
            player.powers.add(choices[2]);
            PowerManager.update(player);
            return null;
        }
        if(key.getKeyCode() == VK_4){
            player.powers.add(choices[3]);
            PowerManager.update(player);
            return null;
        }


        return this;
    }

    @Override
    public Screen respondToUserClick(MouseEvent e) {

        return this;
    }


    @Override
    public void screenTask() {

    }

    private void printText(String s, int sx, int mx, int sy, Color c){
        String[] ss = s.split("\\s");
        int x = sx;
        int y = sy;
        for(String word : ss){
            int l = word.length();
            if(x + l >= mx){
                x = sx; y ++;
            }

            terminal.write(word, x, y, c);
            x += l + 1;

        }
    }
}
