package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {
    Collection<Film> getFilms();
    Film createFilm(Film film);
    public Film updateFilm(Film film);
    public void clearFilms();
}
