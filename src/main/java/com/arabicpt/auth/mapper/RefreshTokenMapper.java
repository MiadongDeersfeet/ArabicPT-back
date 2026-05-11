package com.arabicpt.auth.mapper;

import com.arabicpt.auth.model.dto.RefreshTokenRecordDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;

@Mapper
public interface RefreshTokenMapper {
    RefreshTokenRecordDTO selectActiveByToken(@Param("refreshToken") String refreshToken);
    void revokeAllByMemberId(@Param("memberId") Long memberId);
    void revokeByToken(@Param("refreshToken") String refreshToken);
    void insertRefreshToken(@Param("memberId") Long memberId,
                            @Param("refreshToken") String refreshToken,
                            @Param("expiresAt") LocalDateTime expiresAt);
}
