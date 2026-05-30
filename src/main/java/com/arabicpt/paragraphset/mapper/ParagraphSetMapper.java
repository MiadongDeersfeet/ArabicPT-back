package com.arabicpt.paragraphset.mapper;

import com.arabicpt.paragraphset.model.dto.ParagraphSetResponseDTO;
import com.arabicpt.paragraphset.model.vo.ParagraphSetVO;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ParagraphSetMapper {

    int insertParagraphSet(ParagraphSetVO paragraphSet);

    List<ParagraphSetResponseDTO> selectParagraphSetList(
        @Param("memberId") Long memberId,
        @Param("folderId") Long folderId
    );

    ParagraphSetResponseDTO selectParagraphSetById(
        @Param("paragraphSetId") Long paragraphSetId,
        @Param("memberId") Long memberId
    );

    int updateParagraphSet(ParagraphSetVO paragraphSet);

    int softDeleteParagraphSet(
        @Param("paragraphSetId") Long paragraphSetId,
        @Param("memberId") Long memberId
    );

    int countActiveById(
        @Param("paragraphSetId") Long paragraphSetId,
        @Param("memberId") Long memberId
    );
}
