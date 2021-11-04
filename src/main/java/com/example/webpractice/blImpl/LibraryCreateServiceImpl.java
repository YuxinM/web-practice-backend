package com.example.webpractice.blImpl;

import com.example.webpractice.DAO.AppendixDAO;
import com.example.webpractice.DAO.PaperDAO;
import com.example.webpractice.bl.LibraryCreateService;
import com.example.webpractice.config.AliyunAppendixConfig;
import com.example.webpractice.config.AliyunConfig;
import com.example.webpractice.config.MainConfig;
import com.example.webpractice.po.Appendix;
import com.example.webpractice.po.Papers;
import com.example.webpractice.util.DateUtil;
import com.example.webpractice.util.OssFileManager;
import com.example.webpractice.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.sql.Timestamp;
import java.util.List;

/**
 * @Author MengYuxin
 * @Date 2021/10/26 12:41
 */


@Service
@Slf4j
public class LibraryCreateServiceImpl implements LibraryCreateService {

    //本地附件文件夹
    private static final String AppendixLocalDir = FileUtil.jointPath(MainConfig.PROJECT_ABSOLUTE_PATH,
            MainConfig.APPENDIX);

    //oss附件文件夹
    private static final String ossAppendixDir = "附件/";

    @Autowired
    PaperDAO paperDAO;
    @Autowired
    OssFileManager ossFileManager;
    @Autowired
    AliyunConfig aliyunConfig;
    @Autowired
    AppendixDAO appendixDAO;
    @Autowired
    AliyunAppendixConfig aliyunAppendixConfig;

    @Override
    public void writeInDatabase() {

        log.info("开始读取爬虫数据至数据库");
        List<String> csvList = ossFileManager.getFileNames(aliyunConfig.getBucketName(), aliyunConfig.OSSClient());
        for (String filename : csvList) {

            if (filename.endsWith(".csv")) {
                //本地临时存储路径
                String path = ossFileManager.downloadCsv(aliyunConfig.getBucketName(), filename, aliyunConfig.OSSClient());
                if (path != null) {
                    /**
                     * 这里是一个CSV文件的全部信息 依次如下
                     * 法规标题 法规文号 外规类别
                     * 发文部门 效力等级 发布日期
                     * 实施日期 解读部门 录入人
                     * 录入时间 正文    状态
                     * 内化状态
                     * 这里都是从阿里云OSS读取下来的 为外规
                     * 所以录入人统一为系统  状态统一为已发布  内化状态统一为未内化
                     */


                    String[] csv = ossFileManager.readCsvFile(path);
                    if (csv != null) {
                        Timestamp release_ts = null;
                        Timestamp implement_ts = null;

                        if (csv[5] != null) {
                            release_ts = new Timestamp(DateUtil.dateToStamp(csv[5]));
                        }

                        if (csv[6] != null && csv[6].contains("年")) {
                            implement_ts = new Timestamp(DateUtil.dateToStamp(csv[6]));
                        } else {
                            implement_ts = release_ts;
                        }
                        Timestamp input_ts = new Timestamp(DateUtil.dateToStamp(csv[9]));

                        String department = csv[3].replaceAll(" ", "").replaceAll("\n", ",");

                        Papers papers = new Papers(csv[0], csv[1], csv[2],
                                department.substring(0, department.length() - 1), release_ts, implement_ts, csv[4], csv[7], 1, input_ts,
                                csv[10], Integer.parseInt(csv[11]), -1);

                        //存入一条法规
                        try {
                            //System.out.println(papers);
                            int id = paperDAO.save(papers).getId();
                            //然后把临时文件删除
                            FileUtil.deleteDirRecursion(path);

                            //写入附件
                            if (csv[13] != null && !csv[13].equals("")) {
                                String[] strings = csv[13].split("\n");
                                for (String fileName : strings) {
                                    //手动处理几个特殊情况
                                    if(fileName.equals("附件1：废止的规范性文件目录.doc")){
                                        fileName="废止的规范性文件目录.doc";
                                    }
                                    if(fileName.equals("附件：27 家非银行支付机构《支付业务许可证》续展决定.pdf")){
                                        fileName="27 家非银行支付机构《支付业务许可证》续展决定.pdf";
                                    }
                                    if(fileName.equals("附件1：废止的规章目录.doc")){
                                        fileName="废止的规章目录.doc";
                                    }
                                    if(fileName.equals("附件：中国人民银行关于修改《金融机构大额交易和可疑交易报告管理办法》的决定.doc")){
                                        fileName="中国人民银行关于修改《金融机构大额交易和可疑交易报告管理办法》的决定.doc";
                                    }
                                    if(fileName.equals("附件2：中国人民银行现行有效的规章目录.docx")){
                                        fileName="中国人民银行现行有效的规章目录.docx";
                                    }
                                    if(fileName.equals("附件2：中国人民银行主要规范性文件目录.doc")){
                                        fileName="中国人民银行主要规范性文件目录.doc";
                                    }
                                    if (fileName.equals("附件.doc")) {
                                        fileName = csv[0] + "的" + fileName;
                                    }
                                    String ossPath = csv[4] + "/" + "附件/" + fileName;
                                    File folder = new File(AppendixLocalDir);
                                    if (!folder.exists() && !folder.isDirectory()) {
                                        folder.mkdirs();
                                    }
                                    String fullPath = FileUtil.jointPath(AppendixLocalDir, fileName);
                                    File file = new File(fullPath);
                                    ossFileManager.downloadFile(aliyunConfig.getBucketName(),
                                            ossPath, aliyunConfig.OSSClient(), file);
                                    fileName=FileUtil.solveName(fileName);
                                    //System.out.println(file.exists());
                                    String newName = 1 + "-" + fileName;
                                    String newPath = FileUtil.jointPath(AppendixLocalDir, newName);
                                    file.renameTo(new File(newPath));
                                    file=new File(newPath);
                                    String storeOssPath = ossAppendixDir + newName;
                                    //System.out.println(storeOssPath);
                                    ossFileManager.uploadFile(aliyunAppendixConfig.getBucketName(),
                                            storeOssPath, file, aliyunAppendixConfig.OSSClient1());
                                    FileUtil.deleteDirRecursion(newPath);
                                    Appendix appendix = new Appendix(newName, "admin", id);
                                    appendixDAO.save(appendix);
                                }
                            }
                        } catch (Exception e) {
                           // e.printStackTrace();
                            log.error("数据出错，不能存入数据库");
                        }
                    }
                }
            }
        }
        log.info("爬虫数据读取完毕");
    }


}
