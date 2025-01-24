package com.soosmart.facts.utils.report;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
public class ReportUtils {

    public static <T> byte[] generateReport(
            List<T> data,
            String jasperPath,
            Map<String, Object> parameters
    ) throws JRException, IOException {
        JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile(jasperPath);
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(data);

        if (parameters == null) {
            parameters = new HashMap<>();
        }

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            JasperExportManager.exportReportToPdfStream(jasperPrint, byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        }
    }
}
