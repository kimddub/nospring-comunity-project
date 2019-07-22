DROP DATABASE IF EXISTS `a2`;

CREATE DATABASE `a2`;

USE `a2`;

CREATE TABLE `member` (
  `id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `regDate` DATETIME NOT NULL,
  `loginId` CHAR(100) NOT NULL,
  `loginPw` CHAR(100) NOT NULL,
  `name` CHAR(100) NOT NULL,
  PRIMARY KEY (`id`)
);

INSERT INTO `member`
(`id`, `regDate`, `loginId`, `loginPw`, `name`)
VALUES
(1, '2019-05-27 22:54:25', 'user1', 'user1', '홍길동'),
(2, '2019-05-27 22:54:36', 'user2', 'user2', '홍길순');

CREATE TABLE `board` (
  `id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `regDate` DATETIME NOT NULL,
  `name` CHAR(100) NOT NULL,
  PRIMARY KEY (`id`)
);

INSERT  INTO `board`(`id`,`regDate`,`name`) VALUES
(1,'2019-05-27 22:54:25','공지사항'),
(2,'2019-05-27 22:54:36','자유게시판');

CREATE TABLE `article` (
  `id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `regDate` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `title` CHAR(100) NOT NULL,
  `body` TEXT NOT NULL,
  `memberId` INT(10) UNSIGNED NOT NULL,
  `boardId` INT(10) UNSIGNED NOT NULL,
  PRIMARY KEY (`id`)
);

INSERT  INTO `article`(`id`,`regDate`,`title`,`body`,`memberId`,`boardId`) VALUES
(1,'2019-06-28 22:55:18','제목1','내용1\nㅎㅎ',1,1),
(2,'2019-06-28 22:55:28','제목2','내용2\nㅋㅋ',2,1),
(3,'2019-06-28 22:56:44','제목3','내용3\nㅋㅋ',2,2);

CREATE TABLE `articleReply` (
  `id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `regDate` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `articleId` INT(10) UNSIGNED NOT NULL,
  `boardId` INT(10) UNSIGNED NOT NULL,
  `memberId` INT(10) UNSIGNED NOT NULL,
  `body` TEXT NOT NULL,
  PRIMARY KEY (`id`)
);

INSERT  INTO `articleReply`(`id`,`regDate`,`articleId`,`boardId`,`memberId`,`body`) VALUES
(1,'2019-06-28 22:55:18',1,1,1,'댓글 내용1'),
(2,'2019-06-28 22:55:18',1,1,1,'댓글 내용\n1_2'),
(3,'2019-06-28 22:55:18',1,1,1,'댓글 내용\n1_3');
