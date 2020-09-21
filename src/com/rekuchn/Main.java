package com.rekuchn;

import javax.swing.*;


import com.rekuchn.screen.*;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static com.rekuchn.Global.*;

public class Main extends JFrame implements KeyListener, MouseListener, MouseMotionListener {
    private static final long serialVersionUID = 1060623000000003738L;

    private AsciiPanel terminal;
    private Screen screen;

    private ScheduledFuture<?> scheduledFuture;


    public Main(){
        super();
        terminal = new AsciiPanel(141, 48);

        setLocation(0, 0);
        //terminal.setCharWidth(18); terminal.setCharHeight(32);
        Image icon = Toolkit.getDefaultToolkit().getImage("game_icon.png");
        setIconImage(icon);


        add(terminal);



        pack();






        screen = new StartScreen(terminal);
        addKeyListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);
        

        repaint();




        ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
        scheduledFuture = exec.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() { try {
                if (rePack) {
                    remove(terminal);
                    //setLocationRelativeTo(null);
                    add(terminal);
                    pack();
                    repaint();
                    rePack = false;
                }

                if (screen.getClass() == PlayScreen.class && screen.getSubscreen() == null) {
                    screen.screenTask();
                } else if (screen.getClass() == PlayScreen.class && screen.getSubscreen().getClass() == InventoryScreen.class) {
                    screen.getSubscreen().screenTask();
                }

                if(screen.getSubscreen() != null && screen.getSubscreen().getClass() == DialogueScreen.class){

                } else {

                    if (Global.animate) {
                        repaint();
                    } else {
                        updatePaint();
                    }
                }
                manualRedrawsInARow = 0;

                if(Global.firstFrame){
                    terminal.changeSettings(9, 16);
                    Global.firstFrame = false;
                }

            } catch (Exception e) {}
                if(Math.random() < .7){ terminal.forgetPaint(); }
            }
        }, 100, 100, TimeUnit.MILLISECONDS);

        //terminal.changeSettings(9, 16);
    }

    public void repaint(){
        terminal.clear();
        screen.displayOutput(terminal);
        super.repaint();
    }

    public void updatePaint(){
        screen.displayOutput(terminal);
        super.repaint();
    }

    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_M){
            System.out.println(scheduledFuture);

            if(scheduledFuture.isCancelled()){
                //scheduledFuture.
            }
        }

        screen = screen.respondToUserInput(e);
        //repaint();
        if(Global.animate){ repaint(); } else { updatePaint(); }
        manualRedrawsInARow ++;
    }

    public void keyReleased(KeyEvent e) { }

    public void keyTyped(KeyEvent e) { }

    public static void main(String[] args) {
        Main app = new Main();
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        app.setVisible(true);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        //System.out.println( xCursor + ", " + yCursor );
        screen = screen.respondToUserClick(e);
        //repaint();
        if(Global.animate){ repaint(); } else { updatePaint(); }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(screen.getClass() == PlayScreen.class){
            PlayScreen.mouseWalking = true;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(screen.getClass() == PlayScreen.class) {
            PlayScreen.mouseWalking = false;
            PlayScreen.canClickAttack = true;
            if(PlayScreen.mouseUpdate){
                screen = screen.respondToUserClick(e);
                //repaint();
                if(Global.animate){ repaint(); } else { updatePaint(); }
            }
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        xCursor = (e.getX() - 8) / terminal.getCharWidth();
        yCursor = (e.getY() - 30) / terminal.getCharHeight();

        if(xCursor > terminal.getWidthInCharacters() - 1){ xCursor = terminal.getWidthInCharacters() - 1; }
        if(yCursor > terminal.getHeightInCharacters() - 1){ yCursor = terminal.getHeightInCharacters() - 1; }
        if(xCursor < 0){ xCursor = 0; }
        if(yCursor < 0){ yCursor = 0; }


    }

    @Override
    public void mouseMoved(MouseEvent e) {
        //System.out.println(e.getX() + ", " + e.getY());
        //System.out.println(e.getLocationOnScreen());
        xCursor = (e.getX() - 8) / terminal.getCharWidth();
        yCursor = (e.getY() - 30) / terminal.getCharHeight();

        if(xCursor > terminal.getWidthInCharacters() - 1){ xCursor = terminal.getWidthInCharacters() - 1; }
        if(yCursor > 38){ yCursor = 38; }
        if(xCursor < 0){ xCursor = 0; }
        if(yCursor < 0){ yCursor = 0; }
    }
}
