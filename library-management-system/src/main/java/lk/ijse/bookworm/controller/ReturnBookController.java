package lk.ijse.bookworm.controller;

import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.util.StringConverter;
import lk.ijse.bookworm.dto.Issue;
import lk.ijse.bookworm.dto.Return;
import lk.ijse.bookworm.dto.tm.IssueTM;
import lk.ijse.bookworm.dto.tm.ReturnTM;
import lk.ijse.bookworm.model.ReturnModel;
import lk.ijse.bookworm.util.Regex;
import lombok.SneakyThrows;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class ReturnBookController implements Initializable {

    @FXML
    private AnchorPane WinReturnBook;

    @FXML
    private TableView<ReturnTM> tblReturnBook;

    @FXML
    private TableColumn<ReturnTM, String> colReturnId;

    @FXML
    private TableColumn<ReturnTM, String> colIssueId;

    @FXML
    private TableColumn<ReturnTM, String> colReturndt;

    @FXML
    private TableColumn<ReturnTM, Button> colAction;

    @FXML
    private TextField txtReturnId;

    @FXML
    private DatePicker dtReturndt;

    @FXML
    private ComboBox<String> cmbissueId;

    @FXML
    private Button btnReturnAddRecord;

    @FXML
    private Button btnCheckFine;

    private String searchText = "";

    private ObservableList<ReturnTM> obList = FXCollections.observableArrayList();

    @SneakyThrows
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setCellValueFactory();
        getAllReturnToTable(searchText);
        setComboBoxData();
        generateNextReturnId();
    }

    private void getAllReturnToTable(String searchText) {
        try {
            List<Return> returnList = ReturnModel.searchAllReturn();
            for( Return returnb : returnList) {
                if (returnb.getReturnB_id().contains(searchText) || returnb.getReturnB_id().contains(searchText)){  //Check pass text contains of the supName
                    JFXButton btnDel=new JFXButton("Delete");
                    btnDel.setAlignment(Pos.CENTER);
                    btnDel.setStyle("-fx-background-color: #686de0; ");
                    btnDel.setCursor(Cursor.HAND);

                    ReturnTM tm=new ReturnTM(
                            returnb.getReturnB_id(),
                            returnb.getReturn_date(),
                            returnb.getIssueBook_id()
                            ,btnDel);

                    obList.add(tm);

                    setDeleteButtonTableOnAction(btnDel);
                }
            }
            tblReturnBook.setItems(obList);
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Query error!").show();
        }
    }

    private void setDeleteButtonTableOnAction(JFXButton btnDel) {
        btnDel.setOnAction((e) -> {
            ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
            ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

            Optional<ButtonType> buttonType = new Alert(Alert.AlertType.INFORMATION, "Are you sure to Delete?", yes, no).showAndWait();

            if (buttonType.get() == yes) {
                txtReturnId.setText(tblReturnBook.getSelectionModel().getSelectedItem().getReturnB_id());
                //0btnAuthorSearchOnAction(e);
                //btnAuthorDeleteOnAction(e);
                //btnAuthorDeleteOnAction(e);

                tblReturnBook.getItems().clear();
                getAllReturnToTable(searchText);
            }
        });
    }

    private void setComboBoxData() {

        ObservableList<String> list = FXCollections.observableArrayList();
        try {
            ArrayList<Issue> returnList = ReturnModel.searchAll();
            for (Issue ob : returnList){
                list.add(ob.getIssue_id());
            }
            cmbissueId.setItems(list);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setCellValueFactory() {
        colReturnId.setCellValueFactory(new PropertyValueFactory<ReturnTM,String>("ReturnB_id"));
        colIssueId.setCellValueFactory(new PropertyValueFactory<ReturnTM,String>("IssueBook_id"));
        colReturndt.setCellValueFactory(new PropertyValueFactory<ReturnTM,String>("return_date"));
        colAction.setCellValueFactory(new PropertyValueFactory<ReturnTM,Button>("btn"));
    }

    private void generateNextReturnId() {
        try {
            String nextId = ReturnModel.generateNextReturnId();
            txtReturnId.setText(nextId);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnReturnAddRecordOnAction(ActionEvent event) {
        String ID = txtReturnId.getText();
        String date = dtReturndt.getValue().toString();
        String issueid = cmbissueId.getSelectionModel().getSelectedItem().toString();
        try {
            boolean add = ReturnModel.Add(new Return(ID, date, issueid));
            if (add){
                ReturnTM r2 = new ReturnTM(ID,date,issueid,new Button("Delete"));
                tblReturnBook.getItems().clear();
                getAllReturnToTable("search");
                new Alert(Alert.AlertType.CONFIRMATION,"Done",ButtonType.OK).show();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @FXML
    void txtReturnIdOnAction(ActionEvent event) {
        String ReturnBId= txtReturnId.getText();
        if(Regex.validateReturnBId(ReturnBId)){
            dtReturndt.requestFocus();

        }else {
            new Alert(Alert.AlertType.WARNING, "No Matching ID!!!").show();
        }

    }
}
