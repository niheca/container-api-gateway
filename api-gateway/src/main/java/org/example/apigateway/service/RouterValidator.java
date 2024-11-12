package org.example.apigateway.service;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Predicate;

@Service
public class RouterValidator {

    public static final List<String> openEndpoint = List.of(
            "/v1/auth"
    );

    public Predicate<ServerHttpRequest> isSecured = serverHttpRequest ->
            openEndpoint.stream().noneMatch(uri -> serverHttpRequest.getURI().getPath().contains(uri));
}
