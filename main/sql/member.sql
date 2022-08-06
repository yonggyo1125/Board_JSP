CREATE TABLE `member` (
   `memNo` int NOT NULL AUTO_INCREMENT,
   `memId` varchar(45) NOT NULL COMMENT '아이디',
   `memPw` varchar(60) NOT NULL COMMENT '비밀번호',
   `memNm` varchar(45) NOT NULL COMMENT '회원명',
   `email` varchar(60) DEFAULT NULL COMMENT '이메일 정보',
   `mobile` varchar(11) DEFAULT NULL COMMENT '휴대전화번호',
   `regDt` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
   `modDt` datetime DEFAULT NULL,
   PRIMARY KEY (`memNo`),
   UNIQUE KEY `memId_UNIQUE` (`memId`)
 );