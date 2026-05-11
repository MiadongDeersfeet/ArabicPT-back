package com.arabicpt.audio.mapper;

import com.arabicpt.audio.model.dto.SentenceAudioResponseDTO;
import com.arabicpt.audio.model.vo.SentenceAudioVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SentenceAudioMapper {

    SentenceAudioResponseDTO selectActiveBySentenceId(
        @Param("sentenceId") Long sentenceId,
        @Param("memberId") Long memberId
    );

    int deactivateActiveBySentenceId(
        @Param("sentenceId") Long sentenceId,
        @Param("memberId") Long memberId
    );

    int insertSentenceAudio(SentenceAudioVO audio);

    SentenceAudioResponseDTO selectByAudioId(
        @Param("audioId") Long audioId,
        @Param("memberId") Long memberId
    );
}
