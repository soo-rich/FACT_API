package com.soosmart.facts.commands;

import com.soosmart.facts.utils.report.JasperCompilerService;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
public class JasperCompilerCommand {

    private final JasperCompilerService jasperCompilerService;

    public JasperCompilerCommand(
            JasperCompilerService jasperCompilerService
    ) {
        this.jasperCompilerService = jasperCompilerService;
    }

    @ShellMethod(key = "compile", value = "Compile all jrxml files in the template/src directory")
    public void compileJasper() {
        jasperCompilerService.compileAllJrxmlInDirectory();
        System.out.println("Compilation Jasper");
    }
}
