package vn.edu.fpt.hsf302_group5.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class controller {

    @GetMapping("/test")
    public String test(){
        return "pages/candidate/application-detail";
    }
}
