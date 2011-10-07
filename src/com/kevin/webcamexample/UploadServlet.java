package com.kevin.webcamexample;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Kevin.C
 * 
 */

public class UploadServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4686314267448696575L;


	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		//			// Receive uploaded file
//			ServletFileUpload fileUpload = new ServletFileUpload();
//			FileItemIterator itemIterator = fileUpload.getItemIterator(req);
//			FileItemStream itemStream = itemIterator.next();
////			InputStream stream = itemStream.openStream();	
		resp.getWriter().write(req.getParameter("d"));
	}

}
