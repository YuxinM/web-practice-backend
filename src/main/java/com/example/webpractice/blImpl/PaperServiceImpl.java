package com.example.webpractice.blImpl;

import com.example.webpractice.DAO.PaperDAO;
import com.example.webpractice.DAO.UserDAO;
import com.example.webpractice.bl.PaperService;
import com.example.webpractice.config.AliyunAppendixConfig;
import com.example.webpractice.config.MainConfig;
import com.example.webpractice.po.Papers;
import com.example.webpractice.util.DateUtil;
import com.example.webpractice.util.FileUtil;
import com.example.webpractice.util.OssFileManager;
import com.example.webpractice.vo.PaperVO;
import com.example.webpractice.vo.ResponseVO;
import com.example.webpractice.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.File;
import java.sql.Timestamp;
import java.util.List;

/**
 * @Author MengYuxin
 * @Date 2021/10/27 18:43
 */

@Service
public class PaperServiceImpl implements PaperService {


    @Autowired
    PaperDAO paperDAO;

    @Autowired
    UserDAO userDAO;

    @Autowired
    OssFileManager ossFileManager;

    @Autowired
    AliyunAppendixConfig aliyunAppendixConfig;


    /**
     * 根据id获取法规详情
     * @param id
     * @return
     */
    @Override
    public ResponseVO getPaperById(int id) {
        List<Papers> papers = paperDAO.getPapersById(id);
        if (papers.size() == 0) {
            return ResponseVO.buildFailure("对应id的法律文书不存在");
        } else {
            Papers target = papers.get(0);
            String release_time = DateUtil.StampToDate(target.getRelease_time());
            String implement_time = DateUtil.StampToDate(target.getImplement_time());
            String input_time = DateUtil.StampToDate(target.getInput_time());
            String input_user = userDAO.getUsernameById(target.getUser_id());
            String content="";
            /*
             * 如果正文字段是正文说明是爬取数据，存在数据库
             * 如果是文件名则去阿里云来读文件  filename: 文件路径
             */
            if(target.getContent().startsWith("filename")){
                //这是文件在阿里云中的存储位置
                String ossPath="正文文件/"+target.getContent().substring(target.getContent().indexOf(':'));
                //从阿里云获取文件的流
                BufferedInputStream in= ossFileManager.downloadStream(aliyunAppendixConfig.getBucketName(),
                        ossPath,aliyunAppendixConfig.OSSClient1());

            }else {
                content=target.getContent();
            }
            PaperVO paperVO = new PaperVO(target.getId(), target.getTitle(),
                    target.getPaper_number(), target.getCategory(), target.getDepartment(),
                    release_time, implement_time, target.getGrade(), target.getInterpret_department(),
                    input_user, input_time, content, target.getStatus() == 1);
            return ResponseVO.buildSuccess(paperVO);

        }
    }

    @Override
    public ResponseVO addPaper(String title, String number, String category,
                               String department, String grade, String release_time,
                               String implement_time, String interpret, String input_user,
                               String input_time, MultipartFile multipartFile, String status) {

        String fileName=multipartFile.getOriginalFilename();
        String sqlFileName="filename:"+fileName;
        if(paperDAO.numOfFileName(sqlFileName)!=0){
            return ResponseVO.buildFailure("正文文件名已存在");
        }
        if(paperDAO.numOfTitle(title)!=0){
            return ResponseVO.buildFailure("标题已存在");
        }
        if(userDAO.UserExists(input_user)==0){
            return ResponseVO.buildFailure("用户名不存在");
        }
        String localDir= FileUtil.jointPath(MainConfig.PROJECT_ABSOLUTE_PATH,
                MainConfig.CONTENT);
        //本地临时存储正文文件夹的路径
        File folder=new File(localDir);
        if (!folder.exists() && !folder.isDirectory()) {
            folder.mkdirs();
        }
        //正文文件的完整路径
        String fullPath=FileUtil.jointPath(localDir,fileName);
        if(!FileUtil.saveFile(multipartFile,fullPath)){
            return ResponseVO.buildFailure("未知错误 正文文件存储失败");
        }
        Timestamp release=new Timestamp(DateUtil.dateToStamp(release_time));
        Timestamp implement=new Timestamp(DateUtil.dateToStamp(implement_time));
        Timestamp input=new Timestamp(DateUtil.dateToStamp(input_time));
        int st=status.equals("true")?1:0;
        int userId=userDAO.getLoginInfo(input_user).getId();
        Papers papers=new Papers(title,number,category,department,
                release,implement,grade,interpret,userId,input,sqlFileName,st);
        int id=paperDAO.save(papers).getId();
        //把本地的文件存入阿里云
        //本地临时文件
        File file=new File(fullPath);
        String ossPath="正文文件/"+fileName;
        ossFileManager.uploadFile(aliyunAppendixConfig.getBucketName(),
                ossPath,file,aliyunAppendixConfig.OSSClient1());
        //删除本地的临时文件
        FileUtil.deleteDirRecursion(fullPath);
        return ResponseVO.buildSuccess(id);
    }
}

