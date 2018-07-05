package com.maoding.core.util;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Transparency;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

/*******************************************************************************
 * 缩略图类（通用） 本java类能将jpg、bmp、png、gif图片文件，进行等比或非等比的大小转换。 具体使用方法
 * compressPic(大图片路径,生成小图片路径,大图片文件名,生成小图片文名,生成小图片宽度,生成小图片高度,是否等比缩放(默认为true))
 */
public class ThumbnailUtil {
	private File file = null; // 文件对象
	private String inputDir; // 输入图路径
	private String outputDir; // 输出图路径
	private String inputFileName; // 输入图文件名
	private String outputFileName; // 输出图文件名
	private int outputWidth = 100; // 默认输出图片宽
	private int outputHeight = 100; // 默认输出图片高
	private boolean proportion = true; // 是否等比缩放标记(默认为等比缩放)
	private int wh = 0;// 以宽高为基准等比缩放

	public ThumbnailUtil() { // 初始化变量
		inputDir = "";
		outputDir = "";
		inputFileName = "";
		outputFileName = "";
		outputWidth = 100;
		outputHeight = 100;
	}

	public void setInputDir(String inputDir) {
		this.inputDir = inputDir;
	}

	public void setOutputDir(String outputDir) {
		this.outputDir = outputDir;
	}

	public void setInputFileName(String inputFileName) {
		this.inputFileName = inputFileName;
	}

	public void setOutputFileName(String outputFileName) {
		this.outputFileName = outputFileName;
	}

	public void setOutputWidth(int outputWidth) {
		this.outputWidth = outputWidth;
	}

	public void setOutputHeight(int outputHeight) {
		this.outputHeight = outputHeight;
	}

	public void setWidthAndHeight(int width, int height) {
		this.outputWidth = width;
		this.outputHeight = height;
	}

	/*
	 * 获得图片大小 传入参数 String path ：图片路径
	 */
	public long getPicSize(String path) {
		file = new File(path);
		return file.length();
	}

