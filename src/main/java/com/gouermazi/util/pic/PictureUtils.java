package com.gouermazi.util.pic;

import net.coobird.thumbnailator.Thumbnails;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * created by tab chan on 2017/12/06
 */
public class PictureUtils {
//    private static final Logger LOGGER = LoggerFactory.getLogger(PictureUtils.class);

    /**
     * @param bigPath   底图
     * @param smallPic  填充的图
     * @param finalPath 填充后的图片最终保存的位置
     * @param rectangle 填充区域
     */
    public static void overlapImage(String bigPath, BufferedImage smallPic, String mergedPicExt, String finalPath, Rectangle rectangle) {
        try {
            int x = (int) rectangle.getX();  //左起的x坐标
            int y = (int) rectangle.getY();  //左起的y坐标
            int w = (int) rectangle.getWidth(); //填充图片区域的宽度
            int h = (int) rectangle.getHeight(); //填充图片区域的高度
            BufferedImage source = ImageIO.read(new File(bigPath));
            Graphics g = source.getGraphics();
            g.drawImage(smallPic, x, y, w, h, null);
            g.dispose();
            ImageIO.write(source, mergedPicExt, new File(finalPath));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 整个填充区域
     *
     * @param outterX     整个填充区域左起的x坐标
     * @param outterY     整个填充区域左起的y坐标
     * @param outterW     整个填充区域的宽度
     * @param outterH     整个填充区域的高度
     * @param paddingTopAndBottom  上下边界到content的间隔
     * @param whRate      容器内所需放置图片的宽高比
     * @return            容器内所需放置图片的起始xy坐标, 以及width height
     */
    public static Rectangle calculateCordinate(int outterX, int outterY, int outterW, int outterH, int paddingTopAndBottom, int[] whRate) {
//        LOGGER.info("原始容器位置=[" + outterX + "," + outterY + "," + outterW + "," + outterH + "]");
//        LOGGER.info("所需paddingTop="+paddingTopAndBottom);
        int startY;
        int startX;
        int contentW;
        int contentH;
        float whRate_float = (float) whRate[0] / whRate[1];
        whRate_float = (float) (Math.round(whRate_float * 100)) / 100;  //图片规定比例
        float outter_whRate_float = (float) outterW / outterH;
        outter_whRate_float = (float) (Math.round(outter_whRate_float * 100)) / 100;  //容器实际的比例
        if (outter_whRate_float > whRate_float) {
            contentH = outterH;
            contentW = (int) (contentH * whRate_float);
        } else {
            contentW = outterW;
            contentH = (int) (outterW / whRate_float);
        }
        //计算padding
        if (paddingTopAndBottom > 0 && paddingTopAndBottom < contentH) {
            contentH = contentH - 2 * paddingTopAndBottom;
            contentW = (int) (contentH * whRate_float);

        }
        startX = outterX + (outterW - contentW) / 2;
        startY = outterY + (outterH - contentH) / 2;
        return new Rectangle(startX, startY, contentW, contentH);
    }


    public static void CreateParentIfNotExist(String megerd_path) {
        File file = new File(megerd_path);
        if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
    }
    public static void CreateParentIfNotExist(File file) {
        if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
    }

    /**
     * @param ratio         容器比例
     * @param tobeCropImage 需要根据容器比例必要时进行图片裁剪
     * @return
     */
    public static BufferedImage cropAccordingRatio(float ratio, BufferedImage tobeCropImage) throws IOException {
        Rectangle rectangle = calculateXYWH(ratio, tobeCropImage);
        return Thumbnails.of(tobeCropImage).
                sourceRegion(rectangle).size((int) rectangle.getWidth(), (int) rectangle.getHeight()).asBufferedImage();
    }

    /**
     * 根据比例算出图片的裁剪位置
     *
     * @param ratio         比例
     * @param tobeCropImage 图片
     */
    private static Rectangle calculateXYWH(float ratio, BufferedImage tobeCropImage) {
        int h = tobeCropImage.getHeight();
        int w = tobeCropImage.getWidth();
        int originH = h;
        int originW = w;
        float actualRatio = (float) w / h;
        ratio = (float) (Math.round(ratio * 100)) / 100; //容器比例,保留两位小数
        actualRatio = (float) (Math.round(actualRatio * 100)) / 100; //实际图片比例,保留两位小数
//        LOGGER.info("容器比例="+ratio+",图片实际比例="+actualRatio);
        if (actualRatio > ratio) {
            //->图片宽了
            w = (int) (h * ratio);
//            LOGGER.info("图片过宽后处理后的w="+w);
        } else if (actualRatio < ratio) {
            h = (int) (w / ratio);
//            LOGGER.info("图片过高后处理后的h="+h);
        }
//        LOGGER.info("originW="+originW+",originH="+originH+",w="+w+",h="+h);
        int startX = (originW - w) / 2;
        int startY = (originH - h) / 2;
//        LOGGER.info("截图位置:x=" + startX + ",y=" + startY + ",w=" + w + ",h=" + h);
        return new Rectangle(startX, startY, w, h);
    }
}
