package com.sic.util;

import java.io.File;

public class AttachmentsMail {
	
	private String fileName;
	
	private File file;

	public AttachmentsMail(String fileName, File file) {
		super();
		this.fileName = fileName;
		this.file = file;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}
	
	
}
