package org.jpwh.helloworld;

import org.jpwh.env.TransactionManagerTest;
import org.jpwh.model.helloworld.Message;
import org.testng.annotations.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.transaction.UserTransaction;
import java.util.List;

import static org.testng.Assert.assertEquals;

public class HelloWorldJPA extends TransactionManagerTest {

    @Test
    public void storeLoadMessage() throws Exception {

        EntityManagerFactory emf =
                Persistence.createEntityManagerFactory("HelloWorldCPU");

        try {
            {
               // UserTransaction tx = TM.getUserTransaction();
                // tx.begin();
                EntityManager em =
                        emf.createEntityManager();

                em.getTransaction().begin();

                Message message = new Message();
                message.setText("Datta Digambara");

                em.persist(message);

                // tx.commit();
                em.getTransaction().commit();


            }
            {
                // UserTransaction tx = TM.getUserTransaction();
                // tx.begin();

                EntityManager em =
                        emf.createEntityManager();
                em.getTransaction().begin();

                List<Message> messages =
                        em.createQuery("select m from Message m").getResultList();
                assertEquals(messages.size(), 1);
                assertEquals(messages.get(0).getText(), "Datta Digambara");

                messages.get(0).setText("Take me to your leader!");
                em.getTransaction().rollback();
              //  em.getTransaction().commit();

                // tx.commit();


            }

        } finally {
            //EntityManager em =
                    emf.createEntityManager();
           // em.getTransaction().rollback();
        }

    }
}
