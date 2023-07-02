package com.example.demo1.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class TransferVoucher {
    private static BooleanProperty status =new SimpleBooleanProperty(false);
    private static boolean BookingStatus = true;
    public static boolean getStatus() {
        return status.get();
    }

    public static BooleanProperty statusProperty() {
        return status;
    }

    public static void setStatus(boolean status) {
        TransferVoucher.status.set(status);
    }

    public static void setBookingStatus(boolean status) {BookingStatus = status;}

    public static boolean getBookingStatus() {return BookingStatus;}
}
