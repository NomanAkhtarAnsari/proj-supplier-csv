package dashboard.errorhandler;

public class InvalidInputException extends SupplierDashboardException {

    public InvalidInputException(String errorCode,
                                 String message) {
        super(errorCode, message);
    }

    public InvalidInputException(ErrorCode errorCode) {
        super(errorCode.getCode(), errorCode.getMessage());
    }
}
