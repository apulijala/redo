package org.jpwh.test.simple;

import org.jpwh.env.JPATest;
import org.jpwh.model.simple.Item;
import org.jpwh.model.simple.Item_;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.ManagedType;
import javax.persistence.metamodel.Metamodel;
import javax.persistence.metamodel.SingularAttribute;
import javax.transaction.UserTransaction;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.AssertJUnit.assertTrue;

public class AccessJPAMetamodel extends JPATest {

    @Override
    public void configurePersistenceUnit() throws Exception {
        configurePersistenceUnit("SimpleXMLCompletePU");
    }

    @Test
    public void accessDynamicMetamodel() {

        EntityManagerFactory entityManagerFactory =
                JPA.getEntityManagerFactory();
        Metamodel mm =
                entityManagerFactory.getMetamodel();

        Set<ManagedType<?>> managedTypes = mm.getManagedTypes();
        assertEquals(managedTypes.size(), 1);
        ManagedType<?> managedType =
                managedTypes.iterator().next();
        SingularAttribute nameAttribute =
                managedType.getSingularAttribute("name");

        assertEquals(nameAttribute.getJavaType(), String.class);
        assertEquals(nameAttribute.getPersistentAttributeType(),
                Attribute.PersistentAttributeType.BASIC);
        assertFalse(nameAttribute.isOptional());


        SingularAttribute auctionEndAttribute =
                managedType.getSingularAttribute("auctionEnd");
        assertEquals(auctionEndAttribute.getJavaType(), Date.class);
        assertEquals(auctionEndAttribute.getPersistentAttributeType(),
                Attribute.PersistentAttributeType.BASIC);
        assertTrue(auctionEndAttribute.isOptional());


    }


    @Test
    public void queryStaticMetamodel() throws Exception {

        UserTransaction tx = TM.getUserTransaction();
        try {

            tx.begin();

            EntityManager entityManager = JPA.createEntityManager();
            Item item1 = new Item();
            item1.setName("This is some item");
            item1.setAuctionEnd(new Date(System.currentTimeMillis() + 10000));
            entityManager.persist(item1);

            Item item2 = new Item();
            item2.setName("Another item");
            item2.setAuctionEnd(new Date(System.currentTimeMillis() + 10000));
            entityManager.persist(item2);


            tx.commit();
            entityManager.close();

            entityManager = JPA.createEntityManager();
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Item> criteriaQuery =
                    criteriaBuilder.createQuery(Item.class);
            Root<Item> fromItem =
                    criteriaQuery.from(Item.class);
            List<Item> items =
                    entityManager.createQuery(criteriaQuery).getResultList();
            assertEquals(items.size(), 2);

            // filter 1.
            Path<String> namePath = fromItem.get("name");

            criteriaQuery.where(
                    criteriaBuilder.like(namePath,
                            criteriaBuilder.parameter(String.class, "pattern"))
            );

            items = entityManager.createQuery(criteriaQuery)
                    .setParameter("pattern", "%some item%")
                    .getResultList();
            assertEquals(items.size(), 1);

            // Filter 2 using the name class.

            criteriaQuery.where(
                    criteriaBuilder.like(fromItem.get(Item_.name),
                            criteriaBuilder.parameter(String.class, "pattern"))

            );

            items = entityManager.createQuery(criteriaQuery)
                    .setParameter("pattern", "%some item%")
                    .getResultList();
            assertEquals(items.size(), 1);


        } finally {
            TM.rollback();
        }

    }

}