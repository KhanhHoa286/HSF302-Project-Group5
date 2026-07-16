package vn.edu.fpt.hsf302_group5.dto.recruiter.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.edu.fpt.hsf302_group5.entity.enums.EmploymentType;
import vn.edu.fpt.hsf302_group5.entity.enums.JobLevel;
import vn.edu.fpt.hsf302_group5.entity.enums.JobStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobPostDetailResponse {
    // --- Đã đổi tên trùng khớp hoàn toàn với Entity JobPost ---
    private Integer jobId;
    private Integer vacancies;
    private String title;
    private BigDecimal salaryMin;
    private BigDecimal salaryMax;
    private JobLevel jobLevel;
    private String locationDetail;
    private LocalDateTime expiredDate;
    private EmploymentType employmentType;
    private JobStatus status;
    private String requirement;
    private String description;
    private String benefit;

    private String skills;
    private CompanyJobPostDetailResponse company;
}