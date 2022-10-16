package dashboard.util;

import dashboard.errorhandler.ErrorCode;
import dashboard.errorhandler.InvalidInputException;
import dashboard.model.Inventory;
import liquibase.repackaged.com.opencsv.CSVReader;
import liquibase.repackaged.com.opencsv.exceptions.CsvValidationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Slf4j
public class FileParseUtil {

    private static final String EXCEL_SHEET_CONTENT_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    private static final String CSV_CONTENT_TYPE = "text/csv";

    private static final String[] columnList = {"code", "name", "batch", "stock", "deal", "free", "mrp", "rate", "exp", "company", "supplier"};

    public static List<Inventory> parseFile(MultipartFile file) {
        String contentType = file.getContentType();
        log.info("Content type of the file : {}", contentType);
        if (contentType == null) throw new InvalidInputException(ErrorCode.SPL1004);
        switch(contentType) {
            case EXCEL_SHEET_CONTENT_TYPE:
                return parseExcelFile(file);
            case CSV_CONTENT_TYPE:
                return parseCsvFile(file);
        }
        throw new InvalidInputException(ErrorCode.SPL1004);
    }

    public static List<Inventory> parseCsvFile(MultipartFile file) {
        try {
            int processingRow = 0;
            List<Inventory> inventoryList = new ArrayList<>();
            CSVReader csvReader = new CSVReader(new InputStreamReader(file.getInputStream()));
            String[] nextLine = csvReader.readNext();
            validateCsvFormat(nextLine);
            while((nextLine = csvReader.readNext()) != null) {
                try {
                    processingRow++;
                    String productCode = nextLine[0];
                    String productName = nextLine[1];
                    String batch = nextLine[2];
                    long stock = Long.parseLong(nextLine[3]);
                    long deal = Long.parseLong(nextLine[4]);
                    long free = Long.parseLong(nextLine[5]);
                    BigDecimal mrp = new BigDecimal(nextLine[6]);
                    BigDecimal rate = new BigDecimal(nextLine[7]);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    Date expire = dateFormat.parse(nextLine[8]);
                    String company = nextLine[9];
                    String supplierName = nextLine[10];

                    Inventory inventory = Inventory.builder()
                            .batch(batch)
                            .stock(stock)
                            .deal(deal)
                            .free(free)
                            .productCode(productCode)
                            .productName(productName)
                            .company(company)
                            .expiry(expire)
                            .mrp(mrp)
                            .rate(rate)
                            .supplierName(supplierName)
                            .build();
                    log.info("Row no {}, and inventory data {}", processingRow, inventory);

                    inventoryList.add(inventory);
                } catch(Exception ex) {
                    log.error("Exception occurred while parsing row {}, : {}", processingRow, ex.getMessage(), ex);
                }
            }
            return inventoryList;
        } catch(IOException ioException) {
            log.error("Exception occurred while reading csv file : {}", ioException.getMessage(), ioException);
            throw new InvalidInputException(ErrorCode.SPL1000);
        } catch(CsvValidationException exception) {
            log.error("Exception occurred while parsing csv file : {}", exception.getMessage(), exception);
            throw new InvalidInputException(ErrorCode.SPL1001);
        }
    }

    public static void validateCsvFormat(String[] columns) {
        if (columns.length != columnList.length) throw new InvalidInputException(ErrorCode.SPL1002);
        for (int i = 0; i<columnList.length; i++) {
            if (!columns[i].equals(columnList[i])) throw new InvalidInputException(ErrorCode.SPL1002);
        }
        log.info("CSV file column names validated");
    }

    public static List<Inventory> parseExcelFile(MultipartFile file) {
        try {
            XSSFWorkbook wb = new XSSFWorkbook(file.getInputStream());
            XSSFSheet sheet = wb.getSheetAt(0);
            Iterator<Row> itr = sheet.iterator();
            validateExcelFormat(itr.next());
            int processingRow = 0;
            List<Inventory> inventoryList = new ArrayList<>();
            while(itr.hasNext()) {
                try {
                    processingRow++;
                    Row row = itr.next();
                    String productCode = row.getCell(0).getCellType().equals(CellType.STRING) ? row.getCell(0).getStringCellValue() : String.valueOf(row.getCell(0).getNumericCellValue());
                    String productName = row.getCell(1).getCellType().equals(CellType.STRING) ? row.getCell(1).getStringCellValue() : String.valueOf(row.getCell(1).getNumericCellValue());
                    String batch = row.getCell(2).getCellType().equals(CellType.STRING) ? row.getCell(2).getStringCellValue() : String.valueOf(row.getCell(2).getNumericCellValue());
                    long stock = (long) row.getCell(3).getNumericCellValue();
                    long deal = (long) row.getCell(4).getNumericCellValue();
                    long free = (long) row.getCell(5).getNumericCellValue();
                    BigDecimal mrp = BigDecimal.valueOf(row.getCell(6).getNumericCellValue());
                    BigDecimal rate = BigDecimal.valueOf(row.getCell(7).getNumericCellValue());
                    Date expire = row.getCell(8).getDateCellValue();
                    String company = row.getCell(9).getStringCellValue();
                    String supplierName = row.getCell(10).getStringCellValue();

                    Inventory inventory = Inventory.builder()
                            .batch(batch)
                            .stock(stock)
                            .deal(deal)
                            .free(free)
                            .productCode(productCode)
                            .productName(productName)
                            .company(company)
                            .expiry(expire)
                            .mrp(mrp)
                            .rate(rate)
                            .supplierName(supplierName)
                            .build();
                    log.info("Row no {}, and inventory data {}", processingRow, inventory);

                    inventoryList.add(inventory);
                } catch(Exception ex) {
                    log.error("Exception occurred while parsing row {}, : {}", processingRow, ex.getMessage(), ex);
                }
            }
            return inventoryList;
        } catch(IOException ioException) {
            log.error("Exception occurred while reading excel file : {}", ioException.getMessage(), ioException);
            throw new InvalidInputException(ErrorCode.SPL1003);
        }
    }

    public static void validateExcelFormat(Row row) {
        if (row.getPhysicalNumberOfCells() != columnList.length) throw new InvalidInputException(ErrorCode.SPL1005);
        for (int i = 0; i<columnList.length; i++) {
            if (!row.getCell(i).getStringCellValue().equals(columnList[i])) throw new InvalidInputException(ErrorCode.SPL1005);
        }
        log.info("Excel sheet column names validated");
    }
}
