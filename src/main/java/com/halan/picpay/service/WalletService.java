package com.halan.picpay.service;

import com.halan.picpay.controller.dto.CreateWalletRequest;
import com.halan.picpay.entity.Wallet;
import com.halan.picpay.exception.WalletDataAlreadyExistsException;
import com.halan.picpay.repository.WalletRepository;
import org.springframework.stereotype.Service;

@Service
public class WalletService {

    private final WalletRepository walletRepository;

    public WalletService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    public Wallet createWallet(CreateWalletRequest request) {
        var walletDb = walletRepository.findByCpfCnpjOrEmail(request.cpfCnpj(), request.email());
        if (walletDb.isPresent()) {
            throw new WalletDataAlreadyExistsException("CPF/CNPJ or email already exists!");
        }
        return walletRepository.save(request.toWallet());
    }
}
