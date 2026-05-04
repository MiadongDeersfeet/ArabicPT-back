package com.arabicpt.audio.mapper;

import com.arabicpt.audio.model.dto.SentenceAudioResponseDTO;
import com.arabicpt.audio.model.vo.SentenceAudioVO;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AudioMapper {

    int insertSentenceAudio(SentenceAudioVO audio);

    List<SentenceAudioResponseDTO> selectSentenceAudioList(
        @Param("sentenceId") Long sentenceId,
        @Param("memberId") Long memberId
    );
}
