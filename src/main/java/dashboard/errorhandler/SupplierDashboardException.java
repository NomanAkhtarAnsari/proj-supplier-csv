package dashboard.errorhandler;

import lombok.Data;

@Data
public abstract class SupplierDashboardException extends RuntimeException{

    String errorCode;
    String message;

    public SupplierDashboardException(String errorCode,
                                      String message) {
        super(message);
        this.errorCode = errorCode;
        this.message = message;
    }
}
