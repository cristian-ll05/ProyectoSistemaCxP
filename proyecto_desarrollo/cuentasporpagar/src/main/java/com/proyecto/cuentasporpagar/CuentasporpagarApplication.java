package com.proyecto.cuentasporpagar;

import com.proyecto.cuentasporpagar.modelo.Deuda;
import com.proyecto.cuentasporpagar.modelo.Proveedor;
import com.proyecto.cuentasporpagar.repository.DeudaRepository;
import com.proyecto.cuentasporpagar.repository.ProveedorRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;

@SpringBootApplication
public class CuentasporpagarApplication {

    public static void main(String[] args) {
        SpringApplication.run(CuentasporpagarApplication.class, args);
    }

    @Bean
    CommandLineRunner initData(ProveedorRepository pRepo, DeudaRepository dRepo) {
        return args -> {
            // Solo creamos datos si la base está vacía
            if (pRepo.count() == 0) {
                // 1. Crear Proveedor
                Proveedor p = new Proveedor();
                p.setNombre("Proveedor de Ejemplo S.A.");
                p.setRuc("2012345678");
                p.setCorreo("contacto@ejemplo.com");
                pRepo.save(p);

                // 2. Crear Deuda vinculada a ese proveedor
                Deuda d = new Deuda();
                d.setProveedor(p);
                d.setMonto_total(500.00);
                d.setFecha_emision(LocalDateTime.now());
                d.setFecha_vencimiento(LocalDateTime.now().plusDays(15));
                d.setEstado("Pendiente");
                dRepo.save(d);

                System.out.println(">>> Datos de prueba cargados correctamente <<<");
            }
        };
    }
}