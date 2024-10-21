package org.example;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatController {

    @GetMapping("/api/status")
    public String status() {
        return "El servidor está funcionando correctamente.";
    }
}
