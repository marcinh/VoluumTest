package com.voluum.framework.serialization;

/**
 * Mock object for JSON serializtion
 */
public class TrafficClassObject {
    private String id;

    private TrafficClassObject(String id) {
        this.id = id;
    }

    public static TrafficClassObject getZeroPark() {
        return new TrafficClassObject("afea734c-8a4a-4f04-bfe6-2e720c1ccb86");
    }

    public String getId() {
        return id;
    }
}

