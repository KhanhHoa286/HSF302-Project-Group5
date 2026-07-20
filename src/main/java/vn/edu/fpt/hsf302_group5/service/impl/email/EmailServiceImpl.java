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

    @Override
    @Async("mailExecutor")
    public void sendAcceptanceEmail(String toMail, String candidateName, String jobTitle, String companyName) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toMail);
        message.setSubject("Kết quả ứng tuyển - " + companyName);

        String text = String.format(
                "Kính gửi bạn %s,\n\n" +
                "Đại diện công ty %s, chúng tôi rất vui mừng thông báo rằng bạn đã trúng tuyển vào vị trí %s.\n\n" +
                "Qua các vòng phỏng vấn và đánh giá, chúng tôi ấn tượng với kỹ năng và kinh nghiệm của bạn. Chúng tôi tin rằng bạn sẽ là một nhân tố quan trọng đóng góp vào sự thành công của công ty.\n\n" +
                "Bộ phận Nhân sự sẽ liên hệ với bạn trong thời gian sớm nhất để trao đổi về thư mời nhận việc (Offer Letter) và các thủ tục tiếp theo.\n\n" +
                "Chào mừng bạn đến với đội ngũ của chúng tôi!\n\n" +
                "Trân trọng,\n" +
                "Phòng Tuyển dụng %s",
                candidateName, companyName, jobTitle, companyName
        );

        message.setText(text);
        mailSender.send(message);
        log.info("Gửi email thông báo trúng tuyển thành công tới: {}", toMail);
    }

    @Override
    @Async("mailExecutor")
    public void sendRejectionEmail(String toMail, String candidateName, String jobTitle, String companyName) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toMail);
        message.setSubject("Kết quả ứng tuyển - " + companyName);

        String text = String.format(
                "Kính gửi bạn %s,\n\n" +
                "Cảm ơn bạn đã quan tâm và dành thời gian ứng tuyển vào vị trí %s tại công ty %s.\n\n" +
                "Chúng tôi đánh giá cao kinh nghiệm và kỹ năng của bạn. Tuy nhiên, sau quá trình xem xét cẩn thận, chúng tôi rất tiếc phải thông báo rằng ở thời điểm hiện tại, hồ sơ của bạn chưa hoàn toàn phù hợp với yêu cầu của vị trí này.\n\n" +
                "Chúng tôi sẽ lưu trữ hồ sơ của bạn trong hệ thống và sẽ liên hệ với bạn nếu có cơ hội nghề nghiệp phù hợp hơn trong tương lai.\n\n" +
                "Chúc bạn nhiều sức khỏe, may mắn và thành công trên con đường sự nghiệp.\n\n" +
                "Trân trọng,\n" +
                "Phòng Tuyển dụng %s",
                candidateName, jobTitle, companyName, companyName
        );

        message.setText(text);
        mailSender.send(message);
        log.info("Gửi email thông báo từ chối thành công tới: {}", toMail);
    }
}

