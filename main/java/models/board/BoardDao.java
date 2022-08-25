package models.board;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import mybatis.Connection;

public class BoardDao {
	
	private static BoardDao instance = new BoardDao();
	
	/**
	 * 게시글 목록 
	 * @param String boardId 게시판 아이디
	 * @param page
	 * @param limit
	 * @return
	 */
	public List<BoardDto> gets(String boardId, int page, int limit) {
		SqlSession sqlSession = Connection.getSqlSession();
		BoardListDto param = new BoardListDto();
		param.setBoardId(boardId);
		int offset = (page - 1) * limit;
		param.setOffset(offset);
		param.setLimit(limit);
		List<BoardDto> posts = sqlSession.selectList("BoardMapper.gets", param);
		sqlSession.commit();
		sqlSession.close();
		
		return posts;
	}
	
	public List<BoardDto> gets(String boardId,  int page) {
		return gets(boardId, page, 20);
	}
	
	public List<BoardDto> gets(String boardId) {
		return gets(boardId, 1);
	}
	
	/**
	 * 게시판별 총 게시글 수 
	 * 
	 * @param boardId
	 * @return
	 */
	public int getTotal(String boardId) {
		SqlSession sqlSession = Connection.getSqlSession();
		
		BoardDto param = new BoardDto();
		param.setBoardId(boardId);
		int total = sqlSession.selectOne("BoardMapper.total", param);
		
		sqlSession.close();
		
		return total;
	}
	
	/**
	 * 게시글 조회
	 * 
	 */
	public BoardDto get(int id) {
		SqlSession sqlSession = Connection.getSqlSession();
		BoardDto param = new BoardDto();
		param.setId(id);
		
		BoardDto post = sqlSession.selectOne("BoardMapper.get", param);
		
		sqlSession.close();
		
		return post;
	}
	
	/**
	 * 게시글 추가 
	 * 
	 * @param {BoardDto} dto
	 * @return {boolean}
	 */
	public boolean register(BoardDto dto) {
		SqlSession sqlSession = Connection.getSqlSession();
		int affectedRows = sqlSession.insert("BoardMapper.register", dto);
		sqlSession.commit();
		sqlSession.close();
		
		return affectedRows > 0;
	}
	
	/**
	 * 게시글 수정
	 * 
	 * @param {BoardDto} dto
	 * @return {boolean}
	 */
	public boolean update(BoardDto dto) {
		SqlSession sqlSession = Connection.getSqlSession();
		
		int affectedRows = sqlSession.update("BoardMapper.update", dto);
		
		sqlSession.commit();
		sqlSession.close();
		
		return affectedRows > 0;
	}
	
	/**
	 * 게시글 삭제 
	 * 
	 * @param {int} id 게시글 번호 
	 * @return {boolea}
	 */
	public boolean delete(int id) {
		SqlSession sqlSession = Connection.getSqlSession();
		BoardDto param = new BoardDto();
		param.setId(id);
		int affectedRows = sqlSession.update("BoardMapper.delete", param);
		
		sqlSession.commit();
		sqlSession.close();
		return affectedRows > 0;
	}
	
	public static BoardDao getInstance() {
		if (instance == null) {
			instance = new BoardDao();
		}
		
		return instance;
	}
}
