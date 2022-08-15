# 이미지 게시판 만들기 

#### src/main/java/controllers/board/ImagesController.java

```java
package controllers.board;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/board/images")
public class ImagesController extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		req.setAttribute("addJs", new String[] { "board/image_board"} );
		req.setAttribute("addCss", new String[] {"board/image_board"} );
		RequestDispatcher rd = req.getRequestDispatcher("/board/images.jsp");
		rd.forward(req, resp);
	}
}
```
- 이미지 전용 게시판의 css 및 js는 편의상 다음과 같이 따로 분리합니다.

```java
req.setAttribute("addJs", new String[] { "board/image_board"} );
req.setAttribute("addCss", new String[] {"board/image_board"} );
```

#### src/main/webapp/static/js/layer.js

```javascript
/**
 * 레이어 팝업
 * 
 */
const layer = {
    callback : function() {}, // popup 오픈 후 실행될 콜백 함수 
    /**
     * popup 오픈 후 실행될 콜백 함수 설정 
     * 
     * @param {Function} callback
     */
    setCallback : function(callback) {
        this.callback = callback;
        return this;
    },
    /**
     * 레이어 팝업 열기 
     * 
     * @param {String} url 팝업 URL
     * @param {String} title 팝업 제목 
     * @param {int} width 팝업 너비
     * @param {int} height 팝업 높이
     * @param {boolean} isIframe iframe 형태 사용여부
     */
    open(url, title, width, height, isIframe) {
        if (!url)
            return;

        width = width || 300;
        height = height || 300;
        isIframe = isIframe?true:false;

        var left = Math.round((window.innerWidth - width) / 2);
        var top = Math.round((window.innerHeight - height) / 2);

        var layerDim = document.createElement("div");
        layerDim.id = 'layer_dim';
        layerDim.addEventListener("click", layer.close);
        var layerPopup = document.createElement("div");
        layerPopup.id = 'layer_popup';
        layerPopup.style.width = width + "px";
        layerPopup.style.height = height + "px";
        layerPopup.style.top = top + "px";
        layerPopup.style.left = left + "px";

        /** 팝업 제목 처리 S */
        if (title) {
            var layerTitle = document.createElement("div");
            layerTitle.className = "popup_title";
            var titleNode = document.createTextNode(title);
            layerTitle.appendChild(titleNode);
            layerPopup.appendChild(layerTitle);
        }
        /** 팝업 제목 처리 E */

        /** 팝업 내용 영역 S */
        if (isIframe) {
            var h = height - 45;

            var layerContents = document.createElement("iframe");
            layerContents.src=url;
            layerContents.width=width;
            layerContents.height=h;
            layerContents.setAttribute('frameborder', 0);
            layerContents.setAttribute('scrolling', 'auto');
            layerContents.name = 'ifrmPopup';
            layerContents.id = 'ifrmPopup';
            layerPopup.appendChild(layerContents);
        } else {
            var layerContents = document.createElement("div");
            layerContents.className = "popup_html";
            layerPopup.appendChild(layerContents);
            
            var xhr = new XMLHttpRequest();
            xhr.open("GET", url);
            xhr.onreadystatechange = function() {
            if (xhr.readyState == XMLHttpRequest.DONE && xhr.status == 200) {
                layerContents.innerHTML = xhr.responseText;
                if (typeof layer.callback == 'function') {
                    typeof layer.callback();
                }
            }  
            };
            xhr.send(null);
        }
        /** 팝업 내용 영역 E */

        /** 팝업 닫기 버튼 S */
        var layerClose = document.createElement("i");
        layerClose.className = "xi-close-thin layer_close";
        layerPopup.appendChild(layerClose);
        layerClose.addEventListener("click", layer.close);
        /** 팝업 닫기 버튼 E */

        document.body.appendChild(layerDim);
        document.body.appendChild(layerPopup);
    },
    /**
     * 팝업 닫기
     * 
     */
    close() {
        var layerDim = document.getElementById("layer_dim");
        var layerPopup = document.getElementById("layer_popup");
        if (layerPopup) {
            document.body.removeChild(layerPopup);
        }

        if (layerDim) {
            document.body.removeChild(layerDim);
        }
    }
};

window.addEventListener("DOMContentLoaded", function() {
    /** 팝업 열기 처리 S */
    var layerPopups = document.getElementsByClassName("layer_popup")
    if (layerPopups.length > 0) {
        for (var i = 0; i < layerPopups.length; i++) {
            layerPopups[i].addEventListener("click", function(e) {
                var dataset = e.currentTarget.dataset;
                var excepts = ['width', 'height', 'url', 'title'];
                var url = dataset.url;
                var width = dataset.width || 300;
                var height = dataset.height || 300;
                var title = dataset.title;
                var qs = [];
                for (key in dataset) {
                    if (excepts.indexOf(key) != -1) 
                        continue;
                   
                    qs.push(key + "=" + dataset[key]);

                }
                if (qs.length > 0) {
                    if (url.indexOf("?") == -1) url += "?";
                    else url += "&";
                    url += qs.join("&");
                }
                
                layer.open(url, title, Number(width), Number(height));
            });

        }
    }
    /** 팝업 열기 처리 E */

    /** 팝업 닫기 처리 S */
    var layerCloses = document.getElementsByClassName("layer_close");
    if (layerCloses.length > 0) {
        for (var i = 0; i < layerCloses.length; i++) {
            layerCloses[i].addEventListener("click", layer.close);
        }
    }
    /** 답업 닫기 처리 E */
});
```
- 레이어 팝업 소스 구현

