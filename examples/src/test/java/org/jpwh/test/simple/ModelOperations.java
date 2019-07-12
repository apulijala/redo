package org.jpwh.test.simple;

import org.jpwh.model.simple.Bid;
import org.jpwh.model.simple.Item;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Date;
import java.util.Locale;
import java.util.Set;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class ModelOperations {

    @Test
    public void linkAndBidItem() {

        Item anItem = new Item();
        Bid aBid = new Bid();

        anItem.getBids().add(aBid);
        aBid.setItem(anItem);

        assertEquals(anItem.getBids().size(), 1);
        assertTrue(anItem.getBids().contains(aBid));
        assertEquals(aBid.getItem(), anItem);

        Bid secondBid = new Bid();
        anItem.addBid(secondBid);

        assertEquals(2, anItem.getBids().size());
        assertTrue(anItem.getBids().contains(secondBid));
        assertEquals(anItem, secondBid.getItem());
    }


    @Test
    public void validateItem() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Item item = new Item();
        item.setName("Some Item");
        item.setAuctionEnd(new Date());

        Set<ConstraintViolation<Item>> violations = validator.validate(item);
        assertEquals(1 , violations.size());
        ConstraintViolation<Item> violation = violations.iterator().next();
        String failedProertyName =
                violation.getPropertyPath().iterator().next().getName();
        assertEquals(failedProertyName, "auctionEnd");

        if (Locale.getDefault().getLanguage().equals("en")) {
            assertEquals(violation.getMessage(), "must be in the future");
        }

    }
}
