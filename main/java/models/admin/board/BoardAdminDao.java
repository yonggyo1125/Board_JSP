package models.admin.board;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import mybatis.Connection;

public class BoardAdminDao {
	public static BoardAdminDao instance = new BoardAdminDao();
	
	private BoardAdminDao() {}
	
	/**
	 * 게시글 설정 등록 
	 * @param dto
	 * @return {boolean}
	 */
	public boolean register(BoardAdminDto dto) {
		SqlSession sqlSession = Connection.getSqlSession();
		
		int affectedRows = sqlSession.insert("BoardAdminMapper.register", dto);
		
		sqlSession.commit();
		sqlSession.close();
		
		return affectedRows > 0;
	}
	
	/**
	 * 게시글 설정 수정
	 * 
	 * @param {BoardAdminDto} dto
	 * @return {boolean}
	 */
	public boolean update(BoardAdminDto dto) {
		SqlSession sqlSession = Connection.getSqlSession();
		
		int affectedRows = sqlSession.update("BoardAdminMapper.update", dto);
		
		sqlSession.commit();
		sqlSession.close();
		
		return affectedRows > 0;
	}
	
	/**
	 * 게시글 설정 삭제
	 * 
	 * @param {String} boardId
	 * @return {boolean}
	 */
	public boolean delete(String boardId) {
		SqlSession sqlSession = Connection.getSqlSession();
		BoardAdminDto param = new BoardAdminDto();
		param.setBoardId(boardId);
		
		int affectedRows = sqlSession.delete("BoardAdminMapper.delete", param);
		
		sqlSession.commit();
		sqlSession.close();
		
		return affectedRows > 0;
	}
	
	/**
	 * 게시글 설정 목록
	 * 
	 * @return {List<BoardAdminDto>}
	 */
	public List<BoardAdminDto> gets() {
		SqlSession sqlSession = Connection.getSqlSession();
		List<BoardAdminDto> boards = sqlSession.selectList("BoardAdminMapper.boards");
		
		sqlSession.close();
		
		return boards;
		
	
	}
	
	/**
	 * 게시글 설정 조회
	 * 
	 * @param {String} boardId
	 * @return
	 */
	public BoardAdminDto get(String boardId) {
		SqlSession sqlSession = Connection.getSqlSession();
		
		BoardAdminDto param = new BoardAdminDto();
		param.setBoardId(boardId);
		BoardAdminDto board = sqlSession.selectOne("BoardAdminMapper.board", param);

		sqlSession.close();
		
		return board;
	}
	
	/**
	 * 중복 게시판 아이디 여부 체크 
	 * 
	 * @param {String} boardId
	 * @return {boolean}
	 */
	public boolean checkDuplicateBoardId(String boardId) {
		SqlSession sqlSession = Connection.getSqlSession();
		BoardAdminDto param = new BoardAdminDto();
		param.setBoardId(boardId);
		
		int count = sqlSession.selectOne("BoardAdminMapper.duplicateCheck", param);
		sqlSession.close();
		
		return count > 0;
	}
	
	public static BoardAdminDao getInstance() {
		return instance;
	}
}
