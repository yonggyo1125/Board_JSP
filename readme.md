## 파일 업로드 

### 파일 업로드 관련 라이브러리 다운로드 
- [mvnrepository](https://mvnrepository.com/) 에서 [Apache Commons FileUpload](https://mvnrepository.com/artifact/commons-fileupload/commons-fileupload/1.4) 와 [Apache Commons IO](https://mvnrepository.com/artifact/commons-io/commons-io/2.11.0) jar 파일을 다운받아 WEB-INF/lib에 2개 jar 파일을 복사해서 넣어줍니다.


### 파일 업로드 컨트롤러 구성 

#### src/main/java/controllers/file/UploadController.java

- gid : 파일을 업로드할 경우 여러개를 묶어서 관리하는 경우가 있으므로 추가<br>(예 : 게시글 1개에 여러 이미지 또는 여러 첨부 이미지가 있을 수 있다. 이때는 게시글 1개를 그룹 ID로 지정하여 관리)

```java
package controllers.file;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/file/upload")
public class UploadController extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		/** 파일 그룹 ID S */
		String gid = req.getParameter("gid");
		if (gid == null) gid = "" + System.currentTimeMillis();
		req.setAttribute("gid", gid);
		/** 파일 그룹 ID E */
		
		// 추가 javascript 
		req.setAttribute("addJs", new String[] { "file_upload" });
		RequestDispatcher rd = req.getRequestDispatcher("/file/upload.jsp");
		rd.forward(req, resp);
	}
}
```

#### WEB-INF/tags/layouts/popup.tag

```jsp
<%@ tag description="팝업페이지 레이아웃" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="layout" tagdir="/WEB-INF/tags/layouts" %>
<%@ attribute name="title" type="java.lang.String" %>
<%@ attribute name="bodyClass" type="java.lang.String" %>
<layout:common title="${title}" bodyClass="${bodyClass}">
 	<jsp:body>
 		<jsp:doBody />
 	</jsp:body>
</layout:common>
```

#### webapp/file/upload.jsp

- 파일 업로드 양식(form)에는 반드시 method는 post방식이며 enctype은 multipart/form-data으로 지정해야 된다.

```jsp
<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="layout" tagdir="/WEB-INF/tags/layouts" %>
<fmt:setBundle basename="bundle.common" />
<layout:popup title="파일 업로드" bodyClass="file-upload">
	<div class='mtitle'><fmt:message key="FILE_UPLOAD" /></div>
	
	<div class='stitle'><fmt:message key="FILE_SELECT" /></div>
	<div class='xi-plus' id='add_file'>
		<input type="file" name="file" id="file" multiple data-gid="${gid}">
	</div>
		
	<%-- 업로드된 파일 목록 출력 --%>
	<ul id="uploaded_list"></ul>

	<script type="text/html" id="listTpl">
		<li>
			<a href="<c:url value="/file/download?id=" />#[id]">#[fileName]</a>
			<i class='delete xi-close' data-id="#[id]"></i>
		</li>
	</script>
</layout:popup>
```


#### webapp/static/jsp/file_upload.js

```javascript
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
						const liEl = dom.querySelector("li");
						listEl.appendChild(liEl);
					}
				} // endif
				
				el.value = ""; 
			}
			/** 업로드 성공 시 업로드한 파일을 목록에 추가 E  */
		});
		
		xhr.send(formData);
	},
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
```

#### WEB-INF/classes/application.properties

- 파일 업로드 최대 용량 추가 

```
... 생략 

# 최대 업로드 가능 파일 MB
max_file_upload_size=30 
```

#### WEB-INF/classes/bundle/common_ko.properties

```
# 공통
SITE_TITLE=사이트 제목
BAD_REQUEST=잘못된 요청입니다.

... 생략 

# 파일
FILE_UPLOAD=파일업로드
FILE_SELECT=파일선택
FILE_NOT_FOUND=파일을 찾지 못하였습니다.
FILE_PROCESS_ERROR=파일 처리중 오류가 발생하였습니다.
FILE_DOWNLOAD_FAILED=파일 다운로드 중 오류가 발생하였습니다.
```

#### WEB-INF/classes/bundle/common_en.properties

```
# 공통
SITE_TITLE=SITE TITLE
BAD_REQUEST=Bad Request

... 생략 

# 파일
FILE_UPLOAD=File Upload
FILE_SELECT=Select a file to upload.
FILE_NOT_FOUND=File not found.
FILE_PROCESS_ERROR=File Processing Error.
FILE_DOWNLOAD_FAILED=File Download Error.
```

#### src/main/java/models/file/FileDto.java

- 파일 업로드 전 DB 추가를 위한 DTO 클래스 추가

```java
package models.file;

import java.time.LocalDateTime;

public class FileDto {
	
	private int id; // 파일 등록번호
	private String gid; // 파일 그룹 ID
	private String fileName; // 파일명
	private String contentType; // 파일 유형
	private long fileSize; // 파일 사이즈
	private int isDone; // 파일 업로드 완료 여부 1 - 완료
	private LocalDateTime regDt; // 파일 등록일
	
	public FileDto() {}

	public FileDto(int id, String gid, String fileName, String contentType, long fileSize, byte isDone,
			LocalDateTime regDt) {
		this.id = id;
		this.gid = gid;
		this.fileName = fileName;
		this.contentType = contentType;
		this.fileSize = fileSize;
		this.isDone = isDone;
		this.regDt = regDt;
	}

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getGid() {
		return gid;
	}



	public void setGid(String gid) {
		this.gid = gid;
	}



	public String getFileName() {
		return fileName;
	}
	
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public String getContentType() {
		return contentType;
	}
	
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	
	public long getFileSize() {
		return fileSize;
	}
	
	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}
	
	public int getIsDone() {
		return isDone;
	}
	
	public void setIsDone(int isDone) {
		this.isDone = isDone;
	}
	
	public LocalDateTime getRegDt() {
		return regDt;
	}
	
	public void setRegDt(LocalDateTime regDt) {
		this.regDt = regDt;
	}
	
	@Override
	public String toString() {
		return "FileDto [id=" + id + ", gid=" + gid + ", fileName=" + fileName + ", contentType=" + contentType + ", fileSize="
				+ fileSize + ", isDone=" + isDone + ", regDt=" + regDt + "]";
	}
}
```

#### src/main/sql/fileInfo.sql

- 파일 정보 저장을 위한 fileInfo 테이블 생성

```
CREATE TABLE `fileinfo` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '파일등록번호',
  `fileName` VARCHAR(80) NOT NULL COMMENT '파일명',
  `contentType` VARCHAR(45) NOT NULL COMMENT '파일 유형',
  `isDone` TINYINT(1) NULL DEFAULT 0 COMMENT '파일 업로드 완료 여부 1 - 완료, 0 - 미완료',
  `regDt` DATETIME NULL DEFAULT NOW() COMMENT '파일 등록일'
  PRIMARY KEY (`id`));
```

#### src/main/java/mybatis/config/mybatis-config.xml
- FileMapper.xml 설정 추가

```xml
<mappers>
	<mapper resource="models/member/MemberMapper.xml" />
	<mapper resource="models/file/FileMapper.xml" />
</mappers>
```

#### src/main/java/mybatis/config/mybatis-dev-config.xml
- FileMapper.xml 설정 추가

```xml
<mappers>
	<mapper resource="models/member/MemberMapper.xml" />
	<mapper resource="models/file/FileMapper.xml" />
</mappers>
```


#### src/main/java/models/file/FileMapper.xml

- SQL 실행을 위한 Mapper 설정 작성

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="FileMapper">
	<resultMap id="fileMap" type="models.file.FileDto">
		<result property="id" column="id" />
		<result property="gid" column="gid" />
		<result property="fileName" column="fileName" />
		<result property="contentType" column="contentType" />
		<result property="fileSize" column="fileSize" />
		<result property="isDone" column="isDone" />
		<result property="regDt" column="regDt" />
	</resultMap>
	
	<!-- 파일 조회 -->
	<select id="file" parameterType="models.file.FileDto" resultMap="fileMap">
		SELECT * FROM fileInfo WHERE id=#{id};
	</select>
	
	<!-- 파일 목록 조회  -->
	<select id="files" parameterType="models.file.FileDto" resultMap="fileMap">
		SELECT * FROM fileInfo WHERE gid=#{gid} ORDER BY id ASC;
	</select>
	
	<!--  파일 등록 -->
	<insert id="register"
			parameterType="models.file.FileDto"
			useGeneratedKeys="true"
			keyProperty="id">
		INSERT INTO fileInfo (fileName, contentType) VALUES (#{fileName}, #{contentType});
	</insert>
	
	<!-- 파일 업로드 완료 처리  -->
	<update id="done" parameterType="models.file.FileDto">
		UPDATE fileInfo 
			SET 
				isDone = 1
		WHERE gid=#{gid};
	</update>
	
	<!--  파일 삭제 -->
	<delete id="delete" parameterType="models.file.FileDto">
		DELETE FROM fileInfo WHERE id=#{id};
	</delete>
</mapper>
```

#### src/main/java/models/file/FileDao.java

```java
package models.file;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import mybatis.Connection;

public class FileDao {
	
	private static FileDao instance = new FileDao();
	
	/**
	 * 파일 정보 등록 
	 * 
	 * @param {FileDto} file
	 * @return {FileDto}
	 */
	public FileDto register(FileDto file) {
		
		SqlSession sqlSession = Connection.getSqlSession();
		
		sqlSession.insert("FileMapper.register", file);
		
		sqlSession.commit();
		sqlSession.close();
		
		return file;
	}
	
	/**
	 * 파일 정보 조회
	 * 
	 * @param {int} id 파일 등록 번호 
	 * @return {FileDto}
	 */
	public FileDto get(int id) {
		SqlSession sqlSession = Connection.getSqlSession();
		
		FileDto param = new FileDto();
		param.setId(id);
		FileDto fileInfo = sqlSession.selectOne("FileMapper.file", param);
		
		sqlSession.close();
		
		return fileInfo;
	}
	
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
	 * 파일 그룹별 완료 처리 
	 * 
	 * @param {String} gid 파일 그룹 ID
	 */
	public void updateDone(String gid) {
		SqlSession sqlSession = Connection.getSqlSession();
		FileDto param = new FileDto();
		param.setGid(gid);
		param.setIsDone(1);
		sqlSession.update("FileMapper", param);
		sqlSession.commit();
		sqlSession.close();
	}
	
	/**
	 * 파일 삭제
	 * 
	 * @param {int} id 파일 등록번호
	 */
	public void delete(int id) {
		
		SqlSession sqlSession = Connection.getSqlSession();
		
		FileDto param = new FileDto();
		param.setId(id);

		sqlSession.delete("FileMapper.delete", param);
		
		sqlSession.commit();
		sqlSession.close();
	}
	
	/**
	 * 싱글톤
	 * 
	 * @return {FileDao}
	 */
	public static FileDao getIntance() {
		if (instance == null) {
			instance = new FileDao();
		}
		
		return instance;
	}
}
```

#### src/main/java/models/file/FileUploadService.java

- 파일 업로드 처리, 파일 정보를 fileInfo 테이블에 저장하고 정상 업로드된 파일 정로를 List\<FileDto\> 타입으로 반환 한다. 
- 이 반환값을 가지고 UploadController에서 JSON 형태로 출력한다.


```java
package models.file;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.io.File;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.*;
import org.apache.commons.fileupload.disk.*;
import org.apache.commons.fileupload.servlet.*;

/**
 * 파일 업로드 처리 
 * 
 * @author YONGGYO
 */
public class FileUploadService {
		
	public List<FileDto> upload(HttpServletRequest request) throws Exception  {
		
		String uploadPath = request.getServletContext().getRealPath(File.separator + "static" + File.separator  + "upload");
		
		File _uploadPath = new File(uploadPath);
		if (!_uploadPath.exists()) {
			_uploadPath.mkdir();
		}
		
		DiskFileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		
		/** 업로드 가능 최대 용량 설정 S */
		ResourceBundle application = ResourceBundle.getBundle("application");
		String _maxSize = application.getString("max_file_upload_size");
		int maxSize = _maxSize == null ? 30 : Integer.parseInt(_maxSize.trim()); // 파일 업로드 기본 최대 용량은 30MB
		
		upload.setSizeMax(1024*1024*maxSize); 
		/** 업로드 가능 최대 용량 설정 E */
		
		List<FileItem> items = upload.parseRequest(request);
		
		
		/** 일반 데이터 처리  S */  
		Map<String, String> requestParams = new HashMap<>();
		for(FileItem item : items) {
			String key = item.getFieldName();
			String value = item.getString("UTF-8");
			requestParams.put(key, value);
		}
		/** 일반 데이터 처리  E */
		
		/** 업로드 파일 데이터 처리 S */
		String isImageOnly = requestParams.get("isImageOnly");
		FileDao dao = new FileDao();
		List<FileDto> uploadedFiles = new ArrayList<>();
		for(FileItem item : items) {
			if (!item.isFormField()) { 
				String fileFieldName = item.getFieldName();
				String fileName = item.getName();
				String contentType = item.getContentType();
				fileName = fileName.substring(fileName.lastIndexOf(File.separator) + 1);
				long fileSize = item.getSize();
				
				/** 이미지 전용 업로드일때 이미지가 아닌 파일은 건너 뛰기 */
				if (isImageOnly != null && contentType.indexOf("image") != -1) {
					continue;
				}
				
				/** 파일 업로드 전 DB 처리 S */
				FileDto fileInfo = new FileDto();
				fileInfo.setGid(requestParams.get("gid"));
				fileInfo.setFileName(fileName);
				fileInfo.setContentType(contentType);
				fileInfo.setFileSize(fileSize);
				dao.register(fileInfo);
				
				int id = fileInfo.getId();
				if (id == 0) { // 파일 저장 실패한 경우 건너 뛰기
					continue;
				}
				/** 파일 업로드 전 DB 처리 E */
				
				String uploadDirPath = uploadPath + File.separator + "" + (id % 10);
				File _uploadDirPath = new File(uploadDirPath);
				if (!_uploadDirPath.exists()) {
					_uploadDirPath.mkdir();
				}
				
				File file = new File(uploadDirPath + File.separator + "" + id);
				item.write(file);
				
				uploadedFiles.add(fileInfo);
			}
		}
		/** 업로드 파일 데이터 처리 E */
		
		return uploadedFiles;
	}
}
```

#### src/main/java/controllers/file/UploadController.java

- FileUploadService의 update 메서드로 파일 업로드 처리 하고 그 결과를  JSON으로 출력
- JSON 출력을 위해서는 [mvnrepository](https://mvnrepository.com/) 에서 [Gson](https://mvnrepository.com/artifact/com.google.code.gson/gson/2.9.1) 의 jar 파일을 다운받아 WEB-INF/lib에 복사해 넣어줍니다.

```java
package controllers.file;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

... 생략

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import commons.LocalDateTimeSerializer;
import models.file.FileDto;
import models.file.FileUploadService;

@WebServlet("/file/upload")
public class UploadController extends HttpServlet {

	... 생략

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("application/json; charset=utf-8");
		PrintWriter out = resp.getWriter();
		
		 GsonBuilder gsonBuilder = new GsonBuilder();
         gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer());
         Gson gson = gsonBuilder.setPrettyPrinting().create();
		
		try {
			FileUploadService service = new FileUploadService();
			List<FileDto> uploadedFiles = service.upload(req);
			String json = gson.toJson(uploadedFiles);
			out.print(json);
		} catch (Exception e) {	
			e.printStackTrace();
			
			/** 업로드 실패한 경우 */
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("error", true);
			jsonObject.addProperty("message", e.getMessage());
			String json = gson.toJson(jsonObject);
			out.print(json);
		}
	}
}
```

- Gson은 기본적으로 Java 1.8 이후 추가된 java.time.LocalDateTime을 인식하지 못하는데 다음과 같이 추가하고 LocalDateTimeSerializer 클래스를 추가한다.

```java
gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer());
```


#### src/main/java/commons/LocalDateTimeSerializer.java

```java
package commons;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeSerializer implements JsonSerializer<LocalDateTime> {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public JsonElement serialize(LocalDateTime localDateTime, Type srcType, JsonSerializationContext context) {
        return new JsonPrimitive(formatter.format(localDateTime));
    }
}
```



* * * 
## 파일 다운로드

#### src/main/java/controllers/file/DownloadController.java

```java
package controllers.file;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.file.FileDownloadService;
import static commons.Utils.*;

