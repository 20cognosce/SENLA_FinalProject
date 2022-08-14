create table IF NOT EXISTS public.tariff
(
    id               serial PRIMARY KEY,
    tariff_name      varchar,
    price_per_minute decimal(19, 4),
    discount         decimal(5, 4) default 1.0000, -- 30% = 0.3, 70% = 0.7
    description      varchar
);

create table IF NOT EXISTS public.subscription
(
    id                serial PRIMARY KEY,
    subscription_name varchar,
    price             decimal(19, 4),
    description       varchar
);

create table IF NOT EXISTS public.users
(
    id            serial PRIMARY KEY, -- 2^31 == 2 billion 147 millions
    user_login    varchar(100) UNIQUE NOT NULL,
    user_password varchar,
    user_role     varchar(50),

    status        varchar(50),
    user_name     varchar(100),
    phone         varchar UNIQUE,
    date_of_birth date,

    card          varchar(50),
    tariff_id     int references tariff (id) default 1
);

create table IF NOT EXISTS public.user2subscription
(
    id              serial PRIMARY KEY,
    user_id         int references users (id),
    subscription_id int references subscription (id),
    discount        decimal(5, 4) default 1.0000,
    start_time      timestamp,
    end_time        timestamp
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
    charge          decimal(7, 4), -- from -999.9999 to 999.9999, actual range is 0.0000 to 100.0000
    mileage         int,
    rental_point_id int references rental_point (id)
);

create table IF NOT EXISTS public.ride
(
    id                    serial PRIMARY KEY,
    user_id               int references users (id),
    scooter_id            int references scooter (id),
    status                varchar(50),
    price                 decimal(19, 4),
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