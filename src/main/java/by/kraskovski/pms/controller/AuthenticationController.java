package by.kraskovski.pms.controller;

import by.kraskovski.pms.model.User;
import by.kraskovski.pms.model.dto.TokenDTO;
import by.kraskovski.pms.security.service.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

/**
 * Handle requests for authentication operations.
 * Works with {@link TokenService}.
 */
@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationController.class);
    private final TokenService tokenService;

    @Autowired
    public AuthenticationController(final TokenService tokenService) {
        this.tokenService = tokenService;
    }

    /**
     * Login method
     * Find {@link by.kraskovski.pms.model.User} in database by username
     * Generate token from {@link TokenService}
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity login(@RequestBody final User requestUser) {
        LOGGER.info("Start authentication user with username: " + requestUser.getUsername());
        if (isNotEmpty(requestUser.getUsername()) && isNotEmpty(requestUser.getPassword())) {
            final TokenDTO tokenDTO = tokenService.generate(requestUser.getUsername(), requestUser.getPassword());
            if (tokenDTO != null) {
                LOGGER.info("User authentication with username: {} successful!", requestUser.getUsername());
                return new ResponseEntity<>(tokenDTO, HttpStatus.OK);
            }
        }
        LOGGER.error("User authentication with username: {} failed!", requestUser.getUsername());
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    //TODO: destroy token when user logout
    @RequestMapping(value="/logout", method = RequestMethod.GET)
    public ResponseEntity logoutPage (HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
