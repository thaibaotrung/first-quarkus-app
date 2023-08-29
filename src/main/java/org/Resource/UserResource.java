package org.Resource;

import io.smallrye.mutiny.Uni;

import java.util.List;

import jakarta.inject.Inject;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.core.MediaType;
import org.Model.User;
import org.Service.UserService;

@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    @Inject UserService userService;
    @GET
    public Uni<List<User>> list(){
        return userService.list();
    }

    @POST
    public Uni<List<User>> add(User user){
        return userService.add(user)
                .onItem().ignore().andSwitchTo(this::list);
    }


}
