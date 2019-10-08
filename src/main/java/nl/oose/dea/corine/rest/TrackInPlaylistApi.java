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

@Path("/playlists/{id}/tracks")
public class TrackInPlaylistApi {
    @Inject
    ITrackDAO trackDAO;
    @Inject
    IUserDAO userDAO;



    //Get all tracks from 1 playlist
    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTracksOnePlaylist(@PathParam("id") int id, @QueryParam("token") String token) {
        String userName = userDAO.getUsername(token);
        if(userName == null){
            return Response.status(400).build();
        }

        Tracks tracks = trackDAO.getTracks(id);

        TracksDTO tracksDTO = getTracksDTO(tracks);

        return Response.status(Response.Status.OK).entity(tracksDTO).build();
    }



    //Remove a track from a playlist

    @DELETE
    @Path("{trackID}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteTrackFromPlaylist(@PathParam("id") int playlistID, @PathParam("trackID") int trackID, @QueryParam("token") String token) {
        String userName = userDAO.getUsername(token);
        if(userName == null){
            return Response.status(400).build();
        }

        trackDAO.deleteTrackFromPlaylist(trackID);

        Tracks tracks = trackDAO.getTracks(playlistID);

        TracksDTO tracksDTO = getTracksDTO(tracks);

        return Response.status(Response.Status.OK).entity(tracksDTO).build();
    }

    //Add a track to a playlist
    @POST
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addTrackToPlaylist(@PathParam("id") int playlistID, TrackDTO addTrackDTO, @QueryParam("token") String token){
        String userName = userDAO.getUsername(token);
        if(userName == null){
            return Response.status(400).build();
        }

        trackDAO.addTrackToPlaylist(addTrackDTO.id, playlistID, addTrackDTO.offlineAvailable);

        Tracks tracks = trackDAO.getTracks(playlistID);

        TracksDTO tracksDTO = getTracksDTO(tracks);

        return Response.status(Response.Status.OK).entity(tracksDTO).build();

    }

    private TracksDTO getTracksDTO(Tracks tracks) {
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

    public void setTrackDao(ITrackDAO trackDAO) {
        this.trackDAO = trackDAO;
    }

    public void setUserDao(IUserDAO userDAO) {
        this.userDAO = userDAO;
    }
}
