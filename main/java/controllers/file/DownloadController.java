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
