package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {
    @Autowired
    InMemoryUserStorage userStorage;

    public UserService(InMemoryUserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public Collection<User> getAllUsers() {
        return userStorage.getAllUsers();
    }
    public User getUserById(long id) {
        return userStorage.getUserById(id);
    }
    public User createUser(User user) {
        return userStorage.createUser(user);
    }
    public User updateUser(User user){
        return userStorage.updateUser(user);
    }
    public void clearUsers() {
        userStorage.clearUsers();
    }
    public List<User> getFriendList(long id) {
        List<User> friendList = new ArrayList<>();
        userStorage.getUsers().get(id).getFriends().forEach(friend -> friendList.add(userStorage.getUsers().get(friend)));
        return friendList;
    }

    public List<User> getCommonFriends(long id, long friedId) {
        List<Long> commonFriendsId = userStorage.getUsers().get(id).getFriends().stream()
                .filter(friend -> userStorage.getUsers().get(friedId).getFriends().contains(friend))
                .collect(Collectors.toList());
        List<User> commonFriends = new ArrayList<>();
        commonFriendsId.forEach(friend -> commonFriends.add(userStorage.getUsers().get(friend)));
        return commonFriends;
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
