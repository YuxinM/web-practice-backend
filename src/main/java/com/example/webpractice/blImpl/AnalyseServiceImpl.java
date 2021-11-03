package com.example.webpractice.blImpl;

import com.example.webpractice.DAO.AnalyseDAO;
import com.example.webpractice.DAO.PaperDAO;
import com.example.webpractice.bl.AnalyseService;
import com.example.webpractice.util.SessionManager;
import com.example.webpractice.vo.AnalyseVO;
import com.example.webpractice.vo.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author PanYue
 * @Date 2021/11/3 14:16
 */

@Service
@Slf4j
public class AnalyseServiceImpl implements AnalyseService {

    @Autowired
    AnalyseDAO analyseDAO;

    @Autowired
    PaperDAO paperDAO;

    @Override
    public ResponseVO getAnalyseById(int id) {
        if (SessionManager.getLoginUser() == null) {
            return ResponseVO.buildFailure("请登录");
        }
        return ResponseVO.buildSuccess(new AnalyseVO(analyseDAO.findById(id).get()));
    }

    @Override
    public ResponseVO addAnalyse(String title, String number, String category, String interpret, String input_user, String input_time, MultipartFile file, String paper_id) {
        paperDAO.analyse(Integer.parseInt(paper_id));
        return null;
    }

    @Override
    public ResponseVO updateAnalyse(int id, String title, String number, String category, String interpret, String input_user, String input_time, MultipartFile file, String paper_id) {
        return null;
    }
}
