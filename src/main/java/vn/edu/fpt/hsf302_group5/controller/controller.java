package vn.edu.fpt.hsf302_group5.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import vn.edu.fpt.hsf302_group5.dto.home.HomeDto;
import vn.edu.fpt.hsf302_group5.service.home.HomeService;

@Controller
@RequiredArgsConstructor
public class controller {

    private final HomeService homeService;

    @GetMapping("/test")
    public String test(){
        return "pages/candidate/application-detail";
    }

    @GetMapping("/test1")
    public String test1(){
        return "pages/candidate/job-detail";
    }

    @GetMapping("/test3")
    public String test3(){
        return "pages/candidate/my-applications";
    }

    @GetMapping("/test5")
    public String test5(){
        return "pages/candidate/upload-cv";
    }


    @GetMapping("/")
    public String test6(Model model){
        HomeDto homeDto = homeService.getHomeData();
        model.addAttribute("homeData", homeDto);
        return "pages/public/home";
    }

    @GetMapping("/privacy-policy")
    public String privacyPolicyPage() {
        return "pages/public/privacy-policy";
    }
}
