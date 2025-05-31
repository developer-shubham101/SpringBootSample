package com.example.reactive.controller;


import com.example.reactive.model.CustomEpisode;
import com.example.reactive.model.EpisodeResponse;
import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/episodes")
@RequiredArgsConstructor
public class EpisodeController {

    private final WebClient webClient;
    private final Tracer tracer;  // from micrometer
    private final Logger logger = LoggerFactory.getLogger(EpisodeController.class);

   /* @GetMapping
    public Mono<ResponseEntity<List<CustomEpisode>>> getEpisodes() {
        // Manually create and start a span
        Span newSpan = tracer.nextSpan().name("fetch-episodes").start();
        try (Tracer.SpanInScope ws = tracer.withSpan(newSpan)) {

            return webClient.get()
                    .uri("https://apissa.sampleapis.com/futurama/episodexxs") // Intentionally faulty for error testing
                    .retrieve()
                    .bodyToFlux(EpisodeResponse.class)
                    .map(ep -> new CustomEpisode(ep.title, ep.writers, ep.originalAirDate, ep.desc, ep.id))
                    .collectList()
                    .map(ResponseEntity::ok)
                    .doOnSuccess(res -> {
                        newSpan.tag("episodes.success", "true");
                        newSpan.event("Successfully fetched episodes");
                        newSpan.end();
                    })
                    .doOnError(e -> {
                        newSpan.tag("episodes.success", "false");
                        newSpan.error(e);
                        newSpan.end();
                    })
                    .onErrorResume(e -> {
                        String traceId = newSpan.context().traceId();

//                        logger.error("Error fetching episodes. Trace ID: {}", traceId, e);

                        CustomEpisode errorEpisode = new CustomEpisode(
                                "Error occurred", "", "", "Trace ID: " + traceId + ", error: " + e.getMessage(), -1
                        );
                        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(Collections.singletonList(errorEpisode)));
                    });

        } catch (Exception e) {
            newSpan.error(e);
            newSpan.end();
            throw e;
        }
    }*/

    @GetMapping
    public Mono<ResponseEntity<List<CustomEpisode>>> getEpisodes() {

        String traceId = tracer.currentSpan() != null
                ? tracer.currentSpan().context().traceId()
                : "N/A";

        return webClient.get()
                .uri("https://apissa.sampleapis.com/futurama/episodexxs") // sample typo in URL to trigger error
                .retrieve()
                .bodyToFlux(EpisodeResponse.class)
                .contextWrite(Context.of("traceId", traceId))
                .map(ep -> new CustomEpisode(ep.getTitle(), ep.getWriters(), ep.getOriginalAirDate(), ep.getDesc(), ep.getId()))
                .collectList()
                .map(ResponseEntity::ok)
                .onErrorResume(e ->
                        Mono.deferContextual(ctx -> {
                            String ctxTraceId = ctx.getOrDefault("traceId", "N/A");
                            logger.info("TraceId inside context: {}", ctxTraceId);

                            logger.error("Error fetching episodes. Trace ID: {}", ctxTraceId, e);

                            CustomEpisode errorEpisode = new CustomEpisode(
                                    "Error occurred", "", "", "Trace ID: " + traceId + ", error: " + e.getMessage(), -1
                            );

                            return Mono.just(ResponseEntity
                                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                    .body(Collections.singletonList(errorEpisode)));
                        })
                );
    }


    public Mono<ResponseEntity<String>> callApiWithTrace(WebClient webClient, Tracer tracer) {
        String traceId = tracer.currentSpan() != null
                ? tracer.currentSpan().context().traceId()
                : "N/A";

        return webClient.get()
                .uri("https://api.sampleapis.com/futurama/episodes")
                .retrieve()
                .bodyToMono(String.class)
                .contextWrite(Context.of("traceId", traceId))
                .flatMap(response -> Mono.deferContextual(ctx -> {
                    String ctxTraceId = ctx.getOrDefault("traceId", "N/A");
                    logger.info("TraceId inside context: {}", ctxTraceId);
                    return Mono.just(ResponseEntity.ok("Success with traceId: " + ctxTraceId));
                }))
                .onErrorResume(e -> Mono.deferContextual(ctx -> {
                    String ctxTraceId = ctx.getOrDefault("traceId", "N/A");
                    logger.error("Error with traceId {}: {}", ctxTraceId, e.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body("Error: " + e.getMessage() + " TraceId: " + ctxTraceId));
                }));
    }

}
