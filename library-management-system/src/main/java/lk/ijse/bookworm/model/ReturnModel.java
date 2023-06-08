package lk.ijse.bookworm.model;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import lk.ijse.bookworm.dto.Issue;
import lk.ijse.bookworm.dto.Return;
import lk.ijse.bookworm.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReturnModel {
    public static String generateNextReturnId() throws SQLException,ClassNotFoundException {
        String lastReturn_id=generateReturnId();
        if(lastReturn_id==null){
            return "R001";
        }else{
            String[] split=lastReturn_id.split("[R]");
            int lastDigits=Integer.parseInt(split[1]);
            lastDigits++;
            String newReturn_id=String.format("R%03d", lastDigits);
            return newReturn_id;
        }
    }

    private static String generateReturnId() throws SQLException {
        ResultSet rs = CrudUtil.execute("SELECT ReturnB_id FROM returnb ORDER BY ReturnB_id DESC LIMIT 1");
        if(rs.next()){
            return rs.getString(1);
        }
        return null;
    }


    public static ArrayList<Issue> searchAll() throws SQLException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM issue");
        ArrayList<Issue> dataList = new ArrayList<>();

        while (resultSet.next()) {
            dataList.add(new Issue(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5)
            ));
        }
        return dataList;
    }

    public static ArrayList<Return> searchAllReturn() throws SQLException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM returnb");
        ArrayList<Return> dataList = new ArrayList<>();

        while (resultSet.next()) {
            dataList.add(new Return(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3)
            ));
        }
        return dataList;
    }

    public static boolean Add(Return r1) throws SQLException {
        String sql = "INSERT INTO returnb VALUES(?,?,?)";

        return CrudUtil.execute(sql, r1.getReturnB_id(),r1.getReturn_date(),r1.getIssueBook_id());
    }
}
