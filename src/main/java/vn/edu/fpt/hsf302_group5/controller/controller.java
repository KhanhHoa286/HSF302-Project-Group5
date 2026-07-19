package vn.edu.fpt.hsf302_group5.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import vn.edu.fpt.hsf302_group5.service.home.HomeService;

@Controller
@RequiredArgsConstructor
public class controller {

    private final HomeService homeService;

    @GetMapping("/test")
    public String test(){
        return "pages/candidate/application-detail";
    }

    @GetMapping("/test4")
    public String test4(){
        return "pages/candidate/profile";
    }

    @GetMapping("/test5")
    public String test5(){
        return "pages/candidate/upload-cv";
    }


    @GetMapping("/")
    public String test6(){
        return "pages/public/home";
    }

    @GetMapping("/privacy-policy")
    public String privacyPolicyPage() {
        return "pages/public/privacy-policy";
    }
}
