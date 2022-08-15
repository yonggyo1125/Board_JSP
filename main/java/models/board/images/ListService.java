package models.board.images;

import java.util.List;
import models.file.FileDto;
import models.file.FileDao;

public class ListService {
	
	public List<FileDto> gets() {
		FileDao dao = FileDao.getIntance();
		List<FileDto> files = dao.getsDoneDesc("image_board");
		
		return files;
	}
}
