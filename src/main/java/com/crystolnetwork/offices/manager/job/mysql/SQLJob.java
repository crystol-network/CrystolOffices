package com.crystolnetwork.offices.manager.job.mysql;

import com.crystolnetwork.offices.services.NetworkService;
import com.crystolnetwork.offices.services.OfficesServices;
import dev.king.universal.api.JdbcProvider;

public class SQLJob {

    private final NetworkService networkService;
    private final OfficesServices officesServices;
    private final JdbcProvider jdbcProvider;

    public SQLJob(OfficesServices officesServices) {
        this.officesServices = officesServices;
        this.networkService = officesServices.getNetworkService();
        this.jdbcProvider = networkService.getDataConnection().getJdbcProvider();

        jdbcProvider.update("create table if not exists " + getDatabase() + "(uuid TEXT, offices TEXT)");

    }

    public JdbcProvider getJdbcProvider() {
        return jdbcProvider;
    }

    public String getDatabase() {
        return officesServices.getServerName() + "_Offices";
    }

    public NetworkService getNetworkService() {
        return networkService;
    }

    public OfficesServices getOfficesServices() {
        return officesServices;
    }

}
