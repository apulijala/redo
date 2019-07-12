package org.jpwh.model.simple;

import javax.persistence.Entity;

@Entity
public class Bid {

    protected Item item;

    public  Bid() {

    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Bid(Item item) {
        this.item = item;
        item.getBids().add(this);
    }
}
