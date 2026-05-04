package com.arabicpt.sentenceset.mapper;

import com.arabicpt.sentenceset.model.dto.SentenceSetResponseDTO;
import com.arabicpt.sentenceset.model.vo.SentenceSetVO;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SentenceSetMapper {

    int insertSentenceSet(SentenceSetVO sentenceSet);

    List<SentenceSetResponseDTO> selectSentenceSetList(
        @Param("memberId") Long memberId,
        @Param("folderId") Long folderId
    );

    SentenceSetResponseDTO selectSentenceSetById(
        @Param("setId") Long setId,
        @Param("memberId") Long memberId
    );

    int updateSentenceSet(SentenceSetVO sentenceSet);

    int softDeleteSentenceSet(@Param("setId") Long setId, @Param("memberId") Long memberId);

    int countActiveById(@Param("setId") Long setId, @Param("memberId") Long memberId);
}
