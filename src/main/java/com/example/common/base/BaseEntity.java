package com.example.common.base;

import com.alibaba.fastjson.annotation.JSONField;
import com.example.common.annotations.sql.Column;
import com.example.common.annotations.sql.Exclude;
import com.example.common.annotations.sql.Id;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
public abstract class BaseEntity<ID extends Serializable> implements Serializable {

    @Exclude
    private static final long serialVersionUID = 1L;

    @Id
    private ID id;

    @JSONField
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date createTime;

    @JSONField
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date updateTime;

    @JSONField
    @Column(name = "is_delete")
    private Integer deleteStatus;
}
