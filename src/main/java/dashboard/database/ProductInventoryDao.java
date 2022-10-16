package dashboard.database;

import dashboard.model.Inventory;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

public interface ProductInventoryDao {

    @SqlUpdate("INSERT INTO product_inventory(product_name, product_code, batch, stock, deal, free, mrp, rate, expiry, supplier_name, company) " +
            "VALUES(:productName, :productCode, :batch, :stock, :deal, :free, :mrp, :rate, :expiry, :supplierName, :company)")
    @GetGeneratedKeys
    long insert(@BindBean Inventory inventory);

    @SqlQuery("SELECT product_name, product_code, batch, stock, deal, free, mrp, rate, expiry, supplier_name, company FROM product_inventory " +
            "WHERE (:supplierNameFilter OR supplier_name = :supplierName) " +
            "AND (:productNameFilter OR product_name = :productName) " +
            "AND (:expiryFilter OR expiry >= CURRENT_DATE) " +
            "AND stock > 0 " +
            "ORDER BY id DESC " +
            "OFFSET :offset " +
            "LIMIT :limit")
    @RegisterBeanMapper(Inventory.class)
    List<Inventory> fetchInventory(boolean supplierNameFilter,
                                   String supplierName,
                                   boolean productNameFilter,
                                   String productName,
                                   boolean expiryFilter,
                                   int offset,
                                   int limit);

}
