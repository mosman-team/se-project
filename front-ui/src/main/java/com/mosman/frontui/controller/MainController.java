package com.mosman.frontui.controller;

import com.mosman.frontui.payload.ActivationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

@Controller
@RequestMapping("/")
public class MainController {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${spring.profiles.active}")
    private String profile;

    @GetMapping
    public String main(Model model){
        model.addAttribute("isDevMode", "dev".equals(profile));
        return "index";
    }
    @GetMapping("/activate/{code}")
    public String activate(Model model, @PathVariable String code){

        restTemplate.getForEntity("http://zuul-server/auth/activate/"+code, ActivationResponse.class);

        return "redirect:/login";
    }
    
}










