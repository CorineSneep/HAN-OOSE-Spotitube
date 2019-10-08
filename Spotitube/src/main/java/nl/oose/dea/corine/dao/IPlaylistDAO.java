package nl.oose.dea.corine.dao;

import nl.oose.dea.corine.domain.Playlists;

public interface IPlaylistDAO {
    public Playlists getAllPlaylists(String user);
    public void addPlaylist(String playlistName, String userName);
    public void deletePlaylist(String id);
    public void editPlaylist(int id, String name);
}
