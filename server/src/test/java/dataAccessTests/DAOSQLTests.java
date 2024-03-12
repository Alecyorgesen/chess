package dataAccessTests;

import dataAccess.*;
import model.UserData;
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
    public void testCreateUserAndGetUser() {
        try (var connection = DatabaseManager.getConnection()) {
            UserDAO userDAO = new UserSQLDAO();
            UserData userData = new UserData("lDrac360l", "theBestPassword","eMALL");
            userDAO.createUser("lDrac360l", "theBestPassword","eMALL");
            UserData storedUserData = userDAO.getUser("lDrac360l");
            if (!(userData.password().equals(storedUserData.password())) && !(userData.email().equals(storedUserData.email()))) {
                throw new TestException("The password and email don't match for the user created and the user retrieved");
            }
        } catch (Exception ex) {
            throw new TestException(ex.getMessage());
        }
    }
}
