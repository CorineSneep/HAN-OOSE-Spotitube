package nl.oose.dea.corine.rest.playlistApiTest;

import nl.oose.dea.corine.dao.IPlaylistDAO;
import nl.oose.dea.corine.dao.IUserDAO;
import nl.oose.dea.corine.domain.Playlist;
import nl.oose.dea.corine.domain.Playlists;
import nl.oose.dea.corine.rest.PlaylistApi;
import nl.oose.dea.corine.rest.dto.PlaylistDTO;
import nl.oose.dea.corine.rest.dto.PlaylistsDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.Response;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PlaylistApiChangePlaylistTest {
    String user = "Corine";
    int intId = 1;
    String stringId = "1";
    String name = "Relax";
    String token = "1234-1234-1234";
    Boolean owner = true;

    @Test
    public void deletePlaylistTest(){
        PlaylistDTO playlistDTO = new PlaylistDTO();
        playlistDTO.id = intId;
        playlistDTO.name = name;
        playlistDTO.owner = owner;

        //domain
        Playlist playlist = new Playlist();
        playlist.setId(intId);
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


        Response responseDelete = playlistApi.deletePlaylist(stringId, token);
        Response responseEdit = playlistApi.editPlaylist(playlistDTO, token);
        Response responseAdd = playlistApi.addPlaylist(playlistDTO, token);

        PlaylistsDTO dtoDelete = (PlaylistsDTO) responseDelete.getEntity();
        dtoDelete.playlists.forEach(newPlaylistDTO -> {
            assertEquals(name, newPlaylistDTO.name);
            assertEquals(intId, newPlaylistDTO.id);
            assertEquals(owner, newPlaylistDTO.owner);
        });

        PlaylistsDTO dtoEdit = (PlaylistsDTO) responseEdit.getEntity();
        dtoEdit.playlists.forEach(newPlaylistDTO -> {
            assertEquals(name, newPlaylistDTO.name);
            assertEquals(intId, newPlaylistDTO.id);
            assertEquals(owner, newPlaylistDTO.owner);
        });

        PlaylistsDTO dtoAdd = (PlaylistsDTO) responseAdd.getEntity();
        dtoAdd.playlists.forEach(newPlaylistDTO -> {
            assertEquals(name, newPlaylistDTO.name);
            assertEquals(intId, newPlaylistDTO.id);
            assertEquals(owner, newPlaylistDTO.owner);
        });

        assertEquals(200, responseDelete.getStatus());
        assertEquals(200, responseEdit.getStatus());
        assertEquals(200, responseAdd.getStatus());
    }



}
