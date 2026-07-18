package vn.edu.fpt.hsf302_group5.service.impl.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.edu.fpt.hsf302_group5.dto.recruiter.response.ApplicantResponse;
import vn.edu.fpt.hsf302_group5.entity.Application;
import vn.edu.fpt.hsf302_group5.entity.enums.ApplicationStatus;
import vn.edu.fpt.hsf302_group5.repository.application.ApplicationRepository;
import vn.edu.fpt.hsf302_group5.service.application.ApplicationService;

import vn.edu.fpt.hsf302_group5.dto.recruiter.response.ApplicantDetailResponse;
import vn.edu.fpt.hsf302_group5.mapper.ApplicationMapper;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.fpt.hsf302_group5.service.email.EmailService;

@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final ApplicationMapper applicationMapper;
    private final EmailService emailService;

    @Override
    public Page<ApplicantResponse> getApplicantsByFilter(Integer jobId, String searchKeyword, String status, int page) {
        // Chuẩn hóa từ khóa tìm kiếm
        if (searchKeyword == null || searchKeyword.trim().isEmpty()) {
            searchKeyword = null;
        } else {
            searchKeyword = searchKeyword.trim();
        }

        // Chuẩn hóa trạng thái
        ApplicationStatus statusEnum = null;
        if (status != null && !status.trim().isEmpty() && !status.equals("-1")) {
            try {
                statusEnum = ApplicationStatus.valueOf(status.toUpperCase().trim());
            } catch (IllegalArgumentException e) {
                statusEnum = null;
            }
        }

        // Tạo đối tượng phân trang (mỗi trang hiển thị 5 ứng viên)
        Pageable pageable = PageRequest.of(page, 5);

        // Truy xuất từ Database
        Page<Application> applications = applicationRepository.findApplicants(jobId, searchKeyword, statusEnum, pageable);

        // Ánh xạ sang DTO hiển thị sử dụng mapper
        return applications.map(applicationMapper::toApplicantResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public ApplicantDetailResponse getApplicantDetail(Integer applicationId) {
        Application application = applicationRepository.findByIdWithDetails(applicationId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy hồ sơ ứng tuyển với ID: " + applicationId));

        // Ánh xạ sang DTO hiển thị sử dụng mapper
        return applicationMapper.toApplicantDetailResponse(application);
    }

    @Override
    @Transactional
    public void updateApplicationStatus(Integer applicationId, String status) {
        Application application = applicationRepository.findByIdWithDetails(applicationId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy hồ sơ ứng tuyển với ID: " + applicationId));

        try {
            ApplicationStatus newStatus = ApplicationStatus.valueOf(status.toUpperCase().trim());
            ApplicationStatus oldStatus = application.getStatus();
            application.setStatus(newStatus);
            applicationRepository.save(application);

            if (newStatus == ApplicationStatus.INTERVIEWED && oldStatus != ApplicationStatus.INTERVIEWED) {
                String toEmail = application.getCandidateEmail();
                if (toEmail != null && !toEmail.isEmpty()) {
                    String candidateName = application.getCandidateFullName();
                    String jobTitle = application.getJobTitle();
                    String companyName = "Công ty";
                    if (application.getJobPost() != null && 
                        application.getJobPost().getRecruiter() != null && 
                        application.getJobPost().getRecruiter().getCompany() != null) {
                        companyName = application.getJobPost().getRecruiter().getCompany().getCompanyName();
                    }
                    emailService.sendInterviewInvitationEmail(
                            toEmail,
                            candidateName != null ? candidateName : "Ứng viên",
                            jobTitle != null ? jobTitle : "Vị trí tuyển dụng",
                            companyName
                    );
                }
            }
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Trạng thái không hợp lệ: " + status);
        }
    }
}
