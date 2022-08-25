package commons;

import java.util.List;
import java.util.ArrayList;

public class Pagination {
	
	private int page; // 현재 페이지
	private int total;  // 전체 레코드 수
	private int prev; // 전 구간 마지막 페이지 
	private int next; // 다음 구간 시작 페이지 
	private int lastPage; // 마지막 페이지
	private int pageCnt; // 구간별 페이지 갯수
	private boolean isFirstCnt; // 첫 번째 구간 여부
	private boolean isLastCnt; // 마지막 구간 여부 
	private List<Integer> pages; // 구간별 페이지 번호
		
	/**
	 * 페이지 계산
	 * 
	 * @param {int} page : 현재 페이지  
	 * @param {int} total : 전체 레코드 수 
	 * @param {int} pageCnt : 페이지 구간 갯수  
	 */
	public Pagination(int page, int total, int pageCnt) {
		if (total < 1) {
			return;
		}
		this.total = total;
		this.pageCnt = pageCnt < 1 ? 10 : pageCnt; // 페이지 구간이 0 이하 인 경우는 기본값 10 지정
		this.lastPage = (int)Math.ceil(total / (double)this.pageCnt);  // 마지막 페이지 
		page = page < 1 ? 1 : page; // 페이지가 0 이하 일 경우 1로 고정
		if (page > this.lastPage) page = this.lastPage; // 페이지가 마지막 페이지보다 크다면 마지막 페이지로 고정
		this.page = page;
		pages = new ArrayList<>();
		
		/** 페이지 구간 구하기 S */
		int cnt = (int)Math.ceil(this.page / (double)this.pageCnt) - 1; // 현재 페이지 구간 번호
		int lastCnt = (int)Math.ceil(this.lastPage / (double)this.pageCnt) - 1; // 마지막 페이지 구간 번호

		if (cnt == 0) this.isFirstCnt = true;  // 첫번째 페이지 구간 체크
		if (cnt == lastCnt) this.isLastCnt = true; // 마지막 페이지 구간 체크
		/** 페이지 구간 구하기 E */
		
		/** 구간별 페이지 번호 S */
		int start = cnt * this.pageCnt + 1;
		for (int i = start; i <  start + this.pageCnt; i++) {
			pages.add(i);
			
			if (i == this.lastPage) { // 마지막 페이지에서 멈춤  
				break;
			}
		}
		/** 구간별 페이지 번호 E */
		
		/** 전 구간 마지막 페이지 S */
		if (!this.isFirstCnt) {
			prev = cnt * this.pageCnt;
		}
		/** 전 구간 마지막 페이지  E */
		/** 다음 구간 시작 페이지 S */
		if (!this.isLastCnt) {
			next = (cnt + 1)  * this.pageCnt + 1; 
		}
		/** 다음 구간 시작 페이지  E */
	}
	
	public Pagination(int page, int total) {
		this(page, total, 10);
	}

	public int getPage() {
		return page;
	}

	public int getTotal() {
		return total;
	}

	public int getPrev() {
		return prev;
	}

	public int getNext() {
		return next;
	}

	public int getLastPage() {
		return lastPage;
	}

	public int getPageCnt() {
		return pageCnt;
	}

	public boolean isFirstCnt() {
		return isFirstCnt;
	}

	public boolean isLastCnt() {
		return isLastCnt;
	}

	public List<Integer> getPages() {
		return pages;
	}

	@Override
	public String toString() {
		return "Pagination [page=" + page + ", total=" + total + ", prev=" + prev
				+ ", next=" + next + ", lastPage=" + lastPage + ", pageCnt=" + pageCnt + ", isFirstCnt=" + isFirstCnt
				+ ", isLastCnt=" + isLastCnt + ", pages=" + pages + "]";
	}
}
