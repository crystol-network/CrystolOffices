package com.crystolnetwork.offices.common;

import lombok.Data;

import java.util.UUID;

@Data
public final class UserOffices {

    private final UUID uuid;
    private final String offices;

    public UserOffices(UUID uuid, String offices){
        this.uuid = uuid;
        this.offices = offices;
    }

    public String getOffices() {
        return offices;
    }

}
