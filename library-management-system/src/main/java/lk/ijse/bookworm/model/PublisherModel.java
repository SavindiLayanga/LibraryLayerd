package lk.ijse.bookworm.model;

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
import lk.ijse.bookworm.db.DBConnection;
import lk.ijse.bookworm.dto.Author;
import lk.ijse.bookworm.dto.Employee;
import lk.ijse.bookworm.dto.Publisher;
import lk.ijse.bookworm.dto.tm.AuthorTM;
import lk.ijse.bookworm.dto.tm.PublisherTM;
import lk.ijse.bookworm.util.CrudUtil;
import lk.ijse.bookworm.util.Regex;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class PublisherModel {
    public static boolean add(String Pub_id, String Name,String address, String Email, String Contact) throws SQLException {
        String sql = "INSERT INTO publisher (Pub_id, Name, address, Email, Contact) VALUES(?, ?, ?, ?, ?)";

        return CrudUtil.execute(sql, Pub_id, Name, address, Email, Contact);
    }

    public static Publisher search(String publisherID) throws SQLException {
        String sql = "SELECT * FROM publisher WHERE  Pub_id= ?";
        ResultSet resultSet = CrudUtil.execute(sql, publisherID);

        if(resultSet.next()) {
            String Pub_id = resultSet.getString(1);
            String Name  = resultSet.getString(2);
            String address= resultSet.getString(3);
            String Email = resultSet.getString(4);
            String Contact = resultSet.getString(5);

            return new Publisher( Pub_id, Name, address, Email, Contact);
        }
        return null;
    }

    public static boolean delete(String Pub_id) throws SQLException {
        String sql ="DELETE FROM publisher WHERE Pub_id = ?" ;

        return CrudUtil.execute(sql,Pub_id);
    }

    public static boolean update(Publisher publisher) throws SQLException {
        String sql = "UPDATE Publisher SET Name = ?, address = ?, Email  = ?, Contact=? WHERE Pub_id = ?";
        return CrudUtil.execute(sql,publisher.getName(),publisher.getAddress(),publisher.getEmail(),publisher.getContact(), publisher.getPub_id());
    }

    public static List<Publisher> searchAll() throws SQLException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM Publisher");
        List<Publisher> dataList = new ArrayList<>();

        while (resultSet.next()) {
            dataList.add(new Publisher(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5)
            ));
        }
        return dataList;
    }

    public static List<String> getPublishersIds() throws SQLException{
        Connection con = DBConnection.getInstance().getConnection();

        List<String> PublisherIds = new ArrayList<>();

        String sql = "SELECT Pub_id FROM Publisher";
        ResultSet resultSet = con.createStatement().executeQuery(sql);
        while(resultSet.next()) {
            PublisherIds.add(resultSet.getString(1));
        }
        return PublisherIds;

    }
}
