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
