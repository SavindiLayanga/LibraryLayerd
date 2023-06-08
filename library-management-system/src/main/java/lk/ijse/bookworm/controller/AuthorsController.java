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
import lk.ijse.bookworm.dto.Author;
import lk.ijse.bookworm.dto.Employee;
import lk.ijse.bookworm.dto.tm.AuthorTM;
import lk.ijse.bookworm.model.AuthorModel;
import lk.ijse.bookworm.model.EmployeeModel;
import lk.ijse.bookworm.util.CrudUtil;
import lk.ijse.bookworm.util.Regex;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class AuthorsController implements Initializable {
    @FXML
    private AnchorPane WinAuthor;

    @FXML
    private TextField txtAuthorId;

    @FXML
    private TextField txtAuthorName;

    @FXML
    private Button btnAuthorAdd;

    @FXML
    private Button btnAuthorUpdate;

    @FXML
    private Button btnAuthorDelete;

    @FXML
    private Button btnAuthorSearch;

    @FXML
    private TableView<AuthorTM> tblAuthor;

    @FXML
    private TableColumn<?, ?> colAuthorID;

    @FXML
    private TableColumn<?, ?> colAuthorName;

    @FXML
    private TableColumn<?, ?> colAuthorAction;

    private String searchText="";

    private ObservableList<AuthorTM>  obList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        setCellValueFactory();
        getAllAuthorsToTable(searchText);
        // setDeleteButtonTableOnAction();

        tblAuthor.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> { //Add ActionListener to selected column and display text field values
            //Check select value is not null
            if(null!=newValue) { //newValue!=null --> Get more time to compare (newValue object compare)
                // btnSaveSupplier.setText("Update Supplier");
                setDataToTextFields((AuthorTM) newValue); //Set data to text field of selected row data of table
            }
        });
    }

    private void setCellValueFactory() {
        colAuthorID.setCellValueFactory(new PropertyValueFactory<>("Author_id"));
        colAuthorName.setCellValueFactory(new PropertyValueFactory<>("Name"));
        colAuthorAction.setCellValueFactory(new PropertyValueFactory<>("btn"));
    }

    private void getAllAuthorsToTable(String searchText) {
        try {
            List<Author> authorList = AuthorModel.searchAll();
            for( Author author : authorList) {
                if (author.getAuthor_id().contains(searchText) || author.getName().contains(searchText)){  //Check pass text contains of the supName
                    JFXButton btnDel=new JFXButton("Delete");
                    btnDel.setAlignment(Pos.CENTER);
                    btnDel.setStyle("-fx-background-color: #686de0; ");
                    btnDel.setCursor(Cursor.HAND);

                    AuthorTM tm=new AuthorTM(
                            author.getAuthor_id(),
                            author.getName()
                            ,btnDel);

                    obList.add(tm);

                    setDeleteButtonTableOnAction(btnDel);
                }
            }
            tblAuthor.setItems(obList);
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
                txtAuthorId.setText(tblAuthor.getSelectionModel().getSelectedItem().getAuthor_id());
                btnAuthorSearchOnAction(e);
                btnAuthorDeleteOnAction(e);
                //btnAuthorDeleteOnAction(e);

                tblAuthor.getItems().clear();
                getAllAuthorsToTable(searchText);
            }
        });
    }

    private void setDataToTextFields(AuthorTM authorTM) {
        txtAuthorId.setText(authorTM.getAuthor_id());
        txtAuthorName.setText(authorTM.getName());
    }

    //******************************************************

    @FXML
    void btnAuthorAddOnAction(ActionEvent event) {
        String AuthorID = txtAuthorId.getText();
        String Name = txtAuthorName.getText();

        try {
            boolean isSaved = AuthorModel.save(AuthorID, Name);
            if (isSaved) {
                new Alert(Alert.AlertType.CONFIRMATION, "Author saved!!!").show();
                tblAuthor.getItems().clear();
                getAllAuthorsToTable(searchText);
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "OOPSSS!! something happened!!!").show();
        }
        txtAuthorId.clear();
        txtAuthorName.clear();
    }


    @FXML
    void btnAuthorSearchOnAction(ActionEvent event) {
        String Author_id = txtAuthorId.getText();
        try {
            Author author = AuthorModel.search(Author_id);
            if (author != null) {
                txtAuthorId.setText(author.getAuthor_id());
                txtAuthorName.setText(author.getName());
            } else {
                new Alert(Alert.AlertType.WARNING, "no author found :(").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "oops! something went wrong :(").show();
        }
    }

    @FXML
    void btnAuthorUpdateOnAction (ActionEvent event){
        String Author_id = txtAuthorId.getText();
        String Name = txtAuthorName.getText();

        var author = new Author(Author_id,Name);   //type inference

        try {
            boolean   isUpdated = AuthorModel.update(author);
            if (isUpdated) {
                new Alert(Alert.AlertType.CONFIRMATION, "huree! Author Updated!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.CONFIRMATION, "SQL Error!").show();
        }
    }

    @FXML
    void btnAuthorDeleteOnAction (ActionEvent event){
        String Author_id = txtAuthorId.getText();

        try {
            boolean isDeleted = AuthorModel.delete(Author_id);
            if(isDeleted) {
                new Alert(Alert.AlertType.CONFIRMATION, "Author deleted !").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "something happened !").show();
        }
    }

    //******************************************

    @FXML
    void txtAuthorIdOnAction(ActionEvent event) {
        String Author_id= txtAuthorId.getText();
        if(Regex.validateAuthorid(Author_id)){
            txtAuthorName.requestFocus();

        }else {
            new Alert(Alert.AlertType.WARNING, "No Matching ID!!!").show();
        }
    }

}
