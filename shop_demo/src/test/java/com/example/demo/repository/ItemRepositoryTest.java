package com.example.demo.repository;

import com.example.demo.entity.Item;
import com.example.demo.entity.ItemSellStatus;
import com.example.demo.entity.QItem;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;

import java.util.List;

import static com.example.demo.entity.QItem.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ItemRepositoryTest {

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    EntityManager em;


    @Test
    @Rollback(value = false)
    public void test() throws Exception{
        //given

        for(int i=30; i<40; i++){
            Item item = Item.builder()
                    .itemDetail("테스트 상세설명" + i)
                    .itemName("테스트" + i)
                    .itemSellStatus(ItemSellStatus.SOLD_OUT)
                    .price(100 + i)
                    .stockNumber(i)
                    .build();
            Item save = itemRepository.save(item);
        }
    }

    @Test
    public void findTest() throws Exception{
        //given
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        JPAQuery<Item> itemJPAQuery = queryFactory.selectFrom(item)
                .where(item.price.lt(110))
                .orderBy(item.itemName.desc());
        List<Item> itemList = itemJPAQuery.fetch();

        for(Item item : itemList){
            System.out.println(itemList.toString());
        }

    }
    @Test
    public void quseryTest2() throws Exception{
        //given

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        String itemDetail = "테스트 상세설명";
        int price = 105;
        String itemSellStat = "SELL";

        booleanBuilder.and(item.itemDetail.like("%"+itemDetail+"%"));
        booleanBuilder.and(item.price.gt(price));
        if(StringUtils.equals(itemSellStat,ItemSellStatus.SELL)){
            booleanBuilder.and(item.itemSellStatus.eq(ItemSellStatus.SELL));
        }

        Pageable pageable = PageRequest.of(0,5);
        Page<Item> itemPageResult = itemRepository.findAll(booleanBuilder, pageable);

        List<Item> content = itemPageResult.getContent();
        for(Item item : content){
            System.out.println(item.toString());
        }


        //when

        //then
    }

}