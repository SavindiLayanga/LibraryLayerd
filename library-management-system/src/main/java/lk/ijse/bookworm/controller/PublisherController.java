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
import lk.ijse.bookworm.dto.Employee;
import lk.ijse.bookworm.dto.Publisher;
import lk.ijse.bookworm.dto.tm.AuthorTM;
import lk.ijse.bookworm.dto.tm.PublisherTM;
import lk.ijse.bookworm.model.AuthorModel;
import lk.ijse.bookworm.model.EmployeeModel;
import lk.ijse.bookworm.model.PublisherModel;
import lk.ijse.bookworm.util.Regex;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class PublisherController implements Initializable {
    @FXML
    private AnchorPane WinPublisher;

    @FXML
    private TextField txtPublisherAddress;

    @FXML
    private TextField txtPublisherEmail;

    @FXML
    private TextField txtPublisherName;

    @FXML
    private TextField txtPublisherContact;

    @FXML
    private TextField txtPublisherID;

    @FXML
    private Button btnPublisherUpdate;

    @FXML
    private Button btnPublisherDelete;

    @FXML
    private Button btnPublisherAdd;

    @FXML
    private Button btnPublisherSearch;

    @FXML
    private TableView<PublisherTM> tblPublisher;

    @FXML
    private TableColumn<?, ?> colPiblisherID;

    @FXML
    private TableColumn<?, ?> colName;

    @FXML
    private TableColumn<?, ?> colEmail;

    @FXML
    private TableColumn<?, ?> colAddress;

    @FXML
    private TableColumn<?, ?> colContact;

    @FXML
    private TableColumn<?, ?> colAction;

    private String searchText="";

    private ObservableList<PublisherTM> obList = FXCollections.observableArrayList();

    public void initialize(URL url, ResourceBundle resourceBundle) {

        setCellValueFactory();
        getAllPublisherToTable(searchText);
        // setDeleteButtonTableOnAction();

        tblPublisher.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> { //Add ActionListener to selected column and display text field values
            //Check select value is not null
            if(null!=newValue) { //newValue!=null --> Get more time to compare (newValue object compare)
                // btnSaveSupplier.setText("Update Supplier");
                setDataToTextFields((PublisherTM) newValue); //Set data to text field of selected row data of table
            }
        });
    }

    private void setCellValueFactory() {
        colPiblisherID.setCellValueFactory(new PropertyValueFactory<>("Pub_id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("Name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("Address"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("Email"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("Contact"));
        colAction.setCellValueFactory(new PropertyValueFactory<>("btn"));
    }


    private void setDataToTextFields(PublisherTM publisherTM) {
        txtPublisherID.setText(publisherTM.getPub_id());
        txtPublisherName.setText(publisherTM.getName());
        txtPublisherAddress.setText(publisherTM.getAddress());
        txtPublisherEmail.setText(publisherTM.getEmail());
        txtPublisherContact.setText(String.valueOf(publisherTM.getContact()));

    }

    private void btnAuthorDeleteOnAction(Object e) {
    }

    private void btnAuthorSearchOnAction(Object e) {
    }

    private void getAllPublisherToTable(String searchText) {
        try {
            List<Publisher> PublisherList = PublisherModel.searchAll();
            for( Publisher publisher : PublisherList) {
                if (publisher.getPub_id().contains(searchText) || publisher.getAddress().contains(searchText)){  //Check pass text contains of the supName
                    JFXButton btnDel=new JFXButton("Delete");
                    btnDel.setAlignment(Pos.CENTER);
                    btnDel.setStyle("-fx-background-color: #686de0; ");
                    btnDel.setCursor(Cursor.HAND);

                    PublisherTM tm=new PublisherTM(
                            publisher.getPub_id(),
                            publisher.getName(),
                            publisher.getAddress(),
                            publisher.getEmail(),
                            publisher.getContact()
                            ,btnDel);

                    obList.add(tm);

                    setDeleteButtonTableOnAction(btnDel);
                }
            }
            tblPublisher.setItems(obList);
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
                txtPublisherContact.setText(tblPublisher.getSelectionModel().getSelectedItem().getPub_id());
                btnPublisherSearchOnAction(e);
                btnPublisherDeleteOnAction(e);
                /*try {
                    btnDeleteOnAction(e);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }*/

                tblPublisher.getItems().clear();
                getAllPublisherToTable(searchText);
            }
        });
    }

    @FXML
    void btnPublisherAddOnAction(ActionEvent event) throws SQLException {
        String Pub_id = txtPublisherID.getText();
        String Name = txtPublisherName.getText();
        String address = txtPublisherAddress.getText();
        String Email = txtPublisherEmail.getText();
        String Contact = txtPublisherContact.getText();

        try {
            boolean isSaved = PublisherModel.add(Pub_id, Name, address, Email, Contact);
            if (isSaved) {
                new Alert(Alert.AlertType.CONFIRMATION, "Publisher saved!!!").show();
                tblPublisher.getItems().clear();
                getAllPublisherToTable(searchText);
            }
        } catch (SQLException e) {
            System.out.println(e);
            //
            // new Alert(Alert.AlertType.ERROR, "OOPSSS!! something happened!!!").show();
        }
    }

    @FXML
    void btnPublisherSearchOnAction(ActionEvent event) {
        String PublisherID= txtPublisherID.getText();
        try {
            Publisher publisher = PublisherModel.search(PublisherID);
            if (publisher != null) {
                txtPublisherID.setText(publisher.getPub_id());
                txtPublisherName.setText(publisher.getName());
                txtPublisherAddress.setText(String.valueOf(publisher.getAddress()));
                txtPublisherEmail.setText(String.valueOf(publisher.getEmail()));
                txtPublisherContact.setText(String.valueOf(publisher.getContact()));
            } else {
                new Alert(Alert.AlertType.WARNING, "no publisher found :(").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "oops! something went wrong :(").show();
        }
    }

    @FXML
    void btnPublisherDeleteOnAction(ActionEvent event) {
        String PublisherId = txtPublisherID.getText();

        try {
            boolean isDeleted = PublisherModel.delete(PublisherId);
            if (isDeleted) {
                new Alert(Alert.AlertType.CONFIRMATION, "Publisher deleted !").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "something happened !").show();
        }
    }

    @FXML
    void btnPublisherUpdateOnAction(ActionEvent event) {
        String Pub_id = txtPublisherID.getText();
        String Name = txtPublisherName.getText();
        String Address = txtPublisherAddress.getText();
        String Email = txtPublisherEmail.getText();
        String Contact = txtPublisherContact.getText();

        var publisher = new Publisher(Pub_id, Name, Address, Email, Contact);   //type inference

        try {
            boolean isUpdated = PublisherModel.update(publisher);
            if (isUpdated) {
                new Alert(Alert.AlertType.CONFIRMATION, " Publisher Updated!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "SQL Error!").show();
        }
    }

    //***********************************

    @FXML
    void txtPublisherIDOnAction(ActionEvent event) {
        String Pub_id= txtPublisherID.getText();
        if(Regex.validatePublisherId(Pub_id)){
            txtPublisherName.requestFocus();

        }else {
            new Alert(Alert.AlertType.WARNING, "No Matching ID!!!").show();
        }
    }

    @FXML
    void txtPublisherContactOnAction(ActionEvent event) {
        String contact=txtPublisherContact.getText();
        if (Regex.validatePublisherContact(contact)){
            //txtJobRole.requestFocus();
        }else {
            txtPublisherContact.clear();
            new Alert(Alert.AlertType.WARNING, "No matching contact number please Input SUP format!!!").show();
        }
    }
}