@WebServlet("/file/download")
public class DownloadController extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			FileDownloadService service = new FileDownloadService();
			service.download(req, resp);
		} catch (RuntimeException e) {
			e.printStackTrace();
			alertError(resp, e);
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
}
```

#### src/main/java/models/file/FileDownloadService.java

```java
package models.file;

import java.io.*;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.file.FileDto;
import models.file.FileDao;
import models.file.FileNotFoundException;
import models.file.FileException;
import commons.BadRequestException;

public class FileDownloadService {
	
	public void download(HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("id");
		if (id == null) {
			throw new BadRequestException();
		}
		/** 등록 파일 정보 조회 S */
		FileDao dao = FileDao.getIntance();
		FileDto fileInfo = dao.get(Integer.parseInt(id.trim()));
		if (fileInfo == null) {
			new FileNotFoundException();
		}
		
		/** 등록 파일 정보 조회 E */
		
		/** 파일 존재 여부 체크 S */
		String path = File.separator + "static" + File.separator + "upload" + File.separator + "" + (fileInfo.getId() % 10) + File.separator + "" + fileInfo.getId();
		String filePath = request.getServletContext().getRealPath(path);
		File file = new File(filePath);
		if (!file.exists()) {
			new FileNotFoundException();
		}
		/**파일 존재 여부 체크 E */
		try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file))) {
			OutputStream out = response.getOutputStream();
			response.setHeader("Content-Description", "File Transfer");
			response.setHeader("Content-Disposition", "attachment; filename=" + new String(fileInfo.getFileName().getBytes(), "ISO8859_1"));
			response.setHeader("Content-Type", "application/octet-stream");
			response.setIntHeader("Expires", 0);
			response.setHeader("Cache-Control", "must-revalidate");
			response.setHeader("Pragma", "public");
			response.setHeader("Content-Length", String.valueOf(file.length()));
			
			int i;
			while((i = bis.read()) != -1) {
				out.write(i);
			}
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
			throw new FileException(ResourceBundle.getBundle("bundle.common").getString("FILE_DOWNLOAD_FAILED"));
		}
	}
}
```

#### src/main/java/models/file/FileException.java

```java
package models.file;

import java.util.ResourceBundle;

public class FileException extends RuntimeException {
	protected static ResourceBundle bundle =  ResourceBundle.getBundle("bundle.common");
	
	public FileException() {
		this(bundle.getString("FILE_PROCESS_ERROR"));
	}
	
	public FileException(String message) {
		super(message);
	}
}
```

#### src/main/java/models/file/FileNotFoundException.java

```java
package models.file;

public class FileNotFoundException extends FileException {
	
	public FileNotFoundException() {
		this(bundle.getString("FILE_NOT_FOUND"));
	}
	
	public FileNotFoundException(String message) {
		super(message);
	}
	
}
```

#### src/main/java/commons/BadRequestException.java

```java
package commons;

import java.util.ResourceBundle;

public class BadRequestException extends RuntimeException {
	
	private static ResourceBundle bundle =  ResourceBundle.getBundle("bundle.common");

	public BadRequestException() {
		this(bundle.getString("BAD_REQUEST"));
	}
	
	public BadRequestException(String message) {
		super(message);
	}
}
```

* * * 
## 파일 삭제

#### src/main/java/controllers/file/DeleteController.java

- 삭제 처리는 FileDeleteService의 delete에서 진행하여 삭제가 완료되면 삭제된 파일 등록번호를 반환한다.
- 반환된 삭제처리된 등록번호는 JSON 형태로 출력해 준다.


```java
package controllers.file;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import models.file.FileDeleteService;

