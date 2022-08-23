package models.board;

import javax.servlet.http.HttpServletRequest;

import static commons.Utils.*;

import models.file.FileDao;
import models.member.MemberDto;

import org.mindrot.bcrypt.BCrypt;

public class UpdateService {
	public BoardDto update(HttpServletRequest request) {
		
		/** 유효성 검사 S */
		BoardValidator boardValidator = BoardValidator.getInstance();
		boardValidator.validate(request, "update");
		/** 유효성 검사 E */
		
		/** 게시글 수정 S */
		BoardDao dao = BoardDao.getInstance();
		BoardDto board = new BoardDto();
		board.setId(Integer.parseInt(request.getParameter("id").trim()));
		board.setBoardId(request.getParameter("boardId"));
		board.setSubject(request.getParameter("subject"));
		board.setContent(request.getParameter("content"));
		board.setPoster(request.getParameter("poster"));
		if (board.getMemNo() == 0) {
			String hash = BCrypt.hashpw(request.getParameter("guestPw"), BCrypt.gensalt(12));
			board.setGuestPw(hash);
		}
		boolean result = dao.update(board);
		if (!result) {
			return null;
		}
		
		board = dao.get(board.getId());
		
		// 이미지 및 첨부 파일 업로드 완료 처리
		String gid = board.getGid();
		FileDao fileDao = FileDao.getIntance();
		fileDao.updateDone(gid + "_image");
		fileDao.updateDone(gid + "_attached");
		/** 게시글 수정 E */
		
		return board;
	}
}
