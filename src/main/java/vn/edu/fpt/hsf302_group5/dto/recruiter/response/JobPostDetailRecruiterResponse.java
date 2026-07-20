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
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobPostDetailRecruiterResponse {
    private Integer jobId;
    private Integer vacancies;
    private String title;
    private String salaryDisplay;
    private JobLevel jobLevel;
    private String locationDetail;
    private LocalDateTime expiredDate;
    private EmploymentType employmentType;
    private JobStatus status;
    private String requirement;
    private String description;
    private String benefit;
    private String adminComment;

    private List<String> skillsName;
    private CompanyJobPostDetailResponse company;
}