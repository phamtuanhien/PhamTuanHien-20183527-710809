package utils;

import entity.order.Order;
// Pham Tuan Hien - 20183527
public class NewShippingFeeCalculator implements ShippingFeeCalculator{
    @Override
    public int calculateShippingFee(Order order) {
        int fees = 0;
        int amount = order.getAmount();
        float weight = order.getTotalWeight() + order.getTotalAlternativeWeight();

        // Freeship order amount > 100000
        if (amount >= 100000) {
            return fees;
        }

        String province = order.getDeliveryInfo().get("province").toString();
        if (province.equals("Hà Nội") || province.equals("Hồ Chí Minh")) {
            fees = weight <= 3 ? 22000 : (int) (22000 + 2500 * (weight - 3));
        } else {
            fees = weight <= 0.5 ? 30000 : (int) (30000 + 2500 * (weight - 0.5));
        }

        if (order.isRush()) {
            fees += 10000 * order.getlstOrderMedia().size();
        }

        return fees;
    }
}
