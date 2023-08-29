package org.Service;

import io.quarkus.mongodb.reactive.ReactiveMongoClient;
import io.quarkus.mongodb.reactive.ReactiveMongoCollection;
import io.smallrye.mutiny.Uni;
import org.Model.User;
import org.bson.Document;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
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

    public Uni<Void> add(User user){
        Document document = new Document()
                .append("name", user.getName())
                .append("email", user.getEmail())
                .append("password", user.getPassword());
        return getCollection().insertOne(document)
                .onItem().ignore().andContinueWithNull();
    }
    private ReactiveMongoCollection<Document> getCollection(){
        return mongoClient.getDatabase("trungdeptrai_dev").getCollection("users");
    }
}
