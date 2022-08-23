/**
* 파일 업로드 처리
* 
*/
const fileUpload = {
	
	/** 이벤트 처리  */
	handleEvent(e) {
		const el =  e.currentTarget;
		const files = el.files;
		const gid = el.dataset.gid || Date.now();
		
		const formData = new FormData();
		for (file of files) {
			formData.append("file", file);
		}
		
		formData.append("gid", gid);
		
		/**  이미지 전용 처리 S */
		if (el.dataset.isImageOnly) {
			formData.append("isImageOnly", true);
			try {
				for (file of files) {
					// 이미지가 아닌 파일이 있다면
					if (file.type.indexOf("image") == -1) {
						throw new Error(`이미지 형식의 파일만 업로드 가능합니다. - ${file.name}`);
					}
				}
			} catch (err) {
				alert(err.message);
				return;
			}
		}
		/**  이미지 전용 처리 E */
	
		/** 업로드 완료 처리 항목이 있다면 추가  */
		if (el.dataset.updateDone) {
			formData.append("isUpdateDone", true);
		}
		
		const xhr = new XMLHttpRequest();
		xhr.open("POST", location.pathname);
		xhr.addEventListener("readystatechange", function() {
			if (xhr.status == 200 && xhr.readyState == XMLHttpRequest.DONE) {
				/** 업로드 성공 시 업로드한 파일을 목록에 추가 S  */
				const data  = JSON.parse(xhr.responseText);
				const listEl = document.getElementById("uploaded_list");
				const tplHtml = document.getElementById("listTpl");
				if (data && ! data.error && data.length > 0 && listEl && tplHtml) {
					const domParser = new DOMParser();
					for (file of data) {
						let html = tplHtml.innerHTML;
						html = html.replace(/#\[id\]/g, file.id)
									.replace(/#\[fileName\]/g, file.fileName);
						const dom = domParser.parseFromString(html, "text/html");
						const liEl = dom.getElementsByTagName("li")[0];
						listEl.appendChild(liEl);
						
						/* 삭제 이벤트 처리 S */
						const deleteEl = liEl.querySelector(".delete");
						if (deleteEl) {
							deleteEl.addEventListener("click", fileUpload.delete);
						}
						/** 삭제 이벤트 처리 E */
						
						/** 콜백 이벤트 처리 S */
						if (typeof parent.fileUploadCallback == 'function') {
							parent.fileUploadCallback(data);
						}
						/** 콜백 이벤트 처리 E */
					}
				} // endif
				
				el.value = ""; 
			}
			/** 업로드 성공 시 업로드한 파일을 목록에 추가 E  */
		});
		
		xhr.send(formData);
	},
	/**
	* 파일 삭제 처리 
	*
	 */
	delete(e) {
		if (!confirm('정말 삭제하시겠습니까?')) {
			return;
		}
		
		try {
			const el = e.currentTarget;
			const id = el.dataset.id;
			const xhr = new XMLHttpRequest();
			const url = location.pathname.replace("upload", "delete")  + `?id=${id}`;
			xhr.open("GET", url);
			xhr.addEventListener("readystatechange", function() {
				if (xhr.status == 200 && xhr.readyState == XMLHttpRequest.DONE) {
					const data = JSON.parse(xhr.responseText);
					if (data instanceof Array) {
						const parentEl = el.parentElement;
						parentEl.parentElement.removeChild(parentEl);
					}
				}
			});
			xhr.send(null);
		} catch (err) {
			console.error(err);
		}		
	}
};

/** 이벤트 처리 */
window.addEventListener("DOMContentLoaded", function() {
	const addFileEl = document.getElementById("add_file");
	const fileEl = document.getElementById("file");
	if (addFileEl && fileEl) {
		addFileEl.addEventListener("click", function() {
			fileEl.click();
		});
	} // endif
	if (fileEl) {
		fileEl.addEventListener("change", fileUpload);
	} 
});