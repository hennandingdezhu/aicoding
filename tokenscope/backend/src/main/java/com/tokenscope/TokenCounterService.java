package com.tokenscope;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Path;

@Service
public class TokenCounterService {

    private static final Logger log = LoggerFactory.getLogger(TokenCounterService.class);

    private final String pythonScriptPath;

    @Value("${tokenscope.model:deepseek-v4-pro}")
    private String modelName;

    public TokenCounterService(@Value("${tokenscope.python-script:../python/token_counter.py}") String pythonScriptPath) {
        this.pythonScriptPath = pythonScriptPath;
    }

    public int countTokens(String text) {
        return countTokens(text, modelName);
    }

    public int countTokens(String text, String model) {
        if (text == null || text.isEmpty()) return 0;
        try {
            Path scriptPath = Path.of(pythonScriptPath);
            ProcessBuilder pb = new ProcessBuilder("python", scriptPath.toString(), text, model);
            pb.redirectErrorStream(true);
            Process process = pb.start();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String output = reader.readLine();
                int exitCode = process.waitFor();
                if (exitCode == 0 && output != null) {
                    return Integer.parseInt(output.trim());
                }
            }
            log.warn("TokenCounter failed with exit code {}, using fallback estimation", process.exitValue());
        } catch (Exception e) {
            log.warn("TokenCounter call failed: {}, using fallback estimation", e.getMessage());
        }
        return fallbackCount(text);
    }

    private int fallbackCount(String text) {
        return Math.max(1, text.length() / 4);
    }
}