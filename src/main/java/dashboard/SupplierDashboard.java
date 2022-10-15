package dashboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SupplierDashboard {

    public static void main(String[] args) {
        SpringApplication.run(SupplierDashboard.class, args);
    }
}
