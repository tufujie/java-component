package com.jef.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StopWatch;
import org.springframework.util.StringUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;

/**
 * Created by kim on 16/8/30.
 * 图片压缩工具类
 */
public class ImageCompressUtil {

    private static final Logger log = LoggerFactory.getLogger(ImageCompressUtil.class);

    /*
     * 获得图片大小
     * 传入参数 String path ：图片路径  
     */
    public static long getImageSize(String path) {
        File file = new File(path);
        return file.length();
    }

    /**
     * 图片压缩处理
     * inputUrl 输入图片url
     * outputDir 输出图片路径
     * outputWidth 输出图片宽度
     * outputHeight 输出图片高度
     * proportion 是否等比缩放标记(默认为等比缩放)
     */
    private static void compress(String inputUrl, String outputDir, int outputWidth, int outputHeight, boolean proportion) throws Exception{
        FileOutputStream out = null;
        try {
            if (!StringUtils.hasText(inputUrl)) {
                return;
            }
            File file = new File(outputDir);
            if(file.exists()){
                return;
            }

            StopWatch stopWatchRead = new StopWatch();
            stopWatchRead.start();

            URL url = new URL(inputUrl);
            BufferedImage img = ImageIO.read(url);

            stopWatchRead.stop();
            log.debug("ImageIO read cost: {} s where url: {}",stopWatchRead.getTotalTimeSeconds(),inputUrl);

            // 判断图片格式是否正确
            if (img.getWidth(null) == -1) {
                return;
            }

            int newWidth; int newHeight;
            // 判断是否是等比缩放
            if (proportion == true) {
                // 为等比缩放计算输出的图片宽度及高度
                double rate1 = ((double) img.getWidth(null)) / (double) outputWidth;
                double rate2 = ((double) img.getHeight(null)) / (double) outputHeight;
                // 根据缩放比率大的进行缩放控制
                double rate = rate1 > rate2 ? rate1 : rate2;
                newWidth = (int) (((double) img.getWidth(null)) / rate);
                newHeight = (int) (((double) img.getHeight(null)) / rate);
            } else {
                newWidth = outputWidth; // 输出的图片宽度
                newHeight = outputHeight; // 输出的图片高度
            }

            BufferedImage bufferedImage = new BufferedImage((int) newWidth, (int) newHeight, BufferedImage.TYPE_INT_RGB);

            /*
             * Image.SCALE_SMOOTH 的缩略算法 生成缩略图片的平滑度的优先级比速度高 生成的图片质量比较好 但速度慢
             */
            StopWatch stopWatchGraphics = new StopWatch();
            stopWatchGraphics.start();

            Graphics graphics = bufferedImage.getGraphics();
            graphics.drawImage(img.getScaledInstance(newWidth, newHeight, Image.SCALE_FAST), 0, 0, null);
            graphics.dispose();

            stopWatchGraphics.stop();
            log.debug("graphics drawImage cost: {} s",stopWatchGraphics.getTotalTimeSeconds());

//            out = new FileOutputStream(outputDir);
            // JPEGImageEncoder可适用于其他图片类型的转换
//            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
//            encoder.encode(tag);


//            file.createNewFile();
            StopWatch stopWatchImageIOWrite = new StopWatch();
            stopWatchImageIOWrite.start();

            ImageIO.write(bufferedImage, "JPEG" , file );

            stopWatchImageIOWrite.stop();
            log.debug("ImageIO write cost: {} s", stopWatchGraphics.getTotalTimeSeconds());
        } finally {
            if(out != null) out.close();
        }
    }


    public static void compressImageWidth100(String inputUrl, String outputDir, boolean proportion) throws Exception{
        compressImage(inputUrl, outputDir, 100, 100, proportion);
    }

    public static void compressImage(String inputUrl, String outputDir,int outputWidth, int outputHeight, boolean proportion) throws Exception {
        compress(inputUrl, outputDir, outputWidth, outputHeight, proportion);
    }


    public static void main(String[] arg) {
        try{

            StopWatch stopWatch = new StopWatch();
            stopWatch.start();

            compressImageWidth100(
                    "https://contestimg.wish.com/api/webimage/57999883f88d8d1129cd524c-original.jpg?cache_buster=f311ea9e84d19a6f836de55e8485cffd",
                    "/Users/kim/dev/project/git/ae/scm-web/src/main/webapp/uploads/22248011-3.jpg", true);

            stopWatch.stop();
            System.out.println("总共用了：" + stopWatch.getTotalTimeSeconds() + "秒");
            System.out.println("输出的图片大小：" + getImageSize("/Users/kim/dev/project/git/ae/scm-web/src/main/webapp/uploads/22248011.jpg")/1024 + "KB");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
