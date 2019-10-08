package nl.oose.dea.corine.rest;

import nl.oose.dea.corine.dao.IUserDAO;
import nl.oose.dea.corine.rest.dto.TokenDTO;
import nl.oose.dea.corine.rest.dto.UserDTO;
import nl.oose.dea.corine.utility.TokenGenerator;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.Response;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LoginApiTest {

    @Test
    public void loginTest() {
        String userName = "Corine";
        String password = "testPassword";
        String token = "1234-1234-1234";
        UserDTO userDTO = new UserDTO();
        userDTO.user = userName;
        userDTO.password = password;
        LoginApi loginApi = new LoginApi();
        IUserDAO userDAO = mock(IUserDAO.class);
        TokenGenerator tokenGenerator = mock(TokenGenerator.class);

        when(userDAO.login(userName, password)).thenReturn(true);
        when(tokenGenerator.createToken()).thenReturn(token);
        loginApi.setUserDao(userDAO);
        loginApi.setTokenGenerator(tokenGenerator);
        Response response = loginApi.login(userDTO);

        TokenDTO dto = (TokenDTO) response.getEntity();

        assertEquals(token, dto.token);
        assertEquals(userName, dto.user);
        assertEquals(201, response.getStatus());
    }

    @Test
    public void loginUserDoesNotExistTest() {
        String wrongUserName = "blabla";
        String password = "1234";
        UserDTO userDTO = new UserDTO();
        userDTO.user = wrongUserName;
        userDTO.password = password;
        LoginApi loginApi = new LoginApi();
        IUserDAO userDAO = mock(IUserDAO.class);


        when(userDAO.login(wrongUserName, password)).thenReturn(false);
        loginApi.setUserDao(userDAO);
        Response response = loginApi.login(userDTO);

        assertEquals(401, response.getStatus());


    }

}
