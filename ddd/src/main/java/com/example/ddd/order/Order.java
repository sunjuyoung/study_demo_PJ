package com.example.ddd.order;


import lombok.Data;

import java.util.List;

/**
 *
 * 최소 한 종류 이상의 상품을 주문해야 한다
 * 출고를 하면 배송지정보를 변결할 수 없다.
 * 고객이 결제를 완료하기 전에는 상품배송(준비)하지 않는다.
 * 출고 전에는 취소가 가능하다
 *
 */
@Data
public class Order {

    private OrderState orderState;
    private ShippingInfo shippingInfo;
    private List<OrderLine> orderLines;
    private int totalAmount;

    //주문할때 주문상품, 배송지 정보
    public Order(ShippingInfo shippingInfo, List<OrderLine> orderLines) {
        setOrderLines(orderLines);
        setShippingInfo(shippingInfo);
    }

    //주문 취소는 배송전에 할 수 있다
    public void cancel(){
        verifyNotYetShipping();
        this.orderState = OrderState.CANCELED;
    }

    private void verifyNotYetShipping() {
        if(orderState != OrderState.PREPARING && orderState != OrderState.PAYMENT_WAITING ){
            throw new IllegalStateException("yeah it's too late");
        }
    }


    public void setShippingInfo(ShippingInfo shippingInfo) {
        if(shippingInfo == null){
            throw new IllegalArgumentException("no shippingInfo");
        }
        this.shippingInfo = shippingInfo;
    }

    public void setOrderLines(List<OrderLine> orderLines){
        this.orderLines = orderLines;
        calculateTotalAmount();
    }

    //한개 이상 주문해야 한다
    public void verifyAtLeastOneMoreOrder(List<OrderLine> orderLines){
        if(orderLines == null || orderLines.isEmpty()){
            throw new IllegalStateException("no orderLine");
        }
    }

    //총 주문 금액은 각 상품의 구매 가격 합을 모두 더한 금액이다
    private void calculateTotalAmount() {
        this.totalAmount =  orderLines.stream().mapToInt(x->x.getAmount()).sum();
    }


    public boolean isShippingChangeable(){
        return orderState == OrderState.PAYMENT_WAITING ||
                orderState == OrderState.PREPARING;
    }

    //출고 전 배송지를 변경할 수 있다.
    public void changeShippingInfo(ShippingInfo shippingInfo){
        if(!isShippingChangeable()){
            throw new IllegalStateException("can't change ShippingInfo... state : "+orderState);
        }
        this.shippingInfo = shippingInfo;
    }



}
