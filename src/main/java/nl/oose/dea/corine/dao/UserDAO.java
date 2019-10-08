package nl.oose.dea.corine.dao;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.*;

public class UserDAO implements IUserDAO {
    @Resource(name = "jdbc/spotitubeMySqlDb")
    private DataSource dataSource;

    @Override
    public Boolean login(String user, String password) {
        Boolean exist = false;//the return token of user with password does exist.

        try (Connection dbConnnection = dataSource.getConnection()) {
            String sql = "SELECT password FROM user WHERE name = ?";
            PreparedStatement stmt = dbConnnection.prepareStatement(sql);
            stmt.setString(1,user);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                //Retrieve by column name
                String dbpassword = rs.getString("password");
                if (dbpassword.equals(password)) {
                    exist = true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exist;//token from database
    }

    @Override
    public String getUsername(String token) {
        String username = null;

        try (Connection dbConnnection = dataSource.getConnection()) {
            String sql = "SELECT name FROM user WHERE token = ?";
            PreparedStatement stmt = dbConnnection.prepareStatement(sql);
            stmt.setString(1,token);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                username = rs.getString("name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return username;
    }

    @Override
    public void insertToken(String token, String user) {
        try (Connection dbConnnection = dataSource.getConnection()) {
            String sql = "UPDATE `user` SET `token` = ? WHERE `user`.`name` = ?";
            PreparedStatement stmt = dbConnnection.prepareStatement(sql);
            stmt.setString(1, token);
            stmt.setString(2, user);
            stmt.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void setDataSource(DataSource ds){
        this.dataSource = ds;
    }
}
