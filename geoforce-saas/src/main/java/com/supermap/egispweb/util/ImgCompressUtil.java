package com.supermap.egispweb.util;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import javax.imageio.stream.MemoryCacheImageInputStream;

import org.springframework.web.multipart.MultipartFile;


public final class ImgCompressUtil {
	/**
	 * 从BufferedImage得到InputStream
	 * @param sourceImg
	 * @param imgtype
	 * @return
	 * @Author Juannyoh
	 * 2016-2-22下午4:37:32
	 */
	public static InputStream getImageStream(BufferedImage sourceImg,String imgtype){
	    try {
	    	ByteArrayOutputStream bs = new ByteArrayOutputStream();
		    ImageOutputStream imOut = ImageIO.createImageOutputStream(bs);
		    ImageIO.write(sourceImg,imgtype,imOut);
		    InputStream is= new ByteArrayInputStream(bs.toByteArray());
		    return is;
	    } catch (IOException e) {
	    	e.printStackTrace();
	    	return null;
	    }
	    
	}
	
	/**
	 * 构建新的BufferedImage
	 * @param oldImage
	 * @param width
	 * @param height
	 * @return
	 * @throws IOException
	 * @Author Juannyoh
	 * 2016-2-22下午4:43:19
	 */
	public static  BufferedImage  getNewImage(MultipartFile oldImage,double width,double height) throws IOException{  
        /*srcURl 原图地址；deskURL 缩略图地址；comBase 压缩基数；scale 压缩限制(宽/高)比例*/  
        ByteArrayInputStream bais = new ByteArrayInputStream(oldImage.getBytes());   
        MemoryCacheImageInputStream mciis = new MemoryCacheImageInputStream(bais);        
        Image src = ImageIO.read(mciis);  
        double srcHeight = src.getHeight(null);  
        double srcWidth = src.getWidth(null);  
        double deskHeight = 0;//缩略图高  
        double deskWidth  = 0;//缩略图宽  
        if (srcWidth>srcHeight) {  
            if (srcWidth>width) {  
                if (width/height>srcWidth/srcHeight) {  
                    deskHeight = height;  
                    deskWidth = srcWidth/(srcHeight/height);  
                }  
                else {  
                    deskHeight = width/(srcWidth/srcHeight);  
                    deskWidth = width;  
                }  
            }  
            else {  
                if (srcHeight>height) {  
                    deskHeight = height;  
                    deskWidth = srcWidth/(srcHeight/height);  
                }else {  
                    deskHeight=srcHeight;  
                    deskWidth=srcWidth;  
                }  
            }  
        }  
        else if(srcHeight>srcWidth)  
        {  
            if (srcHeight>(height)) {  
                if ((height)/width>srcHeight/srcWidth) {  
                    deskHeight = srcHeight/(srcWidth/width);  
                    deskWidth = width;  
                }else {  
                    deskHeight = height;  
                    deskWidth = (height)/(srcHeight/srcWidth);  
                }  
            }  
            else {  
                if (srcWidth>width) {  
                    deskHeight = srcHeight/(srcWidth/width);  
                    deskWidth = width;  
                }else {  
                    deskHeight=srcHeight;  
                    deskWidth=srcWidth;  
                }  
  
            }  
              
        }  
        else if (srcWidth==srcHeight) {  
              
            if (width>=(height)&&srcHeight>(height)) {  
                deskWidth=(height);  
                deskHeight=(height);  
            }  
            else if (width<=(height)&&srcWidth>width) {  
                deskWidth=width;  
                deskHeight=width;  
            }  
            else  if (width==(height)&&srcWidth<width) {  
                deskWidth=srcWidth;  
                deskHeight=srcHeight;  
            }  
            else {  
                deskHeight=srcHeight;  
                deskWidth=srcWidth;  
            }  
        }  
        BufferedImage tag = new BufferedImage((int)deskWidth,(int)deskHeight,BufferedImage.TYPE_INT_RGB);  
        tag.getGraphics().drawImage(src, 0, 0, (int)deskWidth, (int)deskHeight, null); //绘制缩小后的图  
        return tag;  
    }
}
