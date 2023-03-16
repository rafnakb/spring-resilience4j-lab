package com.example.serviceA.resource;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/service-a")
public class ServiceAResource {

    @Autowired
    private RestTemplate restTemplate;

    Logger logger = LoggerFactory.getLogger(ServiceAResource.class);


    int retry = 1;

    @GetMapping("/call")
    public String serviceA() {
        return restTemplate.getForObject(
                "http://localhost:8092/service-b",
                String.class
        );
    }

    @GetMapping("/cb")
    @CircuitBreaker(name = "serviceA", fallbackMethod = "fallbackCircuitBreaker")
    public String serviceACircuitBreaker() {
        return restTemplate.getForObject(
                "http://localhost:8092/service-b",
                String.class
        );
    }

    public String fallbackCircuitBreaker(Exception e) {
        return "Fallback do Serviço A. Ao persistir a falha acionar Circuit Breaker.";
    }

    @GetMapping("/retry")
    @Retry(name = "serviceA", fallbackMethod = "fallbackRetry")
    public String serviceARerty() {
        logger.info("Retry: " + retry++);
        return restTemplate.getForObject(
                "http://localhost:8092/service-b",
                String.class
        );
    }

    public String fallbackRetry(Exception e) {
        return "Excedido o número de tentativas para acessar o Serviço B";
    }

    @GetMapping("/ratelimiter")
    @RateLimiter(name = "serviceA", fallbackMethod = "fallbackRateLimit")
    public String serviceARateLimiter() {
        return restTemplate.getForObject(
                "http://localhost:8092/service-b",
                String.class
        );
    }

    public String fallbackRateLimit(Exception e) {
        return "Número de chamadas no período atingido!";
    }

}
