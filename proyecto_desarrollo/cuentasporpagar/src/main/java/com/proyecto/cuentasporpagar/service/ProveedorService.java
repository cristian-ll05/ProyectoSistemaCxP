package com.proyecto.cuentasporpagar.service;

import com.proyecto.cuentasporpagar.modelo.Proveedor;
import com.proyecto.cuentasporpagar.repository.ProveedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProveedorService {

    @Autowired
    private ProveedorRepository proveedorRepository;

    // Listar todos los proveedores
    public List<Proveedor> listarTodos() {
        return proveedorRepository.findAll();
    }

    // Guardar un nuevo proveedor
    public Proveedor guardar(Proveedor proveedor) {
        return proveedorRepository.save(proveedor);
    }

    // Buscar por ID
    public Proveedor buscarPorId(Integer id) {
        return proveedorRepository.findById(id).orElse(null);
    }

    // Eliminar
    public void eliminar(Integer id) {
        proveedorRepository.deleteById(id);
    }
}