package com.nhat.ecommerce_backend.service.mail;

import com.nhat.ecommerce_backend.entity.Order;
import com.nhat.ecommerce_backend.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailServiceImpl implements MailService{

    private final JavaMailSender javaMailSender;
    private final String fromEmail = "tranthiennhat20041@gmail.com";

    public void sendOrderCreatedEmail(User user, Order order) {
        String subject = "Đơn hàng của bạn đã được tạo thành công!";
        String content = String.format(
                "Xin chào %s,\n\nBạn đã đặt hàng thành công với tổng tiền: %s.\nMã đơn hàng: %s.\n\nCảm ơn bạn!",
                user.getFirstName(), order.getTotalPrice(), order.getId()
        );
        sendEmail(user.getEmail(), subject, content);
    }

    public void sendOrderStatusUpdatedEmail(User user, Order order) {
        String subject = "Cập nhật trạng thái đơn hàng";
        String content = String.format(
                "Xin chào %s,\n\nTrạng thái đơn hàng %s của bạn đã được cập nhật thành: %s.\n\nCảm ơn bạn!",
                user.getFirstName(), order.getId(), order.getStatus()
        );
        sendEmail(user.getEmail(), subject, content);
    }

    private void sendEmail(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);

            javaMailSender.send(message);

            log.info("Email sent successfully to {}", to);
        } catch (Exception e) {
            log.error("Failed to send email to {}", to, e);
        }
    }
}