	// 图片处理
	public String compressPic() {
		try {
			// 获得源文件
			file = new File(inputDir + inputFileName);
			if (!file.exists()) {
				return "";
			}
			// Image img = ImageIO.read(file);
			BufferedImage img = ImageIO.read(file);
			// 判断图片格式是否正确
			if (img.getWidth(null) == -1) {
				System.out.println(" can't read,retry!" + "<BR>");
				return "no";
			} else {
				int newWidth = 0;
				int newHeight = 0;
				// 判断是否是等比缩放
				if (this.proportion == true) {

					if (this.wh == 1) {
						newWidth = outputWidth;
						newHeight = (int) (((double) img.getHeight(null))
								/ ((double) img.getWidth(null)) * (double) outputWidth);
					} else if (this.wh == 2) {
						newWidth = (int) (((double) img.getWidth(null))
								/ ((double) img.getHeight(null)) * (double) outputHeight);
						newHeight = outputHeight;
					} else {
						// 为等比缩放计算输出的图片宽度及高度
						double rate1 = ((double) img.getWidth(null))
								/ (double) outputWidth;
						double rate2 = ((double) img.getHeight(null))
								/ (double) outputHeight;
						// 根据缩放比率大的进行缩放控制
						double rate = rate1 > rate2 ? rate1 : rate2;
						newWidth = (int) (((double) img.getWidth(null)) / rate);
						newHeight = (int) (((double) img.getHeight(null)) / rate);
					}
				} else {
					newWidth = outputWidth; // 输出的图片宽度
					newHeight = outputHeight; // 输出的图片高度
				}
				BufferedImage tag = new BufferedImage((int) newWidth,
						(int) newHeight, BufferedImage.TYPE_INT_RGB);

				Graphics2D g2d = tag.createGraphics();
				// 设置背景透明
				tag = g2d.getDeviceConfiguration().createCompatibleImage(
						newWidth, newHeight, Transparency.TRANSLUCENT);
				g2d.dispose();
				g2d = tag.createGraphics();
				Image from = img.getScaledInstance(newWidth, newHeight,
						Image.SCALE_AREA_AVERAGING);
				g2d.drawImage(from, 0, 0, null);
				g2d.dispose();
				ImageIO.write(tag, "png", new File(outputDir + outputFileName));

				/*
				 * Image.SCALE_SMOOTH 的缩略算法 生成缩略图片的平滑度的 优先级比速度高 生成的图片质量比较好 但速度慢
				 */
				// tag.getGraphics().drawImage(img.getScaledInstance(newWidth,
				// newHeight, Image.SCALE_SMOOTH), 0, 0, null);

				// FileOutputStream out = new FileOutputStream(outputDir +
				// outputFileName);
				// // JPEGImageEncoder可适用于其他图片类型的转换
				// JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
				// encoder.encode(tag);
				// out.close();

			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return "ok";
	}

	public String compressPic(String inputDir, String outputDir,
			String inputFileName, String outputFileName) {
		// 输入图路径
		this.inputDir = inputDir;
		// 输出图路径
		this.outputDir = outputDir;
		// 输入图文件名
		this.inputFileName = inputFileName;
		// 输出图文件名
		this.outputFileName = outputFileName;
		return compressPic();
	}

	/**
	 * 
	 * @Title: compressPic
	 * @Description: 生成缩略图处理方法
	 * @param @param inputDir 大图片路径
	 * @param @param outputDir 生成小图片路径
	 * @param @param inputFileName 大图片文件名
	 * @param @param outputFileName 生成小图片文名
	 * @param @param width 生成小图片宽度
	 * @param @param height 生成小图片高度
	 * @param @param wh 以宽或高为基准(1＝以宽为基准等比例缩放，2＝以高为基准等比例缩放,0=默认按输入的宽高等比例缩放以gp为准)
	 * @param @param gp 是否等比缩放(默认为true)
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 * @author idccapp26
	 * @datetime 2015年8月11日 上午11:48:26
	 */
	public String compressPic(String inputDir, String outputDir,
			String inputFileName, String outputFileName, int width, int height,
			int wh, boolean gp) {
		// 输入图路径
		this.inputDir = inputDir;
		// 输出图路径
		this.outputDir = outputDir;
		// 输入图文件名
		this.inputFileName = inputFileName;
		// 输出图文件名
		this.outputFileName = outputFileName;
		// 设置图片长宽
		setWidthAndHeight(width, height);
		// 是否是等比缩放 标记
		this.proportion = gp;
		this.wh = wh;
		return compressPic();
	}

	/**
	 * 方法描述：根据尺寸图片居中裁剪
	 */
	public void cutCenterImage(String src, String dest, int w, int h) {
		try {
			Iterator<ImageReader> iterator = ImageIO.getImageReadersByFormatName("png");
			ImageReader reader = (ImageReader) iterator.next();
			InputStream in = new FileInputStream(src);
			ImageInputStream iis = ImageIO.createImageInputStream(in);
			reader.setInput(iis, true);
			ImageReadParam param = reader.getDefaultReadParam();
			int imageIndex = 0;
			// System.out.println(reader.getWidth(imageIndex));
			int w1 = (reader.getWidth(imageIndex) - w) / 2;
			int h1 = (reader.getHeight(imageIndex) - h) / 2;
			if (w1 < 0) {
				w1 = 0;
			}
			if (h1 < 0) {
				h1 = 0;
			}
			Rectangle rect = new Rectangle(w1, h1, w, h);
			param.setSourceRegion(rect);
			BufferedImage bi = reader.read(0, param);
			ImageIO.write(bi, "png", new File(dest));
		} catch (Exception e) {
			new TxtFileUtil().copyFile(src, dest);
		}

	}

	/**
	 * 图片裁剪二分之一
	 */
	public static void cutHalfImage(String src, String dest) throws IOException {
		Iterator<ImageReader> iterator = ImageIO.getImageReadersByFormatName("png");
		ImageReader reader = (ImageReader) iterator.next();
		InputStream in = new FileInputStream(src);
		ImageInputStream iis = ImageIO.createImageInputStream(in);
		reader.setInput(iis, true);
		ImageReadParam param = reader.getDefaultReadParam();
		int imageIndex = 0;
		int width = reader.getWidth(imageIndex) / 2;
		int height = reader.getHeight(imageIndex) / 2;
		Rectangle rect = new Rectangle(width / 2, height / 2, width, height);
		param.setSourceRegion(rect);
		BufferedImage bi = reader.read(0, param);
		ImageIO.write(bi, "png", new File(dest));
	}

	/**
	 * 图片裁剪通用接口
	 */
	public static void cutImage(String src, String dest, int x, int y, int w,
			int h,String suffix) throws IOException {
		changePicSuffix(src);
		Iterator<ImageReader> iterator = ImageIO.getImageReadersByFormatName("png");
		ImageReader reader = (ImageReader) iterator.next();
		InputStream in = new FileInputStream(src);
		ImageInputStream iis = ImageIO.createImageInputStream(in);
		reader.setInput(iis, true);
		ImageReadParam param = reader.getDefaultReadParam();
		if(x<0){
			x=0;
		}
		if(y<0){
			y=0;
		}
		Rectangle rect = new Rectangle(x, y, w, h);
		param.setSourceRegion(rect);
		BufferedImage bi = reader.read(0, param);
		ImageIO.write(bi, "png", new File(dest));

	}

	/**
	 * 方法描述：图片格式转化
	 * 作        者：MaoSF
	 * 日        期：2016年4月12日-下午6:00:37
	 * @param inputDir
	 */
	public static void changePicSuffix(String inputDir){
		try {
			File file = new File(inputDir);
			BufferedImage img = ImageIO.read(file);
			BufferedImage tag = new BufferedImage( img.getWidth(),
					img.getHeight(), BufferedImage.TYPE_INT_RGB);

			Graphics2D g2d = tag.createGraphics();
			// 设置背景透明
			tag = g2d.getDeviceConfiguration().createCompatibleImage(
					img.getWidth(),img.getHeight(), Transparency.TRANSLUCENT);
			g2d.dispose();
			g2d = tag.createGraphics();
			Image from = img.getScaledInstance(img.getWidth(),img.getHeight(),
					Image.SCALE_AREA_AVERAGING);
			g2d.drawImage(from, 0, 0, null);
			g2d.dispose();
			ImageIO.write(tag, "png", new File(inputDir));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 图片缩放
	 */
	public static void zoomImage(String src, String dest, int w, int h)
			throws Exception {
		double wr = 0, hr = 0;
		File srcFile = new File(src);
		File destFile = new File(dest);
		BufferedImage bufImg = ImageIO.read(srcFile);
		Image Itemp = bufImg.getScaledInstance(w, h, Image.SCALE_SMOOTH);
		wr = w * 1.0 / bufImg.getWidth();
		hr = h * 1.0 / bufImg.getHeight();
		AffineTransformOp ato = new AffineTransformOp(
				AffineTransform.getScaleInstance(wr, hr), null);
		Itemp = ato.filter(bufImg, null);
		try {
			ImageIO.write((BufferedImage) Itemp,
					dest.substring(dest.lastIndexOf(".") + 1), destFile);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

}