package com.example.demo1.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class TransferMoney {
    private static StringProperty strPr = new SimpleStringProperty("");

    private static BooleanProperty blPr = new SimpleBooleanProperty(true);
    public TransferMoney() {
    }

    public static String getStrPr() {
        return strPr.get();
    }

    public static StringProperty strPrProperty() {
        return strPr;
    }

    public static void setStrPr(String strPr) {
        TransferMoney.strPr.set(strPr);
    }

    public static boolean isBlPr() {
        return blPr.get();
    }

    public static BooleanProperty blPrProperty() {
        return blPr;
    }

    public static void setBlPr(boolean blPr) {
        TransferMoney.blPr.set(blPr);
    }
}
