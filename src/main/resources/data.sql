insert into roles values (1, 'USER');

insert into users(email, enabled, first_name, last_name, nickname, password, username, bio) values
                                                                                                ('rick@mail.com', true, 'Rick', 'Sanchez', 'Rick', '$2a$10$D/2JcFzpnoy.XlFPP3eyQu/9wm749pPIyPl.Os9RKI9s7lDY.MbIK', 'RickSanchez', 'Im pickle rick'),
                                                                                                ('morty@mail.com', true, 'Morty', 'Smith', 'MortDawg', '$2a$10$D/2JcFzpnoy.XlFPP3eyQu/9wm749pPIyPl.Os9RKI9s7lDY.MbIK', 'MortySmith', 'Rick is my grandpa'),
                                                                                                ('summer@mail.com', true, 'Summer', 'Smith', 'Summer', '$2a$10$D/2JcFzpnoy.XlFPP3eyQu/9wm749pPIyPl.Os9RKI9s7lDY.MbIK', 'SSSummertime', 'My brother Morty is so annoying, he never lets me join adventures with my Grandpa Rick'),
                                                                                                ('birdperson@mail.com', true, 'Bird', 'Person', 'BirdPerson', '$2a$10$D/2JcFzpnoy.XlFPP3eyQu/9wm749pPIyPl.Os9RKI9s7lDY.MbIK', 'BirdPerson', 'I committed many war crimes in the gears war');

insert into users(first_name, last_name, email, username, password, bio, nickname, verified_account, private_account)
values ('Unknown', 'Koder', 'email@email.com', 'unknownkoder', '$2a$10$NVz6lYlNrOs0ldHOF93kE.9/EfKQ6G9Z7dHW4O9tmaTydIAJREk.q', 'Software Engineer', 'Unknown Koder', true, false);

insert into user_role_junction values (1,1);

insert into following values (5, 1), (5, 2), (5, 3);
insert into followers values (1, 5), (2, 5), (3, 5);

insert into posts (post_id, audience, content, posted_date, is_reply, reply_restriction, reply_to, scheduled, scheduled_date, author_id, poll_id) values (1, 0, 'Aliquam augue quam, sollicitudin vitae, consectetuer eget, rutrum at, lorem. Integer tincidunt ante vel ipsum.', '2023-08-26 08:26:21', true, 0, null, false, null, 4, null);
