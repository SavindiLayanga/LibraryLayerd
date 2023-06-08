package lk.ijse.bookworm.model;

import lk.ijse.bookworm.dto.Author;
import lk.ijse.bookworm.dto.Employee;
import lk.ijse.bookworm.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AuthorModel {
    public static boolean save(String authorID, String name) throws SQLException {
        String sql = "INSERT INTO author (Author_id,Name) VALUES(?, ?)";

        return CrudUtil.execute(sql, authorID, name);
    }

    public static Author search(String author_id) throws SQLException {
        String sql = "SELECT * FROM author WHERE Author_id = ?";
        ResultSet resultSet = CrudUtil.execute(sql, author_id);

        if(resultSet.next()) {
            String Author_id = resultSet.getString(1);
            String Name  = resultSet.getString(2);

            return new Author(Author_id,Name);
        }
        return null;
    }

    public static boolean update(Author author) throws SQLException{
        String sql = "UPDATE author SET Name = ? WHERE Author_id = ?";

        return CrudUtil.execute(sql, author.getName() ,author.getAuthor_id());
    }

    public static boolean delete(String Author_id) throws SQLException{
        String sql ="DELETE FROM Author WHERE Author_id = ?" ;

        return CrudUtil.execute(sql,Author_id);

    }

    public static List<Author> searchAll() throws SQLException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM author");
        List<Author> dataList = new ArrayList<>();

        while (resultSet.next()) {
            dataList.add(new Author(
                    resultSet.getString(1),
                    resultSet.getString(2)
            ));
        }
        return dataList;
    }
}
