package vn.edu.fpt.hsf302_group5.dto.recruiter.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.edu.fpt.hsf302_group5.entity.enums.ApplicationStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApplicantResponse {
    private Integer applicationId;
    private String fullName;
    private String email;
    private String phone;
    private LocalDateTime appliedDate;
    private ApplicationStatus status;
}
