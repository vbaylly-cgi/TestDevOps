package com.cgi.poc.dw.db;

import com.cgi.poc.dw.core.User;
import java.util.List;
import java.util.Optional;
import liquibase.exception.DatabaseException;
import liquibase.exception.LiquibaseException;
import liquibase.exception.LockException;
import org.hibernate.context.internal.ManagedSessionContext;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;

import static org.junit.Assert.*;

/**
 * A class to test UserDAO class.
 *
 */
public class UserDAOTest extends DAOTest {

    /**
     * Class to perform CRUD database operations with users. System Under Test.
     */
    private UserDAO sut;

    /**
     * Initializations before each test method.
     *
     * @throws LiquibaseException if something is wrong with Liquibase.
     */
    @Before
    @Override
    public void setUp() throws LiquibaseException {
        liquibase.update("DEV");
        session = SESSION_FACTORY.openSession();
        sut = new UserDAO(SESSION_FACTORY);
        tx = null;
    }

    /**
     * Cleanup after each test method.
     *
     * @throws DatabaseException if there is an error with database access.
     * @throws LockException if two clients try to apply migrations
     * simultaneously.
     */
    @After
    @Override
    public void tearDown() throws DatabaseException, LockException {
        liquibase.dropAll();
    }

    /**
     * Test of findAll method, of class UserDAO.
     */
    @Test
    public void testFindAll() {
        List<User> users = null;
        try {
            ManagedSessionContext.bind(session);
            tx = session.beginTransaction();

            //Do something here with UserDAO
            users = sut.findAll();

            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw e;
        } finally {
            ManagedSessionContext.unbind(SESSION_FACTORY);
            session.close();
        }
        assertNotNull(users);
        assertFalse(users.isEmpty());

    }

    /**
     * Test of findByUsernameAndPassword method, of class UserDAO.
     */
    @Test
    public void testFindByUsernameAndPassword() {
        String expectedUsername = "user1";
        String expectedPassword = "pwd1";

        Optional<User> user;

        //First
        try {
            ManagedSessionContext.bind(session);
            tx = session.beginTransaction();

            //Do something here with UserDAO
            session
                    .createSQLQuery(
                            "insert into users "
                            + "values(null, :username, :password)"
                    )
                    .setString("username", expectedUsername)
                    .setString("password", expectedPassword)
                    .executeUpdate();

            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw e;
        } finally {
            ManagedSessionContext.unbind(SESSION_FACTORY);
            session.close();
        }

        //Reopen session
        session = SESSION_FACTORY.openSession();
        tx = null;

        //Second
        try {
            ManagedSessionContext.bind(session);
            tx = session.beginTransaction();

            //Do something here with UserDAO
            user = sut.findByUsernameAndPassword(
                    expectedUsername,
                    expectedPassword);

            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw e;
        } finally {
            ManagedSessionContext.unbind(SESSION_FACTORY);
            session.close();
        }

        Assert.assertNotNull(user);
        Assert.assertTrue(user.isPresent());
        Assert.assertEquals(expectedUsername,
                user.get().getUsername());
    }

    /**
     * Test of findById method, of class UserDAO.
     */
    @Test
    public void testFindById() {
        Optional<User> optional;
        try {
            ManagedSessionContext.bind(session);
            tx = session.beginTransaction();

            //Look for a user added by migrations
            optional = sut.findById(1);

            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw e;
        } finally {
            ManagedSessionContext.unbind(SESSION_FACTORY);
            session.close();
        }

        assertNotNull(optional);
        assertTrue(optional.isPresent());

    }

    /**
     * Test of findByUsername method, of class UserDAO.
     */
    @Test
    public void testFindByUsername() {
        String expectedUsername = "user1";
        String expectedPassword = "pwd1";

        Optional<User> user;

        //First
        try {
            ManagedSessionContext.bind(session);
            tx = session.beginTransaction();

            //Do something here with UserDAO
            session
                    .createSQLQuery(
                            "insert into users "
                            + "values(null, :username, :password)"
                    )
                    .setString("username", expectedUsername)
                    .setString("password", expectedPassword)
                    .executeUpdate();

            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw e;
        } finally {
            ManagedSessionContext.unbind(SESSION_FACTORY);
            session.close();
        }

        //Reopen session
        session = SESSION_FACTORY.openSession();
        tx = null;

        //Second
        try {
            ManagedSessionContext.bind(session);
            tx = session.beginTransaction();

            //Do something here with UserDAO
            user = sut.findByUsername(expectedUsername);

            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw e;
        } finally {
            ManagedSessionContext.unbind(SESSION_FACTORY);
            session.close();
        }

        Assert.assertNotNull(user);
        Assert.assertTrue(user.isPresent());
        Assert.assertEquals(expectedUsername,
                user.get().getUsername());
    }

}
