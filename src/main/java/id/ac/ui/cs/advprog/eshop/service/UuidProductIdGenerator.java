package id.ac.ui.cs.advprog.eshop.service;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UuidProductIdGenerator implements ProductIdGenerator {
    @Override
    public String generate() {
        return UUID.randomUUID().toString();
    }
}