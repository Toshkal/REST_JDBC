DROP TABLE IF EXISTS autor, book;

CREATE TABLE IF NOT EXISTS autor
(
    id        INT not null PRIMARY KEY,
    fname     VARCHAR(50) NOT NULL,
    lactname  VARCHAR(50) NOT NULL,
    book_id    INT REFERENCES book (id) ON DELETE CASCADE NOT NULL
);


CREATE TABLE IF NOT EXISTS book
(
    id          INT GENERATED by default as identity PRIMARY KEY,
    binding     VARCHAR(50) not null,
    title       VARCHAR(50) not null,
    numberPages int not null,
    autor_id    INT REFERENCES autor (id) ON DELETE CASCADE NOT NULL
);

INSERT INTO autor (fname, lastname)
VALUES ('Эдгар','Берроуз'),
VALUES ('Владимир','Колычев'),
VALUES ('Жюль','Верн'),
VALUES ('Эрнэст','Хэмингуэй');

INSERT INTO book (binding, title, numberPages, autor_id)
VALUES ('Мягкий','Тарзан','235','1'),
VALUES ('Мягкий','Пуля для солиста','284','2'),
VALUES ('Мягкий','Резонанс','207','2'),
VALUES ('Твердый','Марс','327','1'),
VALUES ('Твердый','20000 лье под водой','319','3'),
VALUES ('Твердый','Над пропастью во ржи','289','4'),
VALUES ('Твердый','Моби Дик','187','4'),
VALUES ('Мягкий','Долг зовет','199','2');