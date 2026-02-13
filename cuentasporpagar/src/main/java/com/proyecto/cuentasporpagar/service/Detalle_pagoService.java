package com.proyecto.cuentasporpagar.service;

import com.proyecto.cuentasporpagar.modelo.Detalle_pago;
import com.proyecto.cuentasporpagar.repository.Detalle_pagoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class Detalle_pagoService {

    @Autowired
    private Detalle_pagoRepository detalle_pagoRepository;

    public List<Detalle_pago> listarDetalles() {
        return detalle_pagoRepository.findAll();
    }

    public Detalle_pago guardar(Detalle_pago detalle) {
        return detalle_pagoRepository.save(detalle);
    }
}