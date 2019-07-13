package org.jpwh.model.simple;


import javax.persistence.Entity;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
public  class Item {


    private Long id;

    @NotNull
    @Size(
            min = 2,
            max = 255,
            message = "Name is required, maximum 255 characters."
    )
    private String name;

    @Future
    private Date auctionEnd;
    private Set<Bid> bids = new HashSet<>();


    public Item() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getAuctionEnd() {
        return auctionEnd;
    }

    public void setAuctionEnd(Date auctionEnd) {
        this.auctionEnd = auctionEnd;
    }

    public Set<Bid> getBids() {
        return bids;
    }

    public void addBid(Bid bid) {

        if (bid == null) {
            throw new NullPointerException("Can't add null bid");
        }
        if (bid.getItem() != null) {
            throw new IllegalStateException("Bid is already assigned to an item");
        }

        // Bi drectional relationship
        getBids().add(bid);
        bid.setItem(this);
    }

}


