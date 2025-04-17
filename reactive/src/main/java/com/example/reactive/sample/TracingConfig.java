package com.example.reactive.sample;

import io.micrometer.tracing.Tracer;
import io.micrometer.tracing.otel.bridge.OtelCurrentTraceContext;
import io.micrometer.tracing.otel.bridge.OtelTracer;
import io.opentelemetry.exporter.logging.LoggingSpanExporter;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.export.SimpleSpanProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TracingConfig {

    /**
     * Creates and configures a Tracer bean for distributed tracing using OpenTelemetry with span data exported to logs.
     *
     * @return a Tracer instance set up with a logging span exporter and current trace context management
     */
    @Bean
    public Tracer tracer() {
        SdkTracerProvider sdkTracerProvider = SdkTracerProvider.builder()
                .addSpanProcessor(SimpleSpanProcessor.create(new LoggingSpanExporter()))
                .build();

        OpenTelemetrySdk openTelemetry = OpenTelemetrySdk.builder()
                .setTracerProvider(sdkTracerProvider)
                .build();

        OtelCurrentTraceContext otelCurrentTraceContext = new OtelCurrentTraceContext();

        return new OtelTracer(
                openTelemetry.getTracer("your-app"),
                otelCurrentTraceContext,
                (span) -> {
                }
        );
    }

//    @Bean
//    public Tracer tracer(OpenTelemetry openTelemetry) {
//        return MicrometerTracerFactory.create(openTelemetry);
//    }
}
