package lk.ijse.bookworm.model;

import javafx.scene.control.Alert;
import lk.ijse.bookworm.dto.Employee;
import lk.ijse.bookworm.util.CrudUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeModel {

    public static boolean add(String Emp_id, String Name, String Email, String Contact, String Job_role) throws SQLException {
        String sql = "INSERT INTO employee (Emp_id, Name, Email, Contact,Job_Role) VALUES(?, ?, ?, ?,?)";

        return CrudUtil.execute(sql, Emp_id, Name, Email, Contact, Job_role);
        }

   public static Employee search(String emp_id) throws SQLException {
        String sql = "SELECT * FROM employee WHERE  Emp_id= ?";
        ResultSet resultSet = CrudUtil.execute(sql, emp_id);

        if(resultSet.next()) {
            String Emp_id = resultSet.getString(1);
            String Name  = resultSet.getString(2);
            String Email= resultSet.getString(3);
            String Contact = resultSet.getString(4);
            String Job_Role = resultSet.getString(5);

            return new Employee(Emp_id, Name, Email, Contact,Job_Role);
        }
        return null;
    }

    public static boolean delete(String Emp_id) throws SQLException {
        String sql ="DELETE FROM Employee WHERE Emp_id = ?" ;

        return CrudUtil.execute(sql,Emp_id);
    }

    public static boolean update(Employee employee) throws SQLException {
        String sql = "UPDATE employee SET Name = ?, Email  = ?, Contact = ? , Job_Role = ?  WHERE Emp_id = ?";
        return CrudUtil.execute(sql,employee.getName(),employee.getEmail(),employee.getContact(),employee.getJob_Role(), employee.getEmp_id());
    }

    public static List<Employee> searchAll() throws SQLException{
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM employee");
        List<Employee> dataList = new ArrayList<>();

        while (resultSet.next()) {
            dataList.add(new Employee(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5)
            ));
        }
        return dataList;
    }
}

