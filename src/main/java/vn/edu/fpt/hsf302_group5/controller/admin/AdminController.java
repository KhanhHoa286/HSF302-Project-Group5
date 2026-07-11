package vn.edu.fpt.hsf302_group5.controller.admin;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.edu.fpt.hsf302_group5.dto.admin.CompanyDashboardResponse;
import vn.edu.fpt.hsf302_group5.dto.admin.CompanyDetailResponse;
import vn.edu.fpt.hsf302_group5.dto.admin.JobPostDashboardResponse;
import vn.edu.fpt.hsf302_group5.entity.Company;
import vn.edu.fpt.hsf302_group5.entity.JobPost;
import vn.edu.fpt.hsf302_group5.entity.enums.CompanyStatus;
import vn.edu.fpt.hsf302_group5.entity.enums.JobStatus;
import vn.edu.fpt.hsf302_group5.service.user.AdminService;

import org.springframework.security.access.prepost.PreAuthorize;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {
    private final AdminService adminService;
    public AdminController(AdminService adminService){
        this.adminService = adminService;
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

    @GetMapping("/jobs/list")
    public String listJob(

            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "status", required = false) JobStatus status,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            Model model
            ){
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
    @GetMapping("/jobs/{id}")
    public String viewDetailJob(
            @PathVariable("id") Integer id, Model model){
        JobPost jobPost = adminService.getJobPostById(id);
        model.addAttribute("job", jobPost);
        return "pages/admin/job-detail-approval";
    }
    @PostMapping("/jobs/{id}/action")
    @PreAuthorize("hasAuthority('JOB_APPROVE')")
    public String approveJob(
            @PathVariable Integer id,
            @RequestParam("status") JobStatus status,
            @RequestParam(value = "adminComment", required = false) String adminComment,
            RedirectAttributes redirectAttributes
    ){
        adminService.updateJobPostStatus(id, status, adminComment);
        redirectAttributes.addFlashAttribute("message", "cap nhat thanh cong");
        return "redirect:/admin/jobs/" + id;
    }

    @GetMapping("/companies/list")
    public String listCompany(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "status", required = false) CompanyStatus status,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            Model model
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<CompanyDashboardResponse> companyPage = adminService.getAllCompanies(keyword, status, pageable);
        model.addAttribute("companyPage", companyPage);
        model.addAttribute("keyword", keyword);
        model.addAttribute("statusFilter", status);
        return "pages/admin/company-list";
    }

    @GetMapping("/companies/detail/{id}")
    public String viewCompanyDetail(@PathVariable("id") Integer id, Model model) {
        CompanyDetailResponse company = adminService.getCompanyById(id);
        model.addAttribute("company", company);
        return "pages/admin/company-detail";
    }

    @PostMapping("/companies/edit/{id}")
    public String updateCompanyStatus(
            @PathVariable Integer id,
            @RequestParam("status") CompanyStatus status,
            RedirectAttributes redirectAttributes
    ) {
        adminService.updateCompanyStatus(id, status);
        redirectAttributes.addFlashAttribute("successMessage", "Cập nhật trạng thái doanh nghiệp thành công.");
        return "redirect:/admin/companies/detail/" + id;
    }
}
