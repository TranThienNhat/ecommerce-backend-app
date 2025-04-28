package com.nhat.ecommerce_backend.service.mail;

import com.nhat.ecommerce_backend.entity.Order;
import com.nhat.ecommerce_backend.entity.User;

public interface MailService {
    void sendOrderCreatedEmail(User user, Order order);
    void sendOrderStatusUpdatedEmail(User user, Order order);
}
