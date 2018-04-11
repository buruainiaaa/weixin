package com.cj.weixin.wxapi;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * ${DESCRIPTION}
 *
 * @author cody
 * @create 2018-03-29 17:20
 **/

public class SaveImg {

    /**
     * 获取临时素材
     */
    private static InputStream getMedia(String mediaId,String accessToken) {
        String url = "https://api.weixin.qq.com/cgi-bin/media/get";
        String params = "access_token=" + accessToken + "&media_id=" + mediaId;
        InputStream is = null;
        try {
            String urlNameString = url + "?" + params;
            URL urlGet = new URL(urlNameString);
            HttpURLConnection http = (HttpURLConnection) urlGet.openConnection();
            http.setRequestMethod("GET"); // 必须是get方式请求
            http.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            http.setDoOutput(true);
            http.setDoInput(true);
            http.connect();
            // 获取文件转化为byte流
            is = http.getInputStream();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return is;
    }

    /**
     * 保存图片至服务器
     * @param mediaId
     * @return 文件名
     */
    public static String saveImageToDisk(String mediaId,String accessToken,String weixindownloadath) throws IOException{
        String filename = "";
         InputStream inputStream = SaveImg.getMedia(mediaId,accessToken);
        byte[] data = new byte[1024];
        int len = 0;
        FileOutputStream fileOutputStream = null;
        try {
            //服务器存图路径
            String path = weixindownloadath;
            File file=new File(path);
            if(!file.exists())
                file.mkdir();
            filename = UUID.randomUUID().toString().replace("-", "") + ".jpg";
            fileOutputStream = new FileOutputStream(path + filename);
            while ((len = inputStream.read(data)) != -1) {
                fileOutputStream.write(data, 0, len);
            }
        } catch (IOException e) {
            throw  new IOException(e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    throw  new IOException(e);
                }
            }
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    throw  new IOException(e);
                }
            }
        }
        return weixindownloadath+filename;
    }

    /**
     * 获取图片尺寸
     * @param path
     * @return
     * @throws IOException
     */
    public static Map<String,String> getPictureSize(String path) throws IOException{
        File picture = new File(path);
        BufferedImage sourceImg = ImageIO.read(new FileInputStream(picture));
        Map<String,String> map=new HashMap<>();
        map.put("size",String.format("%.1f",picture.length()/1024.0));
        map.put("width",sourceImg.getWidth()+"");
        map.put("height",sourceImg.getHeight()+"");
        return  map;
    }

}
