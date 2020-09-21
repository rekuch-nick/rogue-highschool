package com.rekuchn.screen;


import com.rekuchn.AsciiPanel;
import com.rekuchn.Global;
import com.rekuchn.creature.Creature;
import com.rekuchn.model.Item;
import com.rekuchn.service.PowerManager;


import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.rekuchn.Global.*;

public class InventoryScreen implements Screen {

    public AsciiPanel terminal;
    public Screen subscreen;
    public Creature player;

    private String commandText;
    private String keyText;
    private int action;
    int selected;
    String infoText;


    public InventoryScreen(AsciiPanel terminal, Creature player){
        this.terminal = terminal;
        this.player = player;



        keyText = "I:exit  WS/Arrows:move cursor  1:equip or un-equip  2:drop 3:sort";
        commandText = keyText;
        infoText = "";
    }

    @Override
    public void displayOutput(AsciiPanel terminal) {

        terminal.writeCenter("Inventory", 0, AsciiPanel.brightGreen);
        terminal.write("Equipped", 105,1, AsciiPanel.brightYellow);

        terminal.writeCenter("Might:" + player.might(), 3, AsciiPanel.white);
        terminal.writeCenter("Evade:" + player.evade(), 4, AsciiPanel.white);
        terminal.writeCenter("Defend:" + player.defend(), 5, AsciiPanel.white);


        int y = 6;
        for(Item i : player.worn){
            if(i == null){ continue; }
            for(String s : i.traits){
                if(s.equals("Sword")){ continue; }
                if(s.equals("Axe")){ continue; }
                if(s.equals("Polearm")){ continue; }
                if(s.equals("Mace")){ continue; }
                if(s.equals("Rapid")){ continue; }
                if(s.equals("Light Armor")){ continue; }
                if(s.equals("Hide Armor")){ continue; }
                if(s.equals("Ring Armor")){ continue; }
                if(s.equals("Mail Armor")){ continue; }
                if(s.equals("Splint Armor")){ continue; }
                if(s.equals("Plate Armor")){ continue; }
                terminal.writeCenter(s, y, AsciiPanel.white);
                y ++;
            }
        }





        // weapon, armor, helm, cape, belt, glove, boot, ring ...
        for(int i=0; i<18; i++){
            if(player.worn[i] != null){
                printText(player.worn[i].name(), 90, 140, 2 + i, player.worn[i].color());
            } else {
                if(i <= 8) {
                    printText("________________________________________", 90, 140, 2 + i, AsciiPanel.brightBlack);
                } else {
                    if(player.hasTrait("Ring Master")){
                        printText("________________________________________", 90, 140, 2 + i, AsciiPanel.brightBlack);
                    }
                    //printText("__________ __________ __________ __________ __________", 90, 140, 2 + i, AsciiPanel.brightBlack);
                }
            }
        }

        /*
        if(selecting){
            for(int i=0; i<player.inventory().getItems().length; i++){
                if(i == selected) {
                    terminal.write(">>>", 1, i + 2, AsciiPanel.brightYellow);
                    terminal.write("-----------------------------------------------------", 6, i + 2, AsciiPanel.yellow);
                    terminal.write("<<<", 60, i + 2, AsciiPanel.brightYellow);
                }
            }
        }
        */



        infoText = "";
        for(int i=0; i<player.inventory().getItems().length; i++){
            Color c = Color.gray;
            if(i == itemCursor){ c = AsciiPanel.brightYellow; }
            terminal.write("[                                     ]", 2, i + 2, c);
            //String s = "" + i + ":";
            String s = " ";
            //if(i < 10){ s = " " + s; }
            terminal.write(s, 4, i + 2, AsciiPanel.white);



            if(player.inventory().get(i) != null){
                Item item = player.inventory().get(i);

                String name = item.glyph() + " " + item.name();
                Color col = item.color();
                if(player.isWearing(item)){
                    name = "@" + name + "@";
                    col = Color.darkGray;
                }
                if(player.readyItem == i && item.action != null){
                    name = "" + name + " (Selected)";
                    col = Color.pink;
                }

                terminal.write(name, 8, i + 2, col);

                if(itemCursor == i){

                    infoText = item.name() + ": " + item.description();

                    if(item.traits.contains("Sword")){ infoText += "  The classic favored weapon of gentlemen everywhere. 20% less likely to miss.";}
                    if(item.traits.contains("Axe")){ infoText += "  Attacks all 8 spaces around you.";}
                    if(item.traits.contains("Polearm")){ infoText += "  Can attack a few spaces away.";}
                    if(item.traits.contains("Mace")){ infoText += "  So heavy that it ignores the target's defend score.";}
                    if(item.traits.contains("Rapid")){ infoText += "  Will sometimes attack twice.";}

                    if(item.traits.contains("Light Armor") || item.traits.contains("Hide Armor") || item.traits.contains("Chain Armor") ||
                            item.traits.contains("Mail Armor") || item.traits.contains("Splint Armor") || item.traits.contains("Plate Armor") ){
                        int arm = item.armor + item.bonus;
                        int acp = Math.max(0, item.armorCheck - item.bonus);
                        infoText += "  +" + arm + " Defend.";
                        if(acp > 0){
                            infoText += " Reduces your Evade by 5 for every point of Might fewer than " + acp +  ".";
                        }

                    }

                    //TODO: write trait stats

                    for(String tr : item.traits){
                        if(tr.equals("Sword")){ continue; }
                        if(tr.equals("Axe")){ continue; }
                        if(tr.equals("Polearm")){ continue; }
                        if(tr.equals("Mace")){ continue; }
                        if(tr.equals("Rapid")){ continue; }
                        if(tr.equals("Light Armor")){ continue; }
                        if(tr.equals("Hide Armor")){ continue; }
                        if(tr.equals("Ring Armor")){ continue; }
                        if(tr.equals("Mail Armor")){ continue; }
                        if(tr.equals("Splint Armor")){ continue; }
                        if(tr.equals("Plate Armor")){ continue; }
                        infoText += " {" + tr + "}";
                    }

                }
            }
        }

        printText(infoText, 2, 139, 34, AsciiPanel.white);

        terminal.writeCenter(commandText, 40, AsciiPanel.white);



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
        Item item = player.inventory().get(itemCursor);

            switch (key.getKeyCode()) {
                case KeyEvent.VK_ESCAPE:
                case KeyEvent.VK_I:
                case KeyEvent.VK_E:
                    return null;
                case KeyEvent.VK_UP:
                case KeyEvent.VK_W:
                    itemCursor --;
                    if(itemCursor < 0){ itemCursor = 29; }
                    break;
                case KeyEvent.VK_DOWN:
                case KeyEvent.VK_S:
                    itemCursor ++;
                    if(itemCursor > 29){ itemCursor = 0; }
                    break;

                case KeyEvent.VK_1:
                case KeyEvent.VK_ENTER:
                    if(item == null){ break; }
                    if(item.action != null){ player.readyItem = itemCursor; break; }
                    if(player.isWearing(item)){
                        player.takeOff(item);
                    } else {
                        player.wear(item);
                    }


                    break;
                case KeyEvent.VK_2:
                    if(item == null){ break; }
                    if(player.isWearing(item)){ break; }
                    player.drop(item);
                    break;

                case KeyEvent.VK_3:
                    int c = player.readyItem;
                    String red = player.inventory().get(c).name();
                    player.inventory().sort();
                    while (player.inventory().get(player.readyItem) == null || player.inventory().get(player.readyItem).name() != red){
                        player.readyItem(player.readyItem + 1);
                    }


                    break;

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
        //if(selecting){ if(yCursor >= 2 && yCursor <= 33){ selected = yCursor - 2; } }
        //System.out.println(Global.animate);
    }


}


