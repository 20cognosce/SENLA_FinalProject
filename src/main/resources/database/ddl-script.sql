create table IF NOT EXISTS public.tariff
(
    id               serial PRIMARY KEY,
    name      varchar,
    price_per_minute decimal(19, 4),
    description      varchar
);

create table IF NOT EXISTS public.subscription
(
    id                serial PRIMARY KEY,
    name varchar,
    price             decimal(19, 4),
    description       varchar
);

create table IF NOT EXISTS public.users
(
    id            serial PRIMARY KEY, -- 2^31 == 2 billion 147 millions
    login         varchar(100) UNIQUE NOT NULL,
    hash_password varchar,
    role          varchar(50),

    status        varchar(50),
    name          varchar(100),
    phone         varchar UNIQUE,
    date_of_birth date,

    credit_card   varchar(50),
    tariff_id     int references tariff (id) default 1
);

create table IF NOT EXISTS public.user2subscription
(
    id              serial PRIMARY KEY,
    user_id         int references users (id),
    subscription_id int references subscription (id),
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

create table IF NOT EXISTS public.scooter_model
(
    id               serial PRIMARY KEY,
    manufacturer     varchar(50),
    model            varchar(50),
    scooter_weight   decimal(6, 4),
    max_weight_limit smallint,
    max_speed        smallint,
    max_range        smallint,
    price            decimal(19, 4)
);

create table IF NOT EXISTS public.scooter
(
    id              serial PRIMARY KEY,
    model_id        int references scooter_model (id),
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
    ride_mileage          decimal(10, 4)
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