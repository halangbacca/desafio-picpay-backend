package com.halan.picpay.service;

import com.halan.picpay.controller.dto.TransferRequest;
import com.halan.picpay.entity.Transfer;
import com.halan.picpay.entity.Wallet;
import com.halan.picpay.exception.InsufficientBalanceException;
import com.halan.picpay.exception.TransferNotAllowedForWalletTypeException;
import com.halan.picpay.exception.TransferNotAuthorizedException;
import com.halan.picpay.exception.WalletNotFoundException;
import com.halan.picpay.repository.TransferRepository;
import com.halan.picpay.repository.WalletRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class TransferService {

    private final TransferRepository transferRepository;
    private final WalletRepository walletRepository;
    private final NotificationService notificationService;
    private final AuthorizationService authorizationService;

    public TransferService(TransferRepository transferRepository, WalletRepository walletRepository, NotificationService notificationService, AuthorizationService authorizationService) {
        this.transferRepository = transferRepository;
        this.walletRepository = walletRepository;
        this.notificationService = notificationService;
        this.authorizationService = authorizationService;
    }

    @Transactional
    public Transfer transfer(TransferRequest transferRequest) {
        var sender = walletRepository.findById(transferRequest.payer())
                .orElseThrow(() -> new WalletNotFoundException(transferRequest.payer()));

        var receiver = walletRepository.findById(transferRequest.payee())
                .orElseThrow(() -> new WalletNotFoundException(transferRequest.payee()));

        validateTransfer(transferRequest, sender);

        sender.debit(transferRequest.amount());
        receiver.credit(transferRequest.amount());

        var transfer = new Transfer(sender, receiver, transferRequest.amount());

        walletRepository.save(sender);
        walletRepository.save(receiver);
        var transferResult = transferRepository.save(transfer);

        CompletableFuture.runAsync(() -> notificationService.sendNotification(transferResult));

        return transferResult;
    }

    private void validateTransfer(TransferRequest transfer, Wallet sender) {
        if (!sender.isTransferAllowedForWalletType()) {
            throw new TransferNotAllowedForWalletTypeException();
        }

        if (!sender.isBalancerEqualOrGreaterThan(transfer.amount())) {
            throw new InsufficientBalanceException();
        }

        if (!authorizationService.isAuthorized(transfer)) {
            throw new TransferNotAuthorizedException();
        }
    }
}
