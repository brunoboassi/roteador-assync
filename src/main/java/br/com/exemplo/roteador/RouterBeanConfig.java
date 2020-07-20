package br.com.exemplo.roteador;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.io.IOException;
import java.util.UUID;

@Configuration
@EnableBinding(GatewayChannels.class)
@EnableIntegration
@IntegrationComponentScan
@RequiredArgsConstructor
public class RouterBeanConfig {
    private final ObjectMapper objectMapper;
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("path_route", r -> r.path("/get")
                        .uri("forward:/magica"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> testWhenMetricPathIsNotMeet(ObjectMapper mapper, QueueGateway gateway, @Value("${spring.cloud.stream.instanceIndex}") String partition) {
        RouterFunction<ServerResponse> route = RouterFunctions.route(
                RequestPredicates.path("/magica"),
                request -> {
                    try {
                        return ServerResponse.ok().body(BodyInserters
                                .fromValue(mapper.readValue(gateway.handle(Request.builder().id(UUID.randomUUID().toString()).origin(1).messageIndex(1).build(),partition), Response.class)));
                    } catch (IOException e) {
                        e.printStackTrace();
                        return ServerResponse.badRequest().build();
                    }
                });

        return route;
    }

}
