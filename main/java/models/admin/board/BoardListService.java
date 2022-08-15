package models.admin.board;

import java.util.List;

public class BoardListService {
	
	public List<BoardAdminDto> gets() {
		return BoardAdminDao.getInstance().gets();
	}
}