#### src/main/webapp/WEB-INF/tags/layouts/common.tag

```jsp
... 생략 

<!DOCTYPE html> 
<html>
	<head>
		
		.. 생략 
		
		 <script src="<c:url  value='/static/js/layer.js' />"></script>
		<script src="<c:url  value='/static/js/common.js' />"></script>

	</head>
	
	... 생략
	
</html>
```
- 레이어 팝업을 위한 layer.js 추가 

#### src/main/webapp/board/images.jsp

```jsp 
<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="layout" tagdir="/WEB-INF/tags/layouts" %>
<fmt:setBundle basename="bundle.imageboard" />
<fmt:message var="title" key="BOARD_TITLE" />
<layout:main title="${title}" bodyClass="image_board">
	<h1 class='mtitle'>${title}</h1>
	<div class='ar'>
		<button type="button" id="add_images"  class="sbtn">
			<i class='xi-plus'></i> <fmt:message key="ADD_IMAGES" />
		</button>
	</div>
	<div id="image_posts"></div>
</layout:main>
```


#### src/main/webapp/static/css/board/image_board.css

```css
#image_posts  { margin: 20px 0; }
#image_posts  .image_box { cursor: pointer; border: 1px solid #212121; margin-bottom: 20px; }
#image_posts  .image_box img { width: 100%; display: block; }
```

#### src/main/webapp/static/js/board/image_board.js

```javascript
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
```


#### src/main/webapp/file/upload.jsp

```
... 생략 

	<div class='stitle'><fmt:message key="FILE_SELECT" /></div>
	<div class='xi-plus' id='add_file'>
		<input type="file" name="file" id="file" multiple data-gid="${gid}"${empty param.isImageOnly ? "":" data-is-image-only='1'" }${empty param.uploadDone ? "":" data-upload-done='1'" }>
	</div>

... 생략

```
- 이미지 형식 파일로 제한 속성 추가

```
${empty param.isImageOnly ? "":" data-is-image-only='1'" }
```

- 업로드 처리 완료 후 완료처리 속성 추가

```
${empty param.uploadDone ? "":" data-upload-done='1'" }
```


#### src/main/webapp/static/js/file_upload.js 

```javascript
/**
* 파일 업로드 처리
* 
*/
const fileUpload = {
	
	/** 이벤트 처리  */
	handleEvent(e) {
	
		... 생략 
		
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
					
					... 생략
					
					for (file of data) {
					
						... 생략 
						
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
};
```

- 이미지 형식의 파일만 업로드로 제한할 수 있는 기능 추가

```
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
```

- 업로드 후 바로 완료 처리하는 경우 추가 

```javascript
/** 업로드 완료 처리 항목이 있다면 추가  */
if (el.dataset.isUploadDone) {
	formData.append("isUploadDone", true);
}		
```

