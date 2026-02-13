package com.proyecto.cuentasporpagar.service;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.proyecto.cuentasporpagar.modelo.Deuda;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.IOException;
import java.util.List;

@Service
public class ReporteDeudaService {

    public void exportarDeudasPDF(List<Deuda> deudas, String tituloPersonalizado, HttpServletResponse response) throws IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();

        // Fuentes
        Font fontTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, Color.DARK_GRAY);
        Font fontSubtitulo = FontFactory.getFont(FontFactory.HELVETICA, 10, Color.GRAY);
        Font fontCabecera = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, Color.WHITE);
        Font fontCuerpo = FontFactory.getFont(FontFactory.HELVETICA, 9, Color.BLACK);

        // Título Principal
        Paragraph titulo = new Paragraph(tituloPersonalizado.toUpperCase(), fontTitulo);
        titulo.setAlignment(Element.ALIGN_CENTER);
        document.add(titulo);

        // Subtítulo (Fecha del reporte)
        Paragraph sub = new Paragraph("Reporte generado automáticamente por el Sistema de Gestión", fontSubtitulo);
        sub.setAlignment(Element.ALIGN_CENTER);
        sub.setSpacingAfter(20);
        document.add(sub);

        // Crear Tabla con 5 columnas
        PdfPTable tabla = new PdfPTable(5);
        tabla.setWidthPercentage(100);
        tabla.setWidths(new float[] {3.5f, 2f, 2f, 2f, 2f}); // Ajuste de ancho de columnas

        // Definir la cabecera con estilo
        String[] columnas = {"Proveedor", "RUC", "Deuda", "Monto Pagado", "Estado"};
        for (String columna : columnas) {
            PdfPCell cell = new PdfPCell(new Phrase(columna, fontCabecera));
            cell.setBackgroundColor(new Color(33, 37, 41)); // Color oscuro (estilo Bootstrap)
            cell.setPadding(8);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabla.addCell(cell);
        }

        // Llenar datos con colores condicionales
        for (Deuda deuda : deudas) {
            // Columna Proveedor
            tabla.addCell(new PdfPCell(new Phrase(deuda.getProveedor().getNombre(), fontCuerpo)));
            
            // Columna RUC
            tabla.addCell(new PdfPCell(new Phrase(deuda.getProveedor().getRuc(), fontCuerpo)));
            
            // Columna Monto Total
            tabla.addCell(new PdfPCell(new Phrase("$ " + String.format("%.2f", deuda.getMonto_total()), fontCuerpo)));
            
            // Columna Monto Pagado
            tabla.addCell(new PdfPCell(new Phrase("$ " + String.format("%.2f", deuda.getMonto_pagado()), fontCuerpo)));

            // Columna Estado con color condicional
            PdfPCell cellEstado = new PdfPCell(new Phrase(deuda.getEstado(), fontCuerpo));
            cellEstado.setHorizontalAlignment(Element.ALIGN_CENTER);
            
            if ("Pagada".equalsIgnoreCase(deuda.getEstado())) {
                cellEstado.setBackgroundColor(new Color(212, 237, 218)); // Verde clarito
            } else if ("Parcial".equalsIgnoreCase(deuda.getEstado())) {
                cellEstado.setBackgroundColor(new Color(255, 243, 205)); // Amarillo clarito
            } else {
                cellEstado.setBackgroundColor(new Color(248, 215, 218)); // Rojo clarito
            }
            tabla.addCell(cellEstado);
        }

        document.add(tabla);
        document.close();
    }
}