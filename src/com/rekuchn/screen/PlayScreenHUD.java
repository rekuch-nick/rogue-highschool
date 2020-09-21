package com.rekuchn.screen;

import com.rekuchn.AsciiPanel;
import com.rekuchn.Global;
import com.rekuchn.World;
import com.rekuchn.creature.Creature;
import com.rekuchn.model.Message;
import com.rekuchn.model.Status;
import com.rekuchn.model.Tile;
import com.rekuchn.screen.PlayScreen.*;
import com.rekuchn.worldgen.WorldBuilder;

import java.awt.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import static com.rekuchn.Global.*;
import static com.rekuchn.Global.combatLog;
import static com.rekuchn.screen.PlayScreen.playerFlash;
import static com.rekuchn.screen.PowerScreen.getSpellString;

public class PlayScreenHUD {

    private static String mouseoverDescription = "";
    private static Color mouseoverColor = Color.white;

    public static void displayHUD(AsciiPanel terminal, Creature player, World world){

        String hpBar = "";
        for(int i=0; i<30; i++){
            if(((float)player.hp / (float)player.mhp()) >= (float)i / 30){
                hpBar += (char) 219;
            } else {
                hpBar += (char) 176;
            }
        }
        terminal.write(hpBar, 1, 40, AsciiPanel.red);
        terminal.write(("" + player.hp), 1, 40, AsciiPanel.brightWhite, AsciiPanel.red);

        String mpBar = "";
        for(int i=0; i<30; i++){
            if(((float)player.mp / (float)player.mmp()) >= (float)i / 30){
                mpBar += (char) 219;
            } else {
                mpBar += (char) 176;
            }
        }
        terminal.write(mpBar, 43, 40, AsciiPanel.blue);
        terminal.write(("" + player.mp), 43, 40, AsciiPanel.brightWhite, AsciiPanel.blue);

        for(int i=0; i<10; i++){
            if(player.hearts > i){
                terminal.write((char)3, 130 + i, 40, AsciiPanel.pink);
            } else {
                terminal.write((char)3, 130 + i, 40, AsciiPanel.darkGrey);
            }
        }

        String cashString = "" + player.cash;
        NumberFormat myFormat = NumberFormat.getInstance();
        myFormat.setGroupingUsed(true);
        cashString = (char)157 + " " + myFormat.format(player.cash);

        terminal.write(cashString, 80, 40, AsciiPanel.brightYellow);


        String[] spellString = getSpellString(player);
        for(int i=0; i<10; i++){
            if(player.spells[i] != null) {
                Color c = player.spells[i].canCast() ? AsciiPanel.brightWhite : Color.darkGray;
                terminal.write(spellString[i], 5 + (i * 13), 41, c);
            }
        }


        int xStatus = 1; int yStatus = 44;
        for(Status sta : player.status){
            String s = sta.trait;
            if(sta.time > 1){
                s += ":" + sta.time;
            }
            s += " ";

            if(xStatus + s.length() >= 140){ yStatus ++; xStatus = 1;}
            if(yStatus > 47){ break; }

            terminal.write(s, xStatus, yStatus, sta.color);
            xStatus += s.length();

        }



        String readyItemText = "";
        if(player.inventory().get(player.readyItem) != null){
            if(player.inventory().get(player.readyItem).action != null){
                readyItemText = "  ~:Use " + player.inventory().get(player.readyItem).name() + "  ";
            }
        }
        terminal.write(readyItemText, 90, 40, Color.pink);



        String actionHelpText = "";
        //actionHelpText += readyItemText;
        if(world.item(player.x, player.y) != null){
            actionHelpText += "SPACE:Pick up " + world.item(player.x, player.y).name() + "  ";
        } else {
            actionHelpText += "SPACE:Wait  ";
        }
        if(world.tile(player.x, player.y) == Tile.STAIRS){ actionHelpText += "X/ENTER: Goto next level  " + actionHelpText; }
        if(world.tile(player.x, player.y) == Tile.LEVER_UP){ actionHelpText += "X/ENTER: Pull the lever down  " + actionHelpText; }
        if(world.tile(player.x, player.y) == Tile.LEVER_DOWN){ actionHelpText += "X/ENTER: Pull the lever up  " + actionHelpText; }
        actionHelpText += "WASD/Arrows/Click:Move  E/I:Inventory  R/P:Powers  Q/Z/Shift:Change selected item  C: Hide/Show combat log";

        if(actionHelpText.length() > 139){
            actionHelpText = actionHelpText.substring(0, 139);
        }

        terminal.write(actionHelpText, 1, 47, AsciiPanel.white);








        if(Global.currentMessage != null){


            int space = 140 - Global.messageCD;
            //String mess = currentMessage.getText();


            terminal.write(Global.currentMessage.getText().substring(0, Math.min(space, Global.currentMessage.getText().length())), Global.messageCD, 0, Global.currentMessage.getColor());
        }


        lookAtTile(xCursor, yCursor, player, world);
        if(mouseoverDescription.length() < 140) {
            terminal.writeCenter(mouseoverDescription, 39, mouseoverColor);
        } else {
            terminal.writeCenter(mouseoverDescription.substring(0, 138), 39, mouseoverColor);
        }


        String stats = "Might: " + player.might() + "  ";
        stats += "Evade: " + player.evade() + "  ";
        stats += "Defend: " + player.defend() + "  ";
        if(stats.length() > 139){ stats = stats.substring(0, 139); }
        terminal.writeCenter(stats, 46);

    }

