package vn.edu.fpt.hsf302_group5.controller.candidate;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/candidate")
public class CVController {

    @GetMapping("/upload-cv")
    public String uploadCV() {
        return "pages/candidate/upload-cv";
    }
}
