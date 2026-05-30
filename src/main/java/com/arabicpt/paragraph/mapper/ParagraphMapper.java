package com.arabicpt.paragraph.mapper;

import com.arabicpt.paragraph.model.dto.ParagraphResponseDTO;
import com.arabicpt.paragraph.model.vo.ParagraphVO;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ParagraphMapper {

    int insertParagraph(ParagraphVO paragraph);

    List<ParagraphResponseDTO> selectParagraphListBySetId(
        @Param("paragraphSetId") Long paragraphSetId,
        @Param("memberId") Long memberId
    );

    ParagraphResponseDTO selectParagraphById(
        @Param("paragraphId") Long paragraphId,
        @Param("memberId") Long memberId
    );

    int updateParagraph(ParagraphVO paragraph);

    int softDeleteParagraph(
        @Param("paragraphId") Long paragraphId,
        @Param("memberId") Long memberId
    );

    int softDeleteParagraphsBySetId(
        @Param("paragraphSetId") Long paragraphSetId,
        @Param("memberId") Long memberId
    );

    int countActiveSetById(
        @Param("paragraphSetId") Long paragraphSetId,
        @Param("memberId") Long memberId
    );
}