- 파일 업로드 이후 부모(parent) 창에서 처리할 콜백 함수 처리 추가 

```javascript
/** 콜백 이벤트 처리 S */
if (typeof parent.fileUploadCallback == 'function') {
	parent.fileUploadCallback(data);
}
/** 콜백 이벤트 처리 E */
```

#### src/main/java/models/file/FileUploadService.java

- 파일 업로드 후속 처리가 필요 없는 경우는 바로 완료 처리

```java
... 생략

public class FileUploadService {
		
	public List<FileDto> upload(HttpServletRequest request) throws Exception  {
		
		... 생략 
		
		/** 파일 업로드 바로 완료 처리가 있는 경우 처리 S */
		if (requestParams.get("isUpdateDone") != null) {
			String gid = requestParams.get("gid");
			dao.updateDone(gid);
		}
		/** 파일 업로드 바로 완료 처리가 있는 경우 처리 E */
		
		return uploadedFiles;
	}
}

```

#### src/main/java/models/file/FileMapper.xml

```xml

... 생략 

<!-- 파일 목록 조회  -->
<select id="files" parameterType="models.file.FileDto" resultMap="fileMap">
	SELECT * FROM fileInfo WHERE gid=#{gid} ORDER BY id ASC;
</select>
	
<!--  파일 업로드 완료 목록 내림차순 조회 -->
<select id="filesDoneDESC" parameterType="models.file.FileDto" resultMap="fileMap">
	SELECT * FROM fileInfo WHERE gid=#{gid} AND isDone=1 ORDER BY id DESC
</select>

... 생략

```

#### src/main/java/models/file/FileDao.java

```java

... 생략 

public class FileDao {
	
	... 생략 
	
	/**
	 * 파일 그룹별 조회
	 * 
	 * @param {String} gid 그룹 ID 
	 * @return {List<FileDto>}
	 */
	public List<FileDto> gets(String gid) {
		SqlSession sqlSession = Connection.getSqlSession();
		
		FileDto param = new FileDto();
		param.setGid(gid);
		List<FileDto> files = sqlSession.selectList("FileMapper.files", param);
		
		sqlSession.close();
		
		return files;
		
	}
	
	/**
	 * 파일 업로드 완료 처리된 파일 목록 조회
	 * 
	 * @param {String} gid 그룹 ID 
	 * @return {List<FileDto>}
	 */
	public List<FileDto> getsDoneDesc(String gid) {
		SqlSession sqlSession = Connection.getSqlSession();
		
		FileDto param = new FileDto();
		param.setGid(gid);
		
		List<FileDto> files = sqlSession.selectList("FileMapper.filesDoneDESC", param);
		
		sqlSession.close();
		
		return files;
	}
	
	... 생략

}
```


#### src/main/java/models/board/images/ListService.java

```java
package models.board.images;

import java.util.List;
import models.file.FileDto;
import models.file.FileDao;

public class ListService {
	
	public List<FileDto> gets() {
		FileDao dao = FileDao.getIntance();
		List<FileDto> files = dao.getsDoneDesc("image_board");
		
		return files;
	}
}
```

#### src/main/java/controllers/board/ImagesController.java

```java

... 생략 

public class ImagesController extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		... 생략 
		
		/** 이미지 목록 조회 S */
		List<FileDto> images = new ListService().gets();
		req.setAttribute("images", images);
		/** 이미지 목록 조회 E */
		
		RequestDispatcher rd = req.getRequestDispatcher("/board/images.jsp");
		rd.forward(req, resp);
	}
}
```


#### src/main/webapp/WEB-INF/tags/utils/fileurl.tag

```java
<%@ tag description="파일 업로드 URL" pageEncoding="UTF-8" body-content="empty" %>
<%@ tag trimDirectiveWhitespaces="true" %>
<%@ tag import="java.io.File" %>
<%@ attribute name="id" type="java.lang.Integer" required="true" %>
<%
	String url = request.getContextPath() + File.separator + "static" + File.separator + "upload" + File.separator + String.valueOf(id % 10) + File.separator + "" + id;
	out.print(url);
%>
```