    private static void lookAtTile(int x, int y, Creature player, World world){
        if(x < 0 || y < 0 || x > 140 || y > 38){ return; }

        if(world.creature(x, y) != null && world.creature(x, y) != player && player.canSee(x, y)){
            Creature c = world.creature(x, y);

            mouseoverColor = c.color();

            if(c.isWaifu){
                mouseoverDescription = c.getDescription();
            } else {
                mouseoverDescription = c.name + " HP: " + c.hp;
                if(!c.hostile){
                    mouseoverDescription = "Ally " + mouseoverDescription;
                }
                for(Status s : world.creature(x, y).status) {
                    mouseoverDescription += " " + s.trait;
                    if(s.time > 1){ mouseoverDescription += ": " + s.time; }
                }
            }


            return;
        }

        if(world.item(x, y) != null && player.canSee(x, y)){
            mouseoverColor = world.item(x, y).color();
            mouseoverDescription = world.item(x, y).name();
            return;
        }

        if(debugMode){
            mouseoverColor = Color.orange;
            mouseoverDescription = "Room # " + WorldBuilder.roomNumbers[x][y];
            return;
        }

        if(world.memoryTiles()[x][y] == world.tile(x, y).glyph())
            mouseoverColor = world.tile(x, y).descriptionColor();
        mouseoverDescription = world.tile(x, y).description();
    }



    static void displayCombatLog(AsciiPanel terminal, Creature player){
        int xx = 2;
        if(player.x < 71){ xx = 89; }

        for(int x=xx; x<xx+50; x++){
            terminal.write((char)177, x, 1, AsciiPanel.magenta, AsciiPanel.black);
            for(int y=2; y<37; y++){
                char g = 219; Color c = AsciiPanel.black;
                if(x == xx || x == xx + 49){ g = 177; c = AsciiPanel.magenta; }
                terminal.write(g, x, y, c, AsciiPanel.black);
            }
            terminal.write((char)177, x, 37, AsciiPanel.magenta, AsciiPanel.black);
        }

        int messageNum = combatLog.size() - 1;
        for(int y=36; y>=2; y--){
            Message m = null;
            try {
                m = combatLog.get(messageNum);
            } catch (Exception e) {
                return;
            }

            String mm = m.getText();
            if(mm.length() + xx + 2 >= 140){ mm = mm.substring(0, 139 - (xx + 2)); }

            terminal.write(mm, xx + 2, y, m.getColor());
            messageNum --;
        }
    }
}
