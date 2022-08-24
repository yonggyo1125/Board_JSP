package models.board;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static commons.Utils.*;
import commons.BadRequestException;
import models.board.BoardDao;
import models.file.FileDao;
import models.file.FileDto;

/**
 * 게시글 삭제 
 * 
 * @author YONGGYO
 *
 */
public class DeleteService {
	
	public BoardDto delete(HttpServletRequest request, HttpServletResponse response) {
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
		 BoardValidator.getInstance().permissionCheck(request, response);

		ResourceBundle bundle = ResourceBundle.getBundle("bundle.board");
		
		/** 게시글 조회 S */
		BoardDao dao = BoardDao.getInstance();
		BoardDto board = dao.get(id);
		if (board == null) {
			throw new BoardException(bundle.getString("POST_NOT_EXISTS"));
		}
		
		 if ( ! isAdmin(request) && board.getMemNo() == 0 && request.getSession().getAttribute("password_confirmed_" + id) == null) {
			 	return null;
		 }
		/** 게시글 조회 E */
		
		/** 첨부 파일 삭제 S */
		 List<FileDto> files = FileDao.getIntance().gets(board.getGid() + "%");
		 fileDelete(request, files);
		/** 첨부 파일 삭제 E */
		
		/** 게시글 삭제 S */
		if (!dao.delete(id)) {
			throw new BoardException(bundle.getString("FAIL_TO_DELETE"));
		}
		/** 게시글 삭제 E */
		return board;
	}
	
	/**
	 * 파일 삭제 
	 * 
	 * @param request
	 * @param files
	 */
	private void fileDelete(HttpServletRequest request, List<FileDto> files) {
		if (files == null) {
			return;
		}
		FileDao fileDao = FileDao.getIntance();
		String uploadPath = request.getServletContext().getRealPath(File.separator + "static" + File.separator  + "upload");
		for (FileDto file : files) {
			int fid = file.getId();
			String uploadFilePath = uploadPath + File.separator + "" + (fid % 10) + File.separator + "" + fid;
			File f = new File(uploadFilePath);
			if (f.exists()) {
				f.delete();
			}
			fileDao.delete(fid);
		}
	}
}
