package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {
    private final InMemoryUserStorage userStorage;

    public UserService(InMemoryUserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public Collection<User> getAllUsers() {
        log.debug("Выводим всех зарегестрированных пользователей:");
        return userStorage.getAllUsers();
    }

    public User getUserById(long id) {
        log.debug("Выводим пользователя с id = {id}", id);
        User user = userStorage.getUserById(id);
        if (user != null) {
            return user;
        } else {
            throw new UserNotFoundException("Пользователь не зарегистрирован");
        }

    }

    public User createUser(User user) {
        log.debug("Регистрируем нового пользователя");
        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
        log.debug("Редактируем информацию пользователя");
        return userStorage.updateUser(user);
    }

    public void clearUsers() {
        log.debug("Очищаем список зарегистрированных пользователей");
        userStorage.clearUsers();
    }

    public List<User> getFriendList(long id) {
        log.debug("Получаем список друзей пользователя с id = {id}", id);
        if (id > 0) {
            List<User> friendList = new ArrayList<>();
            User user = userStorage.getUsers().get(id);
            if (user != null) {
                user.getFriends().forEach(friend -> friendList.add(userStorage.getUsers().get(friend)));
                return friendList;
            } else {
                throw new UserNotFoundException("Пользователь не зарегистрирован");
            }
        } else {
            throw new ValidationException("Передан неправильный параметр id");
        }
    }

    public List<User> getCommonFriends(long id, long friendId) {
        log.debug("Получаем список общих друзей пользователя с id = {id} и пользователя с id = {friedId}", id, friendId);
        if (id > 0 && friendId > 0) {
            User user = userStorage.getUsers().get(id);
            User friend = userStorage.getUsers().get(friendId);
            if (user != null && friend != null) {
                List<Long> commonFriendsId = user.getFriends().stream()
                        .filter(item -> friend.getFriends().contains(item))
                        .collect(Collectors.toList());
                List<User> commonFriends = new ArrayList<>();
                commonFriendsId.forEach(item -> commonFriends.add(userStorage.getUsers().get(item)));
                return commonFriends;
            } else {
                throw new UserNotFoundException("Пользователь не зарегистрирован");
            }
        } else {
            throw new ValidationException("Переданы неправильные параметр id и filmdId");
        }
    }

    public User addToFriendList(long id, long friendId) {
        log.debug("Пользователь с id = {id} добавляет в друзья пользователя с id = {friedId}", id, friendId);
        if (id > 0 && friendId > 0) {
            User user = userStorage.getUsers().get(id);
            User friend = userStorage.getUsers().get(friendId);
            if (user != null && friend != null) {
                user.addFriend(friendId);
                friend.addFriend(id);
                return user;
            } else {
                throw new UserNotFoundException("Один из пользователей не зарегистрирован");
            }
        } else {
            throw new ValidationException("Переданы неправильные параметр id и filmdId");
        }

    }

    public User deleteFromFriendList(long id, long friendId) {
        log.debug("Пользователь с id = {id} удаляет из друзей пользователя с id = {friedId}", id, friendId);
        if (id > 0 && friendId > 0) {
            User user = userStorage.getUsers().get(id);
            User friend = userStorage.getUsers().get(friendId);
            if (user != null && friend != null) {
                user.deleteFriend(friendId);
                friend.deleteFriend(id);
                return user;
            } else {
                throw new UserNotFoundException("User or friend not found");
            }
        } else {
            throw new ValidationException("Переданы неправильные параметр id и filmdId");
        }
    }
}
