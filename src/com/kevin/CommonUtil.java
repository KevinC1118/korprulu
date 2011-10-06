/**
 * 
 */
package com.kevin;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.google.appengine.api.datastore.Blob;

/**
 * @author Kevin.C
 */
public class CommonUtil {

	private static final Logger LOGGER = Logger.getLogger(CommonUtil.class
			.getName());

	public Blob toZip(List<GeneratedFile> files) {

		try {
			
			ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
			ZipOutputStream zos = new ZipOutputStream(arrayOutputStream);

			for (int i = 0, max = files.size(); i < max; i++) {

				GeneratedFile file = files.get(i);
				zos.putNextEntry(new ZipEntry(file.getName()));
				zos.write(file.getBlob().getBytes());

				zos.closeEntry();
			}

			arrayOutputStream.flush();
			zos.flush();
			arrayOutputStream.close();
			zos.close();

			Blob blob = new Blob(arrayOutputStream.toByteArray());

			return blob;

		} catch (IOException e) {
			LOGGER.warning(e.toString());
		}
		return null;
	}

	public List<GeneratedFile> separateFile(UploadedFile uploadedFile) {

		// New file list
		List<GeneratedFile> generatedFiles = new ArrayList<GeneratedFile>();

		// Read uploaded file
		Scanner scanner = new Scanner(new ByteArrayInputStream(uploadedFile
				.getBlob().getBytes()));

		int i = 0;
		while (scanner.hasNextLine()) {

			i++;

			String str = scanner.nextLine();

			// Use GeneratedFile bean to handle new file
			GeneratedFile file = new GeneratedFile();
			file.setBlob(new Blob(str.getBytes()));
			file.setName("Line" + Integer.toString(i) + ".txt");

			generatedFiles.add(file);
		}

		scanner.close();

		return generatedFiles;
	}
}
