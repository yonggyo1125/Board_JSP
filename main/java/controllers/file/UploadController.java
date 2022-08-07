package controllers.file;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import commons.LocalDateTimeSerializer;
import models.file.FileDto;
import models.file.FileUploadService;

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
