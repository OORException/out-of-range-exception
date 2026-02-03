package br.edu.iff.ccc.webdev;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {
    @GetMapping("/health")
    public String hello(){
        return "OK";
    }
}