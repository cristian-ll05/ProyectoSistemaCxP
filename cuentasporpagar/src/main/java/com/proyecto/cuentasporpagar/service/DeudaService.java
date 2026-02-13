package com.proyecto.cuentasporpagar.service;

import com.proyecto.cuentasporpagar.modelo.Deuda;
import com.proyecto.cuentasporpagar.repository.DeudaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DeudaService {

    @Autowired
    private DeudaRepository deudaRepository;

    public List<Deuda> listarDeudas() {
    return deudaRepository.listarDeudasOrdenadas();
    }
    public Deuda guardar(Deuda deuda) {
        return deudaRepository.save(deuda);
    }
    
    public void eliminar(Integer id) {
        deudaRepository.deleteById(id);
    }
}