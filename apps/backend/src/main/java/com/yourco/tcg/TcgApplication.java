package com.yourco.tcg;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * TODO (class):
 * - Application entry point. Holds main(). Nothing else.
 *
 * Annotations / types: @SpringBootApplication
 */

@SpringBootApplication
public class TcgApplication {

    public static void main(String[] args) {
        SpringApplication.run(TcgApplication.class, args);
    }
}