@WebServlet("/file/delete")
public class DeleteController extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("application/json; charset=utf-8");
		PrintWriter out = resp.getWriter();
		
		GsonBuilder gsonBuilder = new GsonBuilder();
		Gson gson = gsonBuilder.setPrettyPrinting().create();
		
		try {
			FileDeleteService service = new FileDeleteService();
			List<Integer> deleteIds = service.delete(req);
			String json = gson.toJson(deleteIds);
			out.print(json);
		} catch (RuntimeException e) {
			e.printStackTrace();
			
			/** 업로드 실패한 경우 */
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("error", true);
			jsonObject.addProperty("message", e.getMessage());
			String json = gson.toJson(jsonObject);
			out.print(json);
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
}

```

#### src/main/java/models/file/FileDeleteService.java

- 파일 삭제는 파일 등록번호를 가지고 fileInfo 테이블에서 파일 정보를 삭제하고
- 실제 업로드된 파일을 삭제하는 방식으로 구현한다. 

```java
package models.file;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import models.file.FileDto;
import models.file.FileDao;
import commons.BadRequestException;

/**
 * 파일 삭제
 *  
 * @author YONGGYO
 *
 */
public class FileDeleteService {
	
	public List<Integer> delete(HttpServletRequest request) {
		
		String[] ids = request.getParameterValues("id");
		if (ids == null || ids.length == 0) {
			throw new BadRequestException();
		}
		
		String uploadPath = request.getServletContext().getRealPath(File.separator + "static" + File.separator  + "upload");
		
		FileDao dao = FileDao.getIntance();
		List<Integer> deletedIds = new ArrayList<>();
		for (String _id : ids) {
			int id = Integer.parseInt(_id.trim());
			FileDto fileInfo = dao.get(id);
			String uploadFilePath = uploadPath + File.separator + "" + (id % 10) + File.separator + "" + id;
			File file = new File(uploadFilePath);
			if (fileInfo == null) {
				continue;
			}
			
			if (file.exists()) {
				file.delete();
			}
			
			dao.delete(id);
			deletedIds.add(id);
		}
		
		return deletedIds;
	}
}
```

- 파일 등록번호는 여러 파일을 한꺼번에 삭제할수 있으므로 요청 파라미터로 전달된 id 값은 다음과 같은 방식으로 가져온다.

```java
String[] ids = request.getParameterValues("id");
```

#### webapp/static/js/file_upload.js

```javascript 
/**
* 파일 업로드 처리
* 
*/
const fileUpload = {
	
	/** 이벤트 처리  */
	handleEvent(e) {
		
		... 생략 
		
		xhr.addEventListener("readystatechange", function() {
			if (xhr.status == 200 && xhr.readyState == XMLHttpRequest.DONE) {
				
				... 생략
				
				if (data && ! data.error && data.length > 0 && listEl && tplHtml) {
						
						... 생략 
						
						const dom = domParser.parseFromString(html, "text/html");
						const liEl = dom.getElementsByTagName("li")[0];
						listEl.appendChild(liEl);
						
						/* 삭제 이벤트 처리 S */
						const deleteEl = liEl.querySelector(".delete");
						if (deleteEl) {
							deleteEl.addEventListener("click", fileUpload.delete);
						}
						/** 삭제 이벤트 처리 E */
					}
				} // endif
				
				el.value = ""; 
			}
			
		...  생략 
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

... 생략

```