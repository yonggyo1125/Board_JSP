CREATE TABLE `boardAdmin` (
  `boardId` VARCHAR(45) NOT NULL COMMENT '게시판 아이디 ',
  `boardNm` VARCHAR(45) NULL COMMENT '게시판 이름',
  `isUse` TINYINT(1) NULL DEFAULT 0 COMMENT '게시판 사용 여부  1 - 사용, 0 - 미사용',
  `noOfRows` INT NULL DEFAULT 20 COMMENT '1페이지당 노출 게시글 수 ',
  `useComment` TINYINT(1) NULL DEFAULT 0 COMMENT '댓글 사용여부  1 - 사용, 0 - 미사용',
  `regDt` DATETIME NULL DEFAULT NOW() COMMENT '설정 등록일',
  `modDt` DATETIME NULL COMMENT '설정 수정일',
  PRIMARY KEY (`boardId`),
  UNIQUE INDEX `boardId_UNIQUE` (`boardId` ASC) VISIBLE);