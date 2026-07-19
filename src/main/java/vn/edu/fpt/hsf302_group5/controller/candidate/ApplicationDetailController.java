package vn.edu.fpt.hsf302_group5.controller.candidate;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import vn.edu.fpt.hsf302_group5.dto.recruiter.response.ApplicantDetailResponse;
import vn.edu.fpt.hsf302_group5.entity.Application;
import vn.edu.fpt.hsf302_group5.entity.User;
import vn.edu.fpt.hsf302_group5.mapper.ApplicationMapper;
import vn.edu.fpt.hsf302_group5.repository.user.UserRepository;
import vn.edu.fpt.hsf302_group5.service.application.ApplicationService;

@Controller
@RequestMapping("/candidate")
@RequiredArgsConstructor
public class ApplicationDetailController {

    private final ApplicationService applicationService;
    private final UserRepository userRepository;
    private final ApplicationMapper applicationMapper;

    @GetMapping("/application-detail/{id}")
    public String applicationDetail(
            @PathVariable("id") Integer id,
            Model model,
            @AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails == null) {
            return "redirect:/login";
        }

        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng!"));

        try {
            Application application = applicationService.getApplicationDetailForCandidate(id, user.getUserId());
            ApplicantDetailResponse applicantDetail = applicationMapper.toApplicantDetailResponse(application);
            
            model.addAttribute("applicant", applicantDetail);
            
            // Extract the company logo url
            String companyLogo = null;
            if (application.getJobPost() != null && 
                application.getJobPost().getRecruiter() != null && 
                application.getJobPost().getRecruiter().getCompany() != null) {
                companyLogo = application.getJobPost().getRecruiter().getCompany().getLogoUrl();
                model.addAttribute("companyName", application.getJobPost().getRecruiter().getCompany().getCompanyName());
            }
            model.addAttribute("companyLogo", companyLogo);
            
            return "pages/candidate/application-detail";
            
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Không thể lấy chi tiết hồ sơ: " + e.getMessage());
            return "redirect:/candidate/my-applications";
        }
    }
}

