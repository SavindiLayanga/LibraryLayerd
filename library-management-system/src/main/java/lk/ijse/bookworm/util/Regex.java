package lk.ijse.bookworm.util;

public class Regex {
    private static final String USERNAME_REGEX = "^[A-Za-z0-9]{3,}$";
    private static final String PASSWORD_REGEX = "[A-zZ0-9]{8,20}$";
    private static final String EmployeeId_REGEX = "E\\d{4}\n";
    private static final String AuthorId_REGEX = "A\\d{4}\n";
    private static final String ISBN_REGEX = "B\\d{4}\n";
    private static final String MemberId_REGEX = "M\\d{4}\n";
    private static final String PublisherId_REGEX = "P\\d{4}\n";
    private static final String FineId_REGEX = "F\\d{4}\n";
    private static final String ReturnBId_REGEX = "R\\d{4}\n";
    private static final String IssueId_REGEX = "I\\d{4}\n";
    private static final String MOBILE_REGEX = "^\\+?\\d{10}$";
    private static final String MEMBER_CONTACT_REGEX = "^\\+?\\d{10}$";
    private static final String PUBLISHER_CONTACT_REGEX = "^\\+?\\d{10}$";


    public static boolean validateUsername(String username) {
        return username.matches(USERNAME_REGEX);
    }

    public static boolean validatePassword(String password) {
        return password.matches(PASSWORD_REGEX);
    }

    public static boolean validateEmployeeId(String emp_id) {
        return emp_id.matches(EmployeeId_REGEX);
    }

    public static boolean validateAuthorid(String author_id) {
        return author_id.matches(AuthorId_REGEX);
    }

    public static boolean validateISBN(String isbn) {
        return isbn.matches(ISBN_REGEX);
    }

    public static boolean validateMem_Id(String mem_id) {
        return mem_id.matches(MemberId_REGEX);
    }

    public static boolean validatePublisherId(String pub_id) {
        return pub_id.matches(PublisherId_REGEX);
    }

    public static boolean validateFineId(String fine_id) {
        return fine_id.matches(FineId_REGEX);
    }

    public static boolean validateReturnBId(String returnBId) {
        return returnBId.matches(ReturnBId_REGEX);
    }

    public static boolean validateIssueId(String issue_id) {return issue_id.matches(IssueId_REGEX);}



    public static boolean validateContact(String contact) {
            return contact.matches(MOBILE_REGEX);
        }

    public static boolean validateMemContact(String contact) {
        return contact.matches(MEMBER_CONTACT_REGEX);
    }

    public static boolean validatePublisherContact(String contact) {
        return contact.matches(PUBLISHER_CONTACT_REGEX);
    }
}

