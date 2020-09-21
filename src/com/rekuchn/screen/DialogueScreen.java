package com.rekuchn.screen;


import com.rekuchn.AsciiPanel;
import com.rekuchn.Global;
import com.rekuchn.creature.Creature;
import com.rekuchn.creature.Waifu;
import com.rekuchn.service.Draw;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.List;

import static com.rekuchn.Global.xCursor;
import static com.rekuchn.Global.yCursor;

public class DialogueScreen implements Screen {

    public AsciiPanel terminal;
    public Screen subscreen;
    public Screen getSubscreen(){ return subscreen; }

    protected Creature player;
    private Creature other;
    private boolean go;

    private String path;
    private String[] lines;
    private boolean clearScreen;
    private boolean active;
    private int id;
    private int subPosition;

    boolean showGiftText;
    private List<String> plainText;
    private List<String> giftText;


    public DialogueScreen(AsciiPanel terminal, Creature player, Creature other){


        this.terminal = terminal;
        this.player = player;
        this.other = other;
        this.active = true;
        go = false;
        showGiftText = false;
        plainText = new ArrayList<>();
        giftText = new ArrayList<>();
        subPosition = 0;

        if(!player.waifuTalked[id]){ player.waifuPosition[id] ++; }
        player.waifuTalked[id] = true;

        //Global.animate = false;
        clearScreen = true;
        lines = new String[1000];
        lines = fetchLines();

        try {
            id = Waifu.getWaifuByName(other.name).id;
        } catch (NullPointerException e){
            System.out.println("Waifu not found by name!");
            id = 0;
        }



        //TODO: if position > [number] then fall back to [previous number]

    }

    private String[] fetchLines(){
        path = "001"; /////
        path = Waifu.getWaifuByName(other.name).getTextFile();
        path = "text/" + path + ".txt";

        String[] script = new String[1000];
        File girl = new File(path);
        Scanner myReader = null;
        try {
            myReader = new Scanner(girl);

            int i = 0;
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                script[i] = data;
                i ++;
            }
        } catch (Exception e){ } finally {
            if(myReader != null){ myReader.close(); }
        }




        String plainString = script[player.waifuPosition[id]];
        String [] plainArray = plainString.split("_");
        Collections.addAll(plainText, plainArray);



        String giftString = script[player.waifuAffection[id] + 100]; // + gift offset
        String[] giftArray = giftString.split("_");
        Collections.addAll(giftText, giftArray);

        return script;
    }




    public void displayOutput(AsciiPanel terminal){
        if(!active){ return; }

        terminal.write(other.name, 0, 0);


        String path = Waifu.getWaifuByName(other.name).getImage01();
        Draw.html(path, terminal, 0, 0);
        //Global.animate = false;


        //terminal.writeCenter("ghfdjskfgdhjsgfjhdksgfjkhsd", 1);
        //terminal.writeCenter("-- press [enter] to start --", 40);


        String s = selectLines(showGiftText);
        printText(s);


        String actionText = "Esc:Leave  Enter/1:OK";
        if(!player.waifuGifted[id] && player.hearts > 0){
            actionText += "  2:" + (char)3 + "Give Heart" + (char)3;
        }
        terminal.writeCenter(actionText, 47, Color.gray);

        clearScreen = false;
    }





    private String selectLines(boolean gift){
        String line = "";

        if(gift){
            return giftText.get(subPosition);
        } else {
            return plainText.get(subPosition);

        }

    }

    private void printText(String s){
        String[] ss = s.split("\\s");
        int x = 8;
        int y = 39;
        for(String word : ss){
            int l = word.length();
            if(x + l >= 140){
                x = 2; y ++;
            }

            terminal.write(word, x, y, AsciiPanel.brightWhite);
            x += l + 1;

        }
    }


    public Screen respondToUserInput(KeyEvent key) {


        switch (key.getKeyCode()){
            case KeyEvent.VK_ESCAPE:
                Global.animate = true; active = false; return null;


            case KeyEvent.VK_ENTER:
            case KeyEvent.VK_1:
                subPosition ++;
                if( (subPosition >= plainText.size() && !showGiftText) ||
                            (subPosition >= giftText.size() && showGiftText)){

                    Global.animate = true; active = false; return null;
                }
                break;

            case KeyEvent.VK_2:
                if(!player.waifuGifted[id] && player.hearts > 0) {
                    showGiftText = true;
                    subPosition = 0;
                    player.waifuGifted[id] = true;
                    player.waifuAffection[id]++;
                    player.hearts --;
                }



        }

        return this;



        //return key.getKeyCode() == KeyEvent.VK_ENTER ? new PlayScreen(terminal) : this;
    }

    @Override
    public Screen respondToUserClick(MouseEvent e) {
        return this;
    }






    @Override
    public void screenTask() {

    }
}


