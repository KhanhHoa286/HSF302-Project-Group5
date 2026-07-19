package vn.edu.fpt.hsf302_group5.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import vn.edu.fpt.hsf302_group5.dto.home.HomeDto;
import vn.edu.fpt.hsf302_group5.service.home.HomeService;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final HomeService homeService;

    @GetMapping({"/", "/home"})
    public String home(Model model) {
        HomeDto homeData = homeService.getHomeData();
        model.addAttribute("homeData", homeData);
        return "pages/public/home";
    }
}
