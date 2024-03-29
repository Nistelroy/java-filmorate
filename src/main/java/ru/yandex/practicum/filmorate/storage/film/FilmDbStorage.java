package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.InvalidDataException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component("filmDbStorage")
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Film> addFilm(Film film) {
        Optional<Mpa> mpa = getMpaById(film.getMpa().getId());
        if (mpa.isEmpty()) {
            throw new InvalidDataException("Недействительный MPA указан для фильма.");
        }

        Map<String, Object> filmMap = film.toMap();

        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films")
                .usingGeneratedKeyColumns("film_id");

        int filmId = simpleJdbcInsert.executeAndReturnKey(filmMap).intValue();

        Set<Genre> genres = film.getGenres();
        if (genres != null) {
            for (Genre genre : film.getGenres()) {
                Optional<Genre> existingGenre = getGenreById(genre.getId());
                if (existingGenre.isEmpty()) {
                    throw new InvalidDataException("Недействительный жанр указан для фильма: " + genre.getId());
                }
            }
            genres = new HashSet<>(genres.stream().sorted(Genre::compareTo).collect(
                    Collectors.toCollection(LinkedHashSet::new)));
            genres.forEach(genre -> addGenreToFilm(filmId, genre.getId()));
        } else {
            genres = new HashSet<>();
        }

        Film newFilm = Film.builder()
                .id(filmId)
                .name(film.getName())
                .description(film.getDescription())
                .duration(film.getDuration())
                .releaseDate(film.getReleaseDate())
                .mpa(film.getMpa())
                .genres(genres)
                .likes(film.getLikes())
                .build();

        return Optional.of(newFilm);
    }

    @Override
    public Optional<Film> updateFilm(Film film) {
        String sqlQuery =
                "update films set " +
                        "name = ?, description = ?, duration = ?, release_date = ?, mpa_id = ? " +
                        "where film_id = ?";

        int rowCount = jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getDuration(),
                film.getReleaseDate(),
                film.getMpaId(),
                film.getId());

        if (rowCount == 0) {
            return Optional.empty();
        }

        jdbcTemplate.update("delete from film_genres where film_id = ?", film.getId());

        Set<Genre> genres = film.getGenres();
        if (genres != null) {
            genres.forEach(genre -> addGenreToFilm(film.getId(), genre.getId()));
        }

        Film newFilm = Film.builder()
                .id(film.getId())
                .name(film.getName())
                .description(film.getDescription())
                .duration(film.getDuration())
                .releaseDate(film.getReleaseDate())
                .mpa(film.getMpa())
                .genres(genres)
                .likes(film.getLikes())
                .build();

        return Optional.of(newFilm);
    }

    @Override
    public Optional<Film> getFilm(int id) {
        return getFilmById(id);
    }

    @Override
    public Collection<Film> getAllFilms() {
        String sqlQuery =
                "select " +
                        "   films.film_id, " +
                        "   films.name, " +
                        "   films.description, " +
                        "   films.release_date, " +
                        "   films.duration, " +
                        "   films.mpa_id, " +
                        "   mpa.name as mpa_name, " +
                        "   mpa.description as mpa_description " +
                        "from films " +
                        "   left join mpa " +
                        "   on films.mpa_id = mpa.mpa_id";
        return jdbcTemplate.query(sqlQuery, this::mapRowToFilm);
    }

    @Override
    public boolean filmExist(int id) {
        String sqlQuery = "select 1 from films where film_id = ? limit 1";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sqlQuery, id);
        return result.next();
    }

    @Override
    public boolean filmNotExist(int id) {
        return !filmExist(id);
    }

    @Override
    public void addLike(int id, int userId) {
        String sqlQuery = "merge into film_likes(film_id, user_id) key(film_id, user_id) values(?, ?)";
        jdbcTemplate.update(sqlQuery, id, userId);
    }

    @Override
    public void removeLike(int id, int userId) {
        String sqlQuery = "delete from film_likes where film_id = ? and user_id = ?";
        jdbcTemplate.update(sqlQuery, id, userId);
    }

    @Override
    public Collection<Film> getPopularFilms(int count) {
        String sqlQuery =
                "select " +
                        "   films.film_id, " +
                        "   films.name, " +
                        "   films.description, " +
                        "   films.release_date, " +
                        "   films.duration, " +
                        "   films.mpa_id, " +
                        "   mpa.name as mpa_name, " +
                        "   mpa.description as mpa_description " +
                        "from films " +
                        "   left join mpa " +
                        "   on films.mpa_id = mpa.mpa_id " +
                        "where film_id in (" +
                        "   select top ? " +
                        "       films.film_id " +
                        "   from films " +
                        "       left join film_likes " +
                        "       on films.film_id = film_likes.film_id " +
                        "   group by films.film_id " +
                        "   order by count(film_likes.user_id) desc" +
                        ")";

        return jdbcTemplate.query(sqlQuery, this::mapRowToFilm, count);
    }

    @Override
    public Collection<Mpa> getAllMpa() {
        String sqlQuery = "select * from mpa";
        return jdbcTemplate.query(sqlQuery, this::mapRowToMpa);
    }

    @Override
    public Optional<Mpa> getMpaById(int id) {
        String sqlQuery = "select * from mpa where mpa_id = ?";
        Collection<Mpa> mpa = jdbcTemplate.query(sqlQuery, this::mapRowToMpa, id);
        return mpa.stream().findFirst();
    }

    @Override
    public Collection<Genre> getAllGenres() {
        String sqlQuery = "select * from genres";
        return jdbcTemplate.query(sqlQuery, this::mapRowToGenre);
    }

    @Override
    public Optional<Genre> getGenreById(int id) {
        String sqlQuery = "select * from genres where genre_id = ?";
        Collection<Genre> genres = jdbcTemplate.query(sqlQuery, this::mapRowToGenre, id);
        return genres.stream().findFirst();
    }

    private void addGenreToFilm(int filmId, int genreId) {
        if (!isGenreExists(genreId)) {
            throw new RuntimeException("Genre с id " + genreId + " не найден");
        }
        String sqlQuery = "merge into film_genres(film_id, genre_id) key(film_id, genre_id) values(?, ?)";
        jdbcTemplate.update(sqlQuery, filmId, genreId);
    }

    private Set<Genre> getFilmGenres(int id) {
        String sqlQuery =
                "select " +
                        "   film_genres.genre_id, " +
                        "   genres.name " +
                        "from film_genres " +
                        "   left join genres " +
                        "   on film_genres.genre_id = genres.genre_id " +
                        "where film_genres.film_id = ? " +
                        "order by film_genres.genre_id";

        return new HashSet<>(jdbcTemplate.query(sqlQuery, this::mapRowToGenre, id));
    }

    private Set<Integer> getFilmLikes(int id) {
        String sqlQuery =
                "select " +
                        "   film_likes.user_id " +
                        "from film_likes " +
                        "where film_likes.film_id = ?";

        return new HashSet<>(jdbcTemplate.queryForList(sqlQuery, Integer.class, id));
    }

    private Optional<Film> getFilmById(int id) {
        String sqlQuery =
                "select " +
                        "   films.film_id, " +
                        "   films.name, " +
                        "   films.description, " +
                        "   films.release_date, " +
                        "   films.duration, " +
                        "   films.mpa_id, " +
                        "   mpa.name as mpa_name, " +
                        "   mpa.description as mpa_description " +
                        "from films " +
                        "   left join mpa " +
                        "   on films.mpa_id = mpa.mpa_id " +
                        "where film_id = ?";

        Collection<Film> films = jdbcTemplate.query(sqlQuery, this::mapRowToFilm, id);
        return films.stream().findFirst();
    }

    private Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        return Film.builder()
                .id(resultSet.getInt("film_id"))
                .name(resultSet.getString("name"))
                .description(resultSet.getString("description"))
                .duration(resultSet.getInt("duration"))
                .releaseDate(resultSet.getDate("release_date").toLocalDate())
                .mpa(new Mpa(resultSet.getInt("mpa_id"),
                        resultSet.getString("mpa_name"),
                        resultSet.getString("mpa_description")))
                .genres(getFilmGenres(resultSet.getInt("film_id")))
                .likes(getFilmLikes(resultSet.getInt("film_id")))
                .build();
    }

    private Genre mapRowToGenre(ResultSet resultSet, int rowNum) throws SQLException {
        return new Genre(resultSet.getInt("genre_id"), resultSet.getString("name"));
    }

    private Mpa mapRowToMpa(ResultSet resultSet, int rowNum) throws SQLException {
        return new Mpa(resultSet.getInt("mpa_id"), resultSet.getString("name"), resultSet.getString("description"));
    }

    private boolean isGenreExists(int genreId) {
        Integer i = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM genres WHERE genre_id = ?", Integer.class, genreId);
        if (i != null) {
            return i > 0;
        }
        return false;
    }
}