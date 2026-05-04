package com.arabicpt.folder.mapper;

import com.arabicpt.folder.model.vo.FolderVO;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface FolderMapper {
    List<FolderVO> selectFolderList(@Param("memberId") Long memberId);

    FolderVO selectFolderById(@Param("folderId") Long folderId, @Param("memberId") Long memberId);

    FolderVO selectDefaultFolder(@Param("memberId") Long memberId);

    int insertFolder(FolderVO folder);

    int countFolderName(@Param("memberId") Long memberId, @Param("folderName") String folderName);

    int updateFolder(FolderVO folder);

    int softDeleteFolder(@Param("folderId") Long folderId, @Param("memberId") Long memberId);

    int detachSentenceSetsFromFolder(@Param("folderId") Long folderId, @Param("memberId") Long memberId);
}
