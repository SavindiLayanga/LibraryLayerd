package lk.ijse.bookworm.model;

import lk.ijse.bookworm.dto.Author;
import lk.ijse.bookworm.dto.Employee;
import lk.ijse.bookworm.dto.Member;
import lk.ijse.bookworm.util.CrudUtil;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MemberModel {
    public static boolean add(String memberID, String name, String address, String email, String contact, String member_date, String expire_date) throws SQLException {
        String sql = "INSERT INTO member (Mem_id, Name, address, Email, Contact, member_date, expire_date) VALUES(?, ?, ?, ?,?,?,?)";

        return CrudUtil.execute(sql,memberID, name, address, email, contact,member_date,expire_date);
    }

    public static Member search(String mem_id) throws SQLException {
        String sql = "SELECT * FROM member WHERE  Mem_id = ?";
        ResultSet resultSet = CrudUtil.execute(sql, mem_id);

        if(resultSet.next()) {
            String MemberID = resultSet.getString(1);
            String Name  = resultSet.getString(2);
            String address= resultSet.getString(3);
            String Email = resultSet.getString(4);
            String Contact = resultSet.getString(5);
            String member_date = resultSet.getString(4);
            String expire_date = resultSet.getString(5);

            return new Member(MemberID, Name, address, Email, Contact, member_date, expire_date);
        }
        return null;
    }

    public static boolean update(Member member) throws SQLException{
        String sql = "UPDATE Member SET Name = ?, address  = ?, Email = ? , Contact = ?, member_date = ?, expire_date = ? WHERE Mem_id = ?";
        return CrudUtil.execute(sql,member.getName(),member.getAddress(),member.getEmail(),member.getContact(), member.getMember_date(),member.getExpire_date(),member.getMem_id());
    }

    public static boolean delete(String memberID) throws SQLException {
        String sql ="DELETE FROM m" +
                "ember WHERE Mem_id = ?" ;

        return CrudUtil.execute(sql,memberID);
    }

    public static List<Member> searchAll() throws SQLException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM member");
        List<Member> dataList = new ArrayList<>();

        while (resultSet.next()) {
            dataList.add(new Member(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5),
                    resultSet.getString(6),
                    resultSet.getString(7)
            ));
        }
        return dataList;
    }
}

