package nl.oose.dea.corine.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.sql.DataSource;
import java.sql.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;

public class UserDaoTest {
    String user = "Corine";
    String password = "test";
    String wrongPassword = "notRight";
    String passwordLabel = "password";
    String usernameLabel = "name";
    String token = "1234-1234-1234";

    @Mock private DataSource dataSource;
    @Mock private Connection connection;
    @Mock private PreparedStatement preparedStatement;
    @Mock private ResultSet resultSet;

    @BeforeEach
    public void Setup(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void LoginTest() {
        try {
            String expectedSQL = "SELECT password FROM user WHERE name = ?";

            when(dataSource.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(expectedSQL)).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(true,false);
            when(resultSet.getString(passwordLabel)).thenReturn(password);

            UserDAO userDAO = new UserDAO();
            userDAO.setDataSource(dataSource);
            Boolean exist = userDAO.login(user,password);

            verify(dataSource).getConnection();
            verify(connection).prepareStatement(expectedSQL);
            verify(preparedStatement).setString(1,user);
            verify(preparedStatement).executeQuery();
            verify(resultSet, atLeast(2)).next();
            verify(resultSet).getString(passwordLabel);

            assertEquals(true, exist);

        }catch (SQLException e){
            fail();
        }
    }

    @Test
    public void loginUserDoesNotExistTest(){
        try {
            String expectedSQL = "SELECT password FROM user WHERE name = ?";

            when(dataSource.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(expectedSQL)).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(true, false);
            when(resultSet.getString(passwordLabel)).thenReturn(wrongPassword);

            UserDAO userDAO = new UserDAO();
            userDAO.setDataSource(dataSource);
            Boolean exist = userDAO.login(user, password);

            verify(dataSource).getConnection();
            verify(connection).prepareStatement(expectedSQL);
            verify(preparedStatement).setString(1,user);
            verify(preparedStatement).executeQuery();
            verify(resultSet, atLeast(2)).next();
            verify(resultSet).getString(passwordLabel);

            assertEquals(false, exist);
        }catch (SQLException e){
            fail();
        }
    }

    @Test
    public void loginExeptionTest(){
        try {
            String expectedSQL = "SELECT password FROM user WHERE name = ?";

            when(dataSource.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(expectedSQL)).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenThrow(new SQLException());

            UserDAO userDAO = new UserDAO();
            userDAO.setDataSource(dataSource);
            Boolean exist = userDAO.login(user, password);

        }catch (SQLException e){
            verify(e).printStackTrace();
        }
    }

    @Test
    public void getUsernameTest(){
        try {
            String expectedSQL = "SELECT name FROM user WHERE token = ?";

            when(dataSource.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(expectedSQL)).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(true, false);
            when(resultSet.getString(usernameLabel)).thenReturn(user);

            UserDAO userDAO = new UserDAO();
            userDAO.setDataSource(dataSource);
            String userName = userDAO.getUsername(token);

            verify(dataSource).getConnection();
            verify(connection).prepareStatement(expectedSQL);
            verify(preparedStatement).setString(1,token);
            verify(preparedStatement).executeQuery();
            verify(resultSet, atLeast(2)).next();
            verify(resultSet).getString(usernameLabel);

        }catch (SQLException e){
            fail();
        }
    }

    @Test
    public void GetUsernameExeptionTest(){
        try {
            String expectedSQL = "SELECT name FROM user WHERE token = ?";

            when(dataSource.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(expectedSQL)).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenThrow(new SQLException());

            UserDAO userDAO = new UserDAO();
            userDAO.setDataSource(dataSource);
            String userName = userDAO.getUsername(token);

        }catch (SQLException e){
            verify(e).printStackTrace();
        }
    }

    @Test
    public void getUsernameUserNotExistTest(){
        try{
            String expectedSQL = "SELECT name FROM user WHERE token = ?";

            when(dataSource.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(expectedSQL)).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(false);

            UserDAO userDAO = new UserDAO();
            userDAO.setDataSource(dataSource);
            String userName = userDAO.getUsername(token);

            verify(dataSource).getConnection();
            verify(connection).prepareStatement(expectedSQL);
            verify(preparedStatement).setString(1,token);
            verify(preparedStatement).executeQuery();
            verify(resultSet, times(1)).next();

            assertEquals(null,userName);
        }catch (SQLException e){
            fail();
        }
    }

    @Test
    public void insertTokenTest(){
        try{
            String expectedSQL = "UPDATE `user` SET `token` = ? WHERE `user`.`name` = ?";

            when(dataSource.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(expectedSQL)).thenReturn(preparedStatement);

            UserDAO userDAO = new UserDAO();
            userDAO.setDataSource(dataSource);
            userDAO.insertToken(token, user);

            verify(dataSource).getConnection();
            verify(connection).prepareStatement(expectedSQL);
            verify(preparedStatement).setString(1,token);
            verify(preparedStatement).setString(2,user);
            verify(preparedStatement).executeUpdate();
        }catch (SQLException e){
            fail();
        }
    }

    @Test
    public void InsertTokenExeptionTest(){
        try {
            String expectedSQL = "UPDATE `user` SET `token` = ? WHERE `user`.`name` = ?";

            when(dataSource.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(expectedSQL)).thenReturn(preparedStatement);
            when(preparedStatement.executeUpdate()).thenThrow(new SQLException());

            UserDAO userDAO = new UserDAO();
            userDAO.setDataSource(dataSource);
            userDAO.insertToken(token, user);

        }catch (SQLException e){
            verify(e).printStackTrace();
        }
    }





}
