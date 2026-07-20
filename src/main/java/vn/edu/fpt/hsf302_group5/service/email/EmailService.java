package vn.edu.fpt.hsf302_group5.service.email;

public interface EmailService {
    void sendVerificationEmail(String toMail, String verificationTokenLink, String messageTital);
    void sendInterviewInvitationEmail(String toMail, String candidateName, String jobTitle, String companyName);
    void sendAcceptanceEmail(String toMail, String candidateName, String jobTitle, String companyName);
    void sendRejectionEmail(String toMail, String candidateName, String jobTitle, String companyName);
}
