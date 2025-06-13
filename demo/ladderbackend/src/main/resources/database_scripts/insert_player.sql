delete from player_ladder;
delete from player;

INSERT INTO player (player_id, first_name, last_name, court_id, email, cell, level, availability, password) VALUES
(10, 'Craig', 'Rivers', 5, 'craigrivers@mac.com', '301-524-0978', '1', 'Available starting 6/9/25.  Available Tuesday - Friday 8 am - 8 PM, Saturdays 8:30 AM - 7:30 PM.  My preference is playing outdoors when weather permits but will play indoors if necessary.  I prefer to play at courts closer to Watkins.', 'Nydc_1234'),
(17, 'Jeffrey', 'Brown', 2, 'unojef@gmail.com', '7038645334', '1', 'Anybody want to play tomorrow or Thursday? During the day is preferable. Available most days starting at 8:30 a.m. available throughout the day. However have soccer practice Monday and Wednesdays from 5:30 to 7:00 p.m. and tennis practice May 19-22 from 2:30 to 4:30, all day 23rd.', '00Suppo$$'),
(11, 'Jaybe', 'Byers', 1, 'jaybebyers@gmail.com', '2023696427', '1', NULL, 'Ladder@2025'),
(26, 'Gary', 'Mccoy', 1, 'gmccoy50@hotmail.com', '3013990288', '1', 'Available most days, live in Saint Mary''s County would like to meet in waldorf@ Thomas Stone high school or condos off Saint Patrick''s until school gets out on 6-10. Then Stone is always good. Schedule is flexible. Reach out', 'Tennis1962$'),
(12, 'Eric', 'Liley', 1, 'ericliley@comcast.net', '30-741-5850', '1', 'During the week after 3pm. Sat 10:00am  Sun. 5:00pm', 'London0625*'),
(13, 'Kevin', 'Claiborne', 1, 'kevin@snapyourphoto.com', '2024093356', '1', 'varies….just reach out', 'P@$sword@1347'),
(15, 'Daniel', 'Scott', 1, 'dkyles@live.com', '4103820686', '1', 'Available mostly days, 9a is always good. Work nights.', 'Ulanik25@'),
(16, 'Winston', 'Chaney', 3, 'winstonchaney@winstonchaney.com', '3019800700', '1', 'Tuesday, Thursday,, Friday & Saturday', 'Whur92020!'),
(18, 'Mark', 'David', 1, 'mdavid3@hotmail.com', '2403517167', '1', 'I''m booked on Mondays and Saturdays.  Best days to play are Wednesday or Thursday evenings 7pm or later (live in Virginia) or Sundays any time.  Once retired in July, will have wide-open schedule!', 'Goingtoretire10!'),
(21, 'Yusuf', 'House', 2, 'yusuf.house@gmail.com', '6092278010', '1', 'schedule varies week to week.. best to text or just schedule a match', 'Basketball11@'),
(24, 'Damion', 'Trasada', 2, 'dame0619@gmail.com', '2402307144', '1', 'Available most weekends.  Some Tuesdays and Thursdays.', 'Africa1!'),
(25, 'Alan', 'Richardson', 2, 'alanrich653@gmail.com', '3016758963', '1', NULL, 'Lashawn1@'); 

INSERT INTO player_ladder (ladder_id, player_id) VALUES
(1, 10),
(1, 11),
(1, 12),
(1, 13),
(1, 15),
(1, 16),
(1, 17),
(1, 18),
(1, 21),
(1, 24),
(1, 25),
(1, 26); 

