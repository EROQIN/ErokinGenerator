package com.erokin.maker.meta;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.json.JSONUtil;

public class MetaManager {
    //饿汉式单例模式：
    private static volatile Meta meta;
    //私有构造函数，防止在外部实例化
    private MetaManager() {}
    public static Meta getMetaObject(){
        //锁是一个极度消耗性能的操作，在锁外进行判断，避免重复被锁，两次检查，所以说是双检锁
        if(meta == null) {
            synchronized (MetaManager.class) {
                if (meta == null) {
                    meta = initMeta();
                }
            }
        }
        return meta;
    }
    private static Meta initMeta(){
        //String metaJson = ResourceUtil.readUtf8Str("meta.json");
        String metaJson = ResourceUtil.readUtf8Str("springboot-init-meta.json");
        Meta newMeta = JSONUtil.toBean(metaJson, Meta.class);
        return newMeta;
    }




}
