package br.edu.iff.ccc.webdev.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/profile")
public class ProfileViewController {

    @GetMapping
    public String viewProfile() {
        return "profile.html";
    }
}
