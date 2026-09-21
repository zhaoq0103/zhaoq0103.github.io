package org.example.servlet;

import org.apache.tika.Tika;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@WebServlet(urlPatterns = {"/static/*","/favicon.ico"})
public class FileServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ServletContext ctx = req.getServletContext();
		// remove context-path:
		String urlPath = req.getRequestURI().substring(ctx.getContextPath().length());
		String filepath = ctx.getRealPath(urlPath);
		if (filepath == null) {
			resp.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		Path path = Paths.get(filepath);
		if (!path.toFile().isFile()) {
			resp.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
//		String mime = Files.probeContentType(path);
		String mime = new Tika().detect(path.toFile());
		if (mime == null) {
			mime = "application/octet-stream";
		}


		resp.setContentType(mime);
		OutputStream output = resp.getOutputStream();
		try (InputStream input = new BufferedInputStream(new FileInputStream(filepath))) {
//			input.transferTo(output);
			byte[] buffer = new byte[1024];
			for (;;) {
				int len = input.read(buffer);
				if (len == -1) {
					break;
				}
				output.write(buffer, 0, len);
			}
		}
		output.flush();
	}
}
