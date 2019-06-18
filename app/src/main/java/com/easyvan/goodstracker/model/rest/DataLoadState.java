package com.easyvan.goodstracker.model.rest;

/**
 * Created by sm5 on 6/12/2019.
 */

public class DataLoadState<T> {

    public enum Status{
        RUNNING,
        SUCCESS,
        FAILED
    }
    private final Status status;
    private final String msg;

    public static final DataLoadState LOADED;
    public static final DataLoadState LOADING;
    public static final DataLoadState FAILED;

    private DataLoadState(Status status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    static {
        LOADED = new DataLoadState(Status.SUCCESS,"Success");
        LOADING = new DataLoadState(Status.RUNNING,"Running");
        FAILED = new DataLoadState(Status.FAILED,"Failed");
    }

    public Status getStatus() {
        return status;
    }

}
