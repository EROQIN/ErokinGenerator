package com.erokin.generator.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.erokin.generator.model.dto.generator.GeneratorQueryRequest;
import com.erokin.generator.model.entity.Generator;
import com.erokin.generator.model.vo.GeneratorVO;

import javax.servlet.http.HttpServletRequest;

/**
 * @author <a href="https://github.com/EROQIN">Erokin</a>
 * @description 针对表【generator(代码生成器)】的数据库操作Service
 * @createDate 2024-02-28 20:58:33
 */
public interface GeneratorService extends IService<Generator> {

    /**
     * 校验
     *
     * @param generator
     * @param add
     */
    void validGenerator(Generator generator, boolean add);

    /**
     * 获取查询条件
     *
     * @param generatorQueryRequest
     * @return
     */
    QueryWrapper<Generator> getQueryWrapper(GeneratorQueryRequest generatorQueryRequest);


    /**
     * 获取帖子封装
     *
     * @param generator
     * @param request
     * @return
     */
    GeneratorVO getGeneratorVO(Generator generator, HttpServletRequest request);

    /**
     * 分页获取帖子封装
     *
     * @param generatorPage
     * @param request
     * @return
     */
    Page<GeneratorVO> getGeneratorVOPage(Page<Generator> generatorPage, HttpServletRequest request);

    /**
     * 缓存生成器
     *
     * @param id
     */
    void cacheGenerator(long id);

    /**
     * 获取缓存文件路径
     *
     * @param id
     * @param distPath
     * @return
     */
    String getCacheFilePath(long id, String distPath);
}

