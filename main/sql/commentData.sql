CREATE TABLE `commentData` (
   `id` int NOT NULL AUTO_INCREMENT,
   `boardDataId` int DEFAULT NULL COMMENT '게시글 번호',
   `memNo` int DEFAULT NULL COMMENT '회원번호',
   `poster` varchar(45) DEFAULT NULL COMMENT '작성자명',
   `guestPw` varchar(65) DEFAULT NULL COMMENT '비회원 비밀번호',
   `content` text COMMENT '댓글 내용',
   `regDt` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '등록일시',
   `modDt` datetime DEFAULT NULL COMMENT '수정일시',
   PRIMARY KEY (`id`),
   KEY `memNo` (`memNo`),
   KEY `regDt` (`regDt`),
   KEY `fk_boardData_id` (`boardDataId`),
   CONSTRAINT `fk_boardData_id` FOREIGN KEY (`boardDataId`) REFERENCES `boarddata` (`id`)
 );