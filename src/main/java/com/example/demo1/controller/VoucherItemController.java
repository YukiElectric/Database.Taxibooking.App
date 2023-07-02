package com.example.demo1.controller;

import com.example.demo1.App;
import com.example.demo1.model.TransferData;
import com.example.demo1.model.TransferVoucher;
import com.example.demo1.model.Voucher;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;

import java.io.IOException;
import java.util.ArrayList;

public class VoucherItemController extends Parent {
    @FXML
    private Label discount;

    @FXML
    private Label min;

    @FXML
    private Label percent;

    @FXML
    private Label type;
    Voucher item;
    FlowPane parent;
    ArrayList<Voucher> list;
    @FXML
    private FontAwesomeIconView icon;

    @FXML
    void use(ActionEvent event) {
        if(TransferVoucher.getBookingStatus()==true){
            TransferData.voucherProperty().set(item.getID());
            parent.getChildren().remove(this);
            list.remove(item);
            TransferData.setType(item.getType());
            TransferData.statusProperty().set(!TransferData.isStatus());
        }else{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("");
            alert.setHeaderText("");
            alert.setContentText("Đơn hàng đã được đặt không thể sử dụng!");
            alert.getDialogPane().setPrefSize(220,150);
            alert.showAndWait();
        }
    }

    public VoucherItemController() {
    }
    void setElement(Voucher item)
    {
        icon.setGlyphName(item.getType().getGylpName());
        type.setText(item.getType().getName());
        discount.setText("Giảm "+item.getDiscount()+"K");
        min.setText("Cho đơn từ "+item.getMin()+"K");
        percent.setText("Đã dùng "+item.getPercent()+"%");
    }
    public VoucherItemController(Voucher item, FlowPane parent, ArrayList<Voucher> list) throws IOException {
        this.item=item;
        this.parent=parent;
        this.list=list;
        FXMLLoader fxmlLoader = new FXMLLoader(App.HelloApplication.class.getResource("voucher-item.fxml"));
        fxmlLoader.setController(this);
        this.getChildren().add(fxmlLoader.load());
        setElement(item);
    }

}
