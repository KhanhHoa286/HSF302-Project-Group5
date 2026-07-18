package vn.edu.fpt.hsf302_group5.controller.company;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import vn.edu.fpt.hsf302_group5.dto.recruiter.response.CompanyProfileResponse;
import vn.edu.fpt.hsf302_group5.service.company.CompanyService;

@Controller
@RequiredArgsConstructor
public class CompanyProfileViewController {

    private final CompanyService companyService;

    @GetMapping("/company/profile/{id}")
    @PreAuthorize("hasAuthority(T(vn.edu.fpt.hsf302_group5.entity.enums.UserPermission).VIEW_PROFILE_COMPANY.name())")
    public String viewCompanyProfile(@PathVariable("id") Integer id, Model model) {
        CompanyProfileResponse company = companyService.getCompanyProfileById(id);
        model.addAttribute("company", company);
        return "pages/company/company-profile-view";
    }
}
