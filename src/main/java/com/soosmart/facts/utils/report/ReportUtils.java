package com.soosmart.facts.utils.report;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
public class ReportUtils {

    public <T> byte[] generateReport(
            List<T> data,
            String jasperFile,
            Map<String, Object> parameters
    ) throws JRException, IOException {
        File template  = ResourceUtils.getFile("classpath:templates/" + jasperFile);

        JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile(template.getPath());
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
