package com.proyecto.cuentasporpagar.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard"; // Busca templates/dashboard.html
    }
    
    // Opcional: Si alguien escribe la raíz, lo mandamos al dashboard
    @GetMapping("/")
    public String raiz() {
        return "redirect:/dashboard";
    }
}