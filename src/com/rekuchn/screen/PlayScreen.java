package com.rekuchn.screen;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;


import com.rekuchn.AsciiPanel;
import com.rekuchn.Global;
import com.rekuchn.World;
import com.rekuchn.creature.Creature;
import com.rekuchn.model.ObjectFactory;
import com.rekuchn.model.*;
import com.rekuchn.service.Cords;
import com.rekuchn.service.Pathing;
import com.rekuchn.worldgen.WorldBuilder;
import com.rekuchn.spell.Action;

import java.util.ArrayList;
import java.util.List;

import static com.rekuchn.Global.*;
import static com.rekuchn.screen.PowerScreen.getSpellString;

public class PlayScreen implements Screen {
    private World world;
    private Creature player;
    private int screenWidth;
    private int screenHeight;
    public static boolean playerFlash;
    public AsciiPanel terminal;
    public static boolean mouseWalking, canClickAttack, mouseUpdate;
    public Screen subscreen;
    public Screen getSubscreen(){ return subscreen; }

    public PlayScreen(AsciiPanel terminal, Creature player){
        this.terminal = terminal;
        screenWidth = 141;
        screenHeight = 39;


        if(player == null) {
            world = WorldBuilder.create(70, 20);
            this.player = new ObjectFactory(world).newPlayer();
        } else {
            world = WorldBuilder.create(player.x, player.y);

            this.player = new ObjectFactory(world).newPlayer(player);
            player.status = new ArrayList<>();
            player.setWorld(world);
        }



        playerFlash = false;

        mouseWalking = false;
        canClickAttack = true;
        mouseUpdate = false;

        Global.animate = true;

        //Global.addMessage("The early warning bell is ringing.", Color.white);
        //Global.addMessage("That means you have just five minutes to get to class.", Color.white);
        //Global.addMessage("How embarrassing it would be to be late on the very first day!", Color.white);


        ObjectFactory objectFactory = new ObjectFactory(world);
        createCreatures(objectFactory);

    }

    public void screenTask(){

        if(world.levelUp){
            subscreen = new GainPowerScreen(terminal, player, world);
            world.levelUp = false;
            return;
        }

        playerFlash = !playerFlash;
        if(mouseWalking && yCursor < 39) {
            Pathing.avoidCreatures = true;
            List<Cords> path = Pathing.find(player.x, player.y, xCursor, yCursor, world);
            if(path == null){
                path = Pathing.find(player.x, player.y, xCursor, yCursor, world);
            }
            if (path != null && path.size() > 0) {
                player.moveBy(Cords.xDir(player.x, path.get(0).x), Cords.yDir(player.y, path.get(0).y));
                mouseUpdate = true;
                world.update();

                if(Global.chat != null){
                    subscreen = new DialogueScreen(terminal, player, Global.chat);
                    Global.chat = null;
                }

                canClickAttack = false;
            }
        }

        if(Global.currentMessage != null || !Global.messages.isEmpty()){
            if(playerFlash){ Global.messageCD -=2; }
            if(Global.messageCD < 1) {
                messageCD = 0;
                //if(currentMessage.getText().length() > 1){
                //    String s = currentMessage.getText().substring(0, currentMessage.getText().length() - 1);
                //    currentMessage.setText(s);
                //} else {


                if(currentMessage == null || currentMessage.getText().length() < 1){

                    Global.messageCD = 139;

                    Global.currentMessage = null;
                    if (!Global.messages.isEmpty()) {
                        Global.currentMessage = Global.messages.remove();
                    }
                } else { if(playerFlash){
                    currentMessage.setText(currentMessage.getText().substring(1));
                }}

            }
        }

    }

