package com.proyecto.cuentasporpagar.service;

import com.proyecto.cuentasporpagar.modelo.OrdenCompra;
import com.proyecto.cuentasporpagar.repository.OrdenCompraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class OrdenCompraService {

    @Autowired
    private OrdenCompraRepository ordenCompraRepository;

    public List<OrdenCompra> listarTodas() {
        return ordenCompraRepository.findAll();
    }

    public OrdenCompra guardar(OrdenCompra orden) {
        return ordenCompraRepository.save(orden);
    }

    public OrdenCompra buscarPorId(Integer id) {
        return ordenCompraRepository.findById(id).orElse(null);
    }
}