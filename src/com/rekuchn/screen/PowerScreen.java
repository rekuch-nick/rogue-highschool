package com.rekuchn.screen;


import com.rekuchn.AsciiPanel;
import com.rekuchn.creature.Creature;
import com.rekuchn.model.Item;
import com.rekuchn.service.PowerManager;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import static com.rekuchn.Global.yCursor;
import static java.awt.event.KeyEvent.*;


public class PowerScreen implements Screen {

    public AsciiPanel terminal;
    public Screen subscreen;
    public Creature player;

    private String keyText;
    String infoText;
    private int cursor;

    public PowerScreen(AsciiPanel terminal, Creature player){
        this.terminal = terminal;
        this.player = player;



        keyText = "P:exit  X:examine  1/2/3/4/5/6/7/8/9:set key";
        infoText = "";
        cursor = 0;
    }

    @Override
    public void displayOutput(AsciiPanel terminal) {

        terminal.writeCenter("Powers", 0, AsciiPanel.brightGreen);

        int y = 4;

        for(String s : player.powers){
            Color col = PowerManager.getColor(s);
            String desc = PowerManager.getDescription(s);
            printText(s + ": " + desc, 5, 135, y, col);
            y = terminal.getCursorY() + 2;

        }


        String[] spellString = getSpellString(player);
        for(int i=0; i<10; i++){
            Color c = cursor == i ? AsciiPanel.brightYellow : AsciiPanel.white;
            terminal.write(spellString[i], 5 + (i * 13), 40, c);
        }

        terminal.writeCenter("P:exit  WASD/Arrows:set equipped skills", 44, AsciiPanel.white);
    }

    public static String[] getSpellString(Creature c){
        String[] spellString = new String[10];
        for(int i=0; i<10; i++){
            int num = i + 1;
            if(num > 9){ num = 0; }
            String s = i + ": ";

            if(c.spells[i] == null){
                s += "(none)";
            } else {
                s += c.spells[i].getNam();
            }
            spellString[i] = s;
        }

        return spellString;
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


    @Override
    public Screen getSubscreen() { return this.subscreen; }



    @Override
    public Screen respondToUserInput(KeyEvent key) {




        switch (key.getKeyCode()) {
            case KeyEvent.VK_ESCAPE:
            case KeyEvent.VK_P:
            case KeyEvent.VK_R:
                return null;
            case KeyEvent.VK_UP:
            case KeyEvent.VK_W:
                player.cycleSpell(cursor, -1);
                break;

            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_S:
                player.cycleSpell(cursor, 1);
                break;

            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_A:
                cursor --;
                if(cursor < 0){ cursor = 9; }
                break;

            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_D:
                cursor ++;
                if(cursor > 9){ cursor = 0; }
                break;

            case VK_1: cursor = 1; break;
            case VK_2: cursor = 2; break;
            case VK_3: cursor = 3; break;
            case VK_4: cursor = 4; break;
            case VK_5: cursor = 5; break;
            case VK_6: cursor = 6; break;
            case VK_7: cursor = 7; break;
            case VK_8: cursor = 8; break;
            case VK_9: cursor = 9; break;
            case VK_0: cursor = 0; break;

        }





        return this;
    }

    @Override
    public Screen respondToUserClick(MouseEvent e) {

        return this;
    }

    @Override
    public void screenTask() {
        //System.out.println(xCursor + ", " + yCursor);

        //if(selecting){
        //    if(yCursor >= 2 && yCursor <= 33){
        //        selected = yCursor - 2;
        //    }
        //}

        //System.out.println(Global.animate);
    }


}


