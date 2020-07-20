package com.crystolnetwork.offices.manager.job.jedis;

import com.crystolnetwork.offices.events.RedisMessageEvent;
import com.crystolnetwork.offices.events.RedisReceiveMessageEvent;
import com.crystolnetwork.offices.services.OfficesServices;
import com.google.gson.Gson;
import redis.clients.jedis.JedisPubSub;

import java.util.Map;

public final class RedisInboud extends JedisPubSub {

    private final OfficesServices officesServices;
    private final Gson gson = new Gson();

    public RedisInboud(final OfficesServices officesServices) {
        this.officesServices = officesServices;
    }

    @Override
    public void onMessage(String channel, String message) {

        if (channel.equals(officesServices.getChannelName())) {
            final RedisMessageEvent event = new RedisMessageEvent(officesServices.getServerName().replaceFirst("ChannelMessageOf-", ""), message).call();
            if (!event.isCancelled()) {
                new RedisReceiveMessageEvent(event.getServerName(), gson.fromJson(message, Map.class)).call();
            }
        }

    }
}
