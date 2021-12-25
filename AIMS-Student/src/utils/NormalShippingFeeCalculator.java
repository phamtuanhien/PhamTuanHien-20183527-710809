package utils;

import entity.order.Order;

import java.util.Random;
// Pham Tuan Hien - 20183527
public class NormalShippingFeeCalculator implements ShippingFeeCalculator{

    @Override
    public int calculateShippingFee(Order order) {
        Random rand = new Random();
        int fees = (int)( ( (rand.nextFloat()*10)/100 ) * order.getAmount() );
        return fees;
    }
}
