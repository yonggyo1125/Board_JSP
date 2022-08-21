package models.board;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import mybatis.Connection;

public class BoardDao {
	/**
	 * 게시글 목록 
	 * @param page
	 * @param limit
	 * @return
	 */
	public List<BoardDto> gets(int page, int limit) {
		SqlSession sqlSession = Connection.getSqlSession();
		BoardListDto param = new BoardListDto();
		int offset = (page - 1) * limit;
		param.setOffset(offset);
		param.setLimit(limit);
		List<BoardDto> posts = sqlSession.selectList("BoardMapper.gets", param);
		sqlSession.commit();
		sqlSession.close();
		
		return posts;
	}
	
	public List<BoardDto> gets(int page) {
		return gets(page, 20);
	}
	
	public List<BoardDto> gets() {
		return gets(1);
	}
	
	/**
	 * 게시글 조회
	 * 
	 */
}
