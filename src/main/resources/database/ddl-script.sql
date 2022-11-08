create table IF NOT EXISTS public.tariff
(
    id               serial PRIMARY KEY,
    name             varchar,
    price_per_minute decimal(19, 4),
    description      varchar
);

create table IF NOT EXISTS public.subscription
(
    id               serial PRIMARY KEY,
    name             varchar,
    price            decimal(19, 4),
    duration_in_days int NOT NULL,
    description      varchar
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
    geolocation_id int references geolocation (id) NOT NULL
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
    model_id        int references scooter_model (id) not null,
    status          varchar(50),
    charge          decimal(7, 4), -- from -999.9999 to 999.9999, actual range is 0.0000 to 100.0000
    mileage         decimal(10, 4),
    rental_point_id int references rental_point (id)
);

create table IF NOT EXISTS public.tariff2model
(
    id        serial PRIMARY KEY,
    tariff_id int references tariff (id)        not null,
    model_id  int references scooter_model (id) not null
);

create table IF NOT EXISTS public.subscription2model
(
    id              serial PRIMARY KEY,
    subscription_id int references subscription (id)  not null,
    model_id        int references scooter_model (id) not null
);

create table IF NOT EXISTS public.ride
(
    id                    serial PRIMARY KEY,
    user_id               int references users (id),
    scooter_id            int references scooter (id),
    status                varchar(50),
    price_total           decimal(19, 4),
    price_per_minute      decimal(19, 4),
    start_rental_point_id int references rental_point (id),
    end_rental_point_id   int references rental_point (id),
    creation_timestamp    timestamp,
    start_timestamp       timestamp,
    end_timestamp         timestamp,
    ride_mileage          decimal(10, 4)
);

CREATE OR REPLACE FUNCTION calculate_distance_to_point(rental_point_id int, lat1 double precision, lng1 double precision)
    RETURNS double precision
    language plpgsql
as
$$
declare
    geo_id                int;
    lat2                  decimal;
    lng2                  decimal;
    equatorialEarthRadius decimal = 6371;
    dLat                  decimal;
    dLng                  decimal;
    a                     decimal;
    b                     decimal;
begin
    select geolocation_id into geo_id from rental_point where id = rental_point_id;
    select geolocation.latitude into lat2 from geolocation where id = geo_id;
    select geolocation.longitude into lng2 from geolocation where id = geo_id;

    dLat = (lat2 - lat1) * PI() / 180 d;
    dLng = (lng2 - lng1) * PI() / 180 d;
    a = sin(dLat / 2) * sin(dLat / 2) + sin(dLng / 2) * sin(dLng / 2) * cos(lat1 * PI() / 180) * cos(lat2 * PI() / 180);
    b = 2 * atan2(sqrt(a), sqrt(1 - a));

    return equatorialEarthRadius * b;
end;
$$;

--select * from rental_point order by calculate_distance_to_point(rental_point_id := rental_point.id, lat1 := 59.9, lng1 := 30.3);

/*
DROP SCHEMA public CASCADE;
CREATE SCHEMA public;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO public;
*/


/*
Ctrl + Alt + L - reformat code
*/

/*If the table primary keys go out of sync*/;
-- SELECT setval(pg_get_serial_sequence('geolocation', 'id'), (SELECT MAX(id) FROM geolocation) + 1)
-- SELECT setval(pg_get_serial_sequence('subscription2model', 'id'), (SELECT MAX(id) FROM subscription2model) + 1);