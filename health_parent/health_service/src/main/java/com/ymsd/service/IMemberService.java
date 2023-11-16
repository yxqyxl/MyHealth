package com.ymsd.service;

import com.ymsd.pojo.Member;

import java.util.ArrayList;
import java.util.List;

public interface IMemberService {
    Member checkIdCardAndOrderDate(String idCard, String orderDate);

    List<Integer> findMemberCountByMonth(ArrayList<String> monthList);
}
