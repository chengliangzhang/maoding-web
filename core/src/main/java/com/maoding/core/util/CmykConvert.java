/**
 * 深圳市设计同道技术有限公司
 * 类    名：CmykConvert
 * 类描述：
 * 作    者：wangrb
 * 日    期：2016年1月20日-下午4:33:33
 */

package com.maoding.core.util;

/**深圳市设计同道技术有限公司
 * 类    名：CmykConvert
 * 类描述：
 * 作    者：wangrb
 * 日    期：2016年1月20日-下午4:33:33
 */

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;

public class CmykConvert {
    private static BufferedImage createJPEG4(Raster raster) {
        int w = raster.getWidth();
        int h = raster.getHeight();
        byte[] rgb = new byte[w * h * 3];
        //彩色空间转换
        float[] Y = raster.getSamples(0, 0, w, h, 0, (float[]) null);
        float[] Cb = raster.getSamples(0, 0, w, h, 1, (float[]) null);
        float[] Cr = raster.getSamples(0, 0, w, h, 2, (float[]) null);
        float[] K = raster.getSamples(0, 0, w, h, 3, (float[]) null);
        for (int i = 0, imax = Y.length, base = 0; i < imax; i++, base += 3) {
            float k = 220 - K[i], y = 255 - Y[i], cb = 255 - Cb[i],
                    cr = 255 - Cr[i];

            double val = y + 1.402 * (cr - 128) - k;
            val = (val - 128) * .65f + 128;
            rgb[base] = val < 0.0 ? (byte) 0 : val > 255.0 ? (byte) 0xff
                    : (byte) (val + 0.5);

            val = y - 0.34414 * (cb - 128) - 0.71414 * (cr - 128) - k;
            val = (val - 128) * .65f + 128;
            rgb[base + 1] = val < 0.0 ? (byte) 0 : val > 255.0 ? (byte) 0xff
                    : (byte) (val + 0.5);

            val = y + 1.772 * (cb - 128) - k;
            val = (val - 128) * .65f + 128;
            rgb[base + 2] = val < 0.0 ? (byte) 0 : val > 255.0 ? (byte) 0xff
                    : (byte) (val + 0.5);
        }
        raster = Raster.createInterleavedRaster(new DataBufferByte(rgb, rgb.length), w, h, w * 3, 3, new int[]{0, 1, 2}, null);
        ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_sRGB);
        ColorModel cm = new ComponentColorModel(cs, false, true, Transparency.OPAQUE, DataBuffer.TYPE_BYTE);
        return new BufferedImage(cm, (WritableRaster) raster, true, null);
    }

    public static void main(String[] args) throws IOException {
        String filename = "C:/Users/Public/Pictures/Sample Pictures/Redocn_20140911104831321cmyk.jpg";

        CmykConvert cm = new CmykConvert();
        cm.readImage(filename);

    }

    public void readImage(String filename) throws IOException {
        File file = new File(filename);
        BufferedImage img = ImageIO.read(file);
        String newfilename = filename.substring(0, filename.lastIndexOf(".")) + ".jpg";
        File newFile = new File(newfilename);
        ImageIO.write(img, "jpg", newFile);
        img.flush();
        img = null;
    }
}

