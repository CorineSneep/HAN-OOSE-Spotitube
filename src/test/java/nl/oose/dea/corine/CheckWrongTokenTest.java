package nl.oose.dea.corine;

import nl.oose.dea.corine.dao.IUserDAO;
import nl.oose.dea.corine.rest.PlaylistApi;
import nl.oose.dea.corine.rest.TrackInPlaylistApi;
import nl.oose.dea.corine.rest.TrackNotInPlaylistApi;
import nl.oose.dea.corine.rest.dto.PlaylistDTO;
import nl.oose.dea.corine.rest.dto.TrackDTO;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.Response;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CheckWrongTokenTest {
    @Test
    public void getPlaylistWithWrongTokenTest(){
        String user = null;
        String token = "1234";
        String stringId = "2";
        int intId = 2;
        int trackId = 6;

        PlaylistApi playlistApi = new PlaylistApi();
        TrackNotInPlaylistApi trackNotInPlaylistApi = new TrackNotInPlaylistApi();
        TrackInPlaylistApi trackInPlaylistApi = new TrackInPlaylistApi();
        PlaylistDTO playlistDTO = new PlaylistDTO();
        TrackDTO trackDTO = new TrackDTO();
        IUserDAO userDAO = mock(IUserDAO.class);
        when(userDAO.getUsername(token)).thenReturn(user);

        playlistApi.setUserDAO(userDAO);
        trackNotInPlaylistApi.setUserDao(userDAO);
        trackInPlaylistApi.setUserDao(userDAO);
        Response responseGetPlaylist = playlistApi.getAllPlaylists(token);
        Response responseAddPlaylist = playlistApi.addPlaylist(playlistDTO, token);
        Response responseDeletePlaylist = playlistApi.deletePlaylist(stringId, token);
        Response responseEditPlaylist = playlistApi.editPlaylist(playlistDTO, token);
        Response responseGetTrackNotInPlaylist = trackNotInPlaylistApi.getTracks(intId, token);
        Response responseGetTrackInPlaylist = trackInPlaylistApi.getTracksOnePlaylist(intId, token);
        Response responseDeleteTrack = trackInPlaylistApi.deleteTrackFromPlaylist(intId, trackId, token);
        Response responseAddTrack = trackInPlaylistApi.addTrackToPlaylist(intId, trackDTO, token);


        assertEquals(400, responseGetPlaylist.getStatus());
        assertEquals(400, responseAddPlaylist.getStatus());
        assertEquals(400, responseDeletePlaylist.getStatus());
        assertEquals(400, responseEditPlaylist.getStatus());
        assertEquals(400, responseGetTrackNotInPlaylist.getStatus());
        assertEquals(400, responseGetTrackInPlaylist.getStatus());
        assertEquals(400, responseDeleteTrack.getStatus());
        assertEquals(400, responseAddTrack.getStatus());
    }
}
