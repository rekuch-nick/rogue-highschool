package com.rekuchn.screen;

import com.rekuchn.AsciiPanel;
import com.rekuchn.Global;
import com.rekuchn.World;
import com.rekuchn.creature.Creature;
import com.rekuchn.model.*;
import com.rekuchn.service.Cords;
import com.rekuchn.service.EffectManager;
import com.rekuchn.spell.Action;

import java.awt.event.KeyEvent;

import static com.rekuchn.Global.*;
import static com.rekuchn.Global.manualRedrawsInARow;

public class PlayScreenInput {


    public static Screen keyboardInput(KeyEvent key, PlayScreen screen, AsciiPanel terminal, Creature player, World world){


        boolean update = false;

        if(screen.subscreen != null){
            screen.subscreen = screen.subscreen.respondToUserInput(key);
        } else {
            animate = true;




            switch (key.getKeyCode()) {




                case KeyEvent.VK_BACK_SPACE:
                    //subscreen = new DialogueScreen(terminal, player, null);
                    debugMode = !debugMode;
                    if(debugMode){ player.hp = player.mhp(); player.mp = player.mmp(); }
                    world.effects().add(new Effect(player.x, player.y, AsciiPanel.brightYellow, AsciiPanel.black, 0, 0, 0, 0, 10, 1));
                    break;
                case KeyEvent.VK_HOME:
                    if(debugMode){
                        Cords c = world.findRandomTile(Tile.STAIRS);
                        player.x = c.x; player.y = c.y;
                        break;
                    }

                case KeyEvent.VK_F8:
                    //if(debugMode){ EffectManager.spark(player.x, player.y, world); }
                    break;

                case KeyEvent.VK_F9:
                    if(debugMode){ return new LoseScreen(terminal); }
                case KeyEvent.VK_ENTER:
                case KeyEvent.VK_X:
                    //return new WinScreen(terminal);
                    if(world.tile(player.x, player.y) == Tile.STAIRS){
                        return new PlayScreen(terminal, player);
                    }

                    if(world.tile(player.x, player.y) == Tile.LEVER_UP){
                        for(EventTrigger e : world.events){
                            if(e.trigger.equals("Pull Lever") && e.xPoint == player.x && e.yPoint == player.y){ e.resolve(world, player); e.trigger = "-"; }
                        }

                        world.setTile(player.x, player.y, Tile.LEVER_DOWN);
                        break;
                    }
                    if(world.tile(player.x, player.y) == Tile.LEVER_DOWN){

                        world.setTile(player.x, player.y, Tile.LEVER_UP);

                        break;
                    }


                    break;
                case KeyEvent.VK_LEFT:
                case KeyEvent.VK_A:
                    update = true;
                    player.moveBy(-1, 0);
                    break;
                case KeyEvent.VK_RIGHT:
                case KeyEvent.VK_D:
                    update = true;
                    player.moveBy(1, 0);
                    break;
                case KeyEvent.VK_UP:
                case KeyEvent.VK_W:
                    update = true;
                    player.moveBy(0, -1);
                    break;
                case KeyEvent.VK_DOWN:
                case KeyEvent.VK_S:
                    update = true;
                    player.moveBy(0, 1);
                    break;
                case KeyEvent.VK_BACK_QUOTE:
                    Item i = player.inventory().get(player.readyItem);
                    if(i == null || i.action == null){ break; }
                    Action u = player.useItem();
                    if( u == null) {
                        player.readyItem(player.readyItem);
                        update = true;
                    } else {
                        screen.subscreen = new AimScreen(terminal, player, u, world, true);
                    }
                    break;
                case KeyEvent.VK_L:
                    player.addStatus(new Status("Clairvoyance", AsciiPanel.brightCyan, 99, true, true, player));
                    break;
                case KeyEvent.VK_P:
                case KeyEvent.VK_R:
                    screen.subscreen = new PowerScreen(terminal, player);
                    //player.addStatus(new Status("X-Ray Vision", AsciiPanel.brightGreen, 99, true, true, player));
                    break;
                case KeyEvent.VK_SPACE:
                    if(world.item(player.x, player.y) != null){ player.pickup(); }
                    update = true;
                    break;
                case KeyEvent.VK_C:
                    displayCombatLog = !displayCombatLog;
                    break;
                case KeyEvent.VK_I:
                case KeyEvent.VK_E:
                    //player.detailInventory();
                    screen.subscreen = new InventoryScreen(terminal, player);
                    break;
                case KeyEvent.VK_SHIFT:
                case KeyEvent.VK_Q:
                    player.readyItem(player.readyItem);
                    break;
                case KeyEvent.VK_Z:
                    player.readyItemBack(player.readyItem);
                    break;
                case KeyEvent.VK_M:
                    System.out.println("Global.animate = " + animate);
                    System.out.println("xCursor = " + xCursor);
                    System.out.println("yCursor = " + yCursor);
                    System.out.println("Manual Redraw Count = " + manualRedrawsInARow);


                    break;
                case KeyEvent.VK_1: cast(1, player, terminal, world, screen); break;
                case KeyEvent.VK_2: cast(2, player, terminal, world, screen); break;
                case KeyEvent.VK_3: cast(3, player, terminal, world, screen); break;
                case KeyEvent.VK_4: cast(4, player, terminal, world, screen); break;
                case KeyEvent.VK_5: cast(5, player, terminal, world, screen); break;
                case KeyEvent.VK_6: cast(6, player, terminal, world, screen); break;
                case KeyEvent.VK_7: cast(7, player, terminal, world, screen); break;
                case KeyEvent.VK_8: cast(8, player, terminal, world, screen); break;
                case KeyEvent.VK_9: cast(9, player, terminal, world, screen); break;
                case KeyEvent.VK_0: cast(0, player, terminal, world, screen); break;

                case KeyEvent.VK_F1:
                    terminal.changeSettings(9, 16);
                    break;
                case KeyEvent.VK_F2:
                    terminal.changeSettings(11, 17);
                    break;
                case KeyEvent.VK_F3:
                    terminal.changeSettings(13, 20);
                    break;
                case KeyEvent.VK_F4:
                    terminal.fillScreen();
                    break;
                case KeyEvent.VK_F12:
                    rePack = true;
                    break;

            }
        }

        // begin chat if walked into waifu
        if(Global.chat != null && screen.subscreen == null){
            screen.subscreen = new DialogueScreen(terminal, player, Global.chat);
            Global.chat = null;
        }


        if(screen.subscreen == null && update){
            world.update();
            if(player.hp < 1){ return new LoseScreen(terminal); }
        }

        return screen;






    }

    private static void cast(int num, Creature player, AsciiPanel terminal, World world, PlayScreen screen){


        if(player.spells[num] != null && player.spells[num].canCast()){

            if(player.hasTrait("Mute")){
                player.doAction(AsciiPanel.darkPink, "cannot cast while mute");
                return;
            }

            screen.subscreen = new AimScreen(terminal, player, player.spells[num].getAction(), world, player.spells[num]);
        }
    }
}
