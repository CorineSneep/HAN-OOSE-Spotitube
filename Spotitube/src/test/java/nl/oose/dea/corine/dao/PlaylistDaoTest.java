package nl.oose.dea.corine.dao;

import nl.oose.dea.corine.domain.Playlist;
import nl.oose.dea.corine.domain.Playlists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;

public class PlaylistDaoTest {
    String user = "Corine";
    String name = "TPG";
    int id = 1;
    String stringID = "1";
    boolean owner = true;
    int totalDuration = 1234;
    String token = "1234-1234-1234";
    String idLabel = "id";
    String nameLabel = "name";
    String ownerLabel = "owner";
    String totalDurationLabel = "totalDuration";

    @Mock
    private DataSource dataSource;
    @Mock private Connection connection;
    @Mock private PreparedStatement preparedStatement;
    @Mock private ResultSet resultSet;

    @BeforeEach
    public void Setup(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getAllPlaylistsTest(){
        try{
            String expectedSQL = "SELECT *, (SELECT sum(duration) FROM tracks WHERE playlistID IS NOT NULL) AS totalDuration from playlists";

            when(dataSource.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(expectedSQL)).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(true, false);
            when(resultSet.getInt(idLabel)).thenReturn(id);
            when(resultSet.getString(nameLabel)).thenReturn(name);
            when(resultSet.getString(ownerLabel)).thenReturn(user);
            when(resultSet.getInt(totalDurationLabel)).thenReturn(totalDuration);

            PlaylistDAO playlistDAO = new PlaylistDAO();
            playlistDAO.setDatasource(dataSource);
            Playlists playlists = playlistDAO.getAllPlaylists(user);

            verify(dataSource).getConnection();
            verify(connection).prepareStatement(expectedSQL);
            verify(preparedStatement).executeQuery();
            verify(resultSet, atLeast(2)).next();
            verify(resultSet).getInt(idLabel);
            verify(resultSet).getString(nameLabel);
            verify(resultSet).getString(ownerLabel);
            verify(resultSet).getInt(totalDurationLabel);

            playlists.playlists.forEach(playlist -> {
                assertEquals(id,playlist.getId());
                assertEquals(name, playlist.getName());
                assertEquals(owner,playlist.isOwner());
            });

        }catch (SQLException e){
            fail();
        }
    }

    @Test
    public void getAllPlaylistsExeptionTest(){
        try {
            String expectedSQL = "SELECT *, (SELECT sum(duration) FROM tracks WHERE playlistID IS NOT NULL) AS totalDuration from playlists";

            when(dataSource.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(expectedSQL)).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenThrow(new SQLException());

            PlaylistDAO playlistDAO = new PlaylistDAO();
            playlistDAO.setDatasource(dataSource);
            Playlists playlists = playlistDAO.getAllPlaylists(user);

        }catch (SQLException e){
            verify(e).printStackTrace();
        }
    }

    @Test
    public void addPlaylistTest(){
        try{
            String expectedSQL = "INSERT INTO `playlists` (`id`, `name`, `owner`) VALUES (NULL, ? , ?)";

            when(dataSource.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(expectedSQL)).thenReturn(preparedStatement);

            PlaylistDAO playlistDAO = new PlaylistDAO();
            playlistDAO.setDatasource(dataSource);
            playlistDAO.addPlaylist(name, user);

            verify(dataSource).getConnection();
            verify(connection).prepareStatement(expectedSQL);
            verify(preparedStatement).setString(1,name);
            verify(preparedStatement).setString(2,user);
            verify(preparedStatement).executeUpdate();

        }catch (SQLException e){
            fail();
        }
    }

    @Test
    public void addPlaylistExeptionTest(){
        try {
            String expectedSQL = "INSERT INTO `playlists` (`id`, `name`, `owner`) VALUES (NULL, ? , ?)";

            when(dataSource.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(expectedSQL)).thenReturn(preparedStatement);
            when(preparedStatement.executeUpdate()).thenThrow(new SQLException());

            PlaylistDAO playlistDAO = new PlaylistDAO();
            playlistDAO.setDatasource(dataSource);
            playlistDAO.addPlaylist(name, user);

        }catch (SQLException e){
            verify(e).printStackTrace();
        }
    }

    @Test
    public void deletePlaylistTest(){
        try{
            String expectedSQL = "DELETE FROM `playlists` WHERE `playlists`.`id` = ?";

            when(dataSource.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(expectedSQL)).thenReturn(preparedStatement);

            PlaylistDAO playlistDAO = new PlaylistDAO();
            playlistDAO.setDatasource(dataSource);
            playlistDAO.deletePlaylist(stringID);

            verify(dataSource).getConnection();
            verify(connection).prepareStatement(expectedSQL);
            verify(preparedStatement).setString(1, stringID);
            verify(preparedStatement).executeUpdate();

        }catch (SQLException e){
            fail();
        }
    }

    @Test
    public void deletePlaylistExeptionTest(){
        try {
            String expectedSQL = "DELETE FROM `playlists` WHERE `playlists`.`id` = ?";

            when(dataSource.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(expectedSQL)).thenReturn(preparedStatement);
            when(preparedStatement.executeUpdate()).thenThrow(new SQLException());

            PlaylistDAO playlistDAO = new PlaylistDAO();
            playlistDAO.setDatasource(dataSource);
            playlistDAO.deletePlaylist(stringID);

        }catch (SQLException e){
            verify(e).printStackTrace();
        }
    }

    @Test
    public void editPlaylistTest(){
        try{
            String expectedSQL = "UPDATE `playlists` SET `name` = ? WHERE `playlists`.`id` = ?";

            when(dataSource.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(expectedSQL)).thenReturn(preparedStatement);

            PlaylistDAO playlistDAO = new PlaylistDAO();
            playlistDAO.setDatasource(dataSource);
            playlistDAO.editPlaylist(id, name);

            verify(dataSource).getConnection();
            verify(connection).prepareStatement(expectedSQL);
            verify(preparedStatement).setString(1, name);
            verify(preparedStatement).setInt(2,id);
            verify(preparedStatement).executeUpdate();

        }catch (SQLException e){
            fail();
        }
    }

    @Test
    public void editPlaylistExeptionTest(){
        try {
            String expectedSQL = "UPDATE `playlists` SET `name` = ? WHERE `playlists`.`id` = ?";

            when(dataSource.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(expectedSQL)).thenReturn(preparedStatement);
            when(preparedStatement.executeUpdate()).thenThrow(new SQLException());

            PlaylistDAO playlistDAO = new PlaylistDAO();
            playlistDAO.setDatasource(dataSource);
            playlistDAO.editPlaylist(id, name);

        }catch (SQLException e){
            verify(e).printStackTrace();
        }
    }
}
