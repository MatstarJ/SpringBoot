package com.matstar.ex5.entity;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
//JPA 내에서 엔티티 객체가 생성/변경되는 것을 감지하는 역할 ex4Application을 같이 수정해야 한다.
@EntityListeners(value={AuditingEntityListener.class})
@Getter
public class BaseEntity {


    @CreatedDate
    @Column(name="regdate",updatable = false)
    private LocalDateTime regDate;

    @LastModifiedDate
    @Column(name="moddate")
    private LocalDateTime modDate;
}
