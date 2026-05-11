package com.arabicpt.member.mapper;

import com.arabicpt.member.model.dto.MemberDTO;
import com.arabicpt.member.model.dto.MemberUpsertDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MemberMapper {
    MemberDTO selectByMemberId(@Param("memberId") Long memberId);
    MemberDTO selectByGoogleSub(@Param("googleSub") String googleSub);
    MemberDTO selectByEmail(@Param("email") String email);
    void insertMember(MemberUpsertDTO memberUpsertDTO);
    void updateMember(MemberUpsertDTO memberUpsertDTO);
    void updateLastLoginAt(@Param("memberId") Long memberId);
}
