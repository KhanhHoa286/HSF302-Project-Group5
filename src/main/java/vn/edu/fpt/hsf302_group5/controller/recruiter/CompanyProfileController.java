package vn.edu.fpt.hsf302_group5.controller.recruiter;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.edu.fpt.hsf302_group5.dto.recruiter.request.CompanyProfileRequest;
import vn.edu.fpt.hsf302_group5.dto.recruiter.response.CompanyProfileResponse;
import vn.edu.fpt.hsf302_group5.service.company.CompanyService;
import vn.edu.fpt.hsf302_group5.mapper.CompanyMapper;

import org.springframework.security.access.prepost.PreAuthorize;

@Controller
@RequestMapping("/recruiter")
@RequiredArgsConstructor
public class CompanyProfileController {

    private final CompanyService companyService;
    private final CompanyMapper companyMapper;

    @GetMapping("/company-profile")
    @PreAuthorize("hasAuthority(T(vn.edu.fpt.hsf302_group5.entity.enums.UserPermission).COMPANY_VIEW.name())")
    public String showCompanyProfile(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return "redirect:/login";
        }

        try {
            CompanyProfileResponse profile = companyService.getCompanyProfile(userDetails.getUsername());
            CompanyProfileRequest form = companyMapper.toRequest(profile);

            model.addAttribute("companyProfileForm", form);
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            // Trả về form rỗng nếu lỗi
            model.addAttribute("companyProfileForm", new CompanyProfileRequest());
        }

        return "pages/recruiter/company-profile";
    }

    @PostMapping("/company-profile")
    @PreAuthorize("hasAuthority(T(vn.edu.fpt.hsf302_group5.entity.enums.UserPermission).COMPANY_UPDATE.name())")
    public String updateCompanyProfile(
            @Valid @ModelAttribute("companyProfileForm") CompanyProfileRequest form,
            BindingResult bindingResult,
            @AuthenticationPrincipal UserDetails userDetails,
            RedirectAttributes redirectAttributes) {

        if (userDetails == null) {
            return "redirect:/login";
        }

        if (bindingResult.hasErrors()) {
            return "pages/recruiter/company-profile";
        }

        try {
            companyService.updateCompanyProfile(userDetails.getUsername(), form);
            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật thông tin công ty thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/recruiter/company-profile";
    }
}
