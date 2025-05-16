package com.ecommerce.gateway.config;

import jakarta.validation.constraints.NotNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtGrantedAuthoritiesConverterAdapter;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class ReactiveJwtAuthenticationConverter implements Converter<Jwt, Mono<AbstractAuthenticationToken>> {
    private static final String REALM_ACCESS = "realm_access";
    private static final String ROLES = "roles";
    private static final String ROLE_PREFIX = "ROLE_";
    private static final String PREFERRED_USERNAME = "preferred_username";

    private final ReactiveJwtGrantedAuthoritiesConverterAdapter jwtGrantedAuthoritiesConverter =
            new ReactiveJwtGrantedAuthoritiesConverterAdapter(jwt -> Set.of());

    @Override
    public Mono<AbstractAuthenticationToken> convert(@NotNull Jwt jwt) {
        return Objects.requireNonNull(jwtGrantedAuthoritiesConverter.convert(jwt))
                .collectList()
                .map(grantedAuthorities -> createJwtAuthenticationToken(jwt, grantedAuthorities));
    }

    private JwtAuthenticationToken createJwtAuthenticationToken(Jwt jwt, List<GrantedAuthority> grantedAuthorities) {
        Collection<GrantedAuthority> authorities = Stream
                .concat(grantedAuthorities.stream(), extractRealmRoles(jwt).stream())
                .collect(Collectors.toList());

        return new JwtAuthenticationToken(jwt, authorities, jwt.getClaim(PREFERRED_USERNAME));
    }

    private Collection<? extends GrantedAuthority> extractRealmRoles(Jwt jwt) {
        Map<String, Object> realmAccess = jwt.getClaim(REALM_ACCESS);
        if (realmAccess == null || !realmAccess.containsKey(ROLES)) {
            return Collections.emptySet();
        }
        Collection<String> realmRoles = (Collection<String>) realmAccess.get(ROLES);

        return realmRoles.stream()
                .map(role -> {
                    String authority = ROLE_PREFIX + role.toUpperCase();
                    return new SimpleGrantedAuthority(authority);
                })
                .collect(Collectors.toSet());
    }
}
