package com.halan.picpay.controller;

import com.halan.picpay.controller.dto.CreateWalletRequest;
import com.halan.picpay.entity.Wallet;
import com.halan.picpay.service.WalletService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WalletController {

    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @PostMapping("/wallets")
    public ResponseEntity<Wallet> createWallet(@RequestBody @Valid CreateWalletRequest request) {
        var wallet = walletService.createWallet(request);
        return ResponseEntity.ok(wallet);
    }
}
