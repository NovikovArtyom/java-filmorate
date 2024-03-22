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
    private final FilmService filmService;

    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public Collection<Film> getAllFilms() {
        log.debug("Получен GET запрос на эндпоинт /films");
        return filmService.getAllFilms();
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable long id) {
        log.debug("Получен GET запрос на эндпоинт /films/{id}");
        return filmService.getFilmById(id);
    }

    @GetMapping("/popular?count={count}")
    public Collection<Film> getPopularFilms(@RequestParam(defaultValue = "10", required = false) int count) {
        log.debug("Получен GET запрос на эндпоинт /films/popular?count={count}");
        return filmService.getPopularFilms(count);
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        log.debug("Получен POST запрос на эндпоинт /films");
        return filmService.createFilm(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.debug("Получен PUT запрос на эндпоинт /films");
        return filmService.updateFilm(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public Film addLike(@PathVariable long id, @PathVariable long filmId) {
        log.debug("Получен PUT запрос на эндпоинт /films/{id}/like/{userId}");
        return filmService.addLike(id, filmId);
    }

    @DeleteMapping
    public void clearFilms() {
        log.debug("Получен DELETE запрос на эндпоинт /films");
        filmService.clearFilms();
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Film deleteLike(@PathVariable long id, @PathVariable long userId) {
        log.debug("Получен DELETE запрос на эндпоинт /films");
        return filmService.deleteLike(id, userId);
    }
}
