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
        firstUser = new User();
        firstUser.setEmail("firstUser@gmail.com");
        firstUser.setLogin("firstUser123");
        firstUser.setName("First User");
        firstUser.setBirthday(LocalDate.of(1995, 04, 12));

        secondUser = new User();
        secondUser.setEmail("secondUser@yandex.ru");
        secondUser.setLogin("secondUser1");
        secondUser.setName("Second User");
        secondUser.setBirthday(LocalDate.of(1994, 12, 05));
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
        assertEquals("User(id=1, email=firstUser@gmail.com, login=firstUser123, name=First User, birthday=1995-04-12, friends=[])",
                usersArray[0].toString());
        assertEquals("User(id=2, email=secondUser@yandex.ru, login=secondUser1, name=Second User, birthday=1994-12-05, friends=[])",
                usersArray[1].toString());
    }

    @Test
    void createUserTest() {
        User thirdUser = new User();
        thirdUser.setEmail("thirdUser@gmail.com");
        thirdUser.setLogin("thirdUser3");
        thirdUser.setName("Third User");
        thirdUser.setBirthday(LocalDate.of(1995, 04, 12));

        String thirdUserToString = "User(id=3, email=thirdUser@gmail.com, login=thirdUser3, name=Third User, birthday=1995-04-12, friends=[])";
        ResponseEntity<User> entityOfCreateThirdUser = template.postForEntity("/users", thirdUser, User.class);
        assertEquals(HttpStatus.OK, entityOfCreateThirdUser.getStatusCode());
        User createdUser = entityOfCreateThirdUser.getBody();
        assertEquals(thirdUserToString, createdUser.toString());
    }

    @Test
    void createUserWithFailLoginTest() {
        User thirdUserFailLogin = new User();
        thirdUserFailLogin.setEmail("thirdUser@gmail.com");
        thirdUserFailLogin.setLogin("dolore ullamco");
        thirdUserFailLogin.setName("Third User");
        thirdUserFailLogin.setBirthday(LocalDate.of(1995, 04, 12));

        ResponseEntity<User> failLoginEntity = template.postForEntity("/users", thirdUserFailLogin, User.class);
        assertEquals(HttpStatus.BAD_REQUEST, failLoginEntity.getStatusCode());
    }

    @Test
    void createUserWithFailEmailTest() {
        User thirdUserFailEmail = new User();
        thirdUserFailEmail.setEmail("mail.ru");
        thirdUserFailEmail.setLogin("thirdUser3");
        thirdUserFailEmail.setName("Third User");
        thirdUserFailEmail.setBirthday(LocalDate.of(1995, 04, 12));

        ResponseEntity<User> failEmailEntity = template.postForEntity("/users", thirdUserFailEmail, User.class);
        assertEquals(HttpStatus.BAD_REQUEST, failEmailEntity.getStatusCode());
    }

    @Test
    void createUserWithFailBirthdayTest() {
        User thirdUserFailBirthday = new User();
        thirdUserFailBirthday.setEmail("friend@common.ru");
        thirdUserFailBirthday.setLogin("thirdUser3");
        thirdUserFailBirthday.setName("Third User");
        thirdUserFailBirthday.setBirthday(LocalDate.of(2446, 8, 20));

        ResponseEntity<User> failBirthdayEntity = template.postForEntity("/users", thirdUserFailBirthday, User.class);
        assertEquals(HttpStatus.BAD_REQUEST, failBirthdayEntity.getStatusCode());
    }

    @Test
    void createUserWithEmptyNameTest() {
        User thirdUserWithEmptyName = new User();
        thirdUserWithEmptyName.setEmail("friend@common.ru");
        thirdUserWithEmptyName.setLogin("thirdUser3");
        thirdUserWithEmptyName.setName("");
        thirdUserWithEmptyName.setBirthday(LocalDate.of(1995, 04, 12));

        ResponseEntity<User> emptyNameEntity = template.postForEntity("/users", thirdUserWithEmptyName, User.class);
        User userWithEmptyName = emptyNameEntity.getBody();
        assertEquals(HttpStatus.OK, emptyNameEntity.getStatusCode());
        assertEquals("User(id=3, email=friend@common.ru, login=thirdUser3, name=thirdUser3, birthday=1995-04-12, friends=[])",
                userWithEmptyName.toString());
    }

    @Test
    void updateUserTest() {
        User newUser = new User();
        newUser.setId(2);
        newUser.setEmail("thirdUser@gmail.com");
        newUser.setLogin("dolore ullamco");
        newUser.setName("Third User");
        newUser.setBirthday(LocalDate.of(1995, 04, 12));

        String newFilmToString = "Film(id=2, name=Film 2.1, description=Description 2.1, releaseDate=2022-02-01, duration=200)";
    }
}