package br.edu.iff.ccc.webdev.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
@RequestMapping("/home")
public class HomeViewController {
    
    @GetMapping()
    public String paginaPrincipal() {
        return "home.html";
    }

}