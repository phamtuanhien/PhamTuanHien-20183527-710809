package entity.order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import utils.Configs;

public class Order {
    
    private int shippingFees;
    private List lstOrderMedia;
    private HashMap<String, String> deliveryInfo;

    public Order(){
        this.lstOrderMedia = new ArrayList<>();
    }

    public Order(List lstOrderMedia) {
        this.lstOrderMedia = lstOrderMedia;
    }

    public void addOrderMedia(OrderMedia om){
        this.lstOrderMedia.add(om);
    }

    public void removeOrderMedia(OrderMedia om){
        this.lstOrderMedia.remove(om);
    }

    public List getlstOrderMedia() {
        return this.lstOrderMedia;
    }

    public void setlstOrderMedia(List lstOrderMedia) {
        this.lstOrderMedia = lstOrderMedia;
    }

    public void setShippingFees(int shippingFees) {
        this.shippingFees = shippingFees;
    }

    public int getShippingFees() {
        return shippingFees;
    }

    public HashMap getDeliveryInfo() {
        return deliveryInfo;
    }
    // Pham Tuan Hien - 20183527
    public boolean isRush() {
        return deliveryInfo.get("isRush").equals("yes");
    }

    public void setDeliveryInfo(HashMap deliveryInfo) {
        this.deliveryInfo = deliveryInfo;
    }

    public int getAmount(){
        double amount = 0;
        for (Object object : lstOrderMedia) {
            OrderMedia om = (OrderMedia) object;
            amount += om.getPrice();
        }
        return (int) (amount + (Configs.PERCENT_VAT/100)*amount);
    }

    public float getTotalWeight() {
        float totalWeight = 0;
        for (Object object : lstOrderMedia) {
            OrderMedia om = (OrderMedia) object;
            totalWeight += om.getMedia().getWeight();
        }
        return totalWeight;
    }

    public float getTotalAlternativeWeight() {
        float totalAlternativeWeight = 0;
        for (Object object : lstOrderMedia) {
            OrderMedia om = (OrderMedia) object;
            totalAlternativeWeight += om.getMedia().getAlternativeWeight();
        }
        return totalAlternativeWeight;
    }

}
