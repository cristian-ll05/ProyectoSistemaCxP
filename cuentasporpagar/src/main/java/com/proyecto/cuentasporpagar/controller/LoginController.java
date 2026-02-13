package com.proyecto.cuentasporpagar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.proyecto.cuentasporpagar.modelo.Usuario;
import com.proyecto.cuentasporpagar.repository.UsuarioRepository;

@Controller
public class LoginController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    
    
    // Muestra la página de Login
    @GetMapping("/login")
    public String login() {
        return "login"; // Retorna login.html
    }

    // Muestra la página de Registro
    @GetMapping("/registro")
    public String mostrarRegistro() {
        return "registro-usuario"; // Retorna registro-usuario.html
    }


    @PostMapping("/registro")
    public String registrarUsuario(@RequestParam String username, 
                                @RequestParam String password, 
                                @RequestParam String email) {
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setUsername(username);
        nuevoUsuario.setEmail(email);
        nuevoUsuario.setPassword(passwordEncoder.encode(password));
        nuevoUsuario.setRole("USER");

        usuarioRepository.save(nuevoUsuario);
        return "redirect:/login?success=true";
    }
}