package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import javax.validation.Valid;
import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService, InMemoryUserStorage userStorage) {
        this.userService = userService;
    }

    @GetMapping
    public Collection<User> getAllUsers() {
        log.debug("Получен GET запрос на эндпоинт /users");
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable long id) {
        log.debug("Получен GET запрос на эндпоинт /users/{id}");
        return userService.getUserById(id);
    }

    @GetMapping("/{id}/friends")
    public Collection<User> getFriendList(@PathVariable long id) {
        log.debug("Получен GET запрос на эндпоинт /users/{id}/friends");
        return userService.getFriendList(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> getCommonFriends(@PathVariable long id, @PathVariable long otherId) {
        log.debug("Получен GET запрос на эндпоинт /users/{id}/friends/common/{otherId}");
        return userService.getCommonFriends(id, otherId);
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        log.debug("Получен POST запрос на эндпоинт /users");
        return userService.createUser(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        log.debug("Получен PUT запрос на эндпоинт /users");
        return userService.updateUser(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public User addToFriendList(@PathVariable long id, @PathVariable long friendId) {
        log.debug("Получен PUT запрос на эндпоинт /users/{id}/friends/{friendId}");
        return userService.addToFriendList(id, friendId);
    }

    @DeleteMapping
    public void clearFilms() {
        log.debug("Получен DELETE запрос на эндпоинт /users");
        userService.clearUsers();
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public User deleteFromFriendList(@PathVariable long id, @PathVariable long friendId) {
        log.debug("Получен DELETE запрос на эндпоинт /users/{id}/friends/{friendId}");
        return userService.deleteFromFriendList(id, friendId);
    }
}
