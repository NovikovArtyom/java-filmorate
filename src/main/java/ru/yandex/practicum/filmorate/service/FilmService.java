package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {
    @Autowired
    private final InMemoryFilmStorage filmStorage;

    public FilmService(InMemoryFilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Collection<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Film createFilm(Film film) {
        return filmStorage.createFilm(film);
    }

    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    public void clearFilms() {
        filmStorage.clearFilms();
    }

    public Film getFilmById(long id) {
        Film film = filmStorage.getFilmById(id);
        if (film != null) {
            return film;
        } else {
            throw new FilmNotFoundException("Данный фильм не зарегистрирован");
        }
    }

    public Film addLike(long filmdId, long id) {
        Film likedFilm = filmStorage.getFilms().get(filmdId);
        if (likedFilm != null) {
            likedFilm.addLike(id);
            return likedFilm;
        } else {
            throw new FilmNotFoundException("Данный фильм не зарегистрирован");
        }
    }

    public Film deleteLike(long filmId, long id) {
        Film likedFilm = filmStorage.getFilms().get(filmId);
        if (likedFilm != null) {
            likedFilm.deleteLike(id);
            return likedFilm;
        } else {
            throw new FilmNotFoundException("Данный фильм не зарегистрирован");
        }
    }

    public Collection<Film> getPopularFilms(int count) {
        return filmStorage.getFilms().values().stream()
                .sorted((f0, f1) -> (f1.getLikes().size() - f0.getLikes().size()))
                .limit(count)
                .collect(Collectors.toList());
    }
}
