package models.board.comment;

/**
 * 댓글 관련 예외 처리
 *
 */
public class CommentException extends RuntimeException {
	public CommentException(String message) {
		super(message);
	}
}