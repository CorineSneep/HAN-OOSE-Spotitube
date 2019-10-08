package nl.oose.dea.corine.dao;

import nl.oose.dea.corine.domain.Tracks;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.sql.DataSource;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class TrackDaoTest {
    String user = "Corine";
    String token = "1234-1234-1234";
    int trackId = 2;
    int playlistId = 4;
    String idLabel = "id";
    String title = "fight song";
    String titleLabel = "title";
    String performer = "TPG";
    String performerLabel = "performer";
    int duration = 123;
    String durationLabel = "duration";
    String album = "Uncharted";
    String albumLabel = "album";
    int playcount = 4;
    String playcountLabel = "playcount";
    Date publicationDate = new Date(1553694074);
    String publicationDateLabel = "publicationDate";
    String description = "";
    String descriptionLabel = "description";
    Boolean offlineAvailableTrue = true;
    int setOfflineAvailableTrue = 1;
    Boolean offlineAvailableFalse = false;
    int setOfflineAvailableFalse = 0;
    String offlineAvailableLabel = "offlineAvailable";
    int playlistID = 4;
    String playlistIDLabel = "playlistID";

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
    public void getTracksTest(){
        try {
            DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

            String expectedSQL = "SELECT * FROM tracks WHERE playlistID = ?";

            when(dataSource.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(expectedSQL)).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(true, false);
            when(resultSet.getInt(idLabel)).thenReturn(playlistId);
            when(resultSet.getString(titleLabel)).thenReturn(title);
            when(resultSet.getString(performerLabel)).thenReturn(performer);
            when(resultSet.getInt(durationLabel)).thenReturn(duration);
            when(resultSet.getString(albumLabel)).thenReturn(album);
            when(resultSet.getInt(playcountLabel)).thenReturn(playcount);
            when(resultSet.getDate(publicationDateLabel)).thenReturn(publicationDate);
            when(resultSet.getString(descriptionLabel)).thenReturn(description);
            when(resultSet.getBoolean(offlineAvailableLabel)).thenReturn(offlineAvailableTrue);
            when(resultSet.getInt(playlistIDLabel)).thenReturn(playlistID);

            TrackDAO trackDao = new TrackDAO();
            trackDao.setDatasource(dataSource);
            Tracks tracks = trackDao.getTracks(playlistId);

            verify(dataSource).getConnection();
            verify(connection).prepareStatement(expectedSQL);
            verify(preparedStatement).executeQuery();
            verify(resultSet, atLeast(2)).next();
            verify(resultSet).getInt(idLabel);
            verify(resultSet).getString(titleLabel);
            verify(resultSet).getString(performerLabel);
            verify(resultSet).getInt(durationLabel);
            verify(resultSet).getString(albumLabel);
            verify(resultSet).getInt(playcountLabel);
            verify(resultSet, times(2)).getDate(publicationDateLabel);
            verify(resultSet).getString(descriptionLabel);
            verify(resultSet).getBoolean(offlineAvailableLabel);
            verify(resultSet).getInt(playlistIDLabel);

            tracks.tracks.forEach(track -> {
                assertEquals(dateFormat.format(publicationDate), track.getPublicationDate());
            });
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Test
    public void getTracksWithoutDateTest(){
        try {
            DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

            String expectedSQL = "SELECT * FROM tracks WHERE playlistID = ?";

            when(dataSource.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(expectedSQL)).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(true, false);
            when(resultSet.getInt(idLabel)).thenReturn(playlistId);
            when(resultSet.getString(titleLabel)).thenReturn(title);
            when(resultSet.getString(performerLabel)).thenReturn(performer);
            when(resultSet.getInt(durationLabel)).thenReturn(duration);
            when(resultSet.getString(albumLabel)).thenReturn(album);
            when(resultSet.getInt(playcountLabel)).thenReturn(playcount);
            when(resultSet.getDate(publicationDateLabel)).thenReturn(null);
            when(resultSet.getString(descriptionLabel)).thenReturn(description);
            when(resultSet.getBoolean(offlineAvailableLabel)).thenReturn(offlineAvailableTrue);
            when(resultSet.getInt(playlistIDLabel)).thenReturn(playlistID);

            TrackDAO trackDao = new TrackDAO();
            trackDao.setDatasource(dataSource);
            Tracks tracks = trackDao.getTracks(playlistId);

            verify(dataSource).getConnection();
            verify(connection).prepareStatement(expectedSQL);
            verify(preparedStatement).executeQuery();
            verify(resultSet, atLeast(2)).next();
            verify(resultSet).getInt(idLabel);
            verify(resultSet).getString(titleLabel);
            verify(resultSet).getString(performerLabel);
            verify(resultSet).getInt(durationLabel);
            verify(resultSet).getString(albumLabel);
            verify(resultSet).getInt(playcountLabel);
            verify(resultSet).getDate(publicationDateLabel);
            verify(resultSet).getString(descriptionLabel);
            verify(resultSet).getBoolean(offlineAvailableLabel);
            verify(resultSet).getInt(playlistIDLabel);

            tracks.tracks.forEach(track -> {
                assertEquals(null, track.getPublicationDate());
            });
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Test
    public void getTracksExeptionTest(){
        try {
            String expectedSQL = "SELECT * FROM tracks WHERE playlistID = ?";

            when(dataSource.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(expectedSQL)).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenThrow(new SQLException());

            TrackDAO trackDao = new TrackDAO();
            trackDao.setDatasource(dataSource);
            Tracks tracks = trackDao.getTracks(playlistId);

        }catch (SQLException e){
            verify(e).printStackTrace();
        }
    }

    @Test
    public void getTracksNotInPlaylistTest(){
        try {
            DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

            String expectedSQL = "SELECT * FROM tracks WHERE playlistID != ? OR playlistID IS null";

            when(dataSource.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(expectedSQL)).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(true, false);
            when(resultSet.getInt(idLabel)).thenReturn(playlistId);
            when(resultSet.getString(titleLabel)).thenReturn(title);
            when(resultSet.getString(performerLabel)).thenReturn(performer);
            when(resultSet.getInt(durationLabel)).thenReturn(duration);
            when(resultSet.getString(albumLabel)).thenReturn(album);
            when(resultSet.getInt(playcountLabel)).thenReturn(playcount);
            when(resultSet.getDate(publicationDateLabel)).thenReturn(publicationDate);
            when(resultSet.getString(descriptionLabel)).thenReturn(description);
            when(resultSet.getBoolean(offlineAvailableLabel)).thenReturn(offlineAvailableFalse);
            when(resultSet.getInt(playlistIDLabel)).thenReturn(playlistID);

            TrackDAO trackDao = new TrackDAO();
            trackDao.setDatasource(dataSource);
            Tracks tracks = trackDao.getTracksNotInPlaylist(playlistId);

            verify(dataSource).getConnection();
            verify(connection).prepareStatement(expectedSQL);
            verify(preparedStatement).executeQuery();
            verify(resultSet, atLeast(2)).next();
            verify(resultSet).getInt(idLabel);
            verify(resultSet).getString(titleLabel);
            verify(resultSet).getString(performerLabel);
            verify(resultSet).getInt(durationLabel);
            verify(resultSet).getString(albumLabel);
            verify(resultSet).getInt(playcountLabel);
            verify(resultSet,times(2)).getDate(publicationDateLabel);
            verify(resultSet).getString(descriptionLabel);
            verify(resultSet).getBoolean(offlineAvailableLabel);
            verify(resultSet).getInt(playlistIDLabel);

            tracks.tracks.forEach(track -> {
                assertEquals(dateFormat.format(publicationDate), track.getPublicationDate());
            });
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    @Test
    public void getTracksNotInPlaylistExeptionTest(){
        try {
            String expectedSQL = "SELECT * FROM tracks WHERE playlistID != ? OR playlistID IS null";

            when(dataSource.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(expectedSQL)).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenThrow(new SQLException());

            TrackDAO trackDao = new TrackDAO();
            trackDao.setDatasource(dataSource);
            Tracks tracks = trackDao.getTracksNotInPlaylist(playlistId);

        }catch (SQLException e){
            verify(e).printStackTrace();
        }
    }

    @Test
    public void deleteTrackFromPlaylistTest(){
        try {
            String expectedSQL = "UPDATE `tracks` SET `playlistID` = NULL WHERE `tracks`.`id` = ?";

            when(dataSource.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(expectedSQL)).thenReturn(preparedStatement);

            TrackDAO trackDao = new TrackDAO();
            trackDao.setDatasource(dataSource);
            trackDao.deleteTrackFromPlaylist(playlistId);

            verify(dataSource).getConnection();
            verify(connection).prepareStatement(expectedSQL);
            verify(preparedStatement).setInt(1, playlistId);
            verify(preparedStatement).executeUpdate();

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Test
    public void deleteTrackFromPlaylistExeptionTest(){
        try {
            String expectedSQL = "UPDATE `tracks` SET `playlistID` = NULL WHERE `tracks`.`id` = ?";

            when(dataSource.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(expectedSQL)).thenReturn(preparedStatement);
            when(preparedStatement.executeUpdate()).thenThrow(new SQLException());

            TrackDAO trackDao = new TrackDAO();
            trackDao.setDatasource(dataSource);
            trackDao.deleteTrackFromPlaylist(playlistId);

        }catch (SQLException e){
            verify(e).printStackTrace();
        }
    }

    @Test
    public void addTrackToPlaylistOfflineAvailableTest(){
        try {
            String expectedSQL = "UPDATE `tracks` SET `playlistID` = ? , `offlineAvailable` = ? WHERE `tracks`.`id` = ?";

            when(dataSource.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(expectedSQL)).thenReturn(preparedStatement);

            TrackDAO trackDao = new TrackDAO();
            trackDao.setDatasource(dataSource);
            trackDao.addTrackToPlaylist(trackId, playlistID, offlineAvailableTrue);

            verify(dataSource).getConnection();
            verify(connection).prepareStatement(expectedSQL);
            verify(preparedStatement).setInt(1, playlistId);
            verify(preparedStatement).setInt(2, setOfflineAvailableTrue);
            verify(preparedStatement).setInt(3, trackId);
            verify(preparedStatement).executeUpdate();

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Test
    public void addTrackToPlaylistNotOfflineAvailableTest(){
        try {
            String expectedSQL = "UPDATE `tracks` SET `playlistID` = ? , `offlineAvailable` = ? WHERE `tracks`.`id` = ?";

            when(dataSource.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(expectedSQL)).thenReturn(preparedStatement);

            TrackDAO trackDao = new TrackDAO();
            trackDao.setDatasource(dataSource);
            trackDao.addTrackToPlaylist(trackId, playlistID, offlineAvailableFalse);

            verify(dataSource).getConnection();
            verify(connection).prepareStatement(expectedSQL);
            verify(preparedStatement).setInt(1, playlistId);
            verify(preparedStatement).setInt(2, setOfflineAvailableFalse);
            verify(preparedStatement).setInt(3, trackId);
            verify(preparedStatement).executeUpdate();

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Test
    public void addTrackToPlaylistExeptionTest(){
        try {
            String expectedSQL = "UPDATE `tracks` SET `playlistID` = ? , `offlineAvailable` = ? WHERE `tracks`.`id` = ?";

            when(dataSource.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(expectedSQL)).thenReturn(preparedStatement);
            when(preparedStatement.executeUpdate()).thenThrow(new SQLException());

            TrackDAO trackDao = new TrackDAO();
            trackDao.setDatasource(dataSource);
            trackDao.addTrackToPlaylist(trackId, playlistID, offlineAvailableTrue);

        }catch (SQLException e){
            verify(e).printStackTrace();
        }
    }
}
