package by.kraskovski.pms.controller;

import by.kraskovski.pms.domain.model.User;
import by.kraskovski.pms.domain.dto.TokenDTO;
import by.kraskovski.pms.security.service.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

import static java.util.Optional.ofNullable;

/**
 * Handle requests for authentication operations.
 * Works with {@link TokenService}.
 */
@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthenticationController {

    private final TokenService tokenService;

    @Autowired
    public AuthenticationController(final TokenService tokenService) {
        this.tokenService = tokenService;
    }

    /**
     * Login method
     * Find {@link User} in database by username
     * Generate token from {@link TokenService}
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity login(@RequestBody final User requestUser) {
        log.info("Start authentication user with username: " + requestUser.getUsername());
        try {
            return ofNullable(tokenService.generate(requestUser.getUsername(), requestUser.getPassword()))
                    .map(tokenDTO -> {
                        log.info("User authentication with username: {} successful!", requestUser.getUsername());
                        return new ResponseEntity<>(tokenDTO, HttpStatus.ACCEPTED);
                    })
                    .orElseThrow(() -> new IllegalArgumentException("Generated token is null."));
        } catch (IllegalArgumentException | BadCredentialsException e) {
            log.error(
                    "User authentication with username: {} failed! Cause: {}",
                    requestUser.getUsername(),
                    e.getLocalizedMessage());
            return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.UNAUTHORIZED);
        }
    }
}
