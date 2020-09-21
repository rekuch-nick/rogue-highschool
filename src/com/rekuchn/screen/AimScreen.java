package com.rekuchn.screen;

import com.rekuchn.AsciiPanel;
import com.rekuchn.Global;
import com.rekuchn.World;
import com.rekuchn.creature.Creature;
import com.rekuchn.model.Item;
import com.rekuchn.spell.Action;
import com.rekuchn.service.Cords;
import com.rekuchn.service.Line;
import com.rekuchn.spell.Spell;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import static com.rekuchn.Global.xCursor;
import static com.rekuchn.Global.yCursor;

public class AimScreen implements Screen {

    public AsciiPanel terminal;
    public Screen subscreen;
    public Creature player;
    public Action action;
    public String aimType;
    Color color;
    public World world;
    boolean fromItem;
    Spell spell;

    public AimScreen(AsciiPanel terminal, Creature player, Action action, World world, boolean fromItem) {
        this.terminal = terminal;
        this.player = player;
        this.action = action;
        this.aimType = action.aimType();
        this.world = world;
        this.fromItem = fromItem;
        this.spell = null;

        color = AsciiPanel.brightYellow;
    }

    public AimScreen(AsciiPanel terminal, Creature player, Action action, World world, Spell spell) {
        this.terminal = terminal;
        this.player = player;
        this.action = action;
        this.aimType = action.aimType();
        this.world = world;
        this.fromItem = false;
        this.spell = spell;

        color = AsciiPanel.brightYellow;
    }

    @Override
    public Screen getSubscreen() { return this.subscreen; }

    @Override
    public void displayOutput(AsciiPanel terminal) {


        color = color == AsciiPanel.brightYellow ? AsciiPanel.brightCyan : AsciiPanel.brightYellow;

        if(aimType.equals("Line")){
            Line line = new Line(player.x, player.y, xCursor, Global.yCursor);


            for(Cords c : line.getPoints()){

                if(world.lineBetween(player.x, player.y, c.x, c.y)) {
                    boolean onCre = false;
                    Creature cre = world.creature(c.x, c.y);
                    if(cre != null){ onCre = true; }
                    if(onCre) {
                        terminal.write(cre.glyph(), c.x, c.y, AsciiPanel.black, color);
                    } else {
                        terminal.write((char)176, c.x, c.y, color);
                    }
                }
            }
            //terminal.write("@", player.x, player.y, AsciiPanel.black, color);
        }

        if(aimType.equals("Self")){
            terminal.write("" + (char)189, player.x, player.y, AsciiPanel.black, color);

        }

        if(aimType.equals("Single Target")){
            color = color == AsciiPanel.brightYellow ? AsciiPanel.brightCyan : AsciiPanel.brightYellow;
            boolean onCre = false;
            Creature cre = world.creature(xCursor, yCursor);
            if(cre != null){ onCre = true; }
            if(onCre) {
                terminal.write(cre.glyph(), xCursor, yCursor, AsciiPanel.black, color);
            } else {
                terminal.write((char)176, xCursor, yCursor, color);
            }
        }




    }

    @Override
    public Screen respondToUserInput(KeyEvent key) {
        if(key.getKeyCode() == KeyEvent.VK_ESCAPE){
            return null;
        }
        if(key.getKeyCode() == KeyEvent.VK_ENTER){
            return fire();
        }

        return this;
    }

    @Override
    public Screen respondToUserClick(MouseEvent e) {
        return fire();
    }

    private Screen fire(){
        action.resolve(player, xCursor, Global.yCursor);

        if(fromItem) {
            Item item = player.inventory().get(player.readyItem);
            item.charges --;

            if(player.hasTrait("A+ in chemistry") && world.random.nextBoolean()){
                if(item.name().startsWith("Potion")){
                    item.charges ++;
                }
            }

            if(player.hasTrait("Potion Connoisseur") ){
                if(item.name().startsWith("Potion")){
                    player.mp = Math.min(player.mp + 2, player.mmp());
                }
            }


            if(item.charges < 1) {
                player.consumeItem(player.inventory().get(player.readyItem));
                player.readyItem(item);
            }
        }

        if(spell != null){
            spell.cast();
        }

        world.update();
        return null;
    }

    @Override
    public void screenTask() {

    }
}
