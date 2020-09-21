package com.rekuchn.screen;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;


import com.rekuchn.AsciiPanel;
import com.rekuchn.Global;

public class WinScreen implements Screen {

    public AsciiPanel terminal;
    public Screen subscreen;
    public Screen getSubscreen(){ return subscreen; }

    public WinScreen(AsciiPanel terminal) {
        this.terminal = terminal;
        Global.reset();
    }

    public void displayOutput(AsciiPanel terminal) {
        terminal.write("You won.", 1, 1);
        terminal.writeCenter("-- press [enter] to restart --", 22);
    }

    public Screen respondToUserInput(KeyEvent key) {
        //return key.getKeyCode() == KeyEvent.VK_ENTER ? new StartScreen(terminal) : this;
        if(key.getKeyCode() == KeyEvent.VK_ENTER){

            return new StartScreen(terminal);
        } else {
            return this;
        }
    }

    @Override
    public Screen respondToUserClick(MouseEvent e) {
        return this;
    }

    public void screenTask(){}
}
