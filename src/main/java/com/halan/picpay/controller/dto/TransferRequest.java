package com.halan.picpay.controller.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record TransferRequest(
        @DecimalMin("0.01") @NotNull BigDecimal amount,
        @NotNull Long payer,
        @NotNull Long payee
) {
}
