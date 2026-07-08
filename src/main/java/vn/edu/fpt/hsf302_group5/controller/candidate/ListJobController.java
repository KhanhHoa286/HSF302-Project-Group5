package vn.edu.fpt.hsf302_group5.controller.candidate;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vn.edu.fpt.hsf302_group5.dto.industry.IndustryResponse;
import vn.edu.fpt.hsf302_group5.dto.job_post.JobPostResponse;
import vn.edu.fpt.hsf302_group5.dto.province.ProvinceResponse;
import vn.edu.fpt.hsf302_group5.service.industry.IndustryService;
import vn.edu.fpt.hsf302_group5.service.jobpost.JobPostService;
import vn.edu.fpt.hsf302_group5.service.province.ProvinceService;
import vn.edu.fpt.hsf302_group5.util.AppConstants;

import java.math.BigDecimal;
import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/candidate")
public class ListJobController {


    private final ProvinceService provinceService;
    private final IndustryService industryService;
    private final JobPostService jobPostService;

    @GetMapping("/jobs/list-job")
    public String listJob(Model model,
                          @RequestParam(name = "page", defaultValue = "0") int page,
                          @RequestParam(value = "filterLogicInOtherConditions", required = false, defaultValue = "AND") String filterLogicInOtherConditions,
                          @RequestParam(value = "filterLogicInSameConditions", required = false, defaultValue = "AND") String filterLogicInSameConditions,
                          @RequestParam(value = "search-keyword", required = false) List<String> searchKeyword,
                          @RequestParam(value = "search-keyword-operator", required = false) List<String> searchKeywordOperators,
                          @RequestParam(value = "province", required = false) List<Integer> provinceId,
                          @RequestParam(value = "province-operator", required = false) List<String> provinceOperators,
                          @RequestParam(value = "industry", required = false) List<Integer> industryId,
                          @RequestParam(value = "industry-operator", required = false) List<String> industryOperators,
                          @RequestParam(value = "salary", required = false) List<BigDecimal> salary,
                          @RequestParam(value = "salary-operator", required = false) List<String> salaryOperators) {


        if (searchKeyword == null) searchKeyword = new java.util.ArrayList<>();
        if (searchKeywordOperators == null) searchKeywordOperators = new java.util.ArrayList<>();
        if (provinceId == null) provinceId = new java.util.ArrayList<>();
        if (provinceOperators == null) provinceOperators = new java.util.ArrayList<>();
        if (industryId == null) industryId = new java.util.ArrayList<>();
        if (industryOperators == null) industryOperators = new java.util.ArrayList<>();
        if (salary == null) salary = new java.util.ArrayList<>();
        if (salaryOperators == null) salaryOperators = new java.util.ArrayList<>();

        List<ProvinceResponse> provinceResponses = provinceService.getListProvinceResponse();
        List<IndustryResponse> industryResponses = industryService.getAllIndustryResponse();

        // Page<JobPostResponse> jobPage = jobPostService.getJobPostsByFilter(null, null, null, null, page);

        Page<JobPostResponse> jobPageBySpecification = jobPostService.getJobPostsSpecification(page, filterLogicInOtherConditions, filterLogicInSameConditions, searchKeyword, searchKeywordOperators, provinceId, provinceOperators, industryId, industryOperators, salary, salaryOperators);


        int startPage = (jobPageBySpecification.getNumber() / AppConstants.NUMBER_PAGE_PER_BLOCK) * AppConstants.NUMBER_PAGE_PER_BLOCK;
        int endPage = Math.min(startPage + AppConstants.NUMBER_PAGE_PER_BLOCK - 1, jobPageBySpecification.getTotalPages() - 1);

        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("jobPage", jobPageBySpecification);
        model.addAttribute("provinceResponses", provinceResponses);
        model.addAttribute("industryResponses", industryResponses);

        model.addAttribute("searchKeyword", searchKeyword);
        model.addAttribute("searchKeywordOperators", searchKeywordOperators);
        model.addAttribute("provinceId", provinceId);
        model.addAttribute("provinceOperators", provinceOperators);
        model.addAttribute("industryId", industryId);
        model.addAttribute("industryOperators", industryOperators);
        model.addAttribute("salary", salary);
        model.addAttribute("salaryOperators", salaryOperators);
        model.addAttribute("filterLogicInOtherConditions", filterLogicInOtherConditions);
        model.addAttribute("filterLogicInSameConditions", filterLogicInSameConditions);
        return "pages/candidate/job-list";
    }

}
