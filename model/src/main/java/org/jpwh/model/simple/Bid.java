package org.jpwh.model.simple;


public class Bid {

    protected Item item;

    public Bid() {

    }

    public Bid(Item item) {

        this.item = item;
        item.getBids().add(this);
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }
}