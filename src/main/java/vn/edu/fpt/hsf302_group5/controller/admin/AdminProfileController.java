package vn.edu.fpt.hsf302_group5.controller.admin;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import vn.edu.fpt.hsf302_group5.dto.user.CustomUserDetailsResponse;

@Controller
@RequestMapping("/admin/profile")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminProfileController {

    @ModelAttribute("adminUser")
    public CustomUserDetailsResponse adminUser(@AuthenticationPrincipal CustomUserDetailsResponse userDetails) {
        return userDetails;
    }

    @GetMapping
    public String viewAdminProfile(@AuthenticationPrincipal CustomUserDetailsResponse userDetails, Model model) {
        model.addAttribute("admin", userDetails);
        return "pages/admin/profile";
    }
}
