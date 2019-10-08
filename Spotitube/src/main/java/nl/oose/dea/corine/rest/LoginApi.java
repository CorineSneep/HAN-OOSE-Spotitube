package nl.oose.dea.corine.rest;

import nl.oose.dea.corine.dao.IUserDAO;
import nl.oose.dea.corine.rest.dto.TokenDTO;
import nl.oose.dea.corine.rest.dto.UserDTO;
import nl.oose.dea.corine.utility.TokenGenerator;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/login")
public class LoginApi {
    @Inject
    private IUserDAO userDAO;

    @Inject
    private TokenGenerator tokenGenerator;

    @POST
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response login(UserDTO userDTO) {
        Boolean userExist = userDAO.login(userDTO.user, userDTO.password);
        if (!userExist) { //couldn't find user or password isn't correct
            return Response.status(401).build();
        }
        String tokenString = tokenGenerator.createToken();
        userDAO.insertToken(tokenString, userDTO.user);
        TokenDTO tokenDTO = new TokenDTO();
        tokenDTO.user = userDTO.user;
        tokenDTO.token = tokenString;
        return Response.status(201).entity(tokenDTO).build();
    }

    public void setUserDao(IUserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public void setTokenGenerator(TokenGenerator tokenGenerator) {
        this.tokenGenerator = tokenGenerator;
    }
}
