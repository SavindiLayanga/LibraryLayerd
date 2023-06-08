package lk.ijse.bookworm.model;

import lk.ijse.bookworm.db.DBConnection;
import lk.ijse.bookworm.dto.Author;
import lk.ijse.bookworm.dto.Book;
import lk.ijse.bookworm.util.CrudUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookModel {
    public static boolean Add(String isbn, String Title, double Price, int No_Of_Pages, String Pub_id,int Quantity) throws SQLException {
        String sql = "INSERT INTO Book(isbn, Title,Price, No_Of_Pages,Pub_id,Quantity) VALUES(?, ?, ?, ?,?)";

        return CrudUtil.execute(sql, isbn, Title,Price, No_Of_Pages,Pub_id,Quantity);
    }

    public static Book search(String isbn) throws SQLException {
        String sql = "SELECT * FROM Book WHERE isbn = ?";
        ResultSet resultSet = CrudUtil.execute(sql, isbn);

        if(resultSet.next()) {
            String ISBN = resultSet.getString(1);
            String Title  = resultSet.getString(2);
            double Price= resultSet.getDouble(3);
            int No_Of_Pages = resultSet.getInt(4);
            String Pub_id = resultSet.getString(5);
            int Quantity = Integer.parseInt(resultSet.getString(6));

            return new Book(ISBN, Title, Price, No_Of_Pages,Pub_id,Quantity);
        }
        return null;
    }

    public static boolean delete(String isbn) throws SQLException {
        String sql = "DELETE FROM book WHERE isbn = ?";

        return CrudUtil.execute(sql,isbn);
    }

    public static boolean update(Book book) throws SQLException{
        String sql = "UPDATE book SET  Title = ?, Price = ?, No_Of_Pages  = ? , qty = ? ,Pub_id = ? WHERE isbn = ?";
        return CrudUtil.execute(sql,book.getTitle(),book.getPrice(),book.getNo_Of_Pages(),book.getQuantity(),book.getPub_id(),book.getISBN());
    }

    public static List<Book> searchAll() throws SQLException{
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM book");
        List<Book> dataList = new ArrayList<>();

        while (resultSet.next()) {
            dataList.add(new Book(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getDouble(3),
                    resultSet.getInt(4),
                    resultSet.getString(5),
                    resultSet.getInt(6)
            ));
        }
        return dataList;
    }

    public static boolean changeQty(String isbn) throws SQLException {
        return CrudUtil.execute("UPDATE book set qty=qty-1 where isbn = ? ",isbn);
    }

    public static List<String> getbookids() throws SQLException {
        Connection con = DBConnection.getInstance().getConnection();

        List<String> bookids = new ArrayList<>();

        String sql = "SELECT isbn FROM book";
        ResultSet resultSet = con.createStatement().executeQuery(sql);
        while(resultSet.next()) {
            bookids.add(resultSet.getString(1));
        }
        return bookids;
    }
}
