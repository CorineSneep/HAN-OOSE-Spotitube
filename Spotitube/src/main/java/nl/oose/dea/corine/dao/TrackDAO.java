package nl.oose.dea.corine.dao;

import nl.oose.dea.corine.domain.Track;
import nl.oose.dea.corine.domain.Tracks;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class TrackDAO implements ITrackDAO {
    @Resource(name = "jdbc/spotitubeMySqlDb")
    private DataSource dataSource;

    @Override
    public Tracks getTracks(int playlistID) {
        Tracks trackArray = new Tracks();

        try (Connection dbConnnection = dataSource.getConnection()) {
            String sql = "SELECT * FROM tracks WHERE playlistID = ?";
            PreparedStatement stmt = dbConnnection.prepareStatement(sql);
            stmt.setInt(1, playlistID);
            ResultSet rs = stmt.executeQuery();

            trackArray = setLocalTracks(trackArray, rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return trackArray;
    }

    @Override
    public Tracks getTracksNotInPlaylist(int playlistID) {
        Tracks trackArray = new Tracks();

        try (Connection dbConnnection = dataSource.getConnection()) {
            String sql = "SELECT * FROM tracks WHERE playlistID != ? OR playlistID IS null";
            PreparedStatement stmt = dbConnnection.prepareStatement(sql);
            stmt.setInt(1, playlistID);

            ResultSet rs = stmt.executeQuery();

            trackArray = setLocalTracks(trackArray, rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return trackArray;
    }

    @Override
    public void deleteTrackFromPlaylist(int trackID) {
        try (Connection dbConnnection = dataSource.getConnection()) {
            String sql = "UPDATE `tracks` SET `playlistID` = NULL WHERE `tracks`.`id` = ?";
            PreparedStatement stmt = dbConnnection.prepareStatement(sql);
            stmt.setInt(1, trackID);

            int rs = stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addTrackToPlaylist(int trackID, int playlistID, boolean offlineAvailable) {
        try (Connection dbConnnection = dataSource.getConnection()) {
            int setOfflineAvailable;
            if (offlineAvailable) {
                setOfflineAvailable = 1;
            } else {
                setOfflineAvailable = 0;
            }
            String sql = "UPDATE `tracks` SET `playlistID` = ? , `offlineAvailable` = ? WHERE `tracks`.`id` = ?";
            PreparedStatement stmt = dbConnnection.prepareStatement(sql);
            stmt.setInt(1, playlistID);
            stmt.setInt(2, setOfflineAvailable);
            stmt.setInt(3, trackID);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    private Tracks setLocalTracks(Tracks trackArray, ResultSet rs) throws SQLException {
        ArrayList<Track> localTrackArray = new ArrayList<>();
        while (rs.next()) {
            DateFormat df = new SimpleDateFormat("MM/dd/yyyy");

            Track newTrack = new Track();
            newTrack.setId(rs.getInt("id"));
            newTrack.setTitle(rs.getString("title"));
            newTrack.setPerformer(rs.getString("performer"));
            newTrack.setDuration(rs.getInt("duration"));
            newTrack.setAlbum(rs.getString("album"));
            newTrack.setPlaycount(rs.getInt("playcount"));
            if(rs.getDate("publicationDate") != null){
                String publicationDateString = df.format(rs.getDate("publicationDate"));
                newTrack.setPublicationDate(publicationDateString);
            }
            newTrack.setDescription(rs.getString("description"));
            newTrack.setOfflineAvailable(rs.getBoolean("offlineAvailable"));
            newTrack.setPlaylistID(rs.getInt("playlistID"));

            localTrackArray.add(newTrack);

        }
        trackArray.setTracks(localTrackArray);
        return trackArray;
    }


    public void setDatasource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
