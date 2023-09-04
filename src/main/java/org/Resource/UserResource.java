package org.Resource;


import io.smallrye.mutiny.Uni;

import java.util.List;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.Model.User;
import org.Service.UserService;
import org.bson.types.ObjectId;

@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    @Inject UserService userService;
    @GET
    public Uni<List<User>> list(){
        return userService.list();
    }

    @GET
    @Path("/{id}")
    public Uni<List<User>> findUserById(String id){
        return userService.findUserById(id);
    }

    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<Void> updateUser(String id, User user) {
        return userService.updateUserById(id, user);
    }
    @DELETE
    @Path("/{id}")
    public Uni<Void> deleteUserById(@PathParam("id") String id) {
        return userService.deleteUserById(id);
    }
    @POST
    public Uni<List<User>> add(User user){
        return userService.add(user)
                .onItem().ignore().andSwitchTo(this::list);
    }


}
