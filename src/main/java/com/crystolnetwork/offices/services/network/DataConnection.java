package com.crystolnetwork.offices.services.network;

import com.crystolnetwork.offices.annotations.Singleton;
import com.crystolnetwork.offices.security.SecurityObject;
import com.crystolnetwork.offices.services.network.data.DataConnectionType;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import dev.king.universal.UniversalWrapper;
import dev.king.universal.api.JdbcProvider;
import dev.king.universal.api.mysql.UniversalCredentials;

import java.io.File;

@Singleton
public class DataConnection {

    private final UniversalWrapper wrapper = new UniversalWrapper();

    private DataConnectionType connectionType;
    private MongoClient mongoPool;
    private JdbcProvider jdbcProvider;

    public boolean build(final SecurityObject securityObject) {
        connectionType = DataConnectionType.valueOf(securityObject.getType());
        switch (connectionType) {
            case MONGODB: {
                try {
                    mongoPool = MongoClients.create(securityObject.getUri());
                } catch (Exception e) {
                    return false;
                }
            }
            case MYSQL: {
                jdbcProvider = wrapper.newMysqlProvider(new UniversalCredentials(
                        securityObject.getHost()+":"+securityObject.getPort(), //hostname
                        securityObject.getDatabase(), //database
                        securityObject.getUser(), //username
                        securityObject.getPassword()  //password
                ), 2).preOpen();
                if (!jdbcProvider.openConnection()){
                    return false;
                } else {
                    return true;
                }
            }
            case SQLITE: {
                jdbcProvider = wrapper.newSqlProvider(
                        new File(securityObject.getHost())
                ).preOpen();
                if (!jdbcProvider.openConnection()){
                    return false;
                } else {
                    return true;
                }
            }
        }
        return true;
    }

    public DataConnectionType getConnectionType() {
        return connectionType;
    }

    public JdbcProvider getJdbcProvider() {
        return jdbcProvider;
    }

    public MongoClient getMongoPool() {
        return mongoPool;
    }

    public UniversalWrapper getWrapper() {
        return wrapper;
    }

}
