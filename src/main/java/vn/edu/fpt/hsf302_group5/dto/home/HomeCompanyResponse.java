package vn.edu.fpt.hsf302_group5.dto.home;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HomeCompanyResponse {
    private Integer companyId;
    private String companyName;
    private String logoUrl;
    private Long totalApplications;
}
