package dashboard.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Inventory {

    private Long id;
    private String productCode;
    private String productName;
    private BigDecimal mrp;
    private BigDecimal rate;
    private String batch;
    private Long stock;
    private Long deal;
    private Long free;
    private Date expiry;
    private String company;
    private String supplierName;
    private Date createdAt;
}
