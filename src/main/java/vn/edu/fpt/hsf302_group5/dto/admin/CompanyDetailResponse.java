package vn.edu.fpt.hsf302_group5.dto.admin;

import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyDetailResponse {
    private Integer companyId;
    private String companyName;
    private String logoUrl;
    private String website;
    private String description;
    private String addressDetail;
    private String provinceName;
    private String administrativeUnitName;
    private String status;
    private LocalDateTime createdAt;

    // Recruiter info
    private Integer recruiterId;
    private String recruiterName;
    private String recruiterEmail;
    private String recruiterPhone;
    private String recruiterAvatarUrl;

    // Industry list
    private List<String> industries;
}
