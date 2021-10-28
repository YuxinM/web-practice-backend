package com.example.webpractice.util;

import com.aliyun.oss.OSS;
import com.aliyun.oss.model.*;
import com.csvreader.CsvReader;
import com.example.webpractice.config.AliyunConfig;
import com.example.webpractice.config.MainConfig;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * 下载阿里云OSS文件与读取工具类
 *
 * @Author MengYuxin
 * @Date 2021/10/25 17:24
 */
@Component
@Slf4j
public class FileReader {

    @Autowired
    AliyunConfig aliyunConfig;

    // private static final Logger logger= LoggerFactory.getLogger(FileReader.class);

    /**
     * 得到阿里云OSS的指定bucket中所有文件名
     *
     * @param bucketName bucket名称
     * @return 文件名(csv文件)
     */
    public List<String> getFileNames(String bucketName) {

        OSS ossClient = aliyunConfig.OSSClient();
        ObjectListing objectListing = ossClient.listObjects(
                new ListObjectsRequest(bucketName).withMaxKeys(300));
        List<OSSObjectSummary> summaries = objectListing.getObjectSummaries();
        List<String> names = new ArrayList<>();
        for (OSSObjectSummary s : summaries) {
            if (s.getKey().endsWith(".csv")) {
                names.add(s.getKey());
            }
        }
        log.info("得到阿里云OSS上所有的文件，一共{}个", names.size());
        return names;

    }

    /**
     * 下载文件到本地临时存储位置
     *
     * @param bucketName bucket名称
     * @param fileName   OSS中文件名
     * @return 返回正常字符串说明下载成功 null说明下载失败
     */

    public String download(String bucketName, String fileName) {

        OSS ossClient = aliyunConfig.OSSClient();
        OSSObject ossObject = ossClient.getObject(bucketName,
                fileName);

        BufferedInputStream in = null;

        try {
            // 读取文件内容
            //System.out.println("Object content:");
            in = new BufferedInputStream(ossObject.getObjectContent());
            byte[] buffer = new byte[1024];
            String storePath = FileUtil.jointPath(MainConfig.PROJECT_ABSOLUTE_PATH,
                    MainConfig.USER_DATA_DIR_NAME);
            File file = new File(storePath);
            //创建文件夹
            if (!file.exists()) {
                file.mkdirs();
            }
            storePath = FileUtil.jointPath(storePath, FileUtil.getUUID() + ".csv");
            file = new File(storePath);
            //创建文件
            file.createNewFile();
            OutputStream out = new FileOutputStream(file);// response.getOutputStream();
            int len = 0;
            int i = 0;
            while ((len = in.read(buffer)) > 0) {
                i = i + len;
                out.write(buffer, 0, len);
            }
            // 数据读取完成后，获取的流必须关闭，否则会造成连接泄漏，导致请求无连接可用，程序无法正常工作。
            in.close();
            out.close();
            // 关闭OSSClient。
            ossClient.shutdown();
            return storePath;
        } catch (Exception e) {
            log.error("下载阿里云上文件出错,文件名为{}", fileName);
            e.printStackTrace();
        }
        return null; //有异常就返回null
    }

    /**
     * 读取csv文件
     * 这里返回的是法规的12个属性
     *
     * @param filepath 文件路径
     * @return 返回null读取失败
     */
    public String[] readFile(String filepath) {
        try {
            ArrayList<String[]> csv = new ArrayList<String[]>();
            CsvReader reader = new CsvReader(filepath, ',', Charset.forName("GBK"));
            reader.readHeaders();
            while (reader.readRecord()) {
                String[] a = reader.getValues();
            }
           // log.error("出错");
            while (reader.readRecord()){
                String[] a=reader.getValues();
                csv.add(a);
            }
            reader.close();
            //提取需要的信息
            String[] var = new String[12];
            for (int i = 0; i < csv.size(); i++) {
                var[i] = csv.get(i)[1]; //属性在第二列
            }
            return var;
        } catch (Exception e) {
            log.error("读取文件出错");
            System.out.println("error");
            e.printStackTrace();
        }
        return null;
    }


//    public static void main(String[] args) {
//        download("internet-practice",
//                "csv/规范性文件/中国人民银行公告〔2016〕第13号（关于降低个人投资人认购大额存单起点金额有关事宜）（中国人民银行公告〔2016〕第13号）.csv");
//    }

}
