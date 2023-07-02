package com.example.demo1.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;



public class TransferData {
    public static String getVoucher() {
        return voucher.get();
    }
    public static StringProperty voucherProperty() {
        return voucher;
    }
    public static void setVoucher(String voucher) {
        TransferData.voucher.set(voucher);
    }

    public static boolean isStatus() {
        return status.get();
    }
    public static BooleanProperty statusProperty() {
        return status;
    }

    public static void setStatus(boolean status) {
        TransferData.status.set(status);
    }
    public static Type getType() {
        return type;
    }
    public static void setType(Type type) {
        TransferData.type = type;
    }
    private static  StringProperty voucher=new SimpleStringProperty("");
    private static BooleanProperty status =new SimpleBooleanProperty(false);
     private static  Type type;
     private static BooleanProperty requestStatus = new SimpleBooleanProperty(true);

    public static boolean isRequestStatus() {
        return requestStatus.get();
    }

    public static BooleanProperty requestStatusProperty() {
        return requestStatus;
    }

    public static void setRequestStatus(boolean requestStatus) {
        TransferData.requestStatus.set(requestStatus);
    }
}
