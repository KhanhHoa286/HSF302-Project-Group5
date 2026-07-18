package vn.edu.fpt.hsf302_group5.controller.admin;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import vn.edu.fpt.hsf302_group5.dto.user.CustomUserDetailsResponse;
import vn.edu.fpt.hsf302_group5.service.user.AdminService;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminDashboardController {

    private final AdminService adminService;

    public AdminDashboardController(AdminService adminService) {
        this.adminService = adminService;
    }

    @ModelAttribute("adminUser")
    public CustomUserDetailsResponse adminUser(@AuthenticationPrincipal CustomUserDetailsResponse userDetails) {
        return userDetails;
    }

    @GetMapping({"", "/", "/dashboard"})
    public String viewDashboard(Model model) {
        model.addAttribute("totalCandidates", adminService.countCandidates());
        model.addAttribute("totalRecruiters", adminService.countRecruiters());
        model.addAttribute("totalCompanies", adminService.countCompanies());
        model.addAttribute("totalJobPosts", adminService.countJobPosts());
        model.addAttribute("recentPendingJobs", adminService.getRecentPendingJobs());
        model.addAttribute("recentCompanies", adminService.getRecentCompanies());
        return "pages/admin/dashboard";
    }
}
