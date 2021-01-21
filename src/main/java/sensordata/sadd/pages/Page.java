package main.java.sensordata.sadd.pages;

import java.awt.*;
import java.awt.event.ActionListener;

public abstract class Page implements ActionListener {
    CardLayout cards;
    Container parent;

    public Page(CardLayout cards, Container parent) {
        this.cards = cards;
        this.parent = parent;
    }

    public CardLayout getCards() {
        return cards;
    }

    public Container getParent() {
        return parent;
    }
}
