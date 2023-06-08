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
import lk.ijse.bookworm.db.DBConnection;
import lk.ijse.bookworm.dto.Author;
import lk.ijse.bookworm.dto.Book;
import lk.ijse.bookworm.dto.Publisher;
import lk.ijse.bookworm.dto.tm.AuthorTM;
import lk.ijse.bookworm.dto.tm.BookTM;
import lk.ijse.bookworm.dto.tm.PublisherTM;
import lk.ijse.bookworm.model.AuthorModel;
import lk.ijse.bookworm.model.BookModel;
import lk.ijse.bookworm.model.PublisherModel;
import lk.ijse.bookworm.util.Regex;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.net.URL;
import java.nio.file.FileSystems;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import static java.lang.String.valueOf;

public class BookController implements Initializable {
    @FXML
    private Button btnBookAdd;

    @FXML
    private Button btnBookUpdate;

    @FXML
    private Button btnBookDelete;

    @FXML
    private Button btnBookSearch;

    @FXML
    private Button btnGetReport;

    @FXML
    private TextField txtBookisbn;

    @FXML
    private TextField txtBookTitle;

    @FXML
    private TextField txtBookNoOfPages;

    @FXML
    private TextField txtBookPrice;

    @FXML
    private TextField txtBookQuantity;

    @FXML
    private TableView<BookTM> tblBooks;

    @FXML
    private TableColumn<?, ?> colISBN;

    @FXML
    private TableColumn<?, ?> colTitle;

    @FXML
    private TableColumn<?, ?> colNoOfPages;

    @FXML
    private TableColumn<?, ?> colPrice;

    @FXML
    private TableColumn<?, ?> colPublisherID;

    @FXML
    private TableColumn<?, ?> colQuantity;

    @FXML
    private TableColumn<?, ?> colAction;

    @FXML
    private ComboBox<String> cmbBookPublisher;

    private String searchText = "";

    private ObservableList<BookTM> obList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadPublisherIds();
        setCellValueFactory();
        getAllBooksToTable(searchText);

