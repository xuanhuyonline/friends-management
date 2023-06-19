CREATE TABLE users (id BIGSERIAL PRIMARY KEY,
                    email varchar(255));

ALTER TABLE users OWNER TO postgres;

CREATE TABLE friends (id BIGSERIAL PRIMARY KEY,
                      status varchar(255),
                      subscriber boolean,
                      friend_id bigint CONSTRAINT fkc42eihjtiryeriy8axlkpejo7 REFERENCES users,
                      user_id bigint CONSTRAINT fklh21lfp7th1y1tn9g63ihkda9 REFERENCES users);

ALTER TABLE friends OWNER TO postgres;

INSERT INTO public.users (email) VALUES ('lexuanhuy2k1@gmail.com');
INSERT INTO public.users (email) VALUES ('levanan@gmail.com');
INSERT INTO public.users (email) VALUES ('nguyenvankhoa@gmail.com');
INSERT INTO public.users (email) VALUES ('nguyenmylan@gmail.com');
INSERT INTO public.users (email) VALUES ('nguyentrungthanh@gmail.com');
INSERT INTO public.users (email) VALUES ('lethanhmai@gmail.com');
INSERT INTO public.users (email) VALUES ('trantrungdung@gmail.com');
INSERT INTO public.users (email) VALUES ('vohoanglong@gmail.com');
INSERT INTO public.users (email) VALUES ('nguyenthinhan@gmail.com');
INSERT INTO public.users (email) VALUES ('nguyenvanthanh@gmail.com');

INSERT INTO public.friends (status, subscriber, friend_id, user_id) VALUES ('FRIEND', false, 2, 1);
INSERT INTO public.friends (status, subscriber, friend_id, user_id) VALUES ('FRIEND', false, 1, 2);
INSERT INTO public.friends (status, subscriber, friend_id, user_id) VALUES ('FRIEND', true, 1, 3);
INSERT INTO public.friends (status, subscriber, friend_id, user_id) VALUES ('FRIEND', false, 3, 1);