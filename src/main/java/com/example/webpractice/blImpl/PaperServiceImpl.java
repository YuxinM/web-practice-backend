package com.example.webpractice.blImpl;

import com.example.webpractice.DAO.PaperDAO;
import com.example.webpractice.DAO.UserDAO;
import com.example.webpractice.bl.PaperService;
import com.example.webpractice.po.Papers;
import com.example.webpractice.util.DateUtil;
import com.example.webpractice.vo.PaperVO;
import com.example.webpractice.vo.ResponseVO;
import com.example.webpractice.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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


    /**
     * 根据id获取法规详情
     *
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
            PaperVO paperVO = new PaperVO(target.getId(), target.getTitle(),
                    target.getPaper_number(), target.getCategory(), target.getDepartment(),
                    release_time, implement_time, target.getGrade(), target.getInterpret_department(),
                    input_user, input_time, target.getContent(), target.getStatus() == 1);
            return ResponseVO.buildSuccess(paperVO);

        }
    }
}
