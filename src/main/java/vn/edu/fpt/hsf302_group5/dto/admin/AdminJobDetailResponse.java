package vn.edu.fpt.hsf302_group5.dto.admin;

import lombok.*;
import vn.edu.fpt.hsf302_group5.entity.enums.EmploymentType;
import vn.edu.fpt.hsf302_group5.entity.enums.ExperienceLevel;
import vn.edu.fpt.hsf302_group5.entity.enums.JobLevel;
import vn.edu.fpt.hsf302_group5.entity.enums.JobStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminJobDetailResponse {
    private Integer jobId;
    private String title;
    private String companyName;
    private String logoUrl;
    private JobStatus status;
    private String salaryDisplay;
    private String locationDetail;
    private String provinceName;
    private ExperienceLevel experienceLevel;
    private JobLevel jobLevel;
    private EmploymentType employmentType;
    private LocalDateTime postedDate;
    private String description;
    private String requirement;
    private String benefit;
    private String adminComment;
}
