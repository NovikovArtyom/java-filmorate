package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {
    private final InMemoryFilmStorage filmStorage;

    public FilmService(InMemoryFilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Collection<Film> getAllFilms() {
        log.debug("Получаем список всех зарегистрированных фильмов");
        return filmStorage.getAllFilms();
    }

    public Film createFilm(Film film) {
        log.debug("Регистрируем новый фильм");
        return filmStorage.createFilm(film);
    }

    public Film updateFilm(Film film) {
        log.debug("Редактируем информацию фильма");
        return filmStorage.updateFilm(film);
    }

    public void clearFilms() {
        log.debug("Очищаем список зарегистрированных фильмов");
        filmStorage.clearFilms();
    }

    public Film getFilmById(long id) {
        log.debug("Получаем зарегистрированный фильм с id = {id}", id);
        if (id > 0) {
            Film film = filmStorage.getFilmById(id);
            if (film != null) {
                return film;
            } else {
                throw new FilmNotFoundException("Данный фильм не зарегистрирован");
            }
        } else {
            throw new ValidationException("Передан неправильный параметр id");
        }
    }

    public Film addLike(long filmId, long id) {
        log.debug("Пользователь с id = {id} ставит лайк фильму с id = {filmId}", id, filmId);
        if (filmId > 0 && id > 0) {
            Film likedFilm = filmStorage.getFilms().get(filmId);
            if (likedFilm != null) {
                likedFilm.addLike(id);
                return likedFilm;
            } else {
                throw new FilmNotFoundException("Данный фильм не зарегистрирован");
            }
        } else {
            throw new ValidationException("Переданы неправильные параметр id и filmdId");
        }
    }

    public Film deleteLike(long filmId, long id) {
        log.debug("Пользователь с id = {id} ставит лайк фильму с id = {filmId}", id, filmId);
        if (filmId > 0 && id > 0) {
            Film likedFilm = filmStorage.getFilms().get(filmId);
            if (likedFilm != null) {
                likedFilm.deleteLike(id);
                return likedFilm;
            } else {
                throw new FilmNotFoundException("Данный фильм не зарегистрирован");
            }
        } else {
            throw new ValidationException("Переданы неправильные параметр id и filmdId");
        }
    }

    public Collection<Film> getPopularFilms(int count) {
        log.debug("Получаем список самых залайканных фильмов");
        return filmStorage.getFilms().values().stream()
                .sorted((f0, f1) -> (f1.getLikes().size() - f0.getLikes().size()))
                .limit(count)
                .collect(Collectors.toList());
    }
}
