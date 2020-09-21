package com.rekuchn.service;



import com.rekuchn.AsciiPanel;
import com.rekuchn.Global;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Draw {

    public static void html(String path, AsciiPanel terminal){
        html(path, terminal, 0, 0);
    }

    public static void html(String path, AsciiPanel terminal, int xStart, int yStart){

        path = "img/" + path + ".txt";
        int x = xStart;
        int y = yStart;
        int line = 0;

        boolean foundFirstLine = false;

        try{
            File girl = new File(path);
            Scanner myReader = new Scanner(girl);
            while (myReader.hasNextLine()) {
                line ++;
                String data = myReader.nextLine();

                if(!foundFirstLine){
                    foundFirstLine = Parse.htmlStart(data);
                }

                if(foundFirstLine){
                    do {

                        data = Parse.htmlColor(data);
                        if(data.equals("")){ continue; }
                        Color c = Color.decode(data.substring(0, 7));

                        data = Parse.htmlGlyph(data);
                        if(data.equals("")){ continue; }
                        String ch = data.substring(0, 1);

                        terminal.write(ch, x, y, c);
                        x ++;

                    } while(!data.equals(""));

                    x = xStart;
                    y ++;
                }

            }
            myReader.close();

        } catch (IOException e){ }

    }

    private void printChar(){

    }





}
