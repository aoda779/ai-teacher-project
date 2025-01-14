package org.springbus.moban.web.response;

import lombok.Data;

import java.util.List;

@Data
//Page<T> 类是一个简单的分页数据容器，用于封装分页查询的结果。
public class Page<T> {

    //List<T> 是 Java 集合框架中非常强大且灵活的数据结构，广泛用于存储和管理有序的、可重复的元素集合
    private List<T> items;
    private Long total = 0L;
}