        tblBooks.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> { //Add ActionListener to selected column and display text field values
            //Check select value is not null
            if(null!=newValue) { //newValue!=null --> Get more time to compare (newValue object compare)
                // btnSaveSupplier.setText("Update Supplier");
                setDataToTextFields((BookTM) newValue); //Set data to text field of selected row data of table
            }
        });
    }

    private void setDataToTextFields(BookTM bookTM) {
        txtBookisbn.setText(bookTM.getISBN());
        txtBookTitle.setText(bookTM.getTitle());
        txtBookNoOfPages.setText(String.valueOf(bookTM.getNo_Of_Pages()));
        txtBookPrice.setText(String.valueOf(bookTM.getPrice()));
        cmbBookPublisher.setValue(String.valueOf(bookTM.getPub_id()));
        txtBookQuantity.setText(String.valueOf(bookTM.getQuantity()));
    }

    private void getAllBooksToTable(String searchText) {
        try {
            List<Book> bookList = BookModel.searchAll();
            for( Book book : bookList) {
                if (book.getISBN().contains(searchText) || book.getTitle().contains(searchText)){  //Check pass text contains of the supName
                    JFXButton btnDel=new JFXButton("Delete");
                    btnDel.setAlignment(Pos.CENTER);
                    btnDel.setStyle("-fx-background-color: #686de0; ");
                    btnDel.setCursor(Cursor.HAND);

                    BookTM tm=new BookTM(
                            book.getISBN(),
                            book.getTitle(),
                            book.getPrice(),
                            book.getNo_Of_Pages(),
                            book.getPub_id(),
                            book.getQuantity()
                            ,btnDel);

                    obList.add(tm);

                    setDeleteButtonTableOnAction(btnDel);
                }
            }
            tblBooks.setItems(obList);
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
                txtBookPrice.setText(tblBooks.getSelectionModel().getSelectedItem().getISBN());
                btnBookSearchOnAction(e);
                btnBookDeleteOnAction(e);
                //btnAuthorDeleteOnAction(e);

                tblBooks.getItems().clear();
                getAllBooksToTable(searchText);
            }
        });
    }

    private void setCellValueFactory() {
        colISBN.setCellValueFactory(new PropertyValueFactory<>("ISBN"));
        colTitle.setCellValueFactory(new PropertyValueFactory<>("Title"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("Price"));
        colNoOfPages.setCellValueFactory(new PropertyValueFactory<>("No_Of_Pages"));
        colPublisherID.setCellValueFactory(new PropertyValueFactory<>("Pub_id"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("Quantity"));
        colAction.setCellValueFactory(new PropertyValueFactory<>("btn"));
    }

    //**********************************************

    private void loadPublisherIds() {
        try {
            ObservableList<String> obList = FXCollections.observableArrayList();
            List<String> publishersIds = PublisherModel.getPublishersIds();

            for (String Pub_id: publishersIds){
                obList.add(Pub_id);
            }
            cmbBookPublisher.setItems(obList);
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "SQL Error!").show();
        }
    }

    @FXML
        void btnBookAddOnAction(ActionEvent event){
            String isbn = txtBookisbn.getText();
            String Title = txtBookTitle.getText();
            double Price = Double.parseDouble(txtBookPrice.getText());
            int No_Of_Pages = Integer.parseInt(txtBookNoOfPages.getText());
            String Pub_id = cmbBookPublisher.getValue();
            int Quantity = Integer.parseInt(txtBookQuantity.getText());

            try {
                boolean isSaved = BookModel.Add(isbn, Title, Price, No_Of_Pages, Pub_id,Quantity);
                if (isSaved) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Book saved!!!").show();
                    tblBooks.getItems().clear();
                    getAllBooksToTable(searchText);
                }
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, "OOPSSS!! something happened!!!").show();
            }
            txtBookisbn.clear();
            txtBookTitle.clear();
            txtBookNoOfPages.clear();
            txtBookNoOfPages.clear();
            //cmbBookPublisher.clear();
        }

        @FXML
        public void btnBookSearchOnAction (ActionEvent event){
            String isbn= txtBookisbn.getText();
            try {
                Book book = BookModel.search(isbn);
                if (book != null) {
                    txtBookisbn.setText(book.getISBN());
                    txtBookTitle.setText(book.getTitle());
                    txtBookPrice.setText(valueOf(book.getPrice()));
                    txtBookNoOfPages.setText(valueOf(book.getNo_Of_Pages()));
                    //cmbBookPublisher.setValue(String.valueOf(book.getPub_id()));

                } else {
                    new Alert(Alert.AlertType.WARNING, "no book found :(").show();
                }
            } catch (SQLException e) {
                //System.out.println(e);
                new Alert(Alert.AlertType.ERROR, "oops! something went wrong :(").show();
            }
        }

        @FXML
        void btnBookDeleteOnAction (ActionEvent event){
           String ISBN = txtBookisbn.getText();

            try {
              boolean isDeleted = BookModel.delete(ISBN);
              if (isDeleted) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Book deleted !").show();
            }
          } catch (SQLException e) {
              new Alert(Alert.AlertType.ERROR, "something happened !").show();
            }
        }

        @FXML
        void btnBookUpdateOnAction (ActionEvent actionEvent){
        String ISBN = txtBookisbn.getText();
        String Title = txtBookTitle.getText();
        double Price = Double.parseDouble(txtBookPrice.getText());
        int No_Of_Pages = Integer.parseInt(txtBookNoOfPages.getText());
        String Pub_id = cmbBookPublisher.getValue();
        int Quantity= Integer.parseInt(txtBookQuantity.getText());

        var book = new Book(ISBN, Title, Price, No_Of_Pages,Pub_id,Quantity);

        try {
            boolean isUpdated = BookModel.update(book);
            if (isUpdated) {
                new Alert(Alert.AlertType.CONFIRMATION, "huree! Book Updated!").show();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.CONFIRMATION, "SQL Error!").show();
        }
    }

    //******************************************

    @FXML
    void txtBookisbnOnAction(ActionEvent event) {
        String ISBN= txtBookisbn.getText();
        if(Regex.validateISBN(ISBN)){
            txtBookTitle.requestFocus();

        }else {
            new Alert(Alert.AlertType.WARNING, "No Matching ISBN!!!").show();
        }
    }

    @FXML
    void btnGetReportOnAction(ActionEvent event) {
        Thread t1=new Thread(
                () -> {
                    String billPath = "F:\\Library Final Project\\library-management-system\\src\\main\\resources\\Report\\bookReport.jrxml";
                    String sql="select * from book";
                    String path = FileSystems.getDefault().getPath("/Report/bookReport.jrxml").toAbsolutePath().toString();
                    JasperDesign jasdi = null;
                    try {
                        jasdi = JRXmlLoader.load(billPath);
                        JRDesignQuery newQuery = new JRDesignQuery();
                        newQuery.setText(sql);
                        jasdi.setQuery(newQuery);
                        JasperReport js = JasperCompileManager.compileReport(jasdi);
                        JasperPrint jp = JasperFillManager.fillReport(js, null, DBConnection.getInstance().getConnection());
                        JasperViewer viewer = new JasperViewer(jp, false);
                        viewer.show();
                    } catch (JRException e) {
                        e.printStackTrace();
                    } catch (SQLException exception) {
                        exception.printStackTrace();
                    }

                });

        t1.start();

    }
}

