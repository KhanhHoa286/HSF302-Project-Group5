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
import vn.edu.fpt.hsf302_group5.repository.cv.CVRepository;
import vn.edu.fpt.hsf302_group5.repository.user.UserRepository;
import vn.edu.fpt.hsf302_group5.service.application.ApplicationService;

import vn.edu.fpt.hsf302_group5.dto.recruiter.response.ApplicantDetailResponse;
import vn.edu.fpt.hsf302_group5.mapper.ApplicationMapper;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final ApplicationMapper applicationMapper;
    private final UserRepository userRepository;
    private final CVRepository cvRepository;

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
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy hồ sơ ứng tuyển với ID: " + applicationId));

        try {
            ApplicationStatus oldStatus = application.getStatus();
            ApplicationStatus newStatus = ApplicationStatus.valueOf(status.toUpperCase().trim());
            if (oldStatus == newStatus) {
                return;
            }
            application.setStatus(newStatus);

            // Log history
            Integer changedBy = application.getCandidateId(); // fallback
            org.springframework.security.core.Authentication auth = 
                org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
            if (auth != null) {
                if (auth.getPrincipal() instanceof vn.edu.fpt.hsf302_group5.dto.user.CustomUserDetailsResponse) {
                    changedBy = ((vn.edu.fpt.hsf302_group5.dto.user.CustomUserDetailsResponse) auth.getPrincipal()).getId();
                } else {
                    String email = auth.getName();
                    changedBy = userRepository.findByEmail(email).map(vn.edu.fpt.hsf302_group5.entity.User::getUserId).orElse(changedBy);
                }
            }

            vn.edu.fpt.hsf302_group5.entity.ApplicationStatusHistory history = 
                vn.edu.fpt.hsf302_group5.entity.ApplicationStatusHistory.builder()
                        .applicationId(application.getApplicationId())
                        .oldStatus(oldStatus)
                        .newStatus(newStatus)
                        .changedBy(changedBy)
                        .changedAt(java.time.LocalDateTime.now())
                        .build();
            application.getStatusHistories().add(history);

            applicationRepository.save(application);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Trạng thái không hợp lệ: " + status);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasApplied(Integer candidateId, Integer jobId) {
        return applicationRepository.existsByCandidateIdAndJobId(candidateId, jobId);
    }

    @Override
    @Transactional
    public void applyForJob(Integer candidateId, Integer jobId, Integer cvId, String coverLetter) {
        if (hasApplied(candidateId, jobId)) {
            throw new IllegalStateException("Bạn đã ứng tuyển vào công việc này rồi!");
        }

        // Verify CV belongs to candidate
        vn.edu.fpt.hsf302_group5.entity.CV cv = cvRepository.findById(cvId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy CV với ID: " + cvId));
        if (!cv.getCandidateId().equals(candidateId)) {
            throw new SecurityException("Bạn không có quyền sử dụng CV này!");
        }

        // Verify candidate profile exists or create it
        vn.edu.fpt.hsf302_group5.entity.User user = userRepository.findById(candidateId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng!"));
        if (user.getCandidateProfile() == null) {
            vn.edu.fpt.hsf302_group5.entity.CandidateProfile profile = vn.edu.fpt.hsf302_group5.entity.CandidateProfile.builder()
                    .candidateId(candidateId)
                    .user(user)
                    .build();
            user.setCandidateProfile(profile);
            userRepository.save(user);
        }

        Application application = Application.builder()
                .candidateId(candidateId)
                .jobId(jobId)
                .cvId(cvId)
                .appliedDate(java.time.LocalDateTime.now())
                .coverLetter(coverLetter)
                .status(ApplicationStatus.APPLIED)
                .build();

        // Save application first to generate ID
        application = applicationRepository.save(application);

        // Add history log
        vn.edu.fpt.hsf302_group5.entity.ApplicationStatusHistory history = 
                vn.edu.fpt.hsf302_group5.entity.ApplicationStatusHistory.builder()
                        .applicationId(application.getApplicationId())
                        .oldStatus(null)
                        .newStatus(ApplicationStatus.APPLIED)
                        .changedBy(candidateId)
                        .changedAt(java.time.LocalDateTime.now())
                        .build();
        application.getStatusHistories().add(history);
        applicationRepository.save(application);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Application> getApplicationsByCandidate(Integer candidateId, String status, int page, int size) {
        ApplicationStatus statusEnum = null;
        if (status != null && !status.trim().isEmpty() && !status.equalsIgnoreCase("ALL")) {
            try {
                statusEnum = ApplicationStatus.valueOf(status.toUpperCase().trim());
            } catch (IllegalArgumentException e) {
                statusEnum = null;
            }
        }
        Pageable pageable = PageRequest.of(page, size);
        return applicationRepository.findApplicationsByCandidateId(candidateId, statusEnum, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Application getApplicationDetailForCandidate(Integer applicationId, Integer candidateId) {
        Application application = applicationRepository.findByIdWithDetails(applicationId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy hồ sơ ứng tuyển với ID: " + applicationId));
        if (!application.getCandidateId().equals(candidateId)) {
            throw new SecurityException("Bạn không có quyền xem chi tiết hồ sơ này!");
        }
        return application;
    }
}
