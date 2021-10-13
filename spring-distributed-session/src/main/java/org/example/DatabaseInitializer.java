package org.example;

import org.example.domain.dto.CreateUserRequest;
import org.example.domain.model.Role;
import org.example.service.UserService;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class DatabaseInitializer implements ApplicationListener<ApplicationReadyEvent> {

    private final List<String> usernames = Stream.of(
            "ada.lovelace@nix.io",
            "alan.turing@nix.io",
            "dennis.ritchie@nix.io"
    ).collect(Collectors.toList());

    private final List<String> fullNames = Stream.of(
            "Ada Lovelace",
            "Alan Turing",
            "Dennis Ritchie"
    ).collect(Collectors.toList());

    private final List<String> roles = Stream.of(
            Role.USER_ADMIN,
            Role.AUTHOR_ADMIN,
            Role.BOOK_ADMIN
    ).collect(Collectors.toList());

    private final String password = "5495196134";

    private final UserService userService;

    public DatabaseInitializer(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        for (int i = 0; i < usernames.size(); ++i) {
            CreateUserRequest request = new CreateUserRequest();
            request.setUsername(usernames.get(i));
            request.setFullName(fullNames.get(i));
            request.setPassword(password);
            request.setRePassword(password);
            request.setAuthorities(Stream.of(roles.get(i)).collect(Collectors.toSet()));
            userService.upsert(request);
        }
    }
}
