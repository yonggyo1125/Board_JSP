CREATE TABLE `boardData` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '게시글 번호',
  `boardId` VARCHAR(45) NULL COMMENT '게시판 아아디',
  `gid` VARCHAR(45) NULL COMMENT '그룹 ID',
  `memNo` INT NULL DEFAULT 0 COMMENT '회원 번호',
  `poster` VARCHAR(30) NULL COMMENT '작성자명',
  `subject` VARCHAR(255) NULL COMMENT '글 제목',
  `content` LONGTEXT NULL COMMENT '게시글 내용',
  `regDt` DATETIME NULL DEFAULT NOW() COMMENT '등록일시',
  `modDt` DATETIME NULL COMMENT '수정일시',
  PRIMARY KEY (`id`),
  INDEX `recentPost` (`regDt` DESC) INVISIBLE,
  INDEX `memNo` (`memNo` ASC) VISIBLE);
