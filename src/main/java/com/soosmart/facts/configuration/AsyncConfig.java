package com.soosmart.facts.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig {
    @Bean(name = "taskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        // Nombre de threads core
        executor.setCorePoolSize(5);

        // Nombre maximum de threads
        executor.setMaxPoolSize(10);

        // Taille de la queue
        executor.setQueueCapacity(100);

        // Préfixe du nom des threads
        executor.setThreadNamePrefix("ArticleAsync-");

        // Initialiser l'executor
        executor.initialize();

        return executor;
    }
}


