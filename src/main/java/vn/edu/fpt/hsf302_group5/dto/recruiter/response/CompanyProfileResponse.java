package vn.edu.fpt.hsf302_group5.dto.recruiter.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyProfileResponse {

    private Integer companyId;
    private String companyName;
    private String logoUrl;
    private String website;
    private String description;
    private String addressDetail;
    private String email;
    private String phone;
}
