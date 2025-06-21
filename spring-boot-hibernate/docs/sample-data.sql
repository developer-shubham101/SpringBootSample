-- phpMyAdmin SQL Dump
-- version 5.2.2
-- https://www.phpmyadmin.net/
--
-- Host: mysql_db:3306
-- Generation Time: Jun 21, 2025 at 09:45 AM
-- Server version: 8.0.42
-- PHP Version: 8.2.28

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";

--
-- Database: `hibernate_db`
--

-- --------------------------------------------------------

--
-- Table structure for table `blogs`
--

CREATE TABLE `blogs` (
  `id` bigint NOT NULL,
  `content` text,
  `published_date` datetime(6) NOT NULL,
  `title` varchar(255) NOT NULL,
  `author_id` bigint NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Table structure for table `blog_categories`
--

CREATE TABLE `blog_categories` (
  `blog_id` bigint NOT NULL,
  `category_id` bigint NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Table structure for table `categories`
--

CREATE TABLE `categories` (
  `id` bigint NOT NULL,
  `name` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` bigint NOT NULL,
  `email` varchar(255) NOT NULL,
  `username` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `blogs`
--
ALTER TABLE `blogs`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_blog_title` (`title`),
  ADD KEY `idx_blog_published_date` (`published_date`),
  ADD KEY `FKt8g0udj2fq40771g38t2t011n` (`author_id`);

--
-- Indexes for table `blog_categories`
--
ALTER TABLE `blog_categories`
  ADD PRIMARY KEY (`blog_id`,`category_id`),
  ADD KEY `FKcg3a9iawd67s5th9pm1332ibg` (`category_id`);

--
-- Indexes for table `categories`
--
ALTER TABLE `categories`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK_t8o6pivur7nn124jehx7cygw5` (`name`),
  ADD KEY `idx_category_name` (`name`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK_6dotkott2kjsp8vw4d0m25fb7` (`email`),
  ADD UNIQUE KEY `UK_r43af9ap4edm43mmtq01oddj6` (`username`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `blogs`
--
ALTER TABLE `blogs`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `categories`
--
ALTER TABLE `categories`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `blogs`
--
ALTER TABLE `blogs`
  ADD CONSTRAINT `FKt8g0udj2fq40771g38t2t011n` FOREIGN KEY (`author_id`) REFERENCES `users` (`id`);

--
-- Constraints for table `blog_categories`
--
ALTER TABLE `blog_categories`
  ADD CONSTRAINT `FKcg3a9iawd67s5th9pm1332ibg` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`),
  ADD CONSTRAINT `FKg3e3kdc8go3vsajcc408pw5cl` FOREIGN KEY (`blog_id`) REFERENCES `blogs` (`id`);
COMMIT;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `email`, `username`) VALUES
(1, 'john.doe@example.com', 'johndoe'),
(2, 'jane.smith@example.com', 'janesmith'),
(3, 'alice.jones@example.com', 'alicejones'),
(4, 'bob.brown@example.com', 'bobbrown'),
(5, 'charlie.davis@example.com', 'charliedavis'),
(6, 'diana.miller@example.com', 'dianamiller'),
(7, 'ethan.wilson@example.com', 'ethanwilson'),
(8, 'fiona.moore@example.com', 'fionamoore'),
(9, 'george.taylor@example.com', 'georgetaylor'),
(10, 'hannah.anderson@example.com', 'hannahanderson');

--
-- Dumping data for table `categories`
--

INSERT INTO `categories` (`id`, `name`) VALUES
(1, 'Technology'),
(2, 'Travel'),
(3, 'Food'),
(4, 'Lifestyle'),
(5, 'Fashion'),
(6, 'Business'),
(7, 'Health'),
(8, 'Education'),
(9, 'Entertainment'),
(10, 'Sports');

--
-- Dumping data for table `blogs`
--

INSERT INTO `blogs` (`id`, `content`, `published_date`, `title`, `author_id`) VALUES
(1, 'This is the content of the first blog post about technology.', '2025-01-15 09:00:00', 'The Future of AI', 1),
(2, 'This is the content of the second blog post about travel.', '2025-01-16 10:30:00', 'Top 10 Destinations for 2025', 2),
(3, 'This is the content of the third blog post about food.', '2025-01-17 11:45:00', 'Simple and Healthy Recipes', 3),
(4, 'A post about minimalist lifestyle.', '2025-01-18 14:00:00', 'Living with Less', 4),
(5, 'Latest trends in summer fashion.', '2025-01-19 16:20:00', 'Summer Fashion Guide', 5),
(6, 'Tips for small business owners.', '2025-01-20 08:00:00', 'Small Business Success', 6),
(7, 'A guide to a healthy mind and body.', '2025-01-21 18:00:00', 'Holistic Health', 7),
(8, 'The importance of lifelong learning.', '2025-01-22 12:10:00', 'Never Stop Learning', 8),
(9, 'Review of the latest blockbuster movie.', '2025-01-23 19:00:00', 'Movie Night', 9),
(10, 'Recap of the championship game.', '2025-01-24 21:00:00', 'Championship Highlights', 10),
(11, 'Exploring the world of quantum computing.', '2025-02-01 09:30:00', 'Quantum Computing Explained', 1),
(12, 'Hidden gems in Southeast Asia.', '2025-02-02 11:00:00', 'Backpacking Southeast Asia', 2),
(13, 'How to make the perfect sourdough bread.', '2025-02-03 13:20:00', 'Sourdough Baking', 3),
(14, 'Digital nomad lifestyle explored.', '2025-02-04 15:00:00', 'Life as a Digital Nomad', 4),
(15, 'Sustainable fashion choices.', '2025-02-05 17:00:00', 'Eco-Friendly Fashion', 5),
(16, 'Marketing strategies for startups.', '2025-02-06 10:00:00', 'Startup Marketing 101', 6),
(17, 'The benefits of meditation.', '2025-02-07 19:30:00', 'Mindfulness and Meditation', 7),
(18, 'Online learning platforms review.', '2025-02-08 14:00:00', 'Best Online Courses', 8),
(19, 'Top TV shows to binge watch.', '2025-02-09 20:00:00', 'Binge-Worthy TV Shows', 9),
(20, 'A look at the upcoming Olympics.', '2025-02-10 22:00:00', 'Olympic Preview', 10),
(21, 'The rise of 5G technology.', '2025-03-01 11:00:00', 'Understanding 5G', 1),
(22, 'A travel guide to Iceland.', '2025-03-02 13:00:00', 'Iceland: Land of Fire and Ice', 2),
(23, 'Exploring street food around the world.', '2025-03-03 15:00:00', 'Global Street Food', 3),
(24, 'Tips for a productive home office.', '2025-03-04 16:30:00', 'Home Office Setup', 4),
(25, 'The evolution of streetwear.', '2025-03-05 18:00:00', 'Streetwear Trends', 5),
(26, 'Financial planning for freelancers.', '2025-03-06 09:00:00', 'Freelancer Finances', 6),
(27, 'The importance of a balanced diet.', '2025-03-07 17:45:00', 'Eating for Energy', 7),
(28, 'Learning a new language effectively.', '2025-03-08 11:30:00', 'Language Learning Tips', 8),
(29, 'The best music festivals of the year.', '2025-03-09 21:30:00', 'Music Festival Guide', 9),
(30, 'The history of the World Cup.', '2025-03-10 23:00:00', 'World Cup History', 10),
(31, 'Introduction to machine learning.', '2025-04-01 10:15:00', 'Machine Learning Basics', 1),
(32, 'Budget travel tips for Europe.', '2025-04-02 12:45:00', 'Europe on a Budget', 2),
(33, 'The art of making cocktails.', '2025-04-03 16:00:00', 'Cocktail Crafting', 3),
(34, 'Creating a capsule wardrobe.', '2025-04-04 17:30:00', 'Capsule Wardrobe Essentials', 4),
(35, 'Vintage fashion hunting.', '2025-04-05 19:00:00', 'Thrifting for Vintage', 5),
(36, 'E-commerce trends to watch.', '2025-04-06 11:00:00', 'The Future of E-commerce', 6),
(37, 'Yoga for beginners.', '2025-04-07 20:00:00', 'Beginner''s Guide to Yoga', 7),
(38, 'The benefits of reading daily.', '2025-04-08 13:00:00', 'The Power of Reading', 8),
(39, 'A look at classic cinema.', '2025-04-09 22:00:00', 'Hollywood''s Golden Age', 9),
(40, 'The rise of esports.', '2025-04-10 23:30:00', 'Esports Explained', 10),
(41, 'Cybersecurity best practices.', '2025-05-01 12:00:00', 'Protecting Your Digital Life', 1),
(42, 'Solo travel adventures.', '2025-05-02 14:30:00', 'The Joy of Solo Travel', 2),
(43, 'Vegan baking recipes.', '2025-05-03 17:00:00', 'Delicious Vegan Treats', 3),
(44, 'Finding work-life balance.', '2025-05-04 18:30:00', 'Balancing Act', 4),
(45, 'Accessorizing your outfits.', '2025-05-05 20:00:00', 'The Art of Accessorizing', 5),
(46, 'Building a personal brand.', '2025-05-06 13:00:00', 'Personal Branding', 6),
(47, 'The science of sleep.', '2025-05-07 21:00:00', 'Why We Sleep', 7),
(48, 'Coding bootcamps: are they worth it?', '2025-05-08 15:00:00', 'The Coding Bootcamp Debate', 8),
(49, 'The best video games of the decade.', '2025-05-09 23:00:00', 'Decade in Gaming', 9),
(50, 'Extreme sports to try once.', '2025-05-10 18:00:00', 'Adrenaline Rush', 10);

--
-- Dumping data for table `blog_categories`
--

INSERT INTO `blog_categories` (`blog_id`, `category_id`) VALUES
(1, 1),
(1, 8),
(2, 2),
(3, 3),
(4, 4),
(5, 5),
(6, 6),
(7, 7),
(8, 8),
(9, 9),
(10, 10),
(11, 1),
(12, 2),
(13, 3),
(14, 4),
(14, 2),
(15, 5),
(16, 6),
(17, 7),
(18, 8),
(19, 9),
(20, 10),
(21, 1),
(22, 2),
(23, 3),
(23, 2),
(24, 4),
(24, 6),
(25, 5),
(26, 6),
(27, 7),
(28, 8),
(29, 9),
(30, 10),
(31, 1),
(31, 8),
(32, 2),
(33, 3),
(33, 4),
(34, 4),
(34, 5),
(35, 5),
(36, 6),
(36, 1),
(37, 7),
(38, 8),
(39, 9),
(40, 10),
(40, 9),
(41, 1),
(42, 2),
(43, 3),
(44, 4),
(44, 7),
(45, 5),
(46, 6),
(47, 7),
(48, 8),
(48, 1),
(49, 9),
(50, 10);
