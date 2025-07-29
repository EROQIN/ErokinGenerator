package com.erokin.springbootinit.service;

import com.erokin.generator.model.entity.User;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 帖子点赞服务测试
 *
 * @author <a href="https://github.com/EROQIN">Erokin</a>
 *   
 */
@SpringBootTest
class PostThumbServiceTest {


    private static final User loginUser = new User();

    @BeforeAll
    static void setUp() {
        loginUser.setId(1L);
    }

}
