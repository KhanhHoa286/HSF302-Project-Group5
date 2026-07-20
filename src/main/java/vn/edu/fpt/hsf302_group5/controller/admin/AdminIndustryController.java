package vn.edu.fpt.hsf302_group5.controller.admin;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.edu.fpt.hsf302_group5.dto.industry.IndustryRequest;
import vn.edu.fpt.hsf302_group5.dto.industry.IndustryResponse;
import vn.edu.fpt.hsf302_group5.dto.industry.IndustryStatusRequest;
import vn.edu.fpt.hsf302_group5.dto.user.CustomUserDetailsResponse;
import vn.edu.fpt.hsf302_group5.entity.enums.IndustryStatus;
import vn.edu.fpt.hsf302_group5.service.industry.IndustryService;

@Controller
@RequestMapping("/admin/industries")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminIndustryController {

    private final IndustryService industryService;

    public AdminIndustryController(IndustryService industryService) {
        this.industryService = industryService;
    }

    @ModelAttribute("adminUser")
    public CustomUserDetailsResponse adminUser(@AuthenticationPrincipal CustomUserDetailsResponse userDetails) {
        return userDetails;
    }

    @GetMapping("/list")
    public String listIndustry(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "status", required = false) IndustryStatus status,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            Model model
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("industryName").ascending());
        Page<IndustryResponse> industryPage = industryService.getIndustryPage(keyword, status, pageable);
        model.addAttribute("industryPage", industryPage);
        model.addAttribute("keyword", keyword);
        model.addAttribute("statusFilter", status);
        model.addAttribute("industryRequest", new IndustryRequest());
        model.addAttribute("industryStatuses", IndustryStatus.values());
        return "pages/admin/industry-list";
    }

    @PostMapping("/add")
    public String createIndustry(
            @Valid @ModelAttribute("industryRequest") IndustryRequest request,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Vui lòng nhập đầy đủ thông tin ngành nghề.");
            return "redirect:/admin/industries/list";
        }

        try {
            industryService.createIndustry(request);
            redirectAttributes.addFlashAttribute("successMessage", "Thêm ngành nghề thành công.");
        } catch (IllegalArgumentException exception) {
            redirectAttributes.addFlashAttribute("errorMessage", exception.getMessage());
        }
        return "redirect:/admin/industries/list";
    }

    @PostMapping("/edit/{id}")
    public String updateIndustry(
            @PathVariable("id") Integer id,
            @Valid @ModelAttribute("industryRequest") IndustryRequest request,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Vui lòng nhập đầy đủ thông tin ngành nghề.");
            return "redirect:/admin/industries/list";
        }

        try {
            industryService.updateIndustry(id, request);
            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật ngành nghề thành công.");
        } catch (IllegalArgumentException exception) {
            redirectAttributes.addFlashAttribute("errorMessage", exception.getMessage());
        }
        return "redirect:/admin/industries/list";
    }

    @PostMapping("/status/{id}")
    public String updateIndustryStatus(
            @PathVariable("id") Integer id,
            @ModelAttribute IndustryStatusRequest request,
            RedirectAttributes redirectAttributes
    ) {
        try {
            industryService.updateIndustryStatus(id, request.getStatus());
            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật trạng thái ngành nghề thành công.");
        } catch (IllegalArgumentException exception) {
            redirectAttributes.addFlashAttribute("errorMessage", exception.getMessage());
        }
        return "redirect:/admin/industries/list";
    }
}
