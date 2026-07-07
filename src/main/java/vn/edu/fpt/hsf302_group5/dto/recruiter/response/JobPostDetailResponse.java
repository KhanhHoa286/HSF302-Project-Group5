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
    private Integer jobPostId;
    private Integer vacancies;
    private String title;
    private BigDecimal minSalary;
    private BigDecimal maxSalary;
    private JobLevel jobLevel;
    private String location;
    private LocalDateTime deadline;
    private EmploymentType employmentType;
    private String skills;
    private String requiment;
    private String description;
    private String benefit;
    private JobStatus jobStatus;

    private CompanyJobPostDetailResponse company;
}
