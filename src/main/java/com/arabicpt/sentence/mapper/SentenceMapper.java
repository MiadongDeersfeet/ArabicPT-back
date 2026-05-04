package com.arabicpt.sentence.mapper;

import com.arabicpt.sentence.model.dto.SentenceResponseDTO;
import com.arabicpt.sentence.model.vo.SentenceVO;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SentenceMapper {

    int insertSentence(SentenceVO sentence);

    List<SentenceResponseDTO> selectSentenceListBySetId(
        @Param("setId") Long setId,
        @Param("memberId") Long memberId
    );

    SentenceResponseDTO selectSentenceById(
        @Param("sentenceId") Long sentenceId,
        @Param("memberId") Long memberId
    );

    int updateSentence(SentenceVO sentence);

    int softDeleteSentence(@Param("sentenceId") Long sentenceId, @Param("memberId") Long memberId);

    int softDeleteSentencesBySetId(@Param("setId") Long setId, @Param("memberId") Long memberId);

    int countActiveSetById(@Param("setId") Long setId, @Param("memberId") Long memberId);
}
