package by.kraskovski.pms.controller;

import by.kraskovski.pms.controller.config.ControllerConfig;
import by.kraskovski.pms.domain.model.User;
import by.kraskovski.pms.service.UserService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static by.kraskovski.pms.domain.enums.AuthorityEnum.ROLE_ADMIN;
import static by.kraskovski.pms.utils.TestUtils.prepareUser;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class UserControllerIT extends ControllerConfig {

    private static final String BASE_USER_URL = "/user";

    @Autowired
    private UserService userService;

    @Before
    public void before() {
        userService.deleteAll();
        authenticateUserWithAuthority(ROLE_ADMIN);
    }

    @After
    public void after() {
        cleanup();
        userService.deleteAll();
    }

    @Test
    public void loadAllUsersTest() throws Exception {
        final User user = userService.create(prepareUser());

        mvc.perform(get(BASE_USER_URL)
                .header(authHeaderName, token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$[1].id", is(user.getId())));
    }

    @Test
    public void loadUserByIdTest() throws Exception {
        final User user = userService.create(prepareUser());

        mvc.perform(get(BASE_USER_URL + "/" + user.getId())
                .header(authHeaderName, token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(user.getId())));
    }

    @Test
    public void loadUserByUsernameTest() throws Exception {
        final User user = userService.create(prepareUser());

        mvc.perform(get(BASE_USER_URL + "/username/" + user.getUsername())
                .header(authHeaderName, token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(user.getId())))
                .andExpect(jsonPath("$.username", is(user.getUsername())));
    }

    @Test
    public void createUserTest() throws Exception {
        final User user = prepareUser();

        mvc.perform(post(BASE_USER_URL)
                .header(authHeaderName, token)
                .contentType(APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.username", is(user.getUsername())))
                .andExpect(jsonPath("$.password", is(user.getPassword())))
                .andExpect(jsonPath("$.firstName", is(user.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(user.getLastName())))
                .andExpect(jsonPath("$.email", is(user.getEmail())))
                .andExpect(jsonPath("$.phone", is(user.getPhone())));
    }

    @Test
    public void updateUserTest() throws Exception {
        final User user = userService.create(prepareUser());
        user.setUsername(randomAlphabetic(20));
        user.setPassword(randomAlphabetic(20));
        user.setFirstName(randomAlphabetic(20));
        user.setLastName(randomAlphabetic(20));
        user.setEmail(randomAlphabetic(20));
        user.setPhone(randomAlphabetic(20));

        mvc.perform(put(BASE_USER_URL)
                .header(authHeaderName, token)
                .contentType(APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(user.getId())))
                .andExpect(jsonPath("$.username", is(user.getUsername())))
                .andExpect(jsonPath("$.password", notNullValue()))
                .andExpect(jsonPath("$.firstName", is(user.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(user.getLastName())))
                .andExpect(jsonPath("$.email", is(user.getEmail())))
                .andExpect(jsonPath("$.phone", is(user.getPhone())));
    }

    @Test
    public void deleteUserTest() throws Exception {
        final User user = userService.create(prepareUser());

        mvc.perform(delete(BASE_USER_URL + "/" + user.getId())
                .header(authHeaderName, token))
                .andExpect(status().isNoContent());
    }
}