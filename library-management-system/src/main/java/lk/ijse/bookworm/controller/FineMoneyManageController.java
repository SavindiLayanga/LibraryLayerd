/*package lk.ijse.bookworm.controller;

import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import lk.ijse.bookworm.dto.Issue;
import lk.ijse.bookworm.dto.Return;
import lk.ijse.bookworm.dto.tm.FineMoneyTM;
import lk.ijse.bookworm.dto.tm.IssueTM;
import lk.ijse.bookworm.dto.tm.ReturnTM;
import lk.ijse.bookworm.model.BookModel;
import lk.ijse.bookworm.model.IssueModel;
import lk.ijse.bookworm.model.ReturnModel;
import lk.ijse.bookworm.util.Regex;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;

public class FineMoneyManageController implements Initializable {

    @FXML
    private AnchorPane WinFineMoney;

    @FXML
    private TextField txtFineId;

    @FXML
    private TextField txtTotal;

    @FXML
    private DatePicker dtDate;

    @FXML
    private TableView<?> tblFineMoney;

    @FXML
    private TableColumn<?, ?> colFineId;

    @FXML
    private TableColumn<?, ?> colReturnId;

    @FXML
    private TableColumn<?, ?> colDate;

    @FXML
    private TableColumn<?, ?> colTotal;

    @FXML
    private TableColumn<?, ?> colAction;

    @FXML
    private ComboBox<?> cmbReturnId;

    private String searchText = "";

    private ObservableList<FineMoneyTM> obList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setCellValueFactory();
        getAllFineToTable(searchText);
        loadReturnIds();
        generateNextFineId();
        tblFineMoney.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> { //Add ActionListener to selected column and display text field values
            if (null != newValue) { //newValue!=null --> Get more time to compare (newValue object compare)
                // btnSaveSupplier.setText("Update Supplier");
                setDataToTextFields((FineMoneyTM) newValue); //Set data to text field of selected row data of table
            }
        });
    }

    private void setDataToTextFields(IssueTM newValue) {
    }

    private void generateNextFineId() {
    }

    private void loadReturnIds() {
        try {
            ObservableList<String> obList = FXCollections.observableArrayList();
            List<String> returnIds = ReturnModel.getreturnids();

            for (String book_id : returnIds) {
                obList.add(book_id);
            }
            cmbReturnId.setItems(obList);
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "SQL Error!").show();
        }
    }

    private void getAllFineToTable(String searchText) {
        try {
            List<Return> returnList = ReturnModel.searchAll();
            for (Return returnb : returnList) {
                if (returnb.getReturnB_id().contains(searchText) || returnb.getReturnB_id().contains(searchText)) {  //Check pass text contains of the supName
                    JFXButton btnDel = new JFXButton("Delete");
                    btnDel.setAlignment(Pos.CENTER);
                    btnDel.setStyle("-fx-background-color: #686de0; ");
                    btnDel.setCursor(Cursor.HAND);

                    ReturnTM tm = new ReturnTM(
                            returnb.getIssueBook_id(),
                            returnb.getReturn_date(),
                            returnb.getTotal(),
                            returnb.getReturnB_id()
                            , btnDel);

                    obList.add(tm);

                    set(btnDel);
                }
            }
            tblIssueBook.setItems(obList);
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Query error!").show();
        }
    }

    private void setCellValueFactory() {
    }
}*/
