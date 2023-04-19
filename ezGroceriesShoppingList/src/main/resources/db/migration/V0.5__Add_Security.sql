create table users(
                      username varchar(50) not null primary key,
                      password varchar(500) not null,
                      enabled boolean not null
);

create table authorities (
                             username varchar(50) not null,
                             authority varchar(50) not null,
                             constraint fk_authorities_users foreign key(username) references users(username)
);
create unique index ix_auth_username on authorities (username,authority);

INSERT INTO users (username, password, enabled) VALUES ('jani', '{bcrypt}$2a$10$qtcjzXv1rGgTx2L3RsPcZu/gpKfXUI3lMtY8GkgpGUqW6S3uH1mAm', true);

INSERT INTO authorities (username, authority) VALUES ('jani', 'ROLE_USER');

INSERT INTO users (username, password, enabled) VALUES ('bert', '{bcrypt}$2a$10$qtcjzXv1rGgTx2L3RsPcZu/gpKfXUI3lMtY8GkgpGUqW6S3uH1mAm', true);

INSERT INTO authorities (username, authority) VALUES ('bert', 'ROLE_USER');

ALTER TABLE SHOPPING_LIST ADD COLUMN username VARCHAR(255);