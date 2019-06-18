package com.easyvan.goodstracker.utils;

/**
 * Created by sm5 on 6/16/2019.
 */

public class ConnectionModel {

    public enum NetworkType{
        WIFI,
        MOBILE,
        NONE
    }

    private final NetworkType type;
    private final boolean isConnected;

    public ConnectionModel(NetworkType type, boolean isConnected) {
        this.type = type;
        this.isConnected = isConnected;
    }

    public NetworkType getType() {
        return type;
    }

    public boolean isConnected() {
        return isConnected;
    }


}
