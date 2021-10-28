package com.example.webpractice.blImpl.page;

import com.example.webpractice.bl.page.PageService;
import com.example.webpractice.po.Papers;
import com.example.webpractice.vo.ConditionVO;
import com.example.webpractice.vo.PaperVO;
import com.example.webpractice.vo.ResponseVO;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author MengYuxin
 * @Date 2021/10/28 16:22
 */
@Service
public class PageServiceImpl implements PageService {

    @Override
    public ResponseVO page(ConditionVO conditionVO) {

        List<PaperVO>paperVOS=null;

        Specification<Papers>queryCondition=new Specification<Papers>() {
            @Override
            public Predicate toPredicate(Root<Papers> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {

                List<Predicate>predicateList=new ArrayList<>();

                if(conditionVO.getTitle()!=null){
                    predicateList.add(criteriaBuilder.equal(root.get("title"),
                            conditionVO.getTitle()));

                }


                return null;
            }
        };









        return null;
    }
}
