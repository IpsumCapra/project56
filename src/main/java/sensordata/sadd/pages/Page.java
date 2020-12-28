package main.java.sensordata.sadd.pages;

import java.awt.*;
import java.awt.event.ActionListener;

public abstract class Page implements ActionListener {
    private CardLayout cards;
    private Container parent;

    public Page(CardLayout cards, Container parent) {
        this.cards = cards;
        this.parent = parent;
    }
}
