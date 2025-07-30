package com.erokin.generator.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.erokin.generator.model.entity.Generator;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author <a href="https://github.com/EROQIN">Erokin</a>
 * @description 针对表【generator(代码生成器)】的数据库操作Mapper
 * @createDate 2024-02-28 20:58:33
 * @Entity com.erokin.generator.model.entity.Generator
 */
public interface GeneratorMapper extends BaseMapper<Generator> {

    @Select("select id, distPath from generator where isDelete = 1")
    List<Generator> listDeletedGenerator();

}




