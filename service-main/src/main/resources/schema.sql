DROP TABLE IF EXISTS requests CASCADE;
DROP TABLE IF EXISTS events CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS location CASCADE;
DROP TABLE IF EXISTS category CASCADE;
DROP TABLE IF EXISTS compilations CASCADE;

CREATE TABLE IF NOT EXISTS users
(
    id    BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name  VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS category
(
    id   BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS location
(
    id  BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    lat FLOAT NOT NULL,
    lon FLOAT NOT NULL
);

CREATE TABLE IF NOT EXISTS compilations
(
    id     BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    pinned BOOLEAN DEFAULT false,
    title  varchar(70) NOT NULL
);

CREATE TABLE IF NOT EXISTS events
(
    id                 BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    compilation_id     BIGINT,
    annotation         VARCHAR(2000),
    category_id        BIGINT NOT NULL,
    confirmed_requests BIGINT,
    created_on         TIMESTAMP WITHOUT TIME ZONE,
    description        VARCHAR(7000),
    event_date         TIMESTAMP WITHOUT TIME ZONE,
    initiator_id       BIGINT NOT NULL,
    location_id        BIGINT NOT NULL,
    paid               BOOLEAN     DEFAULT false,
    participant_limit  BIGINT      DEFAULT 0,
    published_on       TIMESTAMP WITHOUT TIME ZONE,
    request_moderation BOOLEAN     DEFAULT true,
    state              VARCHAR(20) DEFAULT 'PENDING',
    title              VARCHAR(120),
    views              BIGINT      DEFAULT 0,
    FOREIGN KEY (category_id) REFERENCES category (id),
    FOREIGN KEY (initiator_id) REFERENCES users (id),
    FOREIGN KEY (location_id) REFERENCES users (id),
    FOREIGN KEY (compilation_id) REFERENCES compilations (id)
);

CREATE TABLE IF NOT EXISTS requests
(
    id           BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    created      TIMESTAMP WITHOUT TIME ZONE,
    event_id     BIGINT NOT NULL,
    requester_id BIGINT NOT NULL,
    status       VARCHAR(20) DEFAULT 'PENDING',
    FOREIGN KEY (event_id) REFERENCES events (id),
    FOREIGN KEY (requester_id) REFERENCES users (id)
);


