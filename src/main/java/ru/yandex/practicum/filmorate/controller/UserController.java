package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private final UserService userService;

    public UserController(UserService userService, InMemoryUserStorage userStorage) {
        this.userService = userService;
    }

    @GetMapping
    public Collection<User> getUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}/friends")
    public Collection<User> getFriendList(@PathVariable long id) {
        log.debug("Получен GET запрос на эндпоинт /users/{id}/friends");
        if (id > 0) {
            return userService.getFriendList(id);
        } else {
            throw new ValidationException("Передан неправильный параметр id");
        }
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> getCommonFriends(@PathVariable long id, @PathVariable long otherId) {
        log.debug("Получен GET запрос на эндпоинт /users/{id}/friends/common/{otherId}");
        if (id > 0 && otherId >0) {
            return userService.getCommonFriends(id, otherId);
        } else {
            throw new ValidationException("Переданы неправильные параметр id и filmdId");
        }
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
        if (id > 0 && friendId > 0) {
            return userService.addToFriendList(id, friendId);
        } else {
            throw new ValidationException("Переданы неправильные параметр id и filmdId");
        }
    }

    @DeleteMapping
    public void clearFilms() {
        log.debug("Получен DELETE запрос на эндпоинт /users");
        userService.clearUsers();
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public User deleteFromFriendList(@PathVariable long id, @PathVariable long friendId) {
        log.debug("Получен DELETE запрос на эндпоинт /users/{id}/friends/{friendId}");
        if (id > 0 && friendId > 0) {
            return userService.deleteFromFriendList(id, friendId);
        } else {
            throw new ValidationException("Переданы неправильные параметр id и filmdId");
        }
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handle(final ValidationException e) {
        return new ErrorResponse(
                "error: ", "Ошибка валидации"
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handle(final UserNotFoundException e) {
        return new ErrorResponse(
                "error: ", "Пользователь не зарегистрирован"
        );
    }
}
