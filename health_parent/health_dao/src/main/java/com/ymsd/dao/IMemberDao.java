package com.ymsd.dao;

import com.ymsd.pojo.Member;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;

@Mapper
public interface IMemberDao {

    Member checkIdCardAndOrderDate(@Param("idCard") String idCard);

    void addMember(Member member1);

    Member findById(Integer memberId);

    Integer findMemberCountByMonth(HashMap<String, Object> map);

    Integer findCount();

    Integer findThisWeekMemberCount(HashMap<Object, Object> memberTime);

    Integer thisMonthNewMemberCount(HashMap<Object, Object> memberTime2);
}
