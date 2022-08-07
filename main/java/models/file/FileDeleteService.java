package models.file;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import models.file.FileDto;
import models.file.FileDao;
import commons.BadRequestException;

/**
 * 파일 삭제
 *  
 * @author YONGGYO
 *
 */
public class FileDeleteService {
	
	public List<Integer> delete(HttpServletRequest request) {
		
		String[] ids = request.getParameterValues("id");
		if (ids == null || ids.length == 0) {
			throw new BadRequestException();
		}
		
		String uploadPath = request.getServletContext().getRealPath(File.separator + "static" + File.separator  + "upload");
		
		FileDao dao = FileDao.getIntance();
		List<Integer> deletedIds = new ArrayList<>();
		for (String _id : ids) {
			int id = Integer.parseInt(_id.trim());
			FileDto fileInfo = dao.get(id);
			String uploadFilePath = uploadPath + File.separator + "" + (id % 10) + File.separator + "" + id;
			File file = new File(uploadFilePath);
			if (fileInfo == null) {
				continue;
			}
			
			if (file.exists()) {
				file.delete();
			}
			
			dao.delete(id);
			deletedIds.add(id);
		}
		
		return deletedIds;
	}
}
