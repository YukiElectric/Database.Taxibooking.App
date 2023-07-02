package com.example.demo1.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class TransferRequestData {
     static BooleanProperty requestStatus = new SimpleBooleanProperty(true);

    public TransferRequestData() {
    }

    public static boolean isRequestStatus() {
        return requestStatus.get();
    }

    public static BooleanProperty requestStatusProperty() {
        return requestStatus;
    }

    public static void setRequestStatus(boolean requestStatus) {
        TransferRequestData.requestStatus.set(requestStatus);
    }
}
