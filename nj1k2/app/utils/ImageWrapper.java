package utils;

import models.ImageEntity;
import play.mvc.Http.MultipartFormData.FilePart;

public class ImageWrapper<T extends ImageEntity> {

	private final FilePart filePart;
	private final String caption;
	private final Class<T> clazz;

	public ImageWrapper(FilePart filePart, Class<T> clazz) {
		this.filePart = filePart;
		this.clazz = clazz;
		this.caption = null;
	}

	public ImageWrapper(FilePart filePart, String caption, Class<T> clazz) {
		this.filePart = filePart;
		this.caption = caption;
		this.clazz = clazz;
	}

	public FilePart getFilePart() {
		return filePart;
	}

	public String getCaption() {
		return caption;
	}

	public Class<T> getClazz() {
		return clazz;
	}
}
