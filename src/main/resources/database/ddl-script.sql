create table IF NOT EXISTS public.users
(
    id            serial PRIMARY KEY, -- 2^31 == 2 billion 147 millions
    user_login    varchar(100) UNIQUE NOT NULL,
    user_password varchar,
    user_role     varchar(50),
    status        varchar(50),
    user_name     varchar(100),
    card          varchar(50),
    phone         varchar UNIQUE,
    date_of_birth date
);

create table IF NOT EXISTS public.geolocation
(
    id           serial PRIMARY KEY,

    latitude     decimal(8, 5), -- (+-180.00000, +-90.00000)
    longitude    decimal(7, 5),

    country_code varchar(10),
    country_name varchar(100),
    county       varchar(100),
    city         varchar(50),
    district     varchar(50),
    street       varchar(100),
    house_number varchar(10),

    description  text
);

create table IF NOT EXISTS public.rental_point
(
    id             serial PRIMARY KEY,
    geolocation_id int references geolocation (id)
);

create table IF NOT EXISTS public.scooter
(
    id              serial PRIMARY KEY,
    manufacturer    varchar(50),
    model           varchar(50),
    status          varchar(50),
    charge          decimal(5, 2),                   -- from -999.99 to 999.99, actual range is 0.00 to 100.00
    mileage         int,
    rental_point_id int references rental_point (id)
);

create table IF NOT EXISTS public.ride
(
    id                    serial PRIMARY KEY,
    user_id               int references users (id),
    scooter_id            int references scooter (id),
    status                varchar(50),
    start_rental_point_id int references rental_point (id),
    end_rental_point_id   int references rental_point (id),
    start_timestamp       timestamp,
    end_timestamp         timestamp,
    ride_mileage          int
);

/*
DROP SCHEMA public CASCADE;
CREATE SCHEMA public;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO public;
*/

/*
Ctrl + Alt + L - reformat code
*/