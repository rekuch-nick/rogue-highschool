package com.rekuchn;

import com.rekuchn.creature.Creature;
import com.rekuchn.model.Message;
import com.rekuchn.service.PowerManager;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Global {

    public static boolean animate = true;

    public static Queue<Message> messages = new LinkedList<>();
    public static int messageCD = 0;
    public static void addMessage(String s, Color c){
        messages.add(new Message(s, c));
    }
    public static void addMessage(String s){
        messages.add(new Message(s, Color.white));
    }
    public static Message currentMessage = null;
    public static Creature chat = null;
    public static int messageTickCD = 0;

    public static int xCursor = 0;
    public static int yCursor = 0;

    public static List<Message> combatLog = new ArrayList<>();
    public static boolean displayCombatLog = true;

    public static int seed = 101;
    public static int level = 0;

    public static boolean rePack = false;

    public static int manualRedrawsInARow = 0;

    public static boolean debugMode = false;

    public static int itemCursor = 0;

    public static List<String> powerList = null;

    public static boolean firstFrame = true;





    public static void reset(){
        messages = new LinkedList<>();
        messageCD = 0;
        currentMessage = null;
        chat = null;
        combatLog = new ArrayList<>();
        displayCombatLog = true;
        level = 0;
        powerList = null;
    }


}
