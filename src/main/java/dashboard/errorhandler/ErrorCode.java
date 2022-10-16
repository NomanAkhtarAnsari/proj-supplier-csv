package dashboard.errorhandler;

public enum ErrorCode {

    SPL1000("SPL-1000", "Csv file is not present"),
    SPL1001("SPL-1001", "Csv file is incorrect"),
    SPL1002("SPL-1002", "Invalid CSV format"),
    SPL1003("SPL-1003", "Excel file is not present"),
    SPL1004("SPL-1004", "Invalid file format"),
    SPL1005("SPL-1005", "Invalid Excel format");

    private final String code;
    private final String message;

    ErrorCode(String code,
              String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
