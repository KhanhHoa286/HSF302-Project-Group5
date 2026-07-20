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
import vn.edu.fpt.hsf302_group5.dto.admin.CompanyDashboardResponse;
import vn.edu.fpt.hsf302_group5.dto.admin.CompanyDetailResponse;
import vn.edu.fpt.hsf302_group5.dto.admin.CompanyStatusRequest;
import vn.edu.fpt.hsf302_group5.dto.user.CustomUserDetailsResponse;
import vn.edu.fpt.hsf302_group5.entity.enums.CompanyStatus;
import vn.edu.fpt.hsf302_group5.service.user.AdminService;

@Controller
@RequestMapping("/admin/companies")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminCompanyController {

    private final AdminService adminService;

    public AdminCompanyController(AdminService adminService) {
        this.adminService = adminService;
    }

    @ModelAttribute("adminUser")
    public CustomUserDetailsResponse adminUser(@AuthenticationPrincipal CustomUserDetailsResponse userDetails) {
        return userDetails;
    }

    @GetMapping("/list")
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
        model.addAttribute("companyStatuses", CompanyStatus.values());
        return "pages/admin/company-list";
    }

    @GetMapping("/detail/{id}")
    public String viewCompanyDetail(@PathVariable("id") Integer id, Model model) {
        CompanyDetailResponse company = adminService.getCompanyById(id);
        model.addAttribute("company", company);
        return "pages/admin/company-detail";
    }

    @PostMapping("/edit/{id}")
    public String updateCompanyStatus(
            @PathVariable Integer id,
            @ModelAttribute CompanyStatusRequest request,
            RedirectAttributes redirectAttributes
    ) {
        adminService.updateCompanyStatus(id, request.getStatus());
        redirectAttributes.addFlashAttribute("successMessage", "Cập nhật trạng thái doanh nghiệp thành công.");
        return "redirect:/admin/companies/detail/" + id;
    }
}
