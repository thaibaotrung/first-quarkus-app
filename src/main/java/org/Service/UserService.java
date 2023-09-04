package org.Service;

import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoEntity;
import io.quarkus.mongodb.reactive.ReactiveMongoClient;
import io.quarkus.mongodb.reactive.ReactiveMongoCollection;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.Multi;
import jakarta.jws.soap.SOAPBinding;
import org.Model.User;
import org.bson.Document;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.bson.types.ObjectId;
import static com.mongodb.client.model.Filters.eq;
import java.util.List;

@ApplicationScoped
public class UserService {
    @Inject
    ReactiveMongoClient mongoClient;

    public Uni<List<User>> list(){
        return getCollection()
                .find()
                .map(document -> {
                    User user = new User();
                    user.setName(document.getString("name"));
                    user.setEmail(document.getString("email"));
                    user.setPassword(document.getString("password"));
                    return user;
                }).collect().asList();
    }

    public Uni<List<User>> findUserById(String id){
        ObjectId objectId = new ObjectId(id);
        return getCollection().find(new Document("_id", objectId))
                .map(document -> {
                    User user = new User();
                    user.setName(document.getString("name"));
                    user.setEmail(document.getString("email"));
                    user.setPassword(document.getString("password"));
                    return user;
                }).collect().asList();
    }
    public Uni<Void> deleteUserById(String id) {
        ObjectId objectId = new ObjectId(id);
        return getCollection().deleteOne(new Document("_id", objectId))
                .onItem().ignore().andContinueWithNull();
    }

    public Uni<Void> add(User user){
        Document document = new Document()
                .append("name", user.getName())
                .append("email", user.getEmail())
                .append("password", user.getPassword());
        return getCollection().insertOne(document)
                .onItem().ignore().andContinueWithNull();
    }
    public Uni<Void> updateUserById(String id, User updatedUser) {
        ObjectId objectId = new ObjectId(id);
        Document update = new Document()
                .append("$set", new Document()
                        .append("name", updatedUser.getName())
                        .append("email", updatedUser.getEmail())
                        .append("password", updatedUser.getPassword()));

        return getCollection().updateOne(eq("_id", objectId), update)
                .onItem().ignore().andContinueWithNull();
    }
    private ReactiveMongoCollection<Document> getCollection(){
        return mongoClient.getDatabase("test").getCollection("user");
    }
}
