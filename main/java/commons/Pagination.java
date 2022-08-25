package commons;

public class Pagination {
	
	private int page; // 현재 페이지
	private int total;  // 전체 레코드 수
	private int totalPages; // 전체 페이지 수 
	private int prevPage; // 이전 페이지 
	private int nextPage; // 다음 페이지 
	private int lastPage; // 마지막 페이지 
	private int pageCnt; // 구간별 페이지 갯수 
	private int[] pages; // 페이지 구간
	
	/**
	 * 페이지 계산
	 * 
	 * @param {int} page : 현재 페이지  
	 * @param {int} total : 전체 레코드 수 
	 */
	public Pagination(int page, int total) {
		pageCnt = pageCnt < 1 ? 10:pageCnt;
		
		// 전체 레코드 수 
	}
	
	public int getPage() {
		return page;
	}
	
	public int getTotal() {
		return total;
	}
	
	public int getTotalPages() {
		return totalPages;
	}
	
	public int getPrevPage() {
		return prevPage;
	}
	
	public int getNextPage() {
		return nextPage;
	}
	
	public int getLastPage() {
		return lastPage;
	}
	
	public int[] getPages() {
		return pages;
	}
}
