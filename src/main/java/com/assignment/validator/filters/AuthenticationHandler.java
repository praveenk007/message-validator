package com.assignment.validator.filters;

import com.assignment.validator.pojos.AuthCredentials;
import com.assignment.validator.services.impl.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * <p>
 *
 * </p>
 *
 * @author praveenkamath
 * created on 11/07/20
 * @since 1.0.0
 */
@Component
public class AuthenticationHandler implements WebFilter {

	@Autowired
	private AccountService accountService;

	@Override
	public Mono<Void> filter(ServerWebExchange serverWebExchange, WebFilterChain webFilterChain) {
		final HttpHeaders httpHeaders = serverWebExchange.getRequest().getHeaders();
		final String username = httpHeaders.getFirst("username");
		final String authId = httpHeaders.getFirst("auth_id");
		final boolean isAuthenticUser = accountService.isAuthenticate(AuthCredentials.builder().username(username).authId(authId).build());
		if(isAuthenticUser) {
			return webFilterChain.filter(serverWebExchange);
		}
		final ServerHttpResponse serverHttpResponse = serverWebExchange.getResponse();
		serverHttpResponse.setStatusCode(HttpStatus.FORBIDDEN);
		serverHttpResponse.getHeaders().setContentType(MediaType.APPLICATION_JSON);
		return serverHttpResponse.writeWith(Mono.just(serverHttpResponse.bufferFactory().wrap("{\"message\" : \"Bad credentials!\"}".getBytes())));
	}
}
