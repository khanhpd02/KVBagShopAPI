package com.example.kvbagshopapi.services.impl;

import com.example.kvbagshopapi.Model.Mail;
import com.example.kvbagshopapi.dtos.OrderDto;
import com.example.kvbagshopapi.dtos.OrderItemDto;
import com.example.kvbagshopapi.dtos.UserDto;
import com.example.kvbagshopapi.services.IMailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.List;

@Service
public class MaiServiceImp implements IMailService {

    @Autowired
    private JavaMailSender javaMailSender;
    @Override
    public void sendEmail(Mail mail) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setSubject(mail.getMailSubject());
            mimeMessageHelper.setFrom(new InternetAddress(mail.getMailFrom()));
            mimeMessageHelper.setTo(mail.getMailTo());
            mimeMessageHelper.setText(mail.getMailContent());
            javaMailSender.send(mimeMessageHelper.getMimeMessage());
        }
        catch (MessagingException e) {
            e.printStackTrace();
        }
    }
    @Async
    @Override
    public void sendOrderMail(UserDto userDto, OrderDto orderDto, List<OrderItemDto> orderItemDtos) {
        /// Mail
        Mail mail = new Mail();
        mail.setMailFrom("phanduykhanh2002@.com");
        mail.setMailTo(userDto.getEmail());
        mail.setMailSubject("KVbalu - Bạn đặt hàng thành công");
        String Content = "Tổng số tiền của đơn hàng của bạn là: $" + orderDto.getTotal() +"\n";
        int index = 1;
        for (OrderItemDto OID : orderItemDtos) {
            Content += index +". Tên sản phẩm: " +  OID.getProduct().getProductName() +"\n";
            index ++;
        }
       /* Content += "Sản phẩm được đặt bởi: " + userDto.getUserName() + "\n";
        Content += "Sản phẩm được đưa tới địa chỉ: " + orderDto.getAddress()+ "\n";
        Content += "Số điện thoại nhận hàng: " + orderDto.getPhoneNumber()+ "\n";*/
        Content += "KVbalu xin chân thành cảm ơn!";
        mail.setMailContent(Content);
        sendEmail(mail);
    }
}
