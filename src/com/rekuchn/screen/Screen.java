package com.rekuchn.screen;

import com.rekuchn.AsciiPanel;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;



public interface Screen {

    public Screen getSubscreen();

    public void displayOutput(AsciiPanel terminal);

    public Screen respondToUserInput(KeyEvent key);
    public Screen respondToUserClick(MouseEvent e);
    public void screenTask();
}
