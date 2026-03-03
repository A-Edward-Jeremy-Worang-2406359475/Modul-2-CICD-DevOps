package id.ac.ui.cs.advprog.eshop.repository;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UuidCarIdGenerator implements CarIdGenerator {
    @Override
    public String generate() {
        return UUID.randomUUID().toString();
    }
}