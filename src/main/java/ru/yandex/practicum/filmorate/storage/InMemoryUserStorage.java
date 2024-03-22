package ru.yandex.practicum.filmorate.storage;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@Data
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();
    private int id;

    private int generateId() {
        this.id++;
        return id;
    }

    public User getUserById(long id) {
        return users.get(id);
    }

    public Collection<User> getAllUsers() {
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
            throw new UserNotFoundException("Данный пользователь не зарегистрирован!");
        }
    }

    public void clearUsers() {
        users.clear();
        this.id = 0;
        log.debug("Данные обо всех пользователях удалены!");
    }
}
