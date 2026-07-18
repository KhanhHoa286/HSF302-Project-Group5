package vn.edu.fpt.hsf302_group5.controller.candidate;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.edu.fpt.hsf302_group5.dto.candidate.CandidateProfileRequest;
import vn.edu.fpt.hsf302_group5.dto.candidate.CandidateProfileResponse;
import vn.edu.fpt.hsf302_group5.dto.candidate.EducationRequest;
import vn.edu.fpt.hsf302_group5.dto.candidate.ExperienceRequest;
import vn.edu.fpt.hsf302_group5.dto.user.CustomUserDetailsResponse;
import vn.edu.fpt.hsf302_group5.mapper.CandidateMapper;
import vn.edu.fpt.hsf302_group5.service.candidate.CandidateProfileService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CandidateProfileController {

    private final CandidateProfileService candidateProfileService;
    private final CandidateMapper candidateMapper;

    @GetMapping("/profile")
    public String profileRedirect(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return "redirect:/login";
        }
        if (userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("CANDIDATE"))) {
            return "redirect:/candidate/profile";
        } else if (userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("RECRUITER"))) {
            return "redirect:/recruiter/company-profile";
        }
        return "redirect:/";
    }

    @GetMapping("/candidate/profile")
    @PreAuthorize("hasAuthority('CANDIDATE')")
    public String showProfile(Model model,
                              @AuthenticationPrincipal CustomUserDetailsResponse userDetails,
                              @RequestParam(value = "successMessage", required = false) String successMessage,
                              @RequestParam(value = "errorMessage", required = false) String errorMessage) {
        if (successMessage != null) {
            model.addAttribute("successMessage", successMessage);
        }
        if (errorMessage != null) {
            model.addAttribute("errorMessage", errorMessage);
        }

        CandidateProfileResponse profile = candidateProfileService.getCandidateProfileByEmail(userDetails.getUsername());
        model.addAttribute("profile", profile);
        model.addAttribute("allSkills", candidateProfileService.getAllSkills());

        CandidateProfileRequest personalForm = candidateMapper.toRequest(profile);
        model.addAttribute("personalForm", personalForm);
        model.addAttribute("educationForm", new EducationRequest());
        model.addAttribute("experienceForm", new ExperienceRequest());

        return "pages/candidate/profile";
    }

    @PostMapping("/candidate/profile/update-personal")
    @PreAuthorize("hasAuthority('CANDIDATE')")
    public String updatePersonalProfile(@ModelAttribute("personalForm") CandidateProfileRequest form,
                                        @AuthenticationPrincipal UserDetails userDetails,
                                        RedirectAttributes redirectAttributes) {
        try {
            candidateProfileService.updatePersonalProfile(userDetails.getUsername(), form);
            redirectAttributes.addAttribute("successMessage", "Cập nhật thông tin cá nhân thành công!");
        } catch (Exception e) {
            redirectAttributes.addAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/candidate/profile";
    }

    @PostMapping("/candidate/profile/add-education")
    @PreAuthorize("hasAuthority('CANDIDATE')")
    public String addEducation(@Valid @ModelAttribute("educationForm") EducationRequest form,
                               BindingResult bindingResult,
                               @AuthenticationPrincipal UserDetails userDetails,
                               RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            String errorMsg = bindingResult.getAllErrors().get(0).getDefaultMessage();
            redirectAttributes.addAttribute("errorMessage", errorMsg);
            return "redirect:/candidate/profile";
        }
        if (form.getStartDate() != null && form.getEndDate() != null && !form.getStartDate().isBefore(form.getEndDate())) {
            redirectAttributes.addAttribute("errorMessage", "Ngày bắt đầu học phải trước ngày kết thúc!");
            return "redirect:/candidate/profile";
        }
        CustomUserDetailsResponse customUserDetailsResponse = (CustomUserDetailsResponse) userDetails;
        try {
            candidateProfileService.addEducation(customUserDetailsResponse.getEmail(), form);
            redirectAttributes.addAttribute("successMessage", "Thêm học vấn thành công!");
        } catch (Exception e) {
            redirectAttributes.addAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/candidate/profile";
    }

    @PostMapping("/candidate/profile/delete-education")
    @PreAuthorize("hasAuthority('CANDIDATE')")
    public String deleteEducation(@RequestParam("educationId") Integer educationId,
                                  @AuthenticationPrincipal UserDetails userDetails,
                                  RedirectAttributes redirectAttributes) {
        try {
            candidateProfileService.deleteEducation(userDetails.getUsername(), educationId);
            redirectAttributes.addFlashAttribute("successMessage", "Xóa học vấn thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/candidate/profile";
    }

    @PostMapping("/candidate/profile/add-experience")
    @PreAuthorize("hasAuthority('CANDIDATE')")
    public String addExperience(@Valid @ModelAttribute("experienceForm") ExperienceRequest form,
                                BindingResult bindingResult,
                                @AuthenticationPrincipal UserDetails userDetails,
                                RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            String errorMsg = bindingResult.getAllErrors().get(0).getDefaultMessage();
            redirectAttributes.addAttribute("errorMessage", errorMsg);
            return "redirect:/candidate/profile";
        }
        if (form.getStartDate() != null && form.getEndDate() != null && !form.getStartDate().isBefore(form.getEndDate())) {
            redirectAttributes.addAttribute("errorMessage", "Ngày bắt đầu làm việc phải trước ngày kết thúc!");
            return "redirect:/candidate/profile";
        }
        try {
            candidateProfileService.addExperience(userDetails.getUsername(), form);
            redirectAttributes.addAttribute("successMessage", "Thêm kinh nghiệm làm việc thành công!");
        } catch (Exception e) {
            redirectAttributes.addAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/candidate/profile";
    }

    @PostMapping("/candidate/profile/delete-experience")
    @PreAuthorize("hasAuthority('CANDIDATE')")
    public String deleteExperience(@RequestParam("experienceId") Integer experienceId,
                                   @AuthenticationPrincipal UserDetails userDetails,
                                   RedirectAttributes redirectAttributes) {
        try {
            candidateProfileService.deleteExperience(userDetails.getUsername(), experienceId);
            redirectAttributes.addAttribute("successMessage", "Xóa kinh nghiệm làm việc thành công!");
        } catch (Exception e) {
            redirectAttributes.addAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/candidate/profile";
    }

    @PostMapping("/candidate/profile/update-skills")
    @PreAuthorize("hasAuthority('CANDIDATE')")
    public String updateSkills(@RequestParam(value = "skillIds", required = false) List<Integer> skillIds,
                               @AuthenticationPrincipal UserDetails userDetails,
                               RedirectAttributes redirectAttributes) {
        try {
            candidateProfileService.updateSkills(userDetails.getUsername(), skillIds);
            redirectAttributes.addAttribute("successMessage", "Cập nhật kỹ năng thành công!");
        } catch (Exception e) {
            redirectAttributes.addAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/candidate/profile";
    }
}
