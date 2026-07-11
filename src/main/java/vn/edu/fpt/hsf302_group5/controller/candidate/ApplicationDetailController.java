package vn.edu.fpt.hsf302_group5.controller.candidate;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/candidate")
public class ApplicationDetailController {

    @GetMapping("/application-detail")
    public String applicationDetail(){
        return "pages/candidate/my-applications/application-detail";
    }
}
