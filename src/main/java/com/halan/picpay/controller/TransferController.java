package com.halan.picpay.controller;

import com.halan.picpay.controller.dto.TransferRequest;
import com.halan.picpay.entity.Transfer;
import com.halan.picpay.service.TransferService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransferController {

    private final TransferService transferService;

    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @PostMapping("/transfer")
    public ResponseEntity<Transfer> transfer(@RequestBody @Valid TransferRequest request) {
        var response = transferService.transfer(request);
        return ResponseEntity.ok(response);
    }
}
