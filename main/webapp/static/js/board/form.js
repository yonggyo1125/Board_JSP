const boardForm = {
	/**
	* 파일 업로드 팝업 
	*
	* @param mode :  mode값이 image이면 그룹ID_image 형태로 아니면 그룹ID_attached로 생성 
	*/
	uploadPopup(mode) {
		const gidEl = document.querySelector("input[name='gid']");
		if (!gidEl || gidEl.value.trim() == "") {
			return;
		}
		let gid = gidEl.value.trim();
		gid = mode == 'image' ? `${gid}_image`:`${gid}_attached`;
		let url = `../file/upload?gid=${gid}`;
		if (mode == 'image') {
			url += "&isImageOnly=1";
		}
		
		layer.open(url, null, 360, 500, true);
	},
	/**
	* 파일 삭제 처리 
	*
	 */
	delete(e) {
		if (!confirm('정말 삭제하시겠습니까?')) {
			return;
		}
		
		const target = e.currentTarget;
		const id = target.dataset.id;
		const parentEl = target.parentElement;
		
		const xhr = new XMLHttpRequest();
		const url = `../file/delete?id=${id}`;
		xhr.open("GET", url);
		xhr.addEventListener("readystatechange", function() {
			if (xhr.status == 200 && xhr.readyState == XMLHttpRequest.DONE) {
				const result = JSON.parse(xhr.responseText);
				if (result.error) {
					alert(result.message);
					return;
				}
				
				parentEl.parentElement.removeChild(parentEl);
			}
		});
		xhr.send(null);		
		
	}
};

/** 이벤트 처리 S */
window.addEventListener("DOMContentLoaded", function() {
	/** CK 에디터 로드 */
	CKEDITOR.replace("content", { height: 350 });
	
	/** 이미지 추가 버튼 클릭 처리 S */
	const addImagesEl = document.getElementById("add_images");
	if (addImagesEl) {
		addImagesEl.addEventListener("click", function() {
			boardForm.uploadPopup("image");
		});
	}
	/** 이미지 추가 버튼 클릭 처리 E */
	/** 파일 추가 버튼 클릭 처리 S */
	const addFilesEl = document.getElementById("add_files");
	if (addFilesEl) {
		addFilesEl.addEventListener("click", function() {
			boardForm.uploadPopup();
		});
	}
	/** 파일 추가 버튼 클릭 처리 E */
	
	/** 파일 삭제 버튼 클릭 처리 S */
	const removeEls = document.querySelectorAll(".attach_images .remove, .attach_files .remove");
	for (el of removeEls) {
		el.addEventListener("click", boardForm.delete);
	}
	/** 파일 삭제 버튼 클릭 처리 E */
});
/** 이벤트 처리 E */

/** 파일 업로드 콜백 S */
function fileUploadCallback(files) {
	if (files && files.length > 0) {
		
		const domParser = new DOMParser();
		const tplFile = document.getElementById("tpl_file").innerHTML;
		const imageURI = "../static/upload/";
		const attachImagesEl = document.querySelector(".attach_images");
		 const attachFilesEl = document.querySelector(".attach_files");
		for (file of files) {
			
			let html = tplFile;
			html = html.replace(/#\[id\]/g, file.id)
							.replace(/#\[fileName\]/g, file.fileName);
			const dom = domParser.parseFromString(html, "text/html");
			const liEl = dom.querySelector("li");
			const removeEl = liEl.querySelector(".remove");
			/** 파일 삭제 이벤트 바인딩 S */
			if (removeEl) {
				removeEl.addEventListener("click", boardForm.delete);
			}
			/** 파일 삭제 이벤트 바인딩 E */
			
			if (file.gid.indexOf("image") != -1) { // 이미지 파일 
				const url = `${imageURI}${file.id % 10}/${file.id}`;
				const img = `<img src='${url}' style='max-width: 700px;' />`;
				CKEDITOR.instances.content.insertHtml(img);
				attachImagesEl.appendChild(liEl);
			} else {
				attachFilesEl.appendChild(liEl);
			}
		}
	} // endif 
	
	layer.close();
}
/** 파일 업로드 콜백 E */