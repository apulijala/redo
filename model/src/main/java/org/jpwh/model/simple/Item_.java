package org.jpwh.model.simple;

import javax.persistence.metamodel.SingularAttribute;
import java.util.Date;

public class Item_ {

    public static volatile SingularAttribute<Item, Long> id;
    public static volatile  SingularAttribute<Item, String> name;
    public static volatile  SingularAttribute<Item, Date> auctionEnd;
}