    public void createCreatures(ObjectFactory cf){
        for(int i=0; i<12; i++){
            player.waifuGifted[i] = false;
            player.waifuTalked[i] = false;
        }

        for(int x=0; x<world.width(); x++){
            for(int y=0; y<world.height(); y++){
                if(WorldBuilder.creatures[x][y] != null){ cf.newCreature(WorldBuilder.creatures[x][y], x, y); }

            }
        }

        player.doAction(AsciiPanel.darkGrey, "enter level " + level);
    }


    public void displayOutput(AsciiPanel terminal) {

        if(subscreen == null || subscreen.getClass() != DialogueScreen.class) {
            displayTiles(terminal);
        }

        if(subscreen != null){
            subscreen.displayOutput(terminal);
        } else {
            if(displayCombatLog){ PlayScreenHUD.displayCombatLog(terminal, player); }
        }

    }

    private void displayTiles(AsciiPanel terminal) {

        if(subscreen != null && (
                subscreen.getClass() == InventoryScreen.class ||
                subscreen.getClass() == PowerScreen.class ||
                subscreen.getClass() == GainPowerScreen.class ||
                subscreen.getClass() == DialogueScreen.class
                    )){ return; }

        // draw tiles
        for (int x = 0; x < screenWidth; x++){
            for (int y = 0; y < screenHeight; y++){

                Color bgc = pickBGC(x, y);
                char glyph = world.glyph(x, y);
                Color col = world.color(x, y);

                // items overwrite tiles
                if(world.item(x, y) != null){
                    glyph = world.item(x, y).glyph();
                    col = world.item(x, y).color();
                }

                if(player.canSee(x, y)){
                    world.memoryTiles()[x][y] = glyph;
                } else {
                    col = AsciiPanel.grey;
                }
                glyph = world.memoryTiles()[x][y];

                terminal.write(glyph, x, y, col, bgc);
            }
        }

        // draw creatures
        for(Creature c : world.creatures){
            if(!player.canSee(c.x, c.y)){ continue; }
            Color col = c.color();
            if(c == player){  col = playerFlash ? player.color() : AsciiPanel.brightCyan; }

            Color bgc = pickBGC(c.x, c.y);
            terminal.write(c.glyph(), c.x, c.y, col, bgc);
        }

        //draw effects
        List<Effect> tempList = new ArrayList<>();
        if(world.effects().size() > 0) { tempList = new ArrayList<>(world.effects()); }
        for(Effect e : tempList){
            e.render(terminal, world, (player.x == e.x && player.y == e.y));
        }

        PlayScreenHUD.displayHUD(terminal, player, world);
    }


    private Color pickBGC(int x, int y){
        Color c = AsciiPanel.black;
        if(player.canSee(x, y)) {
            c = world.tile(x, y).bgColor();

            if(world.tile(x, y) == Tile.WATER){
                int n = world.random.nextInt(150) + 100;
                c = new Color(10, 10, n);
            }
            if(world.tile(x, y) == Tile.LAVA || world.tile(x, y) == Tile.FIRE_4
                    || world.tile(x, y) == Tile.FIRE_3
                    || world.tile(x, y) == Tile.FIRE_2
                    || world.tile(x, y) == Tile.FIRE_1
            ){
                int n = world.random.nextInt(150) + 100;
                c = new Color(n, 10, 10);
            }
        }
        if(x == xCursor && y == yCursor && yCursor < 40){ c = AsciiPanel.white; }
        return c;
    }


    public Screen respondToUserInput(KeyEvent key) {
        return PlayScreenInput.keyboardInput(key, this, terminal, player, world);
    }


    @Override
    public Screen respondToUserClick(MouseEvent e) {
        if(subscreen != null){
            subscreen = subscreen.respondToUserClick(e);
        } else {
            if(player.hp < 1){ return new LoseScreen(terminal); }
        }
        return this;
    }


    //@Override
    public void mousePressed(MouseEvent e) {
        mouseWalking = true;
    }

    //@Override
    public void mouseReleased(MouseEvent e) {
        mouseWalking = false;
        canClickAttack = true;
    }




}
