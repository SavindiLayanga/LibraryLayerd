package lk.ijse.bookworm.dto;

import lombok.*;

import java.sql.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Data

public class Issue {
    private String Issue_id;
    private String issue_date;
    private String Mem_id;
    private String return_date;
    private String bookId;
}
