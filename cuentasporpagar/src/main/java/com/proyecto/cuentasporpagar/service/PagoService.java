package com.proyecto.cuentasporpagar.service;

import com.proyecto.cuentasporpagar.modelo.Pago;
import com.proyecto.cuentasporpagar.repository.PagoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PagoService {

    @Autowired
    private PagoRepository pagoRepository;

    public List<Pago> listarPagos() {
        return pagoRepository.findAll();
    }

    public Pago registrarPago(Pago pago) {
        return pagoRepository.save(pago);
    }
}