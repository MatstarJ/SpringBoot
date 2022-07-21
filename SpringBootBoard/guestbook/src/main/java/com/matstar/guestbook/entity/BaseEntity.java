package com.matstar.guestbook.entity;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;
//해당 어노테이션이 적용된 클래스는 엔티티 객체로 생성되지 않는다
// 다른 엔티티 클래스에 상속하여 추가하는 용도로 사용한다.
@MappedSuperclass
//JPA 내에서 엔티티 객체가 생성/변경되는 것을 감지하는 역할 ex4Application을 같이 수정해야 한다.
@EntityListeners(value={AuditingEntityListener.class})
@Getter
public class BaseEntity {


    @CreatedDate  // JPA에서 엔티티 객체의 생성 시간을 처리함
    @Column(name="regdate",updatable = false)
    private LocalDateTime regDate;

    @LastModifiedDate  // 최종 수정 시간을 처리함
    @Column(name="moddate")
    private LocalDateTime modDate;
}
