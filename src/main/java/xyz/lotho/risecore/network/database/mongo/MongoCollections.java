package xyz.lotho.risecore.network.database.mongo;

public enum MongoCollections {

    PROFILES("profiles"),
    GAMES("games");

    private final String collectionId;

    MongoCollections(String collectionId) {
        this.collectionId = collectionId;
    }

    public String getCollectionId() {
        return collectionId;
    }

}
