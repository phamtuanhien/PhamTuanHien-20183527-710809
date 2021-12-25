package utils;

import entity.order.Order;
// Pham Tuan Hien - 20183527
public interface ShippingFeeCalculator {
    int calculateShippingFee(Order order);
}
