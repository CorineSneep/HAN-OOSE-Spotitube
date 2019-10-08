package nl.oose.dea.corine.rest;

import nl.oose.dea.corine.dao.ITrackDAO;
import nl.oose.dea.corine.dao.IUserDAO;
import nl.oose.dea.corine.domain.Tracks;
import nl.oose.dea.corine.rest.dto.TrackDTO;
import nl.oose.dea.corine.rest.dto.TracksDTO;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;

@Path("/tracks")
public class TrackNotInPlaylistApi {
    @Inject
    ITrackDAO trackDAO;
    @Inject
    IUserDAO userDAO;

    //Get all tracks forPlaylist
    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getTracks(@QueryParam("forPlaylist") int forPlaylist, @QueryParam("token") String token){
        String userName = userDAO.getUsername(token);
        if(userName == null){
            return Response.status(400).build();
        }

        Tracks tracks = trackDAO.getTracksNotInPlaylist(forPlaylist);

        TracksDTO tracksDTO = getTracksDTO(tracks);

        return Response.status(Response.Status.OK).entity(tracksDTO).build();
    }

    public TracksDTO getTracksDTO(Tracks tracks) {
        TracksDTO tracksDTO = new TracksDTO();

        ArrayList<TrackDTO> localTracksDTO = new ArrayList<>();
        tracks.tracks.forEach(track -> {
            TrackDTO trackDTO = new TrackDTO();
            trackDTO.id = track.getId();
            trackDTO.title = track.getTitle();
            trackDTO.performer = track.getPerformer();
            trackDTO.duration = track.getDuration();
            trackDTO.album = track.getAlbum();
            trackDTO.playcount = track.getPlaycount();
            trackDTO.publicationDate = track.getPublicationDate();
            trackDTO.description = track.getDescription();
            trackDTO.offlineAvailable = track.isOfflineAvailable();
            localTracksDTO.add(trackDTO);
        });

        tracksDTO.tracks = localTracksDTO;
        return tracksDTO;
    }


    public void setUserDao(IUserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public void setTrackDao(ITrackDAO trackDAO) {
        this.trackDAO = trackDAO;
    }
}
