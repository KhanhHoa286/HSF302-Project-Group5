package vn.edu.fpt.hsf302_group5.controller.candidate;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.edu.fpt.hsf302_group5.entity.Application;
import vn.edu.fpt.hsf302_group5.entity.CV;
import vn.edu.fpt.hsf302_group5.entity.User;
import vn.edu.fpt.hsf302_group5.repository.user.UserRepository;
import vn.edu.fpt.hsf302_group5.service.application.ApplicationService;
import vn.edu.fpt.hsf302_group5.service.cv.CVService;

import org.springframework.security.access.prepost.PreAuthorize;

@Controller
@RequestMapping("/candidate")
@RequiredArgsConstructor
public class MyApplicationsController {

    private final ApplicationService applicationService;
    private final CVService cvService;
    private final UserRepository userRepository;

    @GetMapping("/my-applications")
    public String myApplications(
            @RequestParam(value = "status", required = false, defaultValue = "ALL") String status,
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            Model model,
            @AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails == null) {
            return "redirect:/login";
        }

        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng!"));

        Page<Application> applicationPage = applicationService.getApplicationsByCandidate(user.getUserId(), status, page, 5);

        model.addAttribute("applications", applicationPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", applicationPage.getTotalPages());
        model.addAttribute("status", status);

        return "pages/candidate/my-applications";
    }

    @PostMapping("/apply")
    @PreAuthorize("hasAuthority(T(vn.edu.fpt.hsf302_group5.entity.enums.UserPermission).JOB_APPLY.name())")
    public String applyJob(
            @RequestParam("jobId") Integer jobId,
            @RequestParam("cvOption") String cvOption,
            @RequestParam(value = "cvId", required = false) Integer cvId,
            @RequestParam(value = "cvFile", required = false) MultipartFile cvFile,
            @RequestParam(value = "coverLetter", required = false) String coverLetter,
            @AuthenticationPrincipal UserDetails userDetails,
            RedirectAttributes redirectAttributes) {

        if (userDetails == null) {
            return "redirect:/login";
        }

        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng!"));

        try {
            Integer finalCvId = cvId;

            if ("upload".equalsIgnoreCase(cvOption)) {
                if (cvFile == null || cvFile.isEmpty()) {
                    redirectAttributes.addFlashAttribute("errorMessage", "Vui lòng chọn file CV để tải lên!");
                    return "redirect:/candidate/jobs/job-detail/" + jobId;
                }
                // Validate file type
                String originalFilename = cvFile.getOriginalFilename();
                if (originalFilename == null || !originalFilename.toLowerCase().endsWith(".pdf")) {
                    redirectAttributes.addFlashAttribute("errorMessage", "Chỉ hỗ trợ tải lên file PDF!");
                    return "redirect:/candidate/jobs/job-detail/" + jobId;
                }
                // Validate size (max 5MB)
                if (cvFile.getSize() > 5 * 1024 * 1024) {
                    redirectAttributes.addFlashAttribute("errorMessage", "Dung lượng file vượt quá giới hạn 5MB!");
                    return "redirect:/candidate/jobs/job-detail/" + jobId;
                }
                // Upload new CV
                CV cv = cvService.uploadCV(user.getUserId(), cvFile, "CV_Applied_" + System.currentTimeMillis());
                finalCvId = cv.getCvId();
            } else {
                if (finalCvId == null) {
                    redirectAttributes.addFlashAttribute("errorMessage", "Vui lòng chọn CV để ứng tuyển!");
                    return "redirect:/candidate/jobs/job-detail/" + jobId;
                }
            }

            applicationService.applyForJob(user.getUserId(), jobId, finalCvId, coverLetter);
            redirectAttributes.addFlashAttribute("successMessage", "Ứng tuyển công việc thành công!");
            return "redirect:/candidate/my-applications";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ứng tuyển thất bại: " + e.getMessage());
            return "redirect:/candidate/jobs/job-detail/" + jobId;
        }
    }
}

