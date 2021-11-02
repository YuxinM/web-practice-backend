package com.example.webpractice.blImpl.page;

import com.example.webpractice.DAO.PaperDAO;
import com.example.webpractice.bl.page.PageService;
import com.example.webpractice.po.Papers;
import com.example.webpractice.util.DateUtil;
import com.example.webpractice.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * 分页条件查询类
 *
 * @Author MengYuxin
 * @Date 2021/10/28 16:22
 */
@Service
@Slf4j
public class PageServiceImpl implements PageService {

    @Autowired
    PaperDAO paperDAO;

    //默认页大小为10
    private static final int pageSize = 10;


    @Override
    public ResponseVO page(Integer pageNum, String title, String grade, String[] release_time, String[] implement_time, String[] department, String status) {
        ConditionVO conditionVO = new ConditionVO(pageNum, title, grade, release_time,
                implement_time, department, status);
        List<Papers> papers = find(conditionVO);
        // System.out.println(papers.size());
        List<SimplePaperVO> simplePaperVOS = new ArrayList<>();
        for (Papers p : papers) {
            String release = DateUtil.StampToDate(p.getRelease_time());
            String implement = DateUtil.StampToDate(p.getImplement_time());
            SimplePaperVO in = new SimplePaperVO(p.getId(),
                    p.getTitle(), p.getDepartment(), release,
                    implement, p.getGrade(), p.getStatus() == 1,
                    p.getAnalyse_status() == 1, p.getPaper_number());
            simplePaperVOS.add(in);
        }
        PageInfoVO pageInfoVO = new PageInfoVO(count(conditionVO), simplePaperVOS);
        return ResponseVO.buildSuccess(pageInfoVO);
    }

    /**
     * 多条件分页查询并且分页
     *
     * @param conditionVO 条件集合
     * @return
     */
    public List<Papers> find(ConditionVO conditionVO) {

        List<Papers> result = null;
        Specification<Papers> queryCondition = new Specification<Papers>() {
            @Override
            public Predicate toPredicate(Root<Papers> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicateList = new ArrayList<Predicate>();

                if (!conditionVO.getTitle().equals("")) {
                    //System.out.println("标题");
                    predicateList.add(criteriaBuilder.like(root.get("title").as(String.class),
                            "%" + conditionVO.getTitle() + "%"));

                }
                if (!conditionVO.getGrade().equals("")) {
                    //System.out.println("等级");
                    predicateList.add(criteriaBuilder.equal(root.get("grade").as(String.class),
                            conditionVO.getGrade()));
                }
                if (conditionVO.getRelease_time() != null &&
                        conditionVO.getRelease_time().length != 0) {
                    // System.out.println("时间");
                    Timestamp start = new Timestamp(
                            DateUtil.dateToStamp(conditionVO.getRelease_time()[0]));
                    Timestamp end = new Timestamp(
                            DateUtil.dateToStamp(conditionVO.getRelease_time()[1]));
                    predicateList.add(criteriaBuilder.between(root.get("release_time").as(Timestamp.class),
                            start, end));
                }
                if (conditionVO.getImplement_time() != null &&
                        conditionVO.getImplement_time().length != 0) {
                    Timestamp start = new Timestamp(
                            DateUtil.dateToStamp(conditionVO.getImplement_time()[0]));
                    Timestamp end = new Timestamp(
                            DateUtil.dateToStamp(conditionVO.getImplement_time()[1]));
                    predicateList.add(criteriaBuilder.between(root.get("implement_time").as(Timestamp.class),
                            start, end));
                }
//                if (!conditionVO.getDepartment().equals("")) {
//                    predicateList.add(criteriaBuilder.like(root.get("department").as(String.class),
//                            "%" + conditionVO.getDepartment() + "%"));
//                }
                if (conditionVO.getDepartment().length > 0) {
                    for (String d : conditionVO.getDepartment()) {
                        predicateList.add(criteriaBuilder.like(root.get("department").as(String.class),
                                "%" + d + "%"));
                    }
                }
                if (!conditionVO.getStatus().equals("")) {
                    int flag = conditionVO.getStatus().equals("true") ? 1 : 0;
                    predicateList.add(criteriaBuilder.equal(root.get("status").as(String.class), flag));
                }

                return criteriaBuilder.and(predicateList.toArray(
                        new Predicate[predicateList.size()]
                ));
            }
        };

        try {
            //结果按id升序排序
            result = paperDAO.findAll(queryCondition, PageRequest.of(
                    conditionVO.getPageNum() - 1, pageSize, Sort.by(Sort.Direction.ASC, "id"))
            ).getContent();
            // result=paperDAO.findAll(queryCondition);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("分页查询失败");
        }

        return result;

    }

    /**
     * 多条件分页查询并且分页
     *
     * @param conditionVO 条件集合
     * @return
     */
    public Long count(ConditionVO conditionVO) {

        Long total = 0L;
        Specification<Papers> queryCondition = new Specification<Papers>() {
            @Override
            public Predicate toPredicate(Root<Papers> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicateList = new ArrayList<Predicate>();

                if (!conditionVO.getTitle().equals("")) {
                    //System.out.println("标题");
                    predicateList.add(criteriaBuilder.like(root.get("title").as(String.class),
                            "%" + conditionVO.getTitle() + "%"));

                }
                if (!conditionVO.getGrade().equals("")) {
                    //System.out.println("等级");
                    predicateList.add(criteriaBuilder.equal(root.get("grade").as(String.class),
                            conditionVO.getGrade()));
                }
                if (conditionVO.getRelease_time() != null &&
                        conditionVO.getRelease_time().length != 0) {
                    // System.out.println("时间");
                    Timestamp start = new Timestamp(
                            DateUtil.dateToStamp(conditionVO.getRelease_time()[0]));
                    Timestamp end = new Timestamp(
                            DateUtil.dateToStamp(conditionVO.getRelease_time()[1]));
                    predicateList.add(criteriaBuilder.between(root.get("release_time").as(Timestamp.class),
                            start, end));

                }
                if (conditionVO.getImplement_time() != null &&
                        conditionVO.getImplement_time().length != 0) {
                    Timestamp start = new Timestamp(
                            DateUtil.dateToStamp(conditionVO.getImplement_time()[0]));
                    Timestamp end = new Timestamp(
                            DateUtil.dateToStamp(conditionVO.getImplement_time()[1]));
                    predicateList.add(criteriaBuilder.between(root.get("implement_time").as(Timestamp.class),
                            start, end));
                }
//                if (!conditionVO.getDepartment().equals("")) {
//                    predicateList.add(criteriaBuilder.like(root.get("department").as(String.class),
//                            "%" + conditionVO.getDepartment() + "%"));
//                }
                if (conditionVO.getDepartment().length > 0) {
                    for (String d : conditionVO.getDepartment()) {
                        predicateList.add(criteriaBuilder.like(root.get("department").as(String.class),
                                "%" + d + "%"));
                    }
                }
                if (!conditionVO.getStatus().equals("")) {
                    int flag = conditionVO.getStatus().equals("true") ? 1 : 0;
                    predicateList.add(criteriaBuilder.equal(root.get("status").as(String.class), flag));
                }
//                System.out.println(predicateList.size());
//                System.out.println(predicateList.get(1));

                return criteriaBuilder.and(predicateList.toArray(
                        new Predicate[predicateList.size()]
                ));
            }
        };

        try {
            total = paperDAO.count(queryCondition);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("分页查询失败");
        }

        return total;

    }

}