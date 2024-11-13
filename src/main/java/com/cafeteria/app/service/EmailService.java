package com.cafeteria.app.service;

import com.cafeteria.app.model.Order;
import com.cafeteria.app.model.OrderItem;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.*;
import org.springframework.stereotype.Service;


@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public boolean sendInvoiceEmail(Order order, String recipientEmail) {
        String subject = "Factura de su pedido #" + order.getId();
        String content = generateEmailContent(order);

        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom("no-reply@cherriesbakery.com");
            helper.setTo(recipientEmail);
            helper.setSubject(subject);
            helper.setText(content, true);

            mailSender.send(message);
            return true;
        } catch (MessagingException e) {
            // Handle exception (log it, rethrow it, etc.)
            e.printStackTrace();
            return false;
        }
    }

    private String generateEmailContent(Order order) {
        // Generate HTML content for the email
        StringBuilder content = new StringBuilder();
        content.append("<h1>Gracias por su compra!</h1>");
        content.append("<p>Su pedido número <strong>").append(order.getId()).append("</strong> ha sido procesado.</p>");
        content.append("<h2>Detalles del pedido:</h2>");
        content.append("<ul>");

        for (OrderItem item : order.getItems()) {
            content.append("<li>")
                .append(item.getProduct().getName())
                .append(" x ")
                .append(item.getQuantity())
                .append("</li>");
        }

        content.append("</ul>");
        content.append("<p>Total: <strong>₡").append(String.format("%.2f", order.getTotalPrice())).append("</strong></p>");
        content.append("<p>Esperamos que disfrute de nuestros productos!</p>");
        return content.toString();
    }

    // Optional: Generate a PDF invoice
    // private File generatePdfInvoice(Order order) {
    //     // Implement PDF generation logic
    //     return new File("path/to/invoice.pdf");
    // }
}
