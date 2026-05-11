package com.arabicpt.member.service;

import com.arabicpt.member.model.dto.MemberDTO;
import com.arabicpt.member.model.dto.MemberUpsertDTO;

public interface MemberService {
    MemberDTO findById(Long memberId);
    MemberDTO findByGoogleSub(String googleSub);
    MemberDTO findByEmail(String email);
    Long upsert(MemberUpsertDTO memberUpsertDTO);
    void touchLastLogin(Long memberId);
}
