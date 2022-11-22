package edu.tr.eskisehir.giganews;

import com.vaadin.open.Open;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GigaNewsApplication {

    public static void main(String[] args) {
        SpringApplication.run(GigaNewsApplication.class, args);
        String url = "http://127.0.0.1:8080";
        System.out.println("Web server listening on " + url);
        try {
            Open.open(url);
        } catch (Exception e) {
            System.out.println("Failed to start browser, please start manually " + url);
        }
    }

}
