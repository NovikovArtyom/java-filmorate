package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private Map<Integer, User> users = new HashMap<>();
    private int id;

    private int generateId() {
        this.id++;
        return id;
    }

    @GetMapping
    public Collection<User> getUsers() {
        return users.values();
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        log.debug("Получен POST запрос на эндпоинт /users");
        user.setId(generateId());
        if ((user.getName() == null) || (user.getName().isBlank())) {
            log.debug("Поле name пустое");
            user.setName(user.getLogin());
        }
        users.put(user.getId(), user);
        log.debug("Пользователь {} зарегистрирован", user.getName());
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        log.debug("Получен PUT запрос на эндпоинт /users");
        if (users.containsKey(user.getId())) {
            if (user.getName().isBlank()) {
                log.debug("Поле name пустое");
                user.setName(user.getLogin());
            }
            users.put(user.getId(), user);
            log.debug("Данные пользователя {} обновлены", user.getName());
            return user;
        } else {
            log.debug("Переданный в запросе пользователь не зарегистрирован");
            throw new ValidationException("Данный пользователь не зарегистрирован!");
        }
    }
}
