package vn.edu.fpt.hsf302_group5.controller.candidate;

import lombok.RequiredArgsConstructor;
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
import vn.edu.fpt.hsf302_group5.entity.CV;
import vn.edu.fpt.hsf302_group5.entity.User;
import vn.edu.fpt.hsf302_group5.repository.user.UserRepository;
import vn.edu.fpt.hsf302_group5.service.cv.CVService;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/candidate")
@RequiredArgsConstructor
public class CVController {

    private final CVService cvService;
    private final UserRepository userRepository;

    @GetMapping("/upload-cv")
    public String uploadCV(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return "redirect:/login";
        }
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng!"));

        List<CV> cvs = cvService.getCVsByCandidateId(user.getUserId());
        model.addAttribute("cvList", cvs);
        return "pages/candidate/upload-cv";
    }

    @PostMapping("/upload-cv")
    public String handleUploadCV(
            @RequestParam("cvFile") MultipartFile file,
            @RequestParam(value = "cvName", required = false) String cvName,
            @AuthenticationPrincipal UserDetails userDetails,
            RedirectAttributes redirectAttributes) {

        if (userDetails == null) {
            return "redirect:/login";
        }

        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Vui lòng chọn một file để tải lên!");
            return "redirect:/candidate/upload-cv";
        }

        // Validate file type
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || !originalFilename.toLowerCase().endsWith(".pdf")) {
            redirectAttributes.addFlashAttribute("errorMessage", "Chỉ hỗ trợ tải lên file PDF!");
            return "redirect:/candidate/upload-cv";
        }

        // Validate size (max 5MB)
        if (file.getSize() > 5 * 1024 * 1024) {
            redirectAttributes.addFlashAttribute("errorMessage", "Dung lượng file vượt quá giới hạn 5MB!");
            return "redirect:/candidate/upload-cv";
        }

        try {
            User user = userRepository.findByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng!"));

            cvService.uploadCV(user.getUserId(), file, cvName);
            redirectAttributes.addFlashAttribute("successMessage", "Tải CV lên thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi trong quá trình tải CV lên hệ thống: " + e.getMessage());
        }

        return "redirect:/candidate/upload-cv";
    }

    @PostMapping("/upload-cv/delete")
    public String handleDeleteCV(
            @RequestParam("cvId") Integer cvId,
            @AuthenticationPrincipal UserDetails userDetails,
            RedirectAttributes redirectAttributes) {

        if (userDetails == null) {
            return "redirect:/login";
        }

        try {
            User user = userRepository.findByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng!"));

            cvService.deleteCV(user.getUserId(), cvId);
            redirectAttributes.addFlashAttribute("successMessage", "Xóa CV thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không thể xóa CV: " + e.getMessage());
        }

        return "redirect:/candidate/upload-cv";
    }
}

