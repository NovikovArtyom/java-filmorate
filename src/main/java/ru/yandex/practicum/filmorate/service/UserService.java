package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {
    @Autowired
    private final InMemoryUserStorage userStorage;

    public UserService(InMemoryUserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public Collection<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public User getUserById(long id) {
        User user = userStorage.getUserById(id);
        if (user != null) {
            return user;
        } else {
            throw new UserNotFoundException("Пользователь не зарегистрирован");
        }

    }

    public User createUser(User user) {
        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public void clearUsers() {
        userStorage.clearUsers();
    }

    public List<User> getFriendList(long id) {
        List<User> friendList = new ArrayList<>();
        User user = userStorage.getUsers().get(id);
        if (user != null) {
            user.getFriends().forEach(friend -> friendList.add(userStorage.getUsers().get(friend)));
            return friendList;
        } else {
            throw new UserNotFoundException("Пользователь не зарегистрирован");
        }
    }

    public List<User> getCommonFriends(long id, long friedId) {
        User user = userStorage.getUsers().get(id);
        User friend = userStorage.getUsers().get(friedId);
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
    }

    public User addToFriendList(long id, long friendId) {
        User user = userStorage.getUsers().get(id);
        User friend = userStorage.getUsers().get(friendId);
        if (user != null && friend != null) {
            user.addFriend(friendId);
            friend.addFriend(id);
            return user;
        } else {
            throw new UserNotFoundException("User or friend not found");
        }
    }

    public User deleteFromFriendList(long id, long friendId) {
        User user = userStorage.getUsers().get(id);
        User friend = userStorage.getUsers().get(friendId);
        if (user != null && friend != null) {
            user.deleteFriend(friendId);
            friend.deleteFriend(id);
            return user;
        } else {
            throw new UserNotFoundException("User or friend not found");
        }
    }
}
