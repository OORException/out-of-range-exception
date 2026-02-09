package br.edu.iff.ccc.webdev.controller.restapi;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/api/v1")
public class RestMainApiController {
    
    @GetMapping()
    public ResponseEntity<String> getApiHome() {
        return ResponseEntity.ok("Bem vindo a API REST do WebDev");
    }
    

}