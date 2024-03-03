package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FilmControllerTest {
    @Autowired
    TestRestTemplate template;
    private static Film firstFilm, secondFilm;

    @BeforeAll
    static void createFilms() {
        firstFilm = Film.builder()
                .name("Film 1")
                .description("Description 1")
                .releaseDate(LocalDate.of(2022, 1, 1))
                .duration(120)
                .build();
        secondFilm = Film.builder()
                .name("Film 2")
                .description("Description 2")
                .releaseDate(LocalDate.of(2022, 2, 1))
                .duration(90)
                .build();
    }

    @BeforeEach
    void start() {
        ResponseEntity<Film> entityOfCreateFirstFilm = template.postForEntity("/films", firstFilm, Film.class);
        ResponseEntity<Film> entityOfCreateSecondFilm = template.postForEntity("/films", secondFilm, Film.class);
    }

    @AfterEach
    void stop() {
        HttpEntity<Film> requestEntity = new HttpEntity<>(null);
        ResponseEntity<Void> entity = template.exchange("/films", HttpMethod.DELETE, requestEntity, Void.class);
    }

    @Test
    void getFilmsTest() {
        ResponseEntity<Film[]> getEntity = template.getForEntity("/films", Film[].class);
        assertEquals(HttpStatus.OK, getEntity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, getEntity.getHeaders().getContentType());

        Film[] filmsArray = getEntity.getBody();
        assertTrue(filmsArray.length >= 2);
        assertEquals("Film(id=1, name=Film 1, description=Description 1, releaseDate=2022-01-01, duration=120)",
                filmsArray[0].toString());
        assertEquals("Film(id=2, name=Film 2, description=Description 2, releaseDate=2022-02-01, duration=90)",
                filmsArray[1].toString());
    }

    @Test
    void createFilmTest() {
        Film thirdFilm = Film.builder()
                .name("Film 3")
                .description("Description 3")
                .releaseDate(LocalDate.of(2022, 3, 1))
                .duration(100)
                .build();
        String thirdFilmToString = "Film(id=3, name=Film 3, description=Description 3, releaseDate=2022-03-01, duration=100)";
        ResponseEntity<Film> entity = template.postForEntity("/films", thirdFilm, Film.class);
        assertEquals(HttpStatus.OK, entity.getStatusCode());
        Film createdFilm = entity.getBody();
        assertEquals(thirdFilmToString, createdFilm.toString());

        ResponseEntity<Film[]> getEntity = template.getForEntity("/films", Film[].class);
        Film[] arrayFilms = getEntity.getBody();
        assertTrue(arrayFilms.length == 3);
        assertEquals(thirdFilmToString, arrayFilms[2].toString());
    }

    @Test
    void createFilmWithFailNameTest() {
        Film thirdFilmFailName = Film.builder()
                .name("")
                .description("Description 3")
                .releaseDate(LocalDate.of(2022, 3, 1))
                .duration(100)
                .build();
        ResponseEntity<Film> failNameEntity = template.postForEntity("/films", thirdFilmFailName, Film.class);
        assertEquals(HttpStatus.BAD_REQUEST, failNameEntity.getStatusCode());
    }

    @Test
    void createFilmWithFailDescription() {
        Film thirdFilmFailDescription = Film.builder()
                .name("Film 3")
                .description("Пятеро друзей ( комик-группа «Шарло»), приезжают в город Бризуль. " +
                        "Здесь они хотят разыскать господина Огюста Куглова, который задолжал им деньги, " +
                        "а именно 20 миллионов. о Куглов, который за время «своего отсутствия», " +
                        "стал кандидатом Коломбани.")
                .releaseDate(LocalDate.of(2022, 3, 1))
                .duration(100)
                .build();
        ResponseEntity<Film> failDescriptionEntity = template.postForEntity("/films", thirdFilmFailDescription, Film.class);
        assertEquals(HttpStatus.BAD_REQUEST, failDescriptionEntity.getStatusCode());
    }

    @Test
    void createFilmWithFailReleaseDate() {
        Film thirdFilmFailReleaseDate = Film.builder()
                .name("Film 3")
                .description("Description 3")
                .releaseDate(LocalDate.of(1890, 3, 25))
                .duration(100)
                .build();
        ResponseEntity<Film> failReleaseDateEntity = template.postForEntity("/films", thirdFilmFailReleaseDate, Film.class);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, failReleaseDateEntity.getStatusCode());
    }

    @Test
    void createFilmWithFailDuration() {
        Film thirdFilmFailDuration = Film.builder()
                .name("Film 3")
                .description("Description 3")
                .releaseDate(LocalDate.of(2022, 3, 1))
                .duration(-200)
                .build();
        ResponseEntity<Film> failDuration = template.postForEntity("/films", thirdFilmFailDuration, Film.class);
        assertEquals(HttpStatus.BAD_REQUEST, failDuration.getStatusCode());
    }

    @Test
    void updateFilmTest() {
        Film newFilm = Film.builder()
                .id(2)
                .name("Film 2.1")
                .description("Description 2.1")
                .releaseDate(LocalDate.of(2022, 2, 1))
                .duration(200)
                .build();
        String newFilmToString = "Film(id=2, name=Film 2.1, description=Description 2.1, releaseDate=2022-02-01, duration=200)";
        HttpEntity<Film> requestEntity = new HttpEntity<>(newFilm);
        ResponseEntity<Film> entity = template.exchange("/films", HttpMethod.PUT, requestEntity, Film.class);
        assertEquals(HttpStatus.OK, entity.getStatusCode());
        Film createdFilm = entity.getBody();
        assertEquals(newFilmToString, createdFilm.toString());

        ResponseEntity<Film[]> newEntity = template.getForEntity("/films", Film[].class);
        Film[] arrayFilms = newEntity.getBody();
        assertTrue(arrayFilms.length == 2);
        assertEquals(newFilmToString, arrayFilms[1].toString());
    }
}