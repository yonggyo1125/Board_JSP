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
