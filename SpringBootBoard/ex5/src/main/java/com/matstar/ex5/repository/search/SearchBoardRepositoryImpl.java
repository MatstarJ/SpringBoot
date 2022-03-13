package com.matstar.ex5.repository.search;

import com.matstar.ex5.entity.QBoard;
import com.matstar.ex5.entity.QMember;
import com.matstar.ex5.entity.QReply;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import com.matstar.ex5.entity.Board;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
public class SearchBoardRepositoryImpl extends QuerydslRepositorySupport implements SearchBoardRepository{

    public SearchBoardRepositoryImpl() {
        super(Board.class);
    }


    @Override
    public Board search1() {

        log.info("search1 ...................");

//        QBoard board = QBoard.board;
//        JPQLQuery<Board> jpqlQuery = from(board);
//        jpqlQuery.select(board).where(board.bno.eq(1L));

        QBoard board = QBoard.board;
        QReply reply = QReply.reply;
        QMember member = QMember.member;

        JPQLQuery<Board> jpqlQuery = from(board);
        jpqlQuery.leftJoin(member).on(board.writer.eq(member));
        jpqlQuery.leftJoin(reply).on(reply.board.eq(board));

        //jpqlQuery.select(board,member.email,reply.count()).groupBy(board);

        JPQLQuery<Tuple> tuple = jpqlQuery.select(board, member.email, reply.count());
        tuple.groupBy(board);

        log.info("---------------------------------");
        log.info(tuple);
        log.info("---------------------------------");

        List<Tuple> result = tuple.fetch();

        log.info(result);


        return null;
    }

    @Override
    public Page<Object[]> searchPage(String type, String keyword, Pageable pageable) {

        log.info("searchPage.........");

        QBoard board = QBoard.board;
        QReply reply = QReply.reply;
        QMember member = QMember.member;

        JPQLQuery<Board> jpqlQuery = from(board);
        jpqlQuery.leftJoin(member).on(board.writer.eq(member));
        jpqlQuery.leftJoin(reply).on(reply.board.eq(board));

        //select b, w, count(r) from Board b left join b.writer w left join reply r on r.board = b
        JPQLQuery<Tuple> tuple = jpqlQuery.select(board, member, reply.count());


        BooleanBuilder booleanBuilder = new BooleanBuilder();
        BooleanExpression expression = board.bno.gt(0L);
        booleanBuilder.and(expression);

        if(type != null) {
            String[] typeArr = type.split("");
            //검색조건 작성
            BooleanBuilder conditionBuilder = new BooleanBuilder();

            for(String t : typeArr) {

                switch(t) {
                    case "t" : conditionBuilder.or(board.title.contains(keyword));
                        break;

                    case "w" : conditionBuilder.or(member.email.contains(keyword));
                        break;

                    case "c" : conditionBuilder.or(board.content.contains(keyword));
                        break;
                }
            }

            booleanBuilder.and(conditionBuilder);

        }
        tuple.where(booleanBuilder);

        //orderBy 처리
        Sort sort = pageable.getSort();

        log.info("getSort 확인 : " + sort);

        //tuple.orderBy(board.bno.desc());

        sort.stream().forEach(order -> {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
            String prop = order.getProperty();
            log.info("Order direction 확인 : " + direction);
            log.info("order.getProperty 확인 : " + prop);
            PathBuilder orderByExpression = new PathBuilder(Board.class,"board");
            tuple.orderBy(new OrderSpecifier(direction, orderByExpression.get(prop)));
        });

        tuple.groupBy(board);


        //Page 처리
        tuple.offset(pageable.getOffset());
        tuple.limit(pageable.getPageSize());

        log.info("getoffset : " + pageable.getOffset());
        log.info("getpagesize : " + pageable.getPageSize());

        List<Tuple> result = tuple.fetch();

        log.info(result);

        long count = tuple.fetchCount();

        log.info("COUNT :" + count);

        //Page 타입의 객체는 PageImpl(List<T> content, Pageable pageable, long total)의 생성자로 만든다.
        return new PageImpl<Object[]>(result.stream().map( t->t.toArray()).collect(Collectors.toList()),pageable,count);
    }
}
