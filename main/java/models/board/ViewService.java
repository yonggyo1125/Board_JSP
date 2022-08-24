package models.board;

import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import commons.BadRequestException;
import models.file.FileDao;
import models.file.FileDto;


public class ViewService {
	
	public BoardDto view(HttpServletRequest request, HttpServletResponse response) {
		String _id = request.getParameter("id");
		if (_id == null || _id.isBlank()) {
			throw new BadRequestException();
		}
		
		int id;
		try {
			id = Integer.parseInt(_id.trim());  
		} catch (Exception e) { // 숫자가 아니라면 예외 발생
			throw new BadRequestException();
		}
		
		// 게시글 접근 권한 체크
		if (request.getAttribute("isViewPage") == null) {
			BoardValidator.getInstance().permissionCheck(request, response);
		}
		
		ResourceBundle bundle = ResourceBundle.getBundle("bundle.board");
		
		/** 게시글 조회 S */
		BoardDao dao = BoardDao.getInstance();
		BoardDto board = dao.get(id);
		if (board == null) {
			throw new BoardException(bundle.getString("POST_NOT_EXISTS"));
		}
		
		String gid = board.getGid();
		FileDao fileDao = FileDao.getIntance();
		
		// 이미지 파일 조회 
		List<FileDto> imageFiles = fileDao.getsDoneDesc(gid + "_image");
		board.setImageFiles(imageFiles);
		
		// 첨부 파일 조회 
		List<FileDto> attachedFiles = fileDao.getsDoneDesc(gid + "_attached");
		board.setAttachedFiles(attachedFiles);
		/** 게시글 조회 E */
		
		return board;
	}
}
