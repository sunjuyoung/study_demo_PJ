package com.example.ddd.order;

import com.example.ddd.common.Address;
import com.example.ddd.common.Receiver;
import lombok.Data;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;

@Data
@Embeddable
public class ShippingInfo {

    @Embedded
    private Receiver receiver;
    @Embedded
    private Address address;

 /*   private String receiverName;
    private String receiverPhone;
    private String receiverAddress;
    private String receiverZipCode;*/
}
