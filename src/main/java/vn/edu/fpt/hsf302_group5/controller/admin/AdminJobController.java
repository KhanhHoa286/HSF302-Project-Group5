package vn.edu.fpt.hsf302_group5.controller.admin;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.edu.fpt.hsf302_group5.dto.admin.JobPostDashboardResponse;
import vn.edu.fpt.hsf302_group5.dto.admin.AdminJobDetailResponse;
import vn.edu.fpt.hsf302_group5.dto.admin.JobApprovalRequest;
import vn.edu.fpt.hsf302_group5.dto.user.CustomUserDetailsResponse;
import vn.edu.fpt.hsf302_group5.entity.enums.JobStatus;
import vn.edu.fpt.hsf302_group5.service.user.AdminService;

@Controller
@RequestMapping("/admin/jobs")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminJobController {

    private final AdminService adminService;

    public AdminJobController(AdminService adminService) {
        this.adminService = adminService;
    }

    @ModelAttribute("adminUser")
    public CustomUserDetailsResponse adminUser(@AuthenticationPrincipal CustomUserDetailsResponse userDetails) {
        return userDetails;
    }

    @GetMapping("/list")
    public String listJob(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "status", required = false) JobStatus status,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            Model model
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("postedDate").descending());
        Page<JobPostDashboardResponse> jobPage = adminService.getJobPostForApproval(keyword, status, pageable);
        model.addAttribute("jobPage", jobPage);
        model.addAttribute("keyword", keyword);
        model.addAttribute("statusFilter", status);
        model.addAttribute("pendingCount", adminService.countJobPostsByStatus(JobStatus.PENDING));
        model.addAttribute("approvedCount", adminService.countJobPostsByStatus(JobStatus.APPROVED));
        model.addAttribute("rejectedCount", adminService.countJobPostsByStatus(JobStatus.REJECTED));
        return "pages/admin/job-approval";
    }

    @GetMapping("/{id}")
    public String viewDetailJob(@PathVariable("id") Integer id, Model model) {
        AdminJobDetailResponse jobPost = adminService.getJobPostById(id);
        model.addAttribute("job", jobPost);
        return "pages/admin/job-detail-approval";
    }

    @PostMapping("/{id}/action")
    public String approveJob(
            @PathVariable Integer id,
            @ModelAttribute JobApprovalRequest request,
            RedirectAttributes redirectAttributes
    ) {
        adminService.updateJobPostStatus(id, request.getStatus(), request.getAdminComment());
        redirectAttributes.addFlashAttribute("message", "Cập nhật thành công");
        return "redirect:/admin/jobs/" + id;
    }
}
