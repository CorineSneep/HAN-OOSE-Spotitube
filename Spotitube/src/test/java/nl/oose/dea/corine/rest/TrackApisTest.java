package nl.oose.dea.corine.rest;

import nl.oose.dea.corine.dao.ITrackDAO;
import nl.oose.dea.corine.dao.IUserDAO;
import nl.oose.dea.corine.domain.Track;
import nl.oose.dea.corine.domain.Tracks;
import nl.oose.dea.corine.rest.dto.TrackDTO;
import nl.oose.dea.corine.rest.dto.TracksDTO;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.Response;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TrackApisTest {
    String user = "Corine";
    String token = "1234-1234-1234";
    int id = 4;
    String title = "Fight Song";
    String performer = "The Piano Guys";
    int duration = 523;
    String album = "";
    int playcount = 10;
    String publicationDate = "";
    String description = "";
    boolean offlineAvailable = false;
    int playlistID = 15;
    String rightOperation = "=";
    String wrongOperation = "!=";

    @Test
    public void getTracksFromPlaylistTest(){
        Track track = new Track();
        track.id = id;
        track.title = title;
        track.performer = performer;
        track.duration = duration;
        track.album = album;
        track.playcount = playcount;
        track.publicationDate = publicationDate;
        track.description = description;
        track.offlineAvailable = offlineAvailable;
        track.playlistID = playlistID;

        TrackDTO addTrackDTO = new TrackDTO();

        ArrayList<Track> testTracks = new ArrayList<>();
        testTracks.add(track);
        Tracks tracks = new Tracks();
        tracks.setTracks(testTracks);

        TrackInPlaylistApi trackInPlaylistApi = new TrackInPlaylistApi();
        TrackNotInPlaylistApi trackNotInPlaylistApi = new TrackNotInPlaylistApi();
        ITrackDAO trackDAO = mock(ITrackDAO.class);
        IUserDAO userDAO = mock(IUserDAO.class);
        when(userDAO.getUsername(token)).thenReturn(user);
        when(trackDAO.getTracks(playlistID)).thenReturn(tracks);
        when(trackDAO.getTracksNotInPlaylist(playlistID)).thenReturn(tracks);

        trackInPlaylistApi.setTrackDao(trackDAO);
        trackInPlaylistApi.setUserDao(userDAO);
        trackNotInPlaylistApi.setTrackDao(trackDAO);
        trackNotInPlaylistApi.setUserDao(userDAO);

        Response responseGetTrack = trackInPlaylistApi.getTracksOnePlaylist(playlistID, token);
        Response responseDeleteTrack = trackInPlaylistApi.deleteTrackFromPlaylist(playlistID, track.id, token);
        Response responseAddTrack = trackInPlaylistApi.addTrackToPlaylist(playlistID, addTrackDTO, token);
        Response responseGetTrackNotInPlaylist = trackNotInPlaylistApi.getTracks(playlistID, token);

        TracksDTO dtoGetTrack = (TracksDTO) responseGetTrack.getEntity();
        TracksDTO dtoDeleteTrack = (TracksDTO) responseDeleteTrack.getEntity();
        TracksDTO dtoAddTrack = (TracksDTO) responseAddTrack.getEntity();
        TracksDTO dtoGetTrackNotPlaylist = (TracksDTO) responseGetTrackNotInPlaylist.getEntity();

        dtoGetTrack.tracks.forEach(trackDTO -> {
            assertEquals(id, trackDTO.id);
            assertEquals(title, trackDTO.title);
            assertEquals(performer, trackDTO.performer);
            assertEquals(duration, trackDTO.duration);
            assertEquals(album, trackDTO.album);
            assertEquals(playcount, trackDTO.playcount);
            assertEquals(publicationDate, trackDTO.publicationDate);
            assertEquals(description, trackDTO.description);
            assertEquals(offlineAvailable, trackDTO.offlineAvailable);
        });

        dtoDeleteTrack.tracks.forEach(trackDTO -> {
            assertEquals(id, trackDTO.id);
            assertEquals(title, trackDTO.title);
            assertEquals(performer, trackDTO.performer);
            assertEquals(duration, trackDTO.duration);
            assertEquals(album, trackDTO.album);
            assertEquals(playcount, trackDTO.playcount);
            assertEquals(publicationDate, trackDTO.publicationDate);
            assertEquals(description, trackDTO.description);
            assertEquals(offlineAvailable, trackDTO.offlineAvailable);
        });

        dtoAddTrack.tracks.forEach(trackDTO -> {
            assertEquals(id, trackDTO.id);
            assertEquals(title, trackDTO.title);
            assertEquals(performer, trackDTO.performer);
            assertEquals(duration, trackDTO.duration);
            assertEquals(album, trackDTO.album);
            assertEquals(playcount, trackDTO.playcount);
            assertEquals(publicationDate, trackDTO.publicationDate);
            assertEquals(description, trackDTO.description);
            assertEquals(offlineAvailable, trackDTO.offlineAvailable);
        });

        dtoGetTrackNotPlaylist.tracks.forEach(trackDTO -> {
            assertEquals(id, trackDTO.id);
            assertEquals(title, trackDTO.title);
            assertEquals(performer, trackDTO.performer);
            assertEquals(duration, trackDTO.duration);
            assertEquals(album, trackDTO.album);
            assertEquals(playcount, trackDTO.playcount);
            assertEquals(publicationDate, trackDTO.publicationDate);
            assertEquals(description, trackDTO.description);
            assertEquals(offlineAvailable, trackDTO.offlineAvailable);
        });

        assertEquals(200, responseGetTrack.getStatus());
        assertEquals(200, responseDeleteTrack.getStatus());
        assertEquals(200, responseAddTrack.getStatus());
        assertEquals(200, responseGetTrackNotInPlaylist.getStatus());
    }
}
