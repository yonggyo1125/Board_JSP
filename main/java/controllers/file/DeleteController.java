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
