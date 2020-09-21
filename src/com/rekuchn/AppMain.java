package com.rekuchn;

import java.applet.Applet;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


import com.rekuchn.screen.Screen;
import com.rekuchn.screen.StartScreen;

public class AppMain extends Applet implements KeyListener {
    private static final long serialVersionUID = 2560000000000084198L;

    private AsciiPanel terminal;
    private Screen screen;

    public AppMain(){
        super();
        terminal = new AsciiPanel(141, 48);
        add(terminal);
        screen = new StartScreen(terminal);
        addKeyListener(this);
        repaint();


        ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
        exec.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                repaint();
            }
        }, 800, 800, TimeUnit.MILLISECONDS);
    }

    @Override
    public void init(){
        super.init();
        this.setSize(terminal.getWidth() + 20, terminal.getHeight() + 20);
    }

    @Override
    public void repaint(){
        terminal.clear();
        screen.displayOutput(terminal);
        super.repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        screen = screen.respondToUserInput(e);
        repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) { }

    @Override
    public void keyTyped(KeyEvent e) { }
}
