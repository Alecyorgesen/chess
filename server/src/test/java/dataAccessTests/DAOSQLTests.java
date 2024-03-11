package dataAccessTests;

import dataAccess.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import passoffTests.testClasses.TestException;

import java.sql.Connection;

public class DAOSQLTests {

    @Test
    @BeforeEach
    public void beforeEach() {
        UserDAO userDAO = new UserSQLDAO();
        AuthDAO authDAO = new AuthSQLDAO();
        GameDAO gameDAO = new GameSQLDAO();
        try {
            userDAO.clear();
            authDAO.clear();
            gameDAO.clear();
        } catch (Exception exception) {
            throw new RuntimeException("Hey, make sure the clear database method works for each class");
        }
    }

    @Test
    @Order(1)
    public void testCreateUser() {
        try (var connection = DatabaseManager.getConnection()) {
            UserDAO userDAO = new UserSQLDAO();

        } catch (Exception ex) {
            throw new TestException("The Database broke");
        }
    }
}
