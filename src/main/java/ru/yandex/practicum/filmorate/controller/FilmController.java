package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import javax.validation.Valid;
import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    @Autowired
    private final FilmStorage filmStorage;

    public FilmController(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    @GetMapping
    public Collection<Film> getFilms() {
        return filmStorage.getFilms();
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        log.debug("Получен POST запрос на эндпоинт /films");
        return filmStorage.createFilm(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.debug("Получен PUT запрос на эндпоинт /films");
        return filmStorage.updateFilm(film);
    }

    @DeleteMapping
    public void clearFilms() {
        log.debug("Получен DELETE запрос на эндпоинт /films");
        filmStorage.clearFilms();
    }
}
