package nl.oose.dea.corine.dao;

public interface IUserDAO {
    public Boolean login(String user, String password);
    public String getUsername(String token);
    public void insertToken(String token, String user);
}
