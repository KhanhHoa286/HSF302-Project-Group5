package vn.edu.fpt.hsf302_group5.dto.recruiter.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyJobPostDetailResponse {
    private Integer companyId;
    private String companyImage;
    private String companyName;
    private String logoUrl;
}
