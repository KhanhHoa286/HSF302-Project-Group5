package vn.edu.fpt.hsf302_group5.dto.home;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.edu.fpt.hsf302_group5.dto.industry.IndustryResponse;
import vn.edu.fpt.hsf302_group5.dto.job_post.JobPostResponse;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HomeDto {
    private List<IndustryResponse> industries;
    private List<JobPostResponse> featuredJobs;
    private List<HomeCompanyResponse> topEmployers;
    private List<vn.edu.fpt.hsf302_group5.dto.province.ProvinceResponse> provinces;
}
