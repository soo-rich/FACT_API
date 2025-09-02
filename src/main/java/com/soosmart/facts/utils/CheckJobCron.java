package com.soosmart.facts.utils;

import com.soosmart.facts.dto.pagination.PaginatedRequest;
import com.soosmart.facts.repository.ArticleDAO;
import com.soosmart.facts.utils.pagination.PageMapperUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class CheckJobCron {
    private static final Logger logger = LoggerFactory.getLogger(CheckJobCron.class);

    private final ArticleDAO articleDAO;
    private final RestTemplate restTemplate;

    public CheckJobCron(ArticleDAO articleDAO, RestTemplate restTemplate) {
        this.articleDAO = articleDAO;
        this.restTemplate = restTemplate;
    }

    //    @Scheduled(fixedRate = 20000) // Runs every 20 seconds
    public void checkdatabaseNeon() {
        logger.info("Checking database Neon...");
        try {
            articleDAO.findAllBySupprimerIsFalse(PageMapperUtils.createPageableWithoutSearch(new PaginatedRequest(1, 1, null)));
            logger.info("Database Neon check completed successfully.");
        } catch (Exception e) {
            logger.error("Error checking database Neon: {}", e.getMessage(), e);
        }
    }

    //    @Scheduled(fixedRate = 60000) // Runs every 60 seconds
    public void checkApplicationhealthy() {
        logger.info("Checking application health...");
        try {
            String url = "https://soosmart-facts-api-latest.onrender.com/api/actuator/health"; // Replace with your actual health check URL
            logger.info("Application is healthy.");
            restTemplate.getForObject(url, Map.class);
        } catch (Exception e) {
            logger.error("Error during application health check: {}", e.getMessage(), e);
        }
    }
}
