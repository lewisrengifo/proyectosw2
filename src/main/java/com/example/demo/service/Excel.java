package com.example.demo.service;

import com.example.demo.Dto.ReporteMensualoAnualMosqoyDto;
import com.example.demo.Entity.Ventas;
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
    public ByteArrayInputStream exportarData(String ano,List<ReporteMensualoAnualMosqoyDto> lista,String tipo) throws Exception {

        int linea = 0;
        String[] columns = {"Fecha de Venta", "Factura/Boleta","N Documento","RUC/DNI(Cliente)","Cliente","Cantidad",
        "Codigo de Producto","Nombre de Producto","Color","Metodo de Pago"};

        Workbook workbook = new HSSFWorkbook(); //creando archivo Excel
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Sheet sheet = workbook.createSheet("Ventas "+tipo +" "+ano ); //nombre de la hoja de excel

        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setColor(IndexedColors.BLUE.getIndex());
        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);
        headerCellStyle.setAlignment(HorizontalAlignment.CENTER);
        headerCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        sheet.setColumnWidth(0, 7000);
        sheet.setColumnWidth(1, 7000);
        sheet.setColumnWidth(2, 7000);
        sheet.setColumnWidth(3, 7000);
        sheet.setColumnWidth(4, 7000);
        sheet.setColumnWidth(5, 7000);
        sheet.setColumnWidth(6, 7000);
        sheet.setColumnWidth(7, 7000);
        sheet.setColumnWidth(8, 7000);
        sheet.setColumnWidth(9, 7000);
        Row row = sheet.createRow(linea);

        for (int i = 0; i < columns.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerCellStyle);
        }
        linea++;

        for (ReporteMensualoAnualMosqoyDto reporteMensualoAnualMosqoyDto  : lista){
            CellStyle headerCellStyle2 = workbook.createCellStyle();
            headerCellStyle2.setAlignment(HorizontalAlignment.CENTER);
            headerCellStyle2.setVerticalAlignment(VerticalAlignment.CENTER);
            row = sheet.createRow(linea);
            row.createCell(0).setCellValue(reporteMensualoAnualMosqoyDto.getFechadeventa().toString());
            row.createCell(1).setCellValue(reporteMensualoAnualMosqoyDto.getTipodedocumento());
            row.createCell(2).setCellValue(reporteMensualoAnualMosqoyDto.getDocumento());
            row.createCell(3).setCellValue(reporteMensualoAnualMosqoyDto.getRucodni());
            row.createCell(4).setCellValue(reporteMensualoAnualMosqoyDto.getCliente());
            row.createCell(5).setCellValue(reporteMensualoAnualMosqoyDto.getCantidad());
            row.createCell(6).setCellValue(reporteMensualoAnualMosqoyDto.getCodigoproducto());
            row.createCell(7).setCellValue(reporteMensualoAnualMosqoyDto.getNombredeproducto());
            row.createCell(8).setCellValue(reporteMensualoAnualMosqoyDto.getColor());
            row.createCell(9).setCellValue(reporteMensualoAnualMosqoyDto.getMetododepago());
            /*row.createCell(0).setCellStyle(headerCellStyle2);
            row.createCell(1).setCellStyle(headerCellStyle2);
            row.createCell(2).setCellStyle(headerCellStyle2);
            row.createCell(3).setCellStyle(headerCellStyle2);
            row.createCell(4).setCellStyle(headerCellStyle2);
            row.createCell(5).setCellStyle(headerCellStyle2);
            row.createCell(6).setCellStyle(headerCellStyle2);
            row.createCell(7).setCellStyle(headerCellStyle2);
            row.createCell(8).setCellStyle(headerCellStyle2);
            row.createCell(9).setCellStyle(headerCellStyle2);*/
            linea++;
        }
        workbook.write(stream);
        workbook.close();
        return new ByteArrayInputStream(stream.toByteArray());

    }

}
