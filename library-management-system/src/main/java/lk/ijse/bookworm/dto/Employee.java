package lk.ijse.bookworm.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString

public class Employee {
    private String Emp_id;
    private String Name;
    private String Email;
    private String Contact;
    private String Job_Role;
}
