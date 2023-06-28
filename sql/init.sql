create table users
(
    id       bigserial
        primary key,
    email    varchar(255)
        constraint uk6dotkott2kjsp8vw4d0m25fb7
            unique,
    password varchar(255),
    username varchar(255)
        constraint ukr43af9ap4edm43mmtq01oddj6
            unique
);

alter table users
    owner to postgres;

CREATE TABLE friends (id BIGSERIAL PRIMARY KEY,
                      status varchar(255),
                      subscriber boolean,
                      friend_id bigint CONSTRAINT fkc42eihjtiryeriy8axlkpejo7 REFERENCES users,
                      user_id bigint CONSTRAINT fklh21lfp7th1y1tn9g63ihkda9 REFERENCES users);

ALTER TABLE friends OWNER TO postgres;

create table roles
(
    id   serial
        primary key,
    name varchar(255)
);

alter table roles
    owner to postgres;

create table user_role
(
    user_id bigint  not null
        constraint fkj345gk1bovqvfame88rcx7yyx
            references users,
    role_id integer not null
        constraint fkt7e7djp752sqn6w22i6ocqy6q
            references roles,
    primary key (user_id, role_id)
);

alter table user_role
    owner to postgres;

INSERT INTO public.roles (name) VALUES ('ROLE_ADMIN');
INSERT INTO public.roles (name) VALUES ('ROLE_MODERATOR');
INSERT INTO public.roles (name) VALUES ('ROLE_USER');

INSERT INTO public.users (email, password, username) VALUES ('lexuanhuy2k1@gmail.com', '$2a$10$ZaLXwqLgVoyR0nt/QrZ73uKSLQib1QcmkUn10EzrCWAqs4VD1LDJi', 'xuanhuy');
INSERT INTO public.users (email, password, username) VALUES ('levanan@gmail.com', '$2a$10$U9yJGVnCTdv.qYiti2LpqO.r5xJaDile6RtgeccTqgA0fB5LtE0cu', 'vanan');
INSERT INTO public.users (email, password, username) VALUES ('nguyenvankhoa@gmail.com', '$2a$10$33FFI4Mh0YwNwyAh12XJfuBTH9ZiKLxMZ2Nwqh.3WQJIuTIXNMJRO', 'vankhoa');
INSERT INTO public.users (email, password, username) VALUES ('nguyenmylan@gmail.com', '$2a$10$xf/d8TJHDKe1YKHOjo8YQeEWx9M6gNjhpd9jR32kETzxrO.J8zKTK', 'mylan');
INSERT INTO public.users (email, password, username) VALUES ('nguyentrungthanh@gmail.com', '$2a$10$MYgxFI/stwC5ZbFYBmQIxu33AGkpi1Vddwe5UPMKg3zkK.wkE0urS', 'trungthanh');
INSERT INTO public.users (email, password, username) VALUES ('lethanhmai@gmail.com', '$2a$10$g.jqIHdcsKPI6YUWzRCMgONYZyb8ox53iVjefEmcblPL2dhJ3wbsy', 'thanhmai');
INSERT INTO public.users (email, password, username) VALUES ('trantrungdung@gmail.com', '$2a$10$5BcNp4gBYxFTgoI4c.lLnuXS6FeD/.VaFM9j9/rnAIp9z9..LXMYu', 'trungdung');
INSERT INTO public.users (email, password, username) VALUES ('vohoanglong@gmail.com', '$2a$10$d2dcNVIVQ4jzJ.qcU6Y10uc7rhvWFE0BjsxTg11Bd249RAR7OhE.W', 'hoanglong');
INSERT INTO public.users (email, password, username) VALUES ('nguyenthinhan@gmail.com', '$2a$10$56EGImeAdCQv9wnQq.yzNO4eCOZXLmSYUWp4IJfgIxE9YtOnKNjRq', 'thinhan');
INSERT INTO public.users (email, password, username) VALUES ('nguyenvanthanh@gmail.com', '$2a$10$LfvZvaqIwxOMnvi/VQXzzuJGdjzQYuC3NR2DSkn9URQmzzV5PfD.6', 'vanthanh');


INSERT INTO public.user_role (user_id, role_id) VALUES (1, 1);
INSERT INTO public.user_role (user_id, role_id) VALUES (1, 3);
INSERT INTO public.user_role (user_id, role_id) VALUES (1, 2);
INSERT INTO public.user_role (user_id, role_id) VALUES (2, 3);
INSERT INTO public.user_role (user_id, role_id) VALUES (3, 2);
INSERT INTO public.user_role (user_id, role_id) VALUES (4, 3);
INSERT INTO public.user_role (user_id, role_id) VALUES (5, 3);
INSERT INTO public.user_role (user_id, role_id) VALUES (6, 3);
INSERT INTO public.user_role (user_id, role_id) VALUES (7, 3);
INSERT INTO public.user_role (user_id, role_id) VALUES (8, 3);
INSERT INTO public.user_role (user_id, role_id) VALUES (9, 3);
INSERT INTO public.user_role (user_id, role_id) VALUES (10, 3);

INSERT INTO public.friends (status, subscriber, friend_id, user_id) VALUES ('FRIEND', false, 2, 1);
INSERT INTO public.friends (status, subscriber, friend_id, user_id) VALUES ('FRIEND', false, 1, 2);
INSERT INTO public.friends (status, subscriber, friend_id, user_id) VALUES ('FRIEND', true, 1, 3);
INSERT INTO public.friends (status, subscriber, friend_id, user_id) VALUES ('FRIEND', false, 3, 1);