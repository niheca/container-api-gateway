package org.example.apigateway.service;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    private final RouterValidator validator;
    private final JwtUtils jwtUtils;

    public AuthenticationFilter(RouterValidator validator, JwtUtils jwtUtils) {
        super(Config.class);
        this.validator = validator;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public GatewayFilter apply(Config config) {

        return ((exchange, chain) -> {
            var request = exchange.getRequest();
            ServerHttpRequest serverHttpRequest = null;
            if(validator.isSecured.test(request)) {
                if(authMissing(request)){
                    return onError(exchange, HttpStatus.UNAUTHORIZED);
                }

                String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

                if(authHeader != null && authHeader.startsWith("Bearer ")) {


                    authHeader = authHeader.substring(7);
                }
                else {
                    return onError(exchange, HttpStatus.UNAUTHORIZED);
                }

                if(jwtUtils.isExpired(authHeader)) {
                    return onError(exchange, HttpStatus.UNAUTHORIZED);
                }

                serverHttpRequest = exchange.getRequest()
                        .mutate()
                        .header("userIdRequest",jwtUtils.getUserId(authHeader).toString())
                        .build();
            }

            return chain.filter(exchange.mutate().request(serverHttpRequest).build());
        });
    }

    public Mono<Void> onError(ServerWebExchange exchange, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return Mono.empty();
    }

    public boolean authMissing(ServerHttpRequest request) {
        return !request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION);
    }

    public static class Config {}

}
