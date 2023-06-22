package xyz.lotho.risecore.network.database.mongo;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import org.bson.Document;
import xyz.lotho.risecore.network.RiseCore;

@Getter
public class MongoManager {

    private final RiseCore riseCore;

    private final String username;
    private final String password;
    private final String address;
    private final String database;
    private final boolean srv;

    private MongoClient mongoClient;
    private MongoDatabase mongoDatabase;

    private MongoCollection<Document> profilesCollection;
    private MongoCollection<Document> gamesCollection;

    private ServerApi serverApi;
    private MongoClientSettings mongoSettings;

    public MongoManager(RiseCore riseCore) {
        this.riseCore = riseCore;

        this.username = getRiseCore().getConfigurationFile().get().getString("database.mongo.username");
        this.password = getRiseCore().getConfigurationFile().get().getString("database.mongo.password");
        this.address = getRiseCore().getConfigurationFile().get().getString("database.mongo.address");
        this.database = getRiseCore().getConfigurationFile().get().getString("database.mongo.database");
        this.srv = getRiseCore().getConfigurationFile().get().getBoolean("database.mongo.srv");

        connect();
    }

    private void connect() {
        serverApi = ServerApi.builder().version(ServerApiVersion.V1).build();

        mongoSettings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString("mongodb+" + (isSrv() ? "srv" : "") + "://" + getUsername() + ":" + getPassword() + "@" + getAddress()))
                .serverApi(serverApi)
                .build();

        mongoClient = MongoClients.create(mongoSettings);

        // Create database if it does not exist
        mongoDatabase = getMongoClient().getDatabase(getDatabase());

        // Load collections
        profilesCollection = getMongoDatabase().getCollection(MongoCollections.PROFILES.getCollectionId());
        gamesCollection = getMongoDatabase().getCollection(MongoCollections.GAMES.getCollectionId());
    }

    public void disconnect() {
        try {
            getMongoClient().close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
