package br.com.fiap.fiapeats.adapter.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/autorizar")
public class ClienteController {

    @GetMapping
    public ResponseEntity<Object> consultarCliente(){
        return ResponseEntity.status(201).build();
    }
}
