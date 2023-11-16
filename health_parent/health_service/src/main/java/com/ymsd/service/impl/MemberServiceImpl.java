package com.ymsd.service.impl;

import com.ymsd.dao.IMemberDao;
import com.ymsd.pojo.Member;
import com.ymsd.service.IMemberService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@Transactional
public class MemberServiceImpl implements IMemberService {

    @Resource
    private IMemberDao memberDao;

    @Override
    public Member checkIdCardAndOrderDate(String idCard, String orderDate) {
        return memberDao.checkIdCardAndOrderDate(idCard);
    }

    @Override
    public List<Integer> findMemberCountByMonth(ArrayList<String> monthList) {
        ArrayList<Integer> memberCounts = new ArrayList<>();
        for (String month : monthList) {
            String startTime = month + "." + 1;
            String endTime = month + "." + 31;
            HashMap<String, Object> map = new HashMap<>();
            map.put("startTime",startTime);
            map.put("endTime",endTime);
            Integer memberCount = memberDao.findMemberCountByMonth(map);
            memberCounts.add(memberCount);
        }
        return memberCounts;
    }
}
