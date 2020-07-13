package com.walkgs.crystolnetwork.offices.job;

import com.google.gson.Gson;
import com.walkgs.crystolnetwork.offices.api.ServerOffices;
import com.walkgs.crystolnetwork.offices.events.RedisMessageEvent;
import com.walkgs.crystolnetwork.offices.events.RedisReceiveMessageEvent;
import redis.clients.jedis.JedisPubSub;

import java.util.Map;

public final class RedisInboud extends JedisPubSub {

    private final ServerOffices serverOffices;
    private final Gson gson = new Gson();

    public RedisInboud(final ServerOffices serverOffices) {
        this.serverOffices = serverOffices;
    }

    @Override
    public void onMessage(String channel, String message) {

        if (channel.equals(serverOffices.getChannelName())) {
            final RedisMessageEvent event = new RedisMessageEvent(serverOffices.getServerName(), message).call();
            if (!event.isCancelled()) {
                new RedisReceiveMessageEvent(gson.fromJson(message, Map.class)).call();
            }
        }

    }
}
