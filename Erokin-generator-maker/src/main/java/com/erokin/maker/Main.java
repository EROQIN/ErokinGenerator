package com.erokin.maker;

import com.erokin.maker.generator.main.GenerateTemplate;
import com.erokin.maker.generator.main.ZipGenerator;
import freemarker.template.TemplateException;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws TemplateException, IOException, InterruptedException {
        // GenerateTemplate generateTemplate = new MainGenerator();
        GenerateTemplate generateTemplate = new ZipGenerator();
        generateTemplate.doGenerate();

    }
} 