package main.QuizCraft.config;

import org.springframework.ai.ollama.api.OllamaModel;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OllamaConfig {

    @Bean("Llama3_2")
    public OllamaOptions llama3_2() {
        return OllamaOptions.builder()
                .model(OllamaModel.LLAMA3_2)
                .temperature(0.5)
                .build();

    }

    @Bean("deepseek-r1")
    public OllamaOptions deepseekr1(){
        return OllamaOptions.builder()
                .model(OllamaModel.valueOf("deepseek-r1"))
                .temperature(0.5)
                .build();

    }

}
