package models.admin.board;

import models.validation.Validator;

import java.util.ResourceBundle;

import models.validation.ValidationException;

public class BoardAdminValidator implements Validator {
	/**
	 * 중복 게시판 아이디 여부 체크 
	 * 
	 * @param {String} boardId
	 * @throws {ValidationException}
	 */
	public void checkDuplicateBoardId(String boardId) {
		ResourceBundle bundle = ResourceBundle.getBundle("bundle.admin");
		BoardAdminDao dao = BoardAdminDao.getInstance();
		if (dao.checkDuplicateBoardId(boardId)) {
			throw new ValidationException(bundle.getString("DUPLICATE_BOARD_ID"));
		}
	}
}
