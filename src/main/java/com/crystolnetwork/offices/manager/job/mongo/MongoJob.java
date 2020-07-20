package com.crystolnetwork.offices.manager.job.mongo;

import com.crystolnetwork.offices.services.NetworkService;
import com.crystolnetwork.offices.services.OfficesServices;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class MongoJob {

    private final NetworkService networkService;
    private final OfficesServices officesServices;
    private final MongoDatabase database;
    private final MongoCollection<Document> colletion;

    public MongoJob(OfficesServices officesServices) {
        this.officesServices = officesServices;
        this.networkService = officesServices.getNetworkService();
        final String[] split = networkService.getMongoCredentials().getUri().split("\\s*[^a-zA-Z]+\\s*");
        database = networkService.getMongoPool().getDatabase(split[9]);
        System.out.println("Uploaded to mongo database '" + split[9] + "'.");
        final String colletionName = officesServices.getServerName()+"-Offices";
        MongoCollection<Document> colletion = database.getCollection(colletionName);
        if (colletion == null) {
            database.createCollection(colletionName);
            System.out.println("Created " + colletionName + " colletion.");
        }
        this.colletion = colletion;
    }

    public MongoDatabase getDatabase() {
        return database;
    }

    public MongoCollection<Document> getColletion() {
        return colletion;
    }

    public OfficesServices getOfficesServices() {
        return officesServices;
    }

    public NetworkService getNetworkService() {
        return networkService;
    }

}
