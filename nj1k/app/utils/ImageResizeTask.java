package utils;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.Callable;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import play.mvc.Http.MultipartFormData.FilePart;

public class ImageResizeTask implements Callable<byte[]> {

	private static final Logger logger = LoggerFactory.getLogger(ImageResizeTask.class);
	private static final int IMAGE = 1024;
	private static final int THUMBNAIL = 256;
	
	private FilePart filePart;
	private int x;
	private int y;
	private int w;
	private int h;
	private int size;
	
	public ImageResizeTask(FilePart filePart, int x, int y, int x2, int y2, int w, int h, boolean thumbnail) {
		this.filePart = filePart;
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		
		if (thumbnail) {
			this.size = THUMBNAIL;
		} else {
			this.size = IMAGE;
		}
	}
	
	public ImageResizeTask(FilePart filePart) {
		this.filePart = filePart;
		this.size = IMAGE;
	}
	
	@Override
	public byte[] call() throws Exception {
		
		String fileType = filePart.getFilename().substring(filePart.getFilename().lastIndexOf(".") + 1);

		BufferedImage image = ImageIO.read(filePart.getFile());
		ColorSpace colorSpace = image.getGraphics().getColor().getColorSpace();

		image = image.getSubimage(x, y, w, h);
		
		int width = image.getWidth(null);
		int height = image.getHeight(null);
		double ratio = (double) height / (double) width;
		
		int newWidth = 0;
		int newHeight = 0;
		
		if (width > height) {
			newWidth = (int) (size / ratio);
			newHeight = size;
		} else {
			newHeight = (int) (size / ratio);
			newWidth = size;
		}
		
		BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, colorSpace.getType());
		Graphics2D graphics = resizedImage.createGraphics();
		
		try {
			graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
			graphics.drawImage(image, 0, 0, newWidth, newHeight, null);
		}
		finally {
			graphics.dispose();
		}
		
		logger.debug("Original width: {} Original height: {}", width, height);
		logger.debug("New width: {} New height: {}", resizedImage.getWidth(null), resizedImage.getHeight(null));
		return convert(resizedImage, fileType);
	}
	
	private byte[] convert(BufferedImage image, String fileType) throws IOException {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ImageIO.write(image, fileType, outputStream);
		return outputStream.toByteArray();
	}
}
