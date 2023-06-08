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
import lk.ijse.bookworm.dto.Book;
import lk.ijse.bookworm.dto.Issue;
import lk.ijse.bookworm.dto.tm.IssueTM;
import lk.ijse.bookworm.model.BookModel;
import lk.ijse.bookworm.model.IssueModel;
import lk.ijse.bookworm.util.Regex;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class IssueBookController implements Initializable {
    public ComboBox<String> cmbBooks;
    public DatePicker dtReturndt;
    @FXML
    private AnchorPane WinIssueBook;

    @FXML
    private TextField txtIssueId;

    @FXML
    private DatePicker dtIssuedt;

    @FXML
    private TableView<IssueTM >tblIssueBook;

    @FXML
    private TableColumn colIssueId;

    @FXML
    private TableColumn colMemberId;

    @FXML
    private TableColumn colIssueDate;

    @FXML
    private TableColumn<IssueTM, String> colReturnDate;

    @FXML
    private TableColumn colAction;

    @FXML
    private TextField txtMemberId;

    private String searchText = "";

    private ObservableList<IssueTM> obList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setCellValueFactory();
        getAllIssueToTable(searchText);
        loadBookIds();
        generateNextIssueId();
        tblIssueBook.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> { //Add ActionListener to selected column and display text field values
            if (null != newValue) { //newValue!=null --> Get more time to compare (newValue object compare)
                // btnSaveSupplier.setText("Update Supplier");
                setDataToTextFields((IssueTM) newValue); //Set data to text field of selected row data of table
            }
        });
    }

    private void loadBookIds() {
        try {
            ObservableList<String> obList = FXCollections.observableArrayList();
            List<String> bookids = BookModel.getbookids();

            for (String book_id : bookids) {
                obList.add(book_id);
            }
            cmbBooks.setItems(obList);
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "SQL Error!").show();
        }
    }

    private void getAllIssueToTable(String searchText) {
        try {
            List<Issue> issueList = IssueModel.searchAll();
            for( Issue issue : issueList) {
                if (issue.getIssue_id().contains(searchText) || issue.getIssue_id().contains(searchText)){  //Check pass text contains of the supName
                    JFXButton btnDel=new JFXButton("Delete");
                    btnDel.setAlignment(Pos.CENTER);
                    btnDel.setStyle("-fx-background-color: #686de0; ");
                    btnDel.setCursor(Cursor.HAND);

                    IssueTM tm=new IssueTM(
                            issue.getIssue_id(),
                            issue.getMem_id(),
                            issue.getBookId(),
                            issue.getIssue_date(),
                            issue.getReturn_date()
                            ,btnDel);

                    obList.add(tm);

                    setDeleteButtonTableOnAction(btnDel);
                }
            }
            tblIssueBook.setItems(obList);
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
                txtIssueId.setText(tblIssueBook.getSelectionModel().getSelectedItem().getIssue_id());
                //0btnAuthorSearchOnAction(e);
                //btnAuthorDeleteOnAction(e);
                //btnAuthorDeleteOnAction(e);

                tblIssueBook.getItems().clear();
                getAllIssueToTable(searchText);
            }
        });
    }

    private void setCellValueFactory() {
        colIssueId.setCellValueFactory(new PropertyValueFactory<IssueTM,String>("Issue_id"));
        colMemberId.setCellValueFactory(new PropertyValueFactory<IssueTM,String>("Mem_id"));
        colMemberId.setCellValueFactory(new PropertyValueFactory<Issue,String >("isbn"));
        colIssueDate.setCellValueFactory(new PropertyValueFactory<IssueTM,String>("issue_date"));
        colReturnDate.setCellValueFactory(new PropertyValueFactory<IssueTM,String>("returnDate"));
        colAction.setCellValueFactory(new PropertyValueFactory<IssueTM,Button>("btn"));
    }

    private void generateNextIssueId() {
        try {
            String nextId = IssueModel.generateNextIssueId();
            txtIssueId.setText(nextId);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void setDataToTextFields(IssueTM issueTM) {
        txtIssueId.setText(issueTM.getIssue_id());
        txtMemberId.setText(issueTM.getMem_id());
        cmbBooks.setValue(issueTM.getIsbn());
        dtIssuedt.setValue(LocalDate.parse(issueTM.getIssue_date()));
        dtReturndt.setValue(LocalDate.parse(issueTM.getReturnDate()));
    }

    public void btnAddRecordOnAction(ActionEvent actionEvent) {
        Issue issue = collectData();
        try {
            boolean b = IssueModel.issueBook(issue);
            if(b){
                new Alert(Alert.AlertType.INFORMATION,"Issue Data Added Success").show();
                tblIssueBook.getItems().clear();
                getAllIssueToTable(searchText);
            }else {
                new Alert(Alert.AlertType.ERROR,"Data Adding Failed").show();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    //************************

    @FXML
    void txtIssueIdOnAction(ActionEvent event) {
        String Issue_id= txtIssueId.getText();
        if(Regex.validateIssueId(Issue_id)){
            dtIssuedt.requestFocus();

        }else {
            new Alert(Alert.AlertType.WARNING, "No Matching ID!!!").show();
        }

    }
    private Issue collectData(){
        String book = cmbBooks.getSelectionModel().getSelectedItem().toString();
        return new Issue(txtIssueId.getText(),dtIssuedt.getValue().toString(),txtMemberId.getText(),dtReturndt.getValue().toString(),book);

    }

    public void cmbBooksOnAction(ActionEvent actionEvent) {
    }
}