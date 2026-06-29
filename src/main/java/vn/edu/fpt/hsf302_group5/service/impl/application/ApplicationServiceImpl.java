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

@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepository;

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

        // Ánh xạ sang DTO hiển thị
        return applications.map(app -> new ApplicantResponse(
            app.getApplicationId(),
            app.getCandidateProfile().getUser().getFullName(),
            app.getCandidateProfile().getUser().getEmail(),
            app.getCandidateProfile().getUser().getPhone(),
            app.getAppliedDate(),
            app.getStatus()
        ));
    }
}
