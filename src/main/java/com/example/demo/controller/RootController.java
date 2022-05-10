package com.example.demo.controller;

import com.example.demo.dto.Sorting;
import com.example.demo.dto.SortingResponse;
import com.example.demo.util.SortingUtil;
import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/v1")
public class RootController {

  private static final Logger LOGGER = LoggerFactory.getLogger(RootController.class);

  private List<String> services;

  private final WebClient.Builder webClientBuilder;

  @Value("${target.services}")
  private String targetServices;

  private final MeterRegistry meterRegistry;

  @Autowired
  public RootController(final WebClient.Builder webClientBuilder,
      final MeterRegistry meterRegistry) {
    this.webClientBuilder = webClientBuilder;
    this.meterRegistry = meterRegistry;
  }

  @PostMapping("/sorting")
  public ResponseEntity<SortingResponse> sorting(@RequestBody Sorting sorting){
    if(sorting.getArraySize() == null || sorting.getArraySize() < 0) {
      return ResponseEntity.badRequest().build();
    }

    meterRegistry.counter("demo.request.counter.total").increment();

    LOGGER.info("Received sorting request with array size {}", sorting.getArraySize());

    long timeSpent = SortingUtil.sorting(sorting.getArraySize());

    LOGGER.info("Sorting with array size {} completed in {} milliseconds", sorting.getArraySize(), timeSpent);

    return ResponseEntity.ok(new SortingResponse(timeSpent));
  }

  @PostMapping(value = "/delegated/sorting")
  public ResponseEntity<SortingResponse> delegatedSorting(@RequestBody Sorting sorting) throws URISyntaxException {
    long start = System.currentTimeMillis();

    meterRegistry.counter("demo.request.counter.total").increment();

    if(sorting.getArraySize() == null || sorting.getArraySize() < 0) {
      return ResponseEntity.badRequest().build();
    }

    if(services == null) {
      services = Arrays.asList(targetServices.split(","));
    }

    LOGGER.info("Received delegate request with array size {}", sorting.getArraySize());

    int randomIndex = (int) (Math.random() * (services.size()));

    WebClient randomServiceClient = webClientBuilder
        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .baseUrl(services.get(randomIndex))
        .build();
    randomServiceClient
        .post()
        .uri("/v1/sorting")
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .body(Mono.just(sorting), Sorting.class)
        .retrieve()
        .bodyToMono(SortingResponse.class)
        .subscribe();
    LOGGER.info("Sent sorting request to {} with array size {}", services.get(randomIndex) + "/v1/sorting", sorting.getArraySize());

    long end = System.currentTimeMillis();
    LOGGER.info("Async method finished in {} milliseconds", end - start);
    return ResponseEntity.ok(new SortingResponse(end-start));
  }

}
