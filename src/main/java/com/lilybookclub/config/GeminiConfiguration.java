package com.lilybookclub.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.genai.Client;

@Configuration
public class GeminiConfiguration {

    @Bean
    public Client geminiClient() {
        return new Client();
    }

}

