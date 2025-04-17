package com.example.reactive;


import com.example.reactive.sample.CustomEpisode;
import com.example.reactive.sample.EpisodeResponse;
import io.micrometer.tracing.Span;
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

   /**
     * Handles HTTP GET requests to retrieve a list of Futurama episodes from an external API, propagating the current trace ID for distributed tracing.
     *
     * If the external API call succeeds, returns a list of episodes wrapped in a 200 OK response. If an error occurs, returns a 500 response containing a single episode entry describing the error and including the trace ID for troubleshooting.
     *
     * The trace ID is propagated via Reactor's context and included in error responses and logs for observability.
     *
     * @return a reactive Mono emitting a ResponseEntity containing either the list of episodes or an error description with trace information
     */

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
                .map(ep -> new CustomEpisode(ep.title, ep.writers, ep.originalAirDate, ep.desc, ep.id))
                .collectList()
                .map(ResponseEntity::ok)
                .onErrorResume(e ->
                        Mono.deferContextual(ctx -> {
                             String ctxTraceId = ctx.getOrDefault("traceId", "N/A");
                             logger.info("TraceId inside context: {}", ctxTraceId);

                            logger.error("Error fetching episodes. Trace ID: {}", traceId, e);

                            CustomEpisode errorEpisode = new CustomEpisode(
                                    "Error occurred", "", "", "Trace ID: " + traceId + ", error: " + e.getMessage(), -1
                            );

                            return Mono.just(ResponseEntity
                                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                    .body(Collections.singletonList(errorEpisode)));
                        })
                );
    }


    /**
     * Performs a GET request to an external API and returns a response containing the current trace ID.
     *
     * The trace ID from the current span is propagated through Reactor's context and included in the response.
     * On error, returns a 500 response with the error message and trace ID.
     *
     * @return a Mono emitting a ResponseEntity with a success or error message including the trace ID
     */
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
