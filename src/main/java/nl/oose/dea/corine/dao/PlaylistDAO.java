package nl.oose.dea.corine.dao;

import nl.oose.dea.corine.domain.Playlist;
import nl.oose.dea.corine.domain.Playlists;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;

public class PlaylistDAO implements IPlaylistDAO {
    @Resource(name = "jdbc/spotitubeMySqlDb")
    private DataSource dataSource;

    @Override
    public Playlists getAllPlaylists(String user) {
        Playlists playlistArray = new Playlists();
        ArrayList<Playlist> localPlaylistArray = new ArrayList<>();
        int totalDuration = 0;

        try (Connection dbConnnection = dataSource.getConnection()) {
            String sql = "SELECT *, (SELECT sum(duration) FROM tracks WHERE playlistID IS NOT NULL) AS totalDuration from playlists";
            PreparedStatement stmt = dbConnnection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                //Retrieve by column name
                Boolean owner = false;
                int id = rs.getInt("id");
                String playlistName = rs.getString("name");
                String dbowner = rs.getString("owner");
                totalDuration = rs.getInt("totalDuration");
                if(dbowner.equals(user)){
                    owner = true;
                }
                Playlist newplaylist = new Playlist();
                newplaylist.setId(id);
                newplaylist.setName(playlistName);
                newplaylist.setOwner(owner);
                localPlaylistArray.add(newplaylist);
            }
            playlistArray.setPlaylists(localPlaylistArray);
            playlistArray.setLength(totalDuration);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return playlistArray;
    }

    @Override
    public void addPlaylist(String playlistName, String userName) {
        try (Connection dbConnnection = dataSource.getConnection()) {
            String sql = "INSERT INTO `playlists` (`id`, `name`, `owner`) VALUES (NULL, ? , ?)";
            PreparedStatement stmt = dbConnnection.prepareStatement(sql);
            stmt.setString(1, playlistName);
            stmt.setString(2,userName);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deletePlaylist(String id) {
        try (Connection dbConnnection = dataSource.getConnection()) {
            String sql = "DELETE FROM `playlists` WHERE `playlists`.`id` = ?";
            PreparedStatement stmt = dbConnnection.prepareStatement(sql);
            stmt.setString(1,id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void editPlaylist(int id, String name) {
        try (Connection dbConnnection = dataSource.getConnection()) {
            String sql = "UPDATE `playlists` SET `name` = ? WHERE `playlists`.`id` = ?";
            PreparedStatement stmt = dbConnnection.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setInt(2, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setDatasource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
