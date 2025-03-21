package com.cece.lms.service;

import com.cece.lms.entity.ScoringApplication;
import com.cece.lms.repository.ScoringApplicationRepository;
import com.cece.lms.request.outbound.ScoringClientRequest;
import com.cece.lms.response.outbound.ScoringClientResponse;
import com.cece.lms.response.outbound.ScoringResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
@Slf4j
public class ScoringService {

    private final RestTemplate restTemplate;
    private final ScoringApplicationRepository scoringApplicationRepository;
    private final String registrationUrl;
    private final String transactionsUrl;
    private final String applicationName;
    private final String scoringInitUrl;
    private final String scoringQueryUrl;
    private final String apiUsername;
    private final String apiPassword;
    private final String token;
    private final int maxRetries;
    private final int retryDelaySeconds;

    public ScoringService(
            ScoringApplicationRepository scoringApplicationRepository,
            RestTemplate restTemplate,
            @Value("${api.scoring.registration.url}") String registrationUrl,
            @Value("${api.scoring.transactions.url}") String transactionsUrl,
            @Value("${api.scoring.application}") String applicationName,
            @Value("${api.scoring.init.url}") String scoringInitUrl,
            @Value("${api.scoring.query.url}") String scoringQueryUrl,
            @Value("${api.security.username}") String apiUsername,
            @Value("${api.security.password}") String apiPassword,
            @Value("${api.scoring.max-retries}") int maxRetries,
            @Value("${api.scoring.retry-delay-seconds}") int retryDelaySeconds
    ) {
        this.scoringApplicationRepository = scoringApplicationRepository;
        this.restTemplate = restTemplate;
        this.registrationUrl = registrationUrl;
        this.transactionsUrl = transactionsUrl;
        this.applicationName = applicationName;
        this.scoringInitUrl = scoringInitUrl;
        this.scoringQueryUrl = scoringQueryUrl;
        this.apiUsername = apiUsername;
        this.apiPassword = apiPassword;
        this.maxRetries = maxRetries;
        this.retryDelaySeconds = retryDelaySeconds;

        this.token = scoringApplicationRepository
                .findAll()
                .stream()
                .findFirst()
                .orElseGet(this::registerScoringClient)
                .getToken();
    }

    public ScoringApplication registerScoringClient() {
        try {
            log.info("Registering scoring client with URL: {}", registrationUrl);
            ScoringClientRequest request = ScoringClientRequest.builder()
                    .url(transactionsUrl)
                    .name(applicationName)
                    .username(apiUsername)
                    .password(apiPassword)
                    .build();

            ScoringClientResponse response = restTemplate.postForObject(registrationUrl, request, ScoringClientResponse.class);
            log.info("Scoring client registered with response token: {}", response.getToken());

            ScoringApplication scoringApplication = new ScoringApplication();
            scoringApplication.setToken(response.getToken());
            return scoringApplicationRepository.save(scoringApplication);
        }catch (Exception e){
            log.error("Error registering scoring client", e);
            return new ScoringApplication();
        }
    }

    public String initScoring(String customerNumber) {
        log.info("Initiating scoring for customer number: {}", customerNumber);
        HttpHeaders headers = new HttpHeaders();
        headers.set("client-token", token);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                scoringInitUrl + "/" + customerNumber,
                HttpMethod.GET,
                entity,
                String.class
        );

        log.info("Scoring initiated for customer number: {} with response: {}", customerNumber, response.getBody());
        return response.getBody();
    }

    public Optional<ScoringResponse> queryScoreStatus(String scoringToken) {
        log.info("Querying score status for scoring token: {}", scoringToken);
        HttpHeaders headers = new HttpHeaders();
        headers.set("client-token", token);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        int retryCount = 0;
        while (retryCount < maxRetries) {
            try {
                ResponseEntity<ScoringResponse> response = restTemplate.exchange(
                        scoringQueryUrl + "/" + scoringToken,
                        HttpMethod.GET,
                        entity,
                        ScoringResponse.class
                );

                if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                    ScoringResponse score = response.getBody();
                    log.info("Score status queried successfully for scoring token: {}", score);
                    return Optional.of(score);
                }

            } catch (Exception e) {
                log.error("Error querying score status for scoring token: {}", scoringToken, e);
            }

            retryCount++;
            log.info("Retrying query score status for scoring token: {} (attempt {}/{})", scoringToken, retryCount, maxRetries);

            try {
                Thread.sleep(retryDelaySeconds * 1000L);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Retry interrupted", ie);
            }
        }

        log.warn("Failed to query score status for scoring token: {} after {} attempts", scoringToken, maxRetries);
        return Optional.empty();
    }

}