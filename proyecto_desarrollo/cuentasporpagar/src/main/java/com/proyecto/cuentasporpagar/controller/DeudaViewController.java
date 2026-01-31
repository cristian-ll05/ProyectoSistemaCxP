//package com.proyecto.cuentasporpagar.controller;

//import com.proyecto.cuentasporpagar.modelo.Deuda;
//import com.proyecto.cuentasporpagar.modelo.Proveedor;
//import com.proyecto.cuentasporpagar.service.DeudaService;
//import com.proyecto.cuentasporpagar.service.ProveedorService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.ResponseBody;

//@Controller
//public class DeudaViewController {

    //@Autowired
    //private DeudaService deudaService;

    //@Autowired
    //private ProveedorService proveedorService;
    
    //@GetMapping("/test")
    //@ResponseBody
    //public String pruebaRapida() {
    //return "¡El servidor sí me encuentra!";
    //}
    
    
    //@GetMapping("/deudas")
    //public String mostrarPaginaDeudas(Model model) {
        // Enviamos la lista de deudas y de proveedores al HTML
        //model.addAttribute("deudas", deudaService.listarDeudas());
        //model.addAttribute("proveedores", proveedorService.listarTodos());
        //return "lista-deudas";
    //}
    //@GetMapping("/deudas/nuevo")
    //public String formularioRegistro(Model model) {
        //model.addAttribute("proveedores", proveedorService.listarTodos());
        //model.addAttribute("nuevaDeuda", new Deuda());
        //model.addAttribute("nuevoProveedor", new Proveedor());
        //return "registro"; 
    //}
//}