package vn.edu.fpt.hsf302_group5.service.impl.email;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import vn.edu.fpt.hsf302_group5.service.email.EmailService;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Override
    @Async("mailExecutor")
    public void sendVerificationEmail(String toMail, String verificationTokenLink, String messageTitle) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toMail);
        message.setSubject("Xác thực tài khoản");
        message.setText(
                messageTitle + "\n"
                        + verificationTokenLink
        );
        mailSender.send(message);
        log.info("Gửi email xác thực thành công tới: {}", toMail);
    }

    @Override
    @Async("mailExecutor")
    public void sendInterviewInvitationEmail(String toMail, String candidateName, String jobTitle, String companyName) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toMail);
        message.setSubject("Thư mời phỏng vấn - " + companyName);

        String text = String.format(
                "Kính gửi bạn %s,\n\n" +
                "Lời đầu tiên, chúng tôi xin đại diện công ty %s gửi lời cảm ơn chân thành đến bạn vì đã quan tâm và nộp hồ sơ ứng tuyển vào vị trí %s.\n\n" +
                "Qua quá trình xem xét hồ sơ, chúng tôi đánh giá rất cao năng lực và kinh nghiệm của bạn. Vì vậy, chúng tôi trân trọng kính mời bạn tham gia buổi phỏng vấn trực tiếp/online cùng bộ phận tuyển dụng của công ty.\n\n" +
                "Chúng tôi sẽ sớm liên hệ với bạn qua điện thoại hoặc email này để thống nhất thời gian và địa điểm/đường dẫn phỏng vấn cụ thể.\n\n" +
                "Chúc bạn một ngày tốt lành!\n\n" +
                "Trân trọng,\n" +
                "Phòng Tuyển dụng %s",
                candidateName, companyName, jobTitle, companyName
        );

        message.setText(text);
        mailSender.send(message);
        log.info("Gửi email mời phỏng vấn thành công tới: {}", toMail);
    }
}

