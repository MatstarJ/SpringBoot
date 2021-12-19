package com.matstar.guestbook.entity;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass  // 해당 어노테이션이 적용된 클래스는 테이블로 생성되지 않는다.
@EntityListeners(value={AuditingEntityListener.class})
// AuditingEntityListener : JPA 내부에서 엔티티 객체가 생성 변경되는 것을 감지한다.
//@EntityListeners를 사용하기 위해서는 프로젝트 생성시 존재하는 Application을 수정해야 한다.
@Getter
abstract class BaseEntity {

    //엔티티 객체의 등록시간과 최종 수정 시간을 담당하는 클래스.


    @CreatedDate  //JPA에서 엔티티 객체가 생성 되는 것을 감지하여 처리한다.
    @Column(name="regdate",updatable = false)
    // 해당 엔티티 객체를 데이터 베이스에 반영할 때 regdate 컬럼값은 변경(update)되지 않는다.(최초의 값을 그대로 가져간다.)
    private LocalDateTime regDate;

    @LastModifiedDate //JPA에서 객체의 최종 수정 시간을 자동으로 처리한다.
    @Column(name="moddate")
    private LocalDateTime moDate;
}
