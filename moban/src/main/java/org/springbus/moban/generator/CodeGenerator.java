package org.springbus.moban.generator;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.baomidou.mybatisplus.generator.fill.Column;

import java.nio.file.Paths;

public class CodeGenerator {

    public static void main(String[] args) {
        FastAutoGenerator.create("jdbc:mysql://192.168.101.9:3307/moban",
                        "root", "12345678")
                .globalConfig(builder -> builder
                        .author("Mr.Han")
                        .outputDir(Paths.get(System.getProperty("user.dir")) + "/src/main/java/")
                        .commentDate("yyyy-MM-dd")
                        .enableSwagger()
                )
                .packageConfig(builder -> builder
                        .parent("org.springbus.hbit222")
                        .entity("entity")
                        .mapper("mapper")
                        .service("service")
                        .serviceImpl("service.impl")
                        .xml("mapper.xml")
                        .controller("controller")
                )
                .strategyConfig(builder -> builder
                        .enableCapitalMode()    //开启大写命名
                        .enableSkipView()   //创建实体类的时候跳过视图
                        //4.1、实体类策略配置
                        .entityBuilder()
                        .enableChainModel() //开启链式模型
                        //.disableSerialVersionUID()  //默认是开启实体类序列化，可以手动disable使它不序列化。由于项目中需要使用序列化就按照默认开启了
                        .enableTableFieldAnnotation()       // 开启生成实体时生成字段注解
                        .enableLombok() //开启 Lombok
                        .naming(NamingStrategy.underline_to_camel)  //数据库表映射到实体的命名策略：默认是下划线转驼峰命。这里可以不设置
                        .columnNaming(NamingStrategy.underline_to_camel)    //数据库表字段映射到实体的命名策略：下划线转驼峰命。（默认是和naming一致，所以也可以不设置）
                        .addTableFills(
                                new Column("create_time", FieldFill.INSERT),
                                new Column("modify_time", FieldFill.INSERT_UPDATE)
                        )   //添加表字段填充，"create_time"字段自动填充为插入时间，"modify_time"字段自动填充为插入修改时间
                        .idType(IdType.AUTO)    //设置主键自增

                        //4.2、Controller策略配置
                        .controllerBuilder()
                        .enableHyphenStyle()    //开启驼峰连转字符
                        .formatFileName("%sController") //格式化 Controller 类文件名称，%s进行匹配表名，如 UserController
                        .enableRestStyle()  //开启生成 @RestController 控制器

                        //4.3、service 策略配置
                        .serviceBuilder()
                        .formatServiceFileName("%sService") //格式化 service 接口文件名称，%s进行匹配表名，如 UserService
                        .formatServiceImplFileName("%sServiceImpl") //格式化 service 实现类文件名称，%s进行匹配表名，如 UserServiceImpl

                        //4.4、Mapper策略配置
                        .mapperBuilder()
                        .superClass(BaseMapper.class)   //设置父类
                        .enableBaseResultMap()  //启用 BaseResultMap 生成
                        .enableBaseColumnList() //启用 BaseColumnList
                        .formatMapperFileName("%sMapper")   //格式化 mapper 文件名称
                        .enableMapperAnnotation()       //开启 @Mapper 注解
                        .formatXmlFileName("%sXml") //格式化Xml文件名称
                        .formatMapperFileName("%sMapper")   //格式化Mapper文件名称
                )
                .templateEngine(new FreemarkerTemplateEngine())
                .execute();
    }
}
