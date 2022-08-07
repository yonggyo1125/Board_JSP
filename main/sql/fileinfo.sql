CREATE TABLE `fileinfo` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '파일등록번호',
  `gid` VARCHAR(45) COMMENT '그룹 ID',
  `fileName` VARCHAR(80) NOT NULL COMMENT '파일명',
  `contentType` VARCHAR(45) NOT NULL COMMENT '파일 유형',
  `isDone` TINYINT(1) NULL DEFAULT 0 COMMENT '파일 업로드 완료 여부 1 - 완료, 0 - 미완료',
  `regDt` DATETIME NULL DEFAULT NOW() COMMENT '파일 등록일'
  PRIMARY KEY (`id`));