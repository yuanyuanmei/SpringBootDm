package com.elan.bg.utils;

import com.alibaba.fastjson.JSONObject;
import com.elan.bg.constants.ConstantsUrl;
import com.elan.bg.dto.ResponseDto;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Map;

public class ServletUtils {

    /**
     * 向指定的地址发送get请求
     *
     * @param url
     * @return
     */
    public static String get(String url) {
        try {
            URL urlObject = new URL(url);
            //开始连接
            URLConnection connection = urlObject.openConnection();
            return getInput(connection);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 发送请求
     *
     * @param url
     * @param data
     * @return
     */
    public static String post(String url, String data) {
        try {
            //1.创建连接对象
            URL urlObject = new URL(url);
            //2.建立连接
            URLConnection connection = urlObject.openConnection();
            //3.要发送数据出去、必须要设置为可发送数据状态
            connection.setDoOutput(true);
            //4.获取输出流
            OutputStream os = connection.getOutputStream();
            //5.写出数据
            if (data != null) {
                os.write(data.getBytes());
                os.close();
            }

            return getInput(connection);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String getInput(URLConnection connection) {
        try {
            //获取输入流
            InputStream is = connection.getInputStream();
            byte[] b = new byte[1024];
            int len;
            StringBuilder sb = new StringBuilder();
            while ((len = is.read(b)) != -1) {
                //new String(数组,从下标开始,取长度,字符编码)
                sb.append(new String(b, 0, len));
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ResponseDto getBaiDuApi(Object param,String url){
        JSONObject result = JSONObject.parseObject(ServletUtils.get(getBaiDuUrl(param,url)));
        return DozerUtils.instance().map(result, ResponseDto.class);
    }

    public static String getBaiDuUrl(Object param,String url){
        Map paramMap = DozerUtils.instance().map(param, Map.class);
        try {
            String paramUrl = StringUtils.map2String(paramMap);
            return url + paramUrl;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     *  计算sn跟参数对出现顺序有关，get请求请使用LinkedHashMap保存<key,value>，
     *  该方法根据key的插入顺序排序；post请使用TreeMap保存<key,value>，
     *  该方法会自动将key按照字母a-z顺序排序。所以get请求可自定义参数顺序
     *  （sn参数必须在最后）发送请求，但是post请求必须按照字母a-z顺序填充body（sn参数必须在最后）。
     *  以get请求为例：http://api.map.baidu.com/geocoder/v2/?address=百度大厦&output=json&ak=yourak
     *  ，paramsMap中先放入address，再放output，然后放ak，放入顺序必须跟get请求中对应参数的出现顺序保持一致。
     *         Map paramsMap = new LinkedHashMap<String, String>();
     *         paramsMap.put("address", "百度大厦");
     *         paramsMap.put("output", "json");
     *         paramsMap.put("ak", "yourak");
     *  调用下面的toQueryString方法，对LinkedHashMap内所有value作utf8编码，
     *  拼接返回结果address=%E7%99%BE%E5%BA%A6%E5%A4%A7%E5%8E%A6&output=json&ak=yourak
     *  对paramsStr前面拼接上/geocoder/v2/?，后面直接拼接yoursk得到
     *  /geocoder/v2/?address=%E7%99%BE%E5%BA%A6%E5%A4%A7%E5%8E%A6&output=json&ak=yourakyoursk
     *  对上面wholeStr再作utf8编码
     *  调用下面的MD5方法得到最后的sn签名7de5a22212ffaa9e326444c75a58f9a0
     *  System.out.println(DesUtils.encode(tempStr));
     */
    public static String getSnCal(Map paramsMap){

        try {
            String paramsStr = null;
            paramsStr = StringUtils.map2String(paramsMap);
            String wholeStr = new String("/geocoder/v2/?" + paramsStr + "yoursk");
            String tempStr = URLEncoder.encode(wholeStr, "UTF-8");
            return DesUtils.encode(tempStr);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

}



























