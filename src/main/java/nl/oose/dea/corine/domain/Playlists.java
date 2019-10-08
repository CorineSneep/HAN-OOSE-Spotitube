package nl.oose.dea.corine.domain;

import java.util.ArrayList;

public class Playlists {
    public ArrayList<Playlist> playlists;
    public int length;

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public void setPlaylists(ArrayList<Playlist> playlists) {
        this.playlists = playlists;
    }


}
