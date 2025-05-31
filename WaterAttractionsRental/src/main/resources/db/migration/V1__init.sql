CREATE TABLE app_user (
                          id BIGSERIAL PRIMARY KEY,
                          username VARCHAR(255) NOT NULL UNIQUE,
                          password VARCHAR(255) NOT NULL,
                          role VARCHAR(255),
                          CONSTRAINT app_user_role_check CHECK (role IN ('USER', 'ADMIN'))
);

CREATE TABLE attraction (
                            id BIGSERIAL PRIMARY KEY,
                            name VARCHAR(255),
                            type VARCHAR(255),
                            available BOOLEAN NOT NULL,
                            price DOUBLE PRECISION DEFAULT 0,
                            CONSTRAINT attraction_type_check CHECK (type IN ('KAJAK', 'SKUTER_WODNY', 'ROWER_WODNY'))
);

CREATE TABLE reservation (
                             id BIGSERIAL PRIMARY KEY,
                             start_time TIMESTAMP,
                             end_time TIMESTAMP,
                             user_id BIGINT NOT NULL,
                             attraction_id BIGINT NOT NULL,
                             CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES app_user(id),
                             CONSTRAINT fk_attraction FOREIGN KEY (attraction_id) REFERENCES attraction(id)
);
