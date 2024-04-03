# java-filmorate
Template repository for Filmorate project.

Варианты запросов

Получение всех пользователей:
SELECT *
FROM users;

Получение пользователя по id:
SELECT *
FROM users
WHERE user_id = 1;

Получение всех друзей пользователя:
SELECT *
FROM users
WHERE user_id IN (SELECT friend_id 
FROM friends
WHERE user_id = 1);

Получение общих друзей:
SELECT *
FROM users
WHERE user_id IN (SELECT friends.friend_id 
FROM friends as friends
INNER JOIN friends as other_friends ON friends.friend_id = other_friends.friend_id
WHERE friends.user_id = 1 and other_friends.user_id = 2);          

Получение всех фильмов:
SELECT *
FROM films;

Получение фильма по id:
SELECT *
FROM films
WHERE film_id = 1;

10 популярных фильмов:
SELECT * 
FROM films 
WHERE film_id in (SELECT TOP 10 films.film_id
FROM films
LEFT FOIN film_likes ON films.film_id = film_likes.film_id
GROUP BY films.film_id
ORDER BY COUNT(film_likes.user_id) DESC);

![Filmorate DB structure](/src/main/resources/image/filmorate.png)