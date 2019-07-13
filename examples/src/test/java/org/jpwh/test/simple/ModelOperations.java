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
        // Do it differently.
        Bid aBid = new Bid(anItem);

        assertEquals(anItem.getBids().size(), 1);
        assertTrue(anItem.getBids().contains(aBid));
        assertEquals(aBid.getItem(), anItem);

        Bid secondBid = new Bid();
        anItem.addBid(secondBid);
        assertEquals(anItem.getBids().size(), 2);
        assertTrue(anItem.getBids().contains(secondBid));
        assertEquals(secondBid.getItem(), anItem);


    }

    @Test
    public void validateItem() {

        Item anItem = new Item();
        anItem.setName("Cheating Guru");
        anItem.setAuctionEnd(new Date());

        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<Item>> violations
                = validator.validate(anItem);

        assertEquals(violations.size(), 1);
        ConstraintViolation<Item> constraintViolation =
                violations.iterator().next();

        String propertyName = constraintViolation.getPropertyPath().iterator().next().getName();

        assertEquals(propertyName, "auctionEnd");

        if (Locale.getDefault().getLanguage().equalsIgnoreCase("en")) {
            assertEquals(constraintViolation.getMessage(), "must be in the future");
        }


    }


}

