package com.proyecto.cuentasporpagar.service;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import com.proyecto.cuentasporpagar.modelo.Deuda;
import com.proyecto.cuentasporpagar.modelo.Detalle_pago;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.util.List;

@Service
public class ReporteHistorialService {

    public void exportar(HttpServletResponse response, Deuda deuda, List<Detalle_pago> detalles) throws Exception {
        Document documento = new Document(PageSize.A4);
        PdfWriter.getInstance(documento, response.getOutputStream());

        documento.open();

        // Fuentes
        Font fontTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, Color.DARK_GRAY);
        Font fontSubtitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
        Font fontCuerpo = FontFactory.getFont(FontFactory.HELVETICA, 10);

        // Título
        Paragraph titulo = new Paragraph("ESTADO DE PAGOS POR DEUDA", fontTitulo);
        titulo.setAlignment(Element.ALIGN_CENTER);
        documento.add(titulo);
        documento.add(new Paragraph(" "));

        // Información de la Deuda
        PdfPTable tableInfo = new PdfPTable(2);
        tableInfo.setWidthPercentage(100);
        
        tableInfo.addCell(new Phrase("Proveedor: " + deuda.getProveedor().getNombre(), fontCuerpo));
        tableInfo.addCell(new Phrase("RUC: " + deuda.getProveedor().getRuc(), fontCuerpo));
        tableInfo.addCell(new Phrase("Monto Total: $" + String.format("%.2f", deuda.getMonto_total()), fontCuerpo));
        tableInfo.addCell(new Phrase("Saldo Pendiente: $" + String.format("%.2f", (deuda.getMonto_total() - deuda.getMonto_pagado())), fontCuerpo));
        
        documento.add(tableInfo);
        documento.add(new Paragraph(" "));

        // Tabla de Detalles
        PdfPTable tabla = new PdfPTable(3);
        tabla.setWidthPercentage(100);
        tabla.setWidths(new float[] {1.5f, 1.5f, 4f}); // Ancho de columnas

        // Encabezados
        String[] columnas = {"Fecha Pago", "Monto Abono", "Descripción / Responsable"};
        for (String col : columnas) {
            PdfPCell cell = new PdfPCell(new Phrase(col, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, Color.WHITE)));
            cell.setBackgroundColor(Color.BLACK);
            cell.setPadding(5);
            tabla.addCell(cell);
        }

        // Datos
        for (Detalle_pago d : detalles) {
            tabla.addCell(new Phrase(d.getPago().getFecha_pago().toString().substring(0, 10), fontCuerpo));
            tabla.addCell(new Phrase("$" + String.format("%.2f", d.getPago().getMonto_pago()), fontCuerpo));
            tabla.addCell(new Phrase(d.getDescripcion(), fontCuerpo));
        }

        documento.add(tabla);
        documento.close();
    }
}