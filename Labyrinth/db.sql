DROP DATABASE labyrinthhighscores;
DROP TABLE labyrinthhighscores.labyrinthhighscores;

create DATABASE labyrinthhighscores;
create table labyrinthhighscores.labyrinthhighscores (
timestamp timestamp,
name varchar(255),
level int,
time double
);

DELETE FROM labyrinthhighscores.labyrinthhighscores;
