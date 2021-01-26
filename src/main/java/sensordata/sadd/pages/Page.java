package main.java.sensordata.sadd.pages;

import java.awt.*;
import java.awt.event.ActionListener;

public abstract class Page implements ActionListener {
    // Define variables.
    CardLayout cards;
    Container parent;

    // Constructor.
    public Page(CardLayout cards, Container parent) {
        this.cards = cards;
        this.parent = parent;
    }

    // Getters.
    public CardLayout getCards() {
        return cards;
    }

    public Container getParent() {
        return parent;
    }
}
