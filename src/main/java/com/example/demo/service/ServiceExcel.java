package com.example.demo.service;

import com.example.demo.Dto.ReporteMensualoAnualMosqoyDto;

import java.io.ByteArrayInputStream;
import java.util.List;

public interface ServiceExcel {
    ByteArrayInputStream exportarData(String mes, List<ReporteMensualoAnualMosqoyDto> lista,String tipo) throws Exception;
}
