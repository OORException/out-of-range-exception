package br.edu.iff.ccc.webdev.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
@RequestMapping("/")
public class MainViewController {
    
    @GetMapping()
    public String paginaPrincipal() {
        return "index.html";
    }

}