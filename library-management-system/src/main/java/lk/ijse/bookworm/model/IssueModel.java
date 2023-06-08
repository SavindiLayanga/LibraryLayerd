package lk.ijse.bookworm.model;

import lk.ijse.bookworm.db.DBConnection;
import lk.ijse.bookworm.dto.Author;
import lk.ijse.bookworm.dto.Book;
import lk.ijse.bookworm.dto.Issue;
import lk.ijse.bookworm.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class IssueModel {
    public static String generateNextIssueId() throws SQLException,ClassNotFoundException {
        String lastIssue_id=generateIssueId();
        if(lastIssue_id==null){
            return "I001";
        }else{
            String[] split=lastIssue_id.split("[I]");
            int lastDigits=Integer.parseInt(split[1]);
            lastDigits++;
            String newIssue_id=String.format("I%03d", lastDigits);
            return newIssue_id;
        }
    }

    private static String generateIssueId() throws SQLException, ClassNotFoundException{
        ResultSet rs = CrudUtil.execute("SELECT Issue_id FROM issue ORDER BY Issue_id DESC LIMIT 1");
        if(rs.next()){
            return rs.getString(1);
        }
        return null;
    }

    public static List<Issue> searchAll() throws SQLException{
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM issue");
        List<Issue> dataList = new ArrayList<>();

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

    public static boolean issueBook(Issue issue) throws SQLException {
        DBConnection.getInstance().getConnection().setAutoCommit(false);
        try {
            boolean isIssued = issue(issue);
            if (isIssued) {
                boolean isQtyUpdated = BookModel.changeQty(issue.getBookId());
                if (isQtyUpdated) {
                    DBConnection.getInstance().getConnection().commit();
                    return true;
                }
            }
            DBConnection.getInstance().getConnection().rollback();
            return false;
        }catch (SQLException e){
            DBConnection.getInstance().getConnection().rollback();
            throw e;
        }
    }

    private static boolean issue(Issue issue) throws SQLException {
        return CrudUtil.execute("INSERT INTO issue VALUES(?,?,?,?,?)",issue.getIssue_id(),issue.getIssue_date()
        ,issue.getMem_id(),issue.getReturn_date(),issue.getBookId());
    }
}
