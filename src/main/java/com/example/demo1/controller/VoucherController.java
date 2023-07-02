package com.example.demo1.controller;

import com.example.demo1.model.Type;
import com.example.demo1.model.Voucher;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.FlowPane;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class VoucherController implements Initializable {
    @FXML
    private JFXTextField voucher_input;

    @FXML
    private FlowPane voucher_list;
    ArrayList<Voucher> list = new ArrayList<>();

    @FXML
    void add(ActionEvent event) {
        try {
            voucher_list.getChildren().add(new VoucherItemController(
                    new Voucher(Type.BOOKING,100,200,14),voucher_list,list));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        list.add(new Voucher(Type.BOOKING,100,200,14));
        list.add(new Voucher(Type.BOOKING,100,200,14));

        list.add(new Voucher(Type.BOOKING,100,200,14));

        list.add(new Voucher(Type.BOOKING,100,200,14));

        list.add(new Voucher(Type.SHIP,10,70,30));
        list.add(new Voucher(Type.FOOD,15,30,99));
        list.add(new Voucher(Type.SHIP,10,70,30));
        list.add(new Voucher(Type.FOOD,15,30,99));
        list.add(new Voucher(Type.SHIP,10,70,30));
        list.add(new Voucher(Type.FOOD,15,30,99));
        list.add(new Voucher(Type.SHIP,10,70,30));
        list.add(new Voucher(Type.FOOD,15,30,99));
        for (Voucher v : list) {
            try {
                voucher_list.getChildren().add(new VoucherItemController(v,voucher_list,list));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
