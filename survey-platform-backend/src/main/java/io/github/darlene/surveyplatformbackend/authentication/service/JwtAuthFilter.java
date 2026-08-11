package io.github.darlene.surveyplatformbackend.authentication.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    // Implement a once per request filter for jwt token so that we do not have multiple token generation
    // Once per request filter ensures that this code block can only be executed once.
    private final JwtTokenProvider jwtTokenProvider;

}