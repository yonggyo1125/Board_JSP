/**
* 이미지 게시판 업로드 및 삭제 기능 구현 
*
 */
const imageBoard = {
	/**
	* 이미지 업로드 팝업 노출 
	*
	*/
	showPopup(e) {		
		const gid = "image_board";
		layer.open(`../file/upload?gid=${gid}&isImageOnly=1&updateDone=1`, null, 350, 500, true);	
	},
	/**
	* 이미지 목록 출력
	*
	 */
	loadLists() {
		const el = document.getElementById("image_posts");
		if (!el) {
			return;
		}
		
		const xhr = new XMLHttpRequest();
		xhr.open("GET", "../board/images?isAjax=1");
		xhr.addEventListener("readystatechange", function() {
			if (xhr.status == 200 && xhr.readyState == XMLHttpRequest.DONE) {
				el.innerHTML = xhr.responseText;
				
				/** 이미지 더블 클릭시 삭제 처리  S */
				const images = document.querySelectorAll("#image_posts .image_box");
				for (const image of images) {
					image.addEventListener("dblclick", function(e) {
						if (!confirm('정말 삭제하시겠습니까?')) {
							return;
						}
						
						try {
							const el = e.currentTarget;
							const id = el.dataset.id;
							const xhr = new XMLHttpRequest();
							xhr.open("GET", `../file/delete?id=${id}`);
							xhr.addEventListener("readystatechange", function() {
								if (xhr.status == 200 && xhr.readyState == XMLHttpRequest.DONE) {
									const data = JSON.parse(xhr.responseText);
									if (data instanceof Array) {
										const parentEl = el.parentElement;
										parentEl.removeChild(el);
									}
									alert("삭제되었습니다.");
								}
							});
							xhr.send(null);
						} catch (err) {
							console.error(err);
						}		
					});
				}
				/** 이미지 더블 클릭시 삭제 처리  E */
			}
		});
		xhr.send(null);
	}
};


/**
* 이미지 업로드 후 콜백 처리 
* 
* @param {object} data 업로드 완료된 파일 정보
 */
function fileUploadCallback(data) {
	layer.close();
	imageBoard.loadLists();
} 

window.addEventListener("DOMContentLoaded", function() {
	/** 처음 로딩 시 이미지 목록 출력  */
	imageBoard.loadLists();
	
	const addImagesEl = document.getElementById("add_images");
	if (addImagesEl) {
		addImagesEl.addEventListener("click", imageBoard.showPopup);
	}
});
