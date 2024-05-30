package com.halan.picpay.config;

import com.halan.picpay.entity.WalletType;
import com.halan.picpay.repository.WalletTypeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class DataLoader implements CommandLineRunner {
    private final WalletTypeRepository repository;

    public DataLoader(WalletTypeRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) throws Exception {
        Arrays.stream(WalletType.Enum.values())
                .forEach((walletType -> {
                    repository.save(walletType.get());
                }));
    }
}
