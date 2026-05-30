package com.arabicpt.audio.mapper;

import com.arabicpt.audio.model.dto.ParagraphAudioResponseDTO;
import com.arabicpt.audio.model.vo.ParagraphAudioVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ParagraphAudioMapper {

    ParagraphAudioResponseDTO selectActiveByParagraphId(
        @Param("paragraphId") Long paragraphId,
        @Param("memberId") Long memberId
    );

    int deactivateActiveByParagraphId(
        @Param("paragraphId") Long paragraphId,
        @Param("memberId") Long memberId
    );

    int insertParagraphAudio(ParagraphAudioVO paragraphAudio);

    ParagraphAudioResponseDTO selectByAudioId(
        @Param("audioId") Long audioId,
        @Param("memberId") Long memberId
    );
}
