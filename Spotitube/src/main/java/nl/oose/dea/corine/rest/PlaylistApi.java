package nl.oose.dea.corine.rest;

import nl.oose.dea.corine.dao.IPlaylistDAO;
import nl.oose.dea.corine.dao.IUserDAO;
import nl.oose.dea.corine.domain.Playlists;
import nl.oose.dea.corine.rest.dto.PlaylistsDTO;
import nl.oose.dea.corine.rest.dto.PlaylistDTO;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;

@Path("/playlists")
public class PlaylistApi {
    @Inject
    IPlaylistDAO playlistDAO;

    @Inject
    IUserDAO userDAO;

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllPlaylists(@QueryParam("token") String token){


        String userName = userDAO.getUsername(token);
        if(userName == null){
            return Response.status(400).build();
        }
        PlaylistsDTO playlists = getPlaylists(userName);
        return Response.status(Response.Status.OK).entity(playlists).build();
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deletePlaylist(@PathParam("id") String id, @QueryParam("token") String token){
        String userName = userDAO.getUsername(token);
        if(userName == null){
            return Response.status(400).build();
        }
        playlistDAO.deletePlaylist(id);
        PlaylistsDTO playlists = getPlaylists(userName);
        return Response.status(Response.Status.OK).entity(playlists).build();
    }

    @POST
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public  Response addPlaylist(PlaylistDTO playlistDTO, @QueryParam("token") String token){

        String userName = userDAO.getUsername(token);
        if(userName == null){
            return Response.status(400).build();
        }
        playlistDAO.addPlaylist(playlistDTO.name, userName);
        PlaylistsDTO playlists = getPlaylists(userName);
        return Response.status(Response.Status.OK).entity(playlists).build();
    }

    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response editPlaylist(PlaylistDTO playlistDTO, @QueryParam("token") String token){
        String userName = userDAO.getUsername(token);
        if(userName == null){
            return Response.status(400).build();
        }
        playlistDAO.editPlaylist(playlistDTO.id, playlistDTO.name);
        PlaylistsDTO playlists = getPlaylists(userName);
        return Response.status(Response.Status.OK).entity(playlists).build();
    }


    private PlaylistsDTO getPlaylists(String user){
        Playlists playlists = playlistDAO.getAllPlaylists(user);

        PlaylistsDTO playlistsDTO = new PlaylistsDTO();

        ArrayList<PlaylistDTO> localPlaylistsDTO = new ArrayList<>();
        playlists.playlists.forEach((playlist) -> {
            PlaylistDTO playlistDTO = new PlaylistDTO();
            playlistDTO.id = playlist.getId();
            playlistDTO.name = playlist.getName();
            playlistDTO.owner = playlist.isOwner();
            localPlaylistsDTO.add(playlistDTO);
        });

        playlistsDTO.playlists = localPlaylistsDTO;

        playlistsDTO.length = playlists.getLength();
        return playlistsDTO;
    }

    public void setPlaylistDAO(IPlaylistDAO playlistDAO) {
        this.playlistDAO = playlistDAO;
    }

    public void setUserDAO(IUserDAO userDAO) {
        this.userDAO = userDAO;
    }
}
