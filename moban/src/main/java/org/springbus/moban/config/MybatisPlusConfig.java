package org.springbus.moban.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//这段代码的主要作用是配置 MyBatis-Plus 的分页插件，以便在执行数据库查询时能够轻松实现分页功能。通过这种方式，开发者不需要为每个查询手写分页逻辑，从而提高了开发效率和代码的可维护性。此外，配置类还设置了 MyBatis Mapper 接口的自动扫描路径，使得映射接口可以被 Spring 自动识别和管理。
@Configuration
@MapperScan("org.springbus.moban.mapper")
public class MybatisPlusConfig {

    /**
     * 添加分页插件
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));//如果配置多个插件,切记分页最后添加
        //interceptor.addInnerInterceptor(new PaginationInnerInterceptor()); 如果有多数据源可以不配具体类型 否则都建议配上具体的DbType
        return interceptor;
    }
}
