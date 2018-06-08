package com.spcl.util;

import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.AbstractConfigurableTemplateResolver;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.FileTemplateResolver;

/**
 * Created by Administrator on 2018/6/8.
 */
public class UtilSystem {
    AbstractConfigurableTemplateResolver systemTemplateResolver; //自动识别模板加载，开发环境下不缓存，改动即时刷新
    private UtilSystem(){
        String srcDirPath = UtilSystem.getSrcDirPath();
        AbstractConfigurableTemplateResolver templateResolver = null;
        if(srcDirPath != null) // 如果使用spring-boot从源码目录启动，使用文件系统加载模板，不缓存内容，改动即时刷新
        {
            srcDirPath+="/main/resources/templates/";
            templateResolver = new FileTemplateResolver();
            templateResolver.setPrefix(srcDirPath);
            templateResolver.setCacheable(false);
        }
        else
        {
            templateResolver = new ClassLoaderTemplateResolver();
            templateResolver.setPrefix("templates/");
            templateResolver.setCacheable(true);
        }
        templateResolver.setCharacterEncoding("utf-8");
        templateResolver.setSuffix(".html");
        // HTML is the default value, added here for the sake of clarity.
        templateResolver.setTemplateMode(TemplateMode.HTML);
        this.systemTemplateResolver = templateResolver;
        // Template cache is true by default. Set to false if you want
        // templates to be automatically updated when modified.
    }
    public static UtilSystem getSingleton(){
        return SingletonHolder.singleton;
    }
    private static class SingletonHolder{
        private final static UtilSystem singleton = new UtilSystem();
    }
    public static String getSrcDirPath()
    {
        String classFilePath = UtilSystem.class.getResource("").getPath();
        String classDir = "/target/classes/com/spcl/util/";
        if(classFilePath.endsWith(classDir))
        {
            return classFilePath.substring(0,classFilePath.length() - classDir.length())+"/src";
        }
        return null;
    }

    public static AbstractConfigurableTemplateResolver getTemplateResolver()
    {
        return getSingleton().systemTemplateResolver;
    }
}
