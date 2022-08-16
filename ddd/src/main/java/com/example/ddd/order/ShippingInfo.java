package com.example.ddd.order;

import lombok.Data;

@Data
public class ShippingInfo {

    private String receiverName;
    private String receiverPhone;
    private String receiverAddress;
    private String receiverZipCode;
}
