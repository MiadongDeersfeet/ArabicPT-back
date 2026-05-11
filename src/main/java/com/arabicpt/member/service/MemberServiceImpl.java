package com.arabicpt.member.service;

import com.arabicpt.member.mapper.MemberMapper;
import com.arabicpt.member.model.dto.MemberDTO;
import com.arabicpt.member.model.dto.MemberUpsertDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberMapper memberMapper;

    @Override
    public MemberDTO findById(Long memberId) {
        return memberMapper.selectByMemberId(memberId);
    }

    @Override
    public MemberDTO findByGoogleSub(String googleSub) {
        return memberMapper.selectByGoogleSub(googleSub);
    }

    @Override
    public MemberDTO findByEmail(String email) {
        return memberMapper.selectByEmail(email);
    }

    @Override
    @Transactional
    public Long upsert(MemberUpsertDTO memberUpsertDTO) {
        if (memberUpsertDTO.getMemberId() == null) {
            memberMapper.insertMember(memberUpsertDTO);
        } else {
            memberMapper.updateMember(memberUpsertDTO);
        }
        return memberUpsertDTO.getMemberId();
    }

    @Override
    @Transactional
    public void touchLastLogin(Long memberId) {
        memberMapper.updateLastLoginAt(memberId);
    }
}
