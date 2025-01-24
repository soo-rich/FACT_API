package com.soosmart.facts.utils.report;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.InputStream;

@Service
public class JasperCompilerService {

    /**
     * Compile un fichier .jrxml en un fichier .jasper.
     *
     * @param jrxmlFilePath Chemin du fichier .jrxml dans le classpath.
     * @param jasperOutputDir Répertoire de sortie dans le classpath pour le fichier .jasper compilé.
     * @return Le chemin du fichier .jasper généré.
     */
    public String compileJrxmlToJasper(String jrxmlFilePath, String jasperOutputDir) {
        try {
            // Charger le fichier .jrxml depuis le classpath
            ClassPathResource jrxmlResource = new ClassPathResource(jrxmlFilePath);
            InputStream inputStream = jrxmlResource.getInputStream();
            JasperDesign jasperDesign = JRXmlLoader.load(inputStream);

            // Créer le répertoire de sortie s'il n'existe pas
            File outputDir = new ClassPathResource(jasperOutputDir).getFile();
            if (!outputDir.exists()) {
                System.out.println("Création du répertoire de sortie : " + outputDir.getAbsolutePath());
                outputDir.mkdirs(); // Crée le répertoire et tous les parents nécessaires
                System.out.println("Répertoire créé : " + outputDir.getAbsolutePath());
            }

            // Définir le chemin de sortie pour le fichier .jasper
            String jasperFileName = new File(jrxmlFilePath).getName().replace(".jrxml", ".jasper");
            String jasperOutputPath = new ClassPathResource(jasperOutputDir).getFile().getAbsolutePath() + File.separator + jasperFileName;

            // Compiler le fichier .jrxml en .jasper
            JasperCompileManager.compileReportToFile(jasperDesign, jasperOutputPath);

            System.out.println("Compilation réussie : " + jasperOutputPath);
            return jasperOutputPath;
        } catch (JRException e) {
            System.err.println("Erreur lors de la compilation du fichier .jrxml : " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Erreur inattendue : " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public void compileAllJrxmlInDirectory() {
        try {
            // Lister tous les fichiers .jrxml dans le répertoire source
            File sourceDirectory = new ClassPathResource("templates/src").getFile();
            File[] jrxmlFiles = sourceDirectory.listFiles((dir, name) -> name.endsWith(".jrxml"));

            if (jrxmlFiles != null) {
                for (File jrxmlFile : jrxmlFiles) {
                    String jrxmlFilePath = "templates/src" + "/" + jrxmlFile.getName();
                    this.compileJrxmlToJasper(jrxmlFilePath, "templates");
                }
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de la compilation des fichiers .jrxml : " + e.getMessage());
            e.printStackTrace();
        }
    }
}