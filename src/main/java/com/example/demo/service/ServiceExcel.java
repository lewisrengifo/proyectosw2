package com.example.demo.service;

import java.io.ByteArrayInputStream;

public interface ServiceExcel {
    ByteArrayInputStream exportarData(String mes) throws Exception;
}
