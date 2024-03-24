package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
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

    @GetMapping("/popular")
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

    @PutMapping("/{filmdId}/like/{id}")
    public Film addLike(@PathVariable long filmdId, @PathVariable long id) {
        log.debug("Получен PUT запрос на эндпоинт /films/{id}/like/{userId}");
        return filmService.addLike(filmdId, id);
    }

    @DeleteMapping
    public void clearFilms() {
        log.debug("Получен DELETE запрос на эндпоинт /films");
        filmService.clearFilms();
    }

    @DeleteMapping("/{filmdId}/like/{id}")
    public Film deleteLike(@PathVariable long filmdId, @PathVariable long id) {
        log.debug("Получен DELETE запрос на эндпоинт /films");
        return filmService.deleteLike(filmdId, id);
    }
}
