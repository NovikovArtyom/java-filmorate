package ru.yandex.practicum.filmorate.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class UserService {
    private final Map<Integer, User> users = new HashMap<>();
    private int id;

    private int generateId() {
        this.id++;
        return id;
    }

    public Collection<User> getUsers() {
        return users.values();
    }

    public User createUser(User user) {
        user.setId(generateId());
        if ((user.getName() == null) || (user.getName().isBlank())) {
            log.debug("Поле name пустое");
            user.setName(user.getLogin());
        }
        users.put(user.getId(), user);
        log.debug("Пользователь {} зарегистрирован", user.getName());
        return user;
    }

    public User updateUser(User user) {
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

    public void clearFilms() {
        users.clear();
        this.id = 0;
        log.debug("Данные обо всех пользователях удалены!");
    }
}
