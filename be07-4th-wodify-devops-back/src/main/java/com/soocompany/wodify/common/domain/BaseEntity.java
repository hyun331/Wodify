package com.soocompany.wodify.common.domain;

import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass
public abstract class BaseEntity {

    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdTime;

    @Column(nullable = false, columnDefinition = "char(1) default 'N'")
    private String delYn = "N";

    public void updateDelYn(){
        this.delYn = "Y";
    }
    public void updateDelyN(){
        this.delYn = "N";
    }
}