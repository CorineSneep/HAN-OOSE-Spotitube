package nl.oose.dea.corine.rest.playlistApiTest;

import nl.oose.dea.corine.dao.IPlaylistDAO;
import nl.oose.dea.corine.dao.IUserDAO;
import nl.oose.dea.corine.domain.Playlist;
import nl.oose.dea.corine.domain.Playlists;
import nl.oose.dea.corine.rest.PlaylistApi;
import nl.oose.dea.corine.rest.dto.PlaylistDTO;
import nl.oose.dea.corine.rest.dto.PlaylistsDTO;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.Response;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PlaylistApiGetPlaylistTest {
    String user = "Corine";
    int id = 1;
    String name = "Relax";
    String token = "1234-1234-1234";
    Boolean owner = true;

    @Test
    public void getPlaylistTest() {
        Playlist playlist = new Playlist();
        playlist.setId(id);
        playlist.setName(name);
        playlist.setOwner(owner);
        ArrayList<Playlist> testPlaylist = new ArrayList<>();
        testPlaylist.add(playlist);
        Playlists playlists = new Playlists();
        playlists.setPlaylists(testPlaylist);

        PlaylistApi playlistApi = new PlaylistApi();
        IPlaylistDAO playlistDAO = mock(IPlaylistDAO.class);
        IUserDAO userDAO = mock(IUserDAO.class);
        when(userDAO.getUsername(token)).thenReturn(user);
        when(playlistDAO.getAllPlaylists(user)).thenReturn(playlists);

        playlistApi.setPlaylistDAO(playlistDAO);
        playlistApi.setUserDAO(userDAO);


        Response response = playlistApi.getAllPlaylists(token);

        PlaylistsDTO dto = (PlaylistsDTO) response.getEntity();
        dto.playlists.forEach(playlistDTO -> {
            assertEquals(name, playlistDTO.name);
            assertEquals(id, playlistDTO.id);
            assertEquals(owner, playlistDTO.owner);
        });

        assertEquals(200, response.getStatus());
    }

    @Test
    public void getEmptyPlaylistTest() {
        ArrayList<Playlist> testPlaylist = new ArrayList<>();
        Playlists playlists = new Playlists();
        playlists.setPlaylists(testPlaylist);

        PlaylistApi playlistApi = new PlaylistApi();
        IPlaylistDAO playlistDAO = mock(IPlaylistDAO.class);
        IUserDAO userDAO = mock(IUserDAO.class);
        when(userDAO.getUsername(token)).thenReturn(user);
        when(playlistDAO.getAllPlaylists(user)).thenReturn(playlists);

        playlistApi.setPlaylistDAO(playlistDAO);
        playlistApi.setUserDAO(userDAO);

        Response response = playlistApi.getAllPlaylists(token);

        PlaylistsDTO dto = (PlaylistsDTO) response.getEntity();
        assertEquals(testPlaylist, dto.playlists);
        assertEquals(200, response.getStatus());
    }


}
