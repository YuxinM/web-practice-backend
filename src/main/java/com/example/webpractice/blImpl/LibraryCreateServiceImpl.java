package com.example.webpractice.blImpl;

import com.example.webpractice.DAO.PaperDAO;
import com.example.webpractice.bl.LibraryCreateService;
import com.example.webpractice.config.AliyunConfig;
import com.example.webpractice.po.Papers;
import com.example.webpractice.util.DateUtil;
import com.example.webpractice.util.OssFileManager;
import com.example.webpractice.util.FileUtil;
import com.example.webpractice.vo.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

/**
 * @Author MengYuxin
 * @Date 2021/10/26 12:41
 */


@Service
@Slf4j
public class LibraryCreateServiceImpl implements LibraryCreateService {

    @Autowired
    PaperDAO paperDAO;
    @Autowired
    OssFileManager ossFileManager;
    @Autowired
    AliyunConfig aliyunConfig;

    @Override
    public ResponseVO writeInDatabase() {

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

                        Papers papers = new Papers(csv[0], csv[1], csv[2],
                                csv[3], release_ts, implement_ts, csv[4], csv[7], 1, input_ts,
                                csv[10], Integer.parseInt(csv[11]), 0);

                        //存入一条法规
                        try {
                            //System.out.println(papers);
                            int id = paperDAO.save(papers).getId();
                            //然后把临时文件删除
                            FileUtil.deleteDirRecursion(path);
                        } catch (Exception e) {
                            log.error("数据出错，不能存入数据库");
                        }
                    }
                }
            }
        }
        return null;
    }


}
