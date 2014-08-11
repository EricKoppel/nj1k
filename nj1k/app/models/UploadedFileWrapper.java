package models;

import java.util.ArrayList;
import java.util.List;

public class UploadedFileWrapper {

	private List<UploadedFile> files = new ArrayList<UploadedFile>();

	public void add(String name, String size, String url, String thumbnailUrl, String deleteUrl, String deleteType) {
		files.add(new UploadedFile(name, size, url, thumbnailUrl, deleteUrl, deleteType));
	}
	
	
	public List<UploadedFile> getFiles() {
		return files;
	}

	class UploadedFile {

		public UploadedFile(String name, String size, String url, String thumbnailUrl, String deleteUrl, String deleteType) {
			super();
			this.name = name;
			this.size = size;
			this.url = url;
			this.thumbnailUrl = thumbnailUrl;
			this.deleteUrl = deleteUrl;
			this.deleteType = deleteType;
		}

		private String name;
		private String size;
		private String url;
		private String thumbnailUrl;
		private String deleteUrl;
		private String deleteType;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getSize() {
			return size;
		}

		public void setSize(String size) {
			this.size = size;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public String getThumbnailUrl() {
			return thumbnailUrl;
		}

		public void setThumbnailUrl(String thumbnailUrl) {
			this.thumbnailUrl = thumbnailUrl;
		}

		public String getDeleteUrl() {
			return deleteUrl;
		}

		public void setDeleteUrl(String deleteUrl) {
			this.deleteUrl = deleteUrl;
		}

		public String getDeleteType() {
			return deleteType;
		}

		public void setDeleteType(String deleteType) {
			this.deleteType = deleteType;
		}

	}
}
