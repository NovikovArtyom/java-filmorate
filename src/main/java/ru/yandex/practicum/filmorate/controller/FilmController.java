package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private Map<Integer, Film> films = new HashMap<>();
    private int id;
    private static final LocalDate CINEMA_DAY = LocalDate.of(1895, 12, 28);

    private int generateId() {
        this.id++;
        return id;
    }

    @GetMapping
    public Collection<Film> getFilms() {
        return films.values();
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        log.debug("Получен POST запрос на эндпоинт /films");
        if (film.getReleaseDate().isAfter(CINEMA_DAY)) {
            film.setId(generateId());
            films.put(film.getId(), film);
            log.debug("Фильм {} зарегистрирован", film.getName());
            return film;
        } else
            throw new ValidationException("Указанная дата релиза раньше 28 декабря 1895 года!");
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.debug("Получен PUT запрос на эндпоинт /films");
        if (films.containsKey(film.getId()) && film.getReleaseDate().isAfter(CINEMA_DAY)) {
            films.put(film.getId(), film);
            log.debug("Данные фильма {} обновлены", film.getName());
            return film;
        } else
            throw new ValidationException("Фильм не зарегистрирован или указанная дата релиза раньше 28 декабря 1895 года!");
    }
}
