package com.halan.picpay.service;

import com.halan.picpay.client.NotificationClient;
import com.halan.picpay.entity.Transfer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    private final NotificationClient notificationClient;

    public NotificationService(NotificationClient notificationClient) {
        this.notificationClient = notificationClient;
    }

    public void sendNotification(Transfer transfer) {
        try {
            logger.info("Sending notification to {}", transfer.getReceiver());

            var response = notificationClient.sendNotification(transfer);

            if (response.getStatusCode().isError()) {
                logger.error("Error sending notification, status code is not OK");
            }
        } catch (Exception e) {
            logger.error("Error sending notification", e);
        }
    }
}
