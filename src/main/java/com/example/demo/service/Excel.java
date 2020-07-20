package com.example.demo.service;

import com.example.demo.Dto.ReporteMensualoAnualMosqoyDto;
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
    public  ByteArrayInputStream exportarData(String ano,List<ReporteMensualoAnualMosqoyDto> lista,String tipo) throws Exception {

        int linea = 0;
        //los nombres(títulos) de las columnas en el archivo excel
        String[] columns = {"Fecha de Venta", "Factura/Boleta","N Documento","RUC/DNI(Cliente)","Cliente","Cantidad",
        "Codigo de Producto","Nombre de Producto","Color","Metodo de Pago","Precio de Venta"};

        Workbook workbook = new HSSFWorkbook(); //creando archivo Excel

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Sheet sheet = workbook.createSheet("Ventas "+tipo +" "+ano); //nombre de la hoja de excel


        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        //estilos
        headerFont.setColor(IndexedColors.BLUE.getIndex());

        CellStyle headerCellStyle = workbook.createCellStyle();//estilo de la celda
        headerCellStyle.setFont(headerFont);
        headerCellStyle.setAlignment(HorizontalAlignment.CENTER);
        headerCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        Font headerFont2 = workbook.createFont();
        headerFont2.setBold(true);
        //estilos
        headerFont2.setColor(IndexedColors.BLACK.getIndex());
        CellStyle headerCellStyle2 = workbook.createCellStyle();//estilo de la celda
        headerCellStyle2.setFont(headerFont2);
        headerCellStyle2.setAlignment(HorizontalAlignment.CENTER);
        headerCellStyle2.setVerticalAlignment(VerticalAlignment.CENTER);
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
        sheet.setColumnWidth(10, 7000);
        Row row = sheet.createRow(linea);

        for (int i = 0; i < columns.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerCellStyle);
        }
        linea++;

        for(ReporteMensualoAnualMosqoyDto reporteMensualoAnualMosqoyDto  : lista){
            Row r=sheet.createRow(linea);
            Cell c0 =r.createCell(0);
            c0.setCellValue(reporteMensualoAnualMosqoyDto.getFechadeventa().toString());
            c0.setCellStyle(headerCellStyle2);
            Cell c1 =r.createCell(1);
            c1.setCellValue(reporteMensualoAnualMosqoyDto.getTipodedocumento());
            c1.setCellStyle(headerCellStyle2);
            Cell c2 =r.createCell(2);
            c2.setCellValue(reporteMensualoAnualMosqoyDto.getDocumento());
            c2.setCellStyle(headerCellStyle2);
            Cell c3 =r.createCell(3);
            c3.setCellValue(reporteMensualoAnualMosqoyDto.getRucodni());
            c3.setCellStyle(headerCellStyle2);
            Cell c4 =r.createCell(4);
            c4.setCellValue(reporteMensualoAnualMosqoyDto.getCliente());
            c4.setCellStyle(headerCellStyle2);
            Cell c5 =r.createCell(5);
            c5.setCellValue(reporteMensualoAnualMosqoyDto.getCantidad());
            c5.setCellStyle(headerCellStyle2);
            Cell c6 =r.createCell(6);
            c6.setCellValue(reporteMensualoAnualMosqoyDto.getCodigoproducto());
            c6.setCellStyle(headerCellStyle2);
            Cell c7 =r.createCell(7);
            c7.setCellValue(reporteMensualoAnualMosqoyDto.getNombredeproducto());
            c7.setCellStyle(headerCellStyle2);
            Cell c8 =r.createCell(8);
            c8.setCellValue(reporteMensualoAnualMosqoyDto.getColor());
            c8.setCellStyle(headerCellStyle2);
            Cell c9 =r.createCell(9);
            c9.setCellValue(reporteMensualoAnualMosqoyDto.getMetododepago());
            c9.setCellStyle(headerCellStyle2);
            Cell c10 =r.createCell(10);
            c10.setCellValue(reporteMensualoAnualMosqoyDto.getPrecioVenta());
            c10.setCellStyle(headerCellStyle2);

            linea++;
        }
        /*for (ReporteMensualoAnualMosqoyDto reporteMensualoAnualMosqoyDto  : lista){
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
            row.createCell(10).setCellValue(reporteMensualoAnualMosqoyDto.getPrecioVenta());
            linea++;
        }*/
        workbook.write(stream);
        workbook.close();
        return new ByteArrayInputStream(stream.toByteArray());

    }

}
