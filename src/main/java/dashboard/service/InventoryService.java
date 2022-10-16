package dashboard.service;

import dashboard.database.ProductInventoryDao;
import dashboard.model.Inventory;
import dashboard.model.InventoryResponse;
import dashboard.util.FileParseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class InventoryService {

    private final ProductInventoryDao productInventoryDao;

    @Autowired
    public InventoryService(ProductInventoryDao productInventoryDao) {
        this.productInventoryDao = productInventoryDao;
    }

    public InventoryResponse addInventory(MultipartFile file) {
        List<Inventory> inventoryList = FileParseUtil.parseFile(file);
        for (Inventory inventory : inventoryList) {
            Long inventoryId = productInventoryDao.insert(inventory);
            log.info("Inventory inserted with id : {}", inventoryId);
        }
        return InventoryResponse.builder().rowUpdated(inventoryList.size()).build();
    }

    public InventoryResponse fetchInventory(String supplierName,
                                            String productName,
                                            boolean notExpired,
                                            int pageNo,
                                            int pageSize) {
        boolean supplierFilter = applyFilter(supplierName);
        supplierName = supplierFilter ? supplierName.trim() : "";

        boolean productFilter = applyFilter(productName);
        productName = productFilter ? productName.trim() : "";

        pageNo = Math.max(0, pageNo);
        pageSize = Math.max(0, pageSize);

        int offset = pageNo*pageSize;
        int limit = pageSize;
        
        log.info("Supplier name {}, supplier filter {}, product name {}, product filter {}, notExpired {}, offset {}, limit {}",
                supplierName, supplierFilter, productName, productFilter, notExpired, offset, limit);
        List<Inventory> inventoryList = productInventoryDao.fetchInventory(!supplierFilter,
                supplierName,
                !productFilter,
                productName,
                !notExpired,
                offset,
                limit);

        return InventoryResponse.builder().inventoryList(inventoryList).build();
    }

    private boolean applyFilter(String filterKey) {
        return Objects.nonNull(filterKey) && filterKey.trim().length()>1;
    }
}
