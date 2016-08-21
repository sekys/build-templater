package sk.seky.gradle;

import com.google.common.io.Files;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Lukas Sekerak
 */
public class TemplateProcessor {
    private File templatesDir;
    private File outputDir;
    private Configuration configuration;

    public TemplateProcessor(File templatesDir, File outputDir) throws IOException {
        this.templatesDir = templatesDir;
        this.outputDir = outputDir;

        configuration = new Configuration();
        configuration.setDefaultEncoding("utf-8");
        configuration.setOutputEncoding("utf-8");
        configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        configuration.setTemplateUpdateDelayMilliseconds(0); // disable cache
        configuration.setDirectoryForTemplateLoading(this.templatesDir);
    }

    public void processTemplate(String input, File outputFile) throws IOException, TemplateException {
        Map<String, String> properties = new HashMap<>();
        FileWriter fw = new FileWriter(outputFile);
        Template template = configuration.getTemplate(input);
        template.process(properties, fw);
    }

    public String computeRelativePath(File file) {
        java.nio.file.Path relativePath = templatesDir.toPath().relativize(file.toPath());
        return relativePath.toString();
    }

    public void processFile(File file) throws IOException, TemplateException {
        String relativePath = computeRelativePath(file);
        boolean ftl = relativePath.endsWith(".ftl");
        if (ftl && file.getName().contains(".inc.")) {
            this.processAll();
            return;
        }
        String newPath = relativePath;
        if (ftl) {
            newPath = newPath.substring(0, newPath.length() - 4);
        }
        File outputFile = new File(outputDir, newPath);
        outputFile.delete();
        outputFile.getParentFile().mkdirs();
        if (ftl) {
            this.processTemplate(relativePath, outputFile);
        } else {
            Files.copy(file, outputFile);
        }
    }

    public void processAll() throws IOException, TemplateException {
        for (File file : Files.fileTreeTraverser().preOrderTraversal(templatesDir)) {
            if (!file.getName().endsWith(".ftl")) {
                continue;
            }
            if (file.getName().contains(".inc.")) {
                continue;
            }
            this.processFile(file);
        }
    }
}
