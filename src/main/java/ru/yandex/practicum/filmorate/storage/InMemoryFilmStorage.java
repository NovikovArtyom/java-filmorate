package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.ValidationException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();
    private int id;
    private static final LocalDate CINEMA_DAY = LocalDate.of(1895, 12, 28);

    private int generateId() {
        this.id++;
        return id;
    }

    public Collection<Film> getFilms() {
        return films.values();
    }

    public Film createFilm(Film film) {
        if (film.getReleaseDate().isAfter(CINEMA_DAY)) {
            film.setId(generateId());
            films.put(film.getId(), film);
            log.debug("Фильм {} зарегистрирован", film.getName());
            return film;
        } else
            throw new ValidationException("Указанная дата релиза раньше 28 декабря 1895 года!");
    }

    public Film updateFilm(Film film) {
        if (films.containsKey(film.getId()) && film.getReleaseDate().isAfter(CINEMA_DAY)) {
            films.put(film.getId(), film);
            log.debug("Данные фильма {} обновлены", film.getName());
            return film;
        } else
            throw new ValidationException("Фильм не зарегистрирован или указанная дата релиза раньше 28 декабря 1895 года!");
    }

    public void clearFilms() {
        films.clear();
        this.id = 0;
        log.debug("Данные обо всех фильмах удалены!");
    }
}
