package com.example.demo.service;

import com.example.demo.Entity.Ventas;
import com.example.demo.Repository.VentaRepository;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class Excel implements ServiceExcel{
    @Autowired
    VentaRepository ventaRepository;

    @Override
    public ByteArrayInputStream exportarData() throws Exception {

        int linea = 0;
        String[] columns = {"#", "RUC/DNI","Nombre Comprador","# Documento","Lugar de Vente","Fecha Venta",
        "Tipo de Documento(Factura/Boleta)","Sede","Tienda"};

        Workbook workbook = new HSSFWorkbook(); //creando archivo Excel
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Sheet sheet = workbook.createSheet("Total Ventas"); //nombre de la hoja de excel

        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setColor(IndexedColors.BLUE.getIndex());

        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);
        headerCellStyle.setAlignment(HorizontalAlignment.CENTER);
        headerCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        headerCellStyle.setFillBackgroundColor(HSSFColor.HSSFColorPredefined.YELLOW.getIndex());
        //headerCellStyle.setFillPattern(FillPatternType.THIN_BACKWARD_DIAG);


        sheet.setColumnWidth(0, 1000);
        sheet.setColumnWidth(1, 5000);
        sheet.setColumnWidth(2, 5000);
        sheet.setColumnWidth(3, 5000);
        sheet.setColumnWidth(4, 5000);
        sheet.setColumnWidth(5, 5000);
        sheet.setColumnWidth(6, 9000);
        sheet.setColumnWidth(7, 5000);
        sheet.setColumnWidth(8, 5000);
        Row row = sheet.createRow(linea);

        for (int i = 0; i < columns.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerCellStyle);

        }
        linea++;
        for (Ventas ventas : ventaRepository.findAll()){
            row = sheet.createRow(linea);
            row.createCell(0).setCellValue(ventas.getIdventas());
            row.createCell(1).setCellValue(ventas.getRucdni());
            row.createCell(2).setCellValue(ventas.getNombrecomprador());
            row.createCell(3).setCellValue(ventas.getNumerodocumento());
            row.createCell(4).setCellValue(ventas.getLugarventa());
            row.createCell(5).setCellValue(ventas.getFechaventa());
            row.createCell(6).setCellValue(ventas.getTipodocumento());
            row.createCell(7).setCellValue(ventas.getSede().getNombre());
            row.createCell(8).setCellValue(ventas.getTienda().getNombre());
            linea++;
        }
        workbook.write(stream);
        workbook.close();
        return new ByteArrayInputStream(stream.toByteArray());

    }

}
