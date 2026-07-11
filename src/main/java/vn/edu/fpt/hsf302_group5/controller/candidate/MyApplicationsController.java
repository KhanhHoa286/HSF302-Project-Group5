package vn.edu.fpt.hsf302_group5.controller.candidate;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/candidate")
public class MyApplicationsController {

    @GetMapping("/my-applications")
    public String myApplications(){
        return "pages/candidate/my-applications";
    }
}
