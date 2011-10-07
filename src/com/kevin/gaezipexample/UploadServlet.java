package com.kevin.gaezipexample;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.google.appengine.api.datastore.Blob;

/**
 * @author Kevin.C
 * 
 */

public class UploadServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4686314267448696575L;
	private static final Logger LOGGER = Logger.getLogger(UploadServlet.class
			.getName());

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		try {
			
			// Receive uploaded file
			ServletFileUpload fileUpload = new ServletFileUpload();
			FileItemIterator itemIterator = fileUpload.getItemIterator(req);
			FileItemStream itemStream = itemIterator.next();
			InputStream stream = itemStream.openStream();

			byte[] bs = new byte[8192];
			ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();

			int len;
			while ((len = stream.read(bs, 0, bs.length)) != -1) {
				arrayOutputStream.write(bs, 0, len);
			}
			
			// Save file as byte array into blob object
			Blob blob = new Blob(arrayOutputStream.toByteArray());
			stream.close();
			arrayOutputStream.close();
			
			// UploadedFile bean object
			UploadedFile uploadedFile = new UploadedFile();
			uploadedFile.setName(itemStream.getName());
			uploadedFile.setBlob(blob);

			CommonUtil util = new CommonUtil();
			
			// Separate text file into one line per file.
			List<GeneratedFile> generatedFiles = util.separateFile(uploadedFile);
			// Compress into zip file.
			Blob zipFile = util.toZip(generatedFiles);
			
			// Set response content type
			resp.setContentType("application/octet-stream");
			// Set response header
			resp.setHeader("Content-Disposition",
					"attachment; filename=\"download.zip\"");
			
			// return to download
			resp.getOutputStream().write(zipFile.getBytes());

		} catch (FileUploadException e) {
			LOGGER.warning(e.toString());
		}
	}

}
