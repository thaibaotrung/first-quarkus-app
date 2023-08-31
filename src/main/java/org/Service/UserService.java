package org.Service;

import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoEntity;
import io.quarkus.mongodb.reactive.ReactiveMongoClient;
import io.quarkus.mongodb.reactive.ReactiveMongoCollection;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.Multi;
import org.Model.User;
import org.bson.Document;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.bson.types.ObjectId;

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
                    user.setId(document.getObjectId("id"));
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
                    user.setId(document.getObjectId("id"));
                    user.setName(document.getString("name"));
                    user.setEmail(document.getString("email"));
                    user.setPassword(document.getString("password"));
                    return user;
                }).collect().asList();
    }
    public Uni<User> deleteUserById(String id){
        ObjectId objectId = new ObjectId(id);
        return getCollection().findOneAndDelete(new Document("_id", objectId))
                .map(document -> {
                    User user = new User();
                    user.setId(document.getObjectId("id"));
                    user.setName(document.getString("name"));
                    user.setEmail(document.getString("email"));
                    user.setPassword(document.getString("password"));
                    return user;
                });
    }

    public Uni<Void> add(User user){
        Document document = new Document()
                .append("id", user.getId())
                .append("name", user.getName())
                .append("email", user.getEmail())
                .append("password", user.getPassword());
        return getCollection().insertOne(document)
                .onItem().ignore().andContinueWithNull();
    }
    public Uni<Void> updateUser(String id, User user){
        ObjectId objectId = new ObjectId(id);
        Document updateDocument = new Document()
                .append("name", user.getName())
                .append("email", user.getEmail())
                .append("password", user.getPassword());
        return getCollection().updateOne(new Document("_id", objectId), updateDocument)
                .onItem().ignore().andContinueWithNull();
    }
    private ReactiveMongoCollection<Document> getCollection(){
        return mongoClient.getDatabase("test").getCollection("user");
    }
}
