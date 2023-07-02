package com.example.demo1.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class RunningStatus {
    public static BooleanProperty runningStatus = new SimpleBooleanProperty(true);

    public RunningStatus() {
    }

    public static boolean isRunningStatus() {
        return runningStatus.get();
    }

    public static BooleanProperty runningStatusProperty() {
        return runningStatus;
    }

    public static void setRunningStatus(boolean runningStatus) {
        RunningStatus.runningStatus.set(runningStatus);
    }

}
