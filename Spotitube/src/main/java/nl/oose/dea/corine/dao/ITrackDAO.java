package nl.oose.dea.corine.dao;

import nl.oose.dea.corine.domain.Tracks;

public interface ITrackDAO {
    public void deleteTrackFromPlaylist(int trackID);
    public void addTrackToPlaylist(int trackID, int playlistID, boolean offlineAvailable);

    public Tracks getTracks(int playlistID);

    public Tracks getTracksNotInPlaylist(int playlistID);
}
