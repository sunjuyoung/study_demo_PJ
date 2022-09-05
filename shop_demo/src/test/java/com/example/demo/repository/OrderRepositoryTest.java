package com.example.demo.repository;

import com.example.demo.entity.Item;
import com.example.demo.entity.Member;
import com.example.demo.entity.Order;
import com.example.demo.entity.OrderItem;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OrderRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ItemRepository itemRepository;
    @Autowired
    EntityManager em;

    @Test
    @Transactional
    public void test() throws Exception{
        //given


        Member member = memberRepository.findByEmail("syseoz@naver.com");
        Item item = itemRepository.findByItemName("테스트");
        OrderItem orderItem = new OrderItem(item,2);
        Order order = new Order(orderItem,member);
        orderItem.setOrder(order);
        orderRepository.save(order);


        Order order1 = orderRepository.findById(order.getId()).orElseThrow(EntityNotFoundException::new);


        //when

        //then
    }

}