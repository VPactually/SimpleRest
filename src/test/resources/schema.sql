drop table if exists task_labels;
drop table if exists user_tasks;
drop table if exists labels;
drop table if exists tasks;
drop table if exists users;
drop table if exists task_statuses;



CREATE TABLE users
(
    id         SERIAL PRIMARY KEY,
    name       VARCHAR(255) NOT NULL,
    email      VARCHAR(255) NOT NULL,
    password   VARCHAR(255) NOT NULL,
    created_at TIMESTAMP
);

CREATE TABLE task_statuses
(
    id         SERIAL PRIMARY KEY,
    name       VARCHAR(255) NOT NULL UNIQUE,
    slug       VARCHAR(255) NOT NULL UNIQUE,
    created_at TIMESTAMP
);

CREATE TABLE tasks
(
    id          SERIAL PRIMARY KEY,
    title       VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    status_id   INTEGER      NOT NULL REFERENCES task_statuses (id),
    user_id     INTEGER      NOT NULL REFERENCES users (id),
    created_at  TIMESTAMP
);

CREATE TABLE labels
(
    id         SERIAL PRIMARY KEY,
    name       VARCHAR(255) NOT NULL,
    created_at TIMESTAMP
);

CREATE TABLE task_labels
(
    task_id  INTEGER NOT NULL REFERENCES tasks (id),
    label_id INTEGER NOT NULL REFERENCES labels (id)
);

CREATE TABLE user_tasks
(
    user_id    INTEGER NOT NULL REFERENCES users (id),
    task_id    INTEGER NOT NULL REFERENCES tasks (id)
);

INSERT INTO users (name, email, password, created_at)
VALUES ('admin', 'admin@gmail.com', 'admin', now());

INSERT INTO task_statuses (name, slug, created_at)
VALUES ('Draft', 'draft', now()),
       ('ToReview', 'to_review', now()),
       ('ToPublish', 'to_publish', now()),
       ('ToBeFixed', 'to_be_fixed', now()),
       ('Published', 'published', now());

INSERT INTO labels (name, created_at)
VALUES ('Bug', now()),
       ('Feature', now());

INSERT INTO tasks (title, description, status_id, user_id, created_at)
VALUES ('Task 1', 'Description 1', 5, 1, now());

INSERT INTO task_labels (task_id, label_id)
VALUES (1, 1), (1, 2);

INSERT INTO user_tasks (user_id, task_id)
VALUES (1, 1);