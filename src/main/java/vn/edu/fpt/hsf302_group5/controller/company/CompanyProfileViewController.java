package vn.edu.fpt.hsf302_group5.controller.company;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import vn.edu.fpt.hsf302_group5.dto.admin.CompanyDetailResponse;
import vn.edu.fpt.hsf302_group5.service.company.CompanyService;
import vn.edu.fpt.hsf302_group5.repository.jobpost.JobPostRepository;
import vn.edu.fpt.hsf302_group5.dto.job_post.JobPostResponse;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class CompanyProfileViewController {

    private final CompanyService companyService;
    private final JobPostRepository jobPostRepository;

    @GetMapping("/company/profile/{id}")
    @PreAuthorize("hasAuthority(T(vn.edu.fpt.hsf302_group5.entity.enums.UserPermission).VIEW_PROFILE_COMPANY.name())")
    public String viewCompanyProfile(@PathVariable("id") Integer id, Model model) {
        CompanyDetailResponse company = companyService.getCompanyDetailById(id);
        List<JobPostResponse> jobPosts = jobPostRepository.findActiveJobsByCompanyId(id);
        model.addAttribute("company", company);
        model.addAttribute("jobPosts", jobPosts);
        return "pages/company/company-profile-view";
    }
}
