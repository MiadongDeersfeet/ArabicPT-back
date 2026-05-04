package com.arabicpt.common.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
// mapper 인터페이스를 자동으로 스캔해 Bean 등록합니다.
@MapperScan(basePackages = {
    "com.arabicpt.member.mapper",
    "com.arabicpt.auth.mapper",
    "com.arabicpt.folder.mapper",
    "com.arabicpt.sentence.mapper",
    "com.arabicpt.sentenceset.mapper",
    "com.arabicpt.audio.mapper"
})
public class MyBatisConfig {
}
