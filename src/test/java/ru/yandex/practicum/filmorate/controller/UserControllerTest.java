package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {
    @Autowired
    TestRestTemplate template;
    private static User firstUser, secondUser;

    @BeforeAll
    static void createNewUsers() {
        firstUser = User.builder()
                .email("firstUser@gmail.com")
                .login("firstUser123")
                .name("First User")
                .birthday(LocalDate.of(1995, 04, 12))
                .build();
        secondUser = User.builder()
                .email("secondUser@yandex.ru")
                .login("secondUser1")
                .name("Second User")
                .birthday(LocalDate.of(1994, 12, 05))
                .build();
    }

    @BeforeEach
    void start() {
        ResponseEntity<User> entityOfCreateFirstUser = template.postForEntity("/users", firstUser, User.class);
        ResponseEntity<User> entityOfCreateSecondUser = template.postForEntity("/users", secondUser, User.class);
    }

    @BeforeEach
    void stop() {
        HttpEntity<User> requestEntity = new HttpEntity<>(null);
        ResponseEntity<Void> entity = template.exchange("/users", HttpMethod.DELETE, requestEntity, Void.class);
    }

    @Test
    void getUsersTest() {
        ResponseEntity<User[]> getEntity = template.getForEntity("/users", User[].class);
        assertEquals(HttpStatus.OK, getEntity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, getEntity.getHeaders().getContentType());

        User[] usersArray = getEntity.getBody();
        assertTrue(usersArray.length >= 2);
        assertEquals("User(id=1, email=firstUser@gmail.com, login=firstUser123, name=First User, birthday=1995-04-12)",
                usersArray[0].toString());
        assertEquals("User(id=2, email=secondUser@yandex.ru, login=secondUser1, name=Second User, birthday=1994-12-05)",
                usersArray[1].toString());
    }

    @Test
    void createUserTest() {
        User thirdUser = User.builder()
                .email("thirdUser@gmail.com")
                .login("thirdUser3")
                .name("Third User")
                .birthday(LocalDate.of(1995, 04, 12))
                .build();
        String thirdUserToString = "User(id=3, email=thirdUser@gmail.com, login=thirdUser3, name=Third User, birthday=1995-04-12)";
        ResponseEntity<User> entityOfCreateThirdUser = template.postForEntity("/users", thirdUser, User.class);
        assertEquals(HttpStatus.OK, entityOfCreateThirdUser.getStatusCode());
        User createdUser = entityOfCreateThirdUser.getBody();
        assertEquals(thirdUserToString, createdUser.toString());
    }

    @Test
    void createUserWithFailLoginTest() {
        User thirdUserFailLogin = User.builder()
                .email("thirdUser@gmail.com")
                .login("dolore ullamco")
                .name("Third User")
                .birthday(LocalDate.of(1995, 04, 12))
                .build();
        ResponseEntity<User> failLoginEntity = template.postForEntity("/users", thirdUserFailLogin, User.class);
        assertEquals(HttpStatus.BAD_REQUEST, failLoginEntity.getStatusCode());
    }

    @Test
    void createUserWithFailEmailTest() {
        User thirdUserFailEmail = User.builder()
                .email("mail.ru")
                .login("thirdUser3")
                .name("Third User")
                .birthday(LocalDate.of(1995, 04, 12))
                .build();
        ResponseEntity<User> failEmailEntity = template.postForEntity("/users", thirdUserFailEmail, User.class);
        assertEquals(HttpStatus.BAD_REQUEST, failEmailEntity.getStatusCode());
    }

    @Test
    void createUserWithFailBirthdayTest() {
        User thirdUserFailBirthday = User.builder()
                .email("friend@common.ru")
                .login("thirdUser3")
                .name("Third User")
                .birthday(LocalDate.of(2446, 8, 20))
                .build();
        ResponseEntity<User> failBirthdayEntity = template.postForEntity("/users", thirdUserFailBirthday, User.class);
        assertEquals(HttpStatus.BAD_REQUEST, failBirthdayEntity.getStatusCode());
    }

    @Test
    void createUserWithEmptyNameTest() {
        User thirdUserWithEmptyName = User.builder()
                .email("friend@common.ru")
                .login("thirdUser3")
                .name("")
                .birthday(LocalDate.of(1995, 04, 12))
                .build();
        ResponseEntity<User> emptyNameEntity = template.postForEntity("/users", thirdUserWithEmptyName, User.class);
        User userWithEmptyName = emptyNameEntity.getBody();
        assertEquals(HttpStatus.OK, emptyNameEntity.getStatusCode());
        assertEquals("User(id=3, email=friend@common.ru, login=thirdUser3, name=thirdUser3, birthday=1995-04-12)",
                userWithEmptyName.toString());
    }

    @Test
    void updateUserTest() {
        User newUser = User.builder()
                .id(2)
                .email("thirdUser@gmail.com")
                .login("dolore ullamco")
                .name("Third User")
                .birthday(LocalDate.of(1995, 04, 12))
                .build();
        String newFilmToString = "Film(id=2, name=Film 2.1, description=Description 2.1, releaseDate=2022-02-01, duration=200)";
    }
}