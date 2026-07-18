package vn.edu.fpt.hsf302_group5.service.impl.home;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.edu.fpt.hsf302_group5.dto.home.HomeCompanyResponse;
import vn.edu.fpt.hsf302_group5.dto.home.HomeDto;
import vn.edu.fpt.hsf302_group5.dto.industry.IndustryResponse;
import vn.edu.fpt.hsf302_group5.dto.job_post.JobPostResponse;
import vn.edu.fpt.hsf302_group5.repository.company.CompanyRepository;
import vn.edu.fpt.hsf302_group5.repository.jobpost.JobPostRepository;
import vn.edu.fpt.hsf302_group5.service.home.HomeService;
import vn.edu.fpt.hsf302_group5.service.industry.IndustryService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HomeServiceImpl implements HomeService {

    private final JobPostRepository jobPostRepository;
    private final CompanyRepository companyRepository;
    private final IndustryService industryService;
    private final vn.edu.fpt.hsf302_group5.service.province.ProvinceService provinceService;

    @Override
    public HomeDto getHomeData() {
        // Fetch industries
        List<IndustryResponse> industries = industryService.getAllIndustryResponse();

        // Fetch provinces
        List<vn.edu.fpt.hsf302_group5.dto.province.ProvinceResponse> provinces = provinceService.getListProvinceResponse();

        // Fetch top 3 featured jobs
        Pageable topJobsPageable = PageRequest.of(0, 3);
        Page<JobPostResponse> topJobsPage = jobPostRepository.findTopFeaturedJobs(topJobsPageable);
        List<JobPostResponse> featuredJobs = topJobsPage.getContent();

        // Fetch top 8 employers
        Pageable topCompaniesPageable = PageRequest.of(0, 8);
        Page<HomeCompanyResponse> topCompaniesPage = companyRepository.findTopCompanies(topCompaniesPageable);
        List<HomeCompanyResponse> topEmployers = topCompaniesPage.getContent();

        return HomeDto.builder()
                .industries(industries)
                .provinces(provinces)
                .featuredJobs(featuredJobs)
                .topEmployers(topEmployers)
                .build();
    }
}
