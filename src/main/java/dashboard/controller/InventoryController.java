package dashboard.controller;

import dashboard.model.InventoryResponse;
import dashboard.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("v1/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    @Autowired
    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public InventoryResponse addInventory(@RequestParam("file") MultipartFile file) {
        return inventoryService.addInventory(file);
    }

    @GetMapping()
    public InventoryResponse fetchInventory(@RequestParam(value = "supplierName", required = false) String supplierName,
                                            @RequestParam(value = "productName", required = false) String productName,
                                            @RequestParam(value = "notExpired", required = false, defaultValue = "false") boolean notExpired,
                                            @RequestParam(value = "pageNo", required = false, defaultValue = "0") int pageNo,
                                            @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize) {
        return inventoryService.fetchInventory(supplierName, productName, notExpired, pageNo, pageSize);
    }
}
