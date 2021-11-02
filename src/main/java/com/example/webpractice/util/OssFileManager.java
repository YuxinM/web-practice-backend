package com.example.webpractice.util;

import com.aliyun.oss.OSS;
import com.aliyun.oss.model.*;
import com.csvreader.CsvReader;
import com.example.webpractice.config.AliyunConfig;
import com.example.webpractice.config.MainConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.POIXMLTextExtractor;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
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
 * 阿里云文件管理类
 *
 * @Author MengYuxin
 * @Date 2021/10/25 17:24
 */
@Component
@Slf4j
public class OssFileManager {


    /**
     * 获取文件信息
     *
     * @param bucketName bucketName
     * @param path       阿里云路径
     * @param ossClient  oss
     * @return
     */
    public SimplifiedObjectMeta getFileInfo(String bucketName, String path, OSS ossClient) {
        SimplifiedObjectMeta meta = ossClient.getSimplifiedObjectMeta(bucketName, path);
        ossClient.shutdown();
        //meta.getSize()
        return meta;
    }

    /**
     * 上传文件
     *
     * @param bucketName bucketName
     * @param path       阿里云路径
     * @param file       文件
     * @param ossClient  oss
     */
    public void uploadFile(String bucketName, String path, File
            file, OSS ossClient) {

        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, path, file);
        ossClient.putObject(putObjectRequest);
        ossClient.shutdown();

    }

    /**
     * 删除阿里云上的文件
     *
     * @param bucketName bucket名称
     * @param path       路径
     * @param ossClient  oss
     * @return 删除是否成功
     */
    public boolean deleteFile(String bucketName, String path, OSS ossClient) {

        if (!ossClient.doesObjectExist(bucketName, path)) {
            return false;
        }
        ossClient.deleteObject(bucketName, path);
        ossClient.shutdown();
        return true;
    }


    /**
     * 得到阿里云OSS的指定bucket中所有文件名
     *
     * @param bucketName bucket名称
     * @return 文件名
     */
    public List<String> getFileNames(String bucketName, OSS ossClient) {

        //  OSS ossClient = aliyunConfig.OSSClient();
        ObjectListing objectListing = ossClient.listObjects(
                new ListObjectsRequest(bucketName).withMaxKeys(500));
        List<OSSObjectSummary> summaries = objectListing.getObjectSummaries();
        List<String> names = new ArrayList<>();
        for (OSSObjectSummary s : summaries) {
            names.add(s.getKey());
        }
        log.info("得到阿里云OSS上所有的文件，一共{}个", names.size());
        return names;

    }

    /**
     * 下载文件
     *
     * @param bucketName bucket名称
     * @param fileName   路径
     * @param ossClient  oss
     * @return
     */
    public void downloadContent(String bucketName, String fileName, OSS ossClient, File file) {

        ossClient.getObject(new GetObjectRequest(bucketName, fileName), file);
        ossClient.shutdown();

    }

    /**
     * 读取word文件内容
     *
     * @param filePath 本地路径
     * @return
     * @throws Exception
     */
    public String readWord(String filePath) {
        String buffer = "";
        try {
            if (filePath.endsWith(".doc")) {
                InputStream is = new FileInputStream(filePath);
                //HWPFDocument hwpfDocument=new HWPFDocument(is);
                WordExtractor ex = new WordExtractor(is);
                buffer = ex.getText();
                is.close();

            } else if (filePath.endsWith(".docx")) {
                OPCPackage opcPackage = POIXMLDocument.openPackage(filePath);
                POIXMLTextExtractor extractor = new XWPFWordExtractor(opcPackage);
                buffer = extractor.getText();
                opcPackage.close();
            } else {
                return null;
            }
            return buffer;
        } catch (Exception e) {
            log.warn("读取{}失败", filePath);
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 下载文件到本地临时存储位置
     *
     * @param bucketName bucket名称
     * @param fileName   OSS中文件名
     * @return 返回正常字符串是文件本地路径说明下载成功 null说明下载失败
     */

    public String downloadCsv(String bucketName, String fileName, OSS ossClient) {

        ///   OSS ossClient = aliyunConfig.OSSClient();
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
    public String[] readCsvFile(String filepath) {
        try {
            ArrayList<String[]> csv = new ArrayList<String[]>();
            CsvReader reader = new CsvReader(filepath, ',', Charset.forName("GBK"));
            reader.readHeaders();
            // log.error("出错");
            while (reader.readRecord()) {
                String[] a = reader.getValues();
                csv.add(a);
            }
            reader.close();
            //提取需要的信息
            String[] var = new String[13];
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
