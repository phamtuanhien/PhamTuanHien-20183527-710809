package controller;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Random;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import common.exception.InvalidDeliveryInfoException;
import entity.cart.Cart;
import entity.cart.CartMedia;
import entity.invoice.Invoice;
import entity.order.Order;
import entity.order.OrderMedia;
import utils.NewShippingFeeCalculator;
import utils.NormalShippingFeeCalculator;
import utils.ShippingFeeCalculator;

/**
 * This class controls the flow of place order usecase in our AIMS project
 * @author nguyenlm
 */
public class PlaceOrderController extends BaseController{

    /**
     * Just for logging purpose
     */
    private static final Logger LOGGER = utils.Utils.getLogger(PlaceOrderController.class.getName());

    // Pham Tuan Hien - 20183527
    private ShippingFeeCalculator shippingFeeCalculator;
    public void setShippingFeeCalculator(ShippingFeeCalculator shippingFeeCalculator) {
        this.shippingFeeCalculator = shippingFeeCalculator;
    }

    /**
     * This method checks the avalibility of product when user click PlaceOrder button
     * @throws SQLException SQL Exception
     */
    public void placeOrder() throws SQLException{
        Cart.getCart().checkAvailabilityOfProduct();
    }


    /**
     * This method creates the new Order based on the Cart
     * @return Order
     */
    public Order createOrder() {
        Order order = new Order();
        for (Object object : Cart.getCart().getListMedia()) {
            CartMedia cartMedia = (CartMedia) object;
            OrderMedia orderMedia = new OrderMedia(cartMedia.getMedia(), 
                                                   cartMedia.getQuantity(), 
                                                   cartMedia.getPrice());    
            order.getlstOrderMedia().add(orderMedia);
        }
        return order;
    }

    /**
     * This method creates the new Invoice based on order
     * @param order Order information
     * @return Invoice
     */
    public Invoice createInvoice(Order order) {
        return new Invoice(order);
    }

    /**
     * This method takes responsibility for processing the shipping info from user
     * @param info Delivery info
     */
    public void processDeliveryInfo(HashMap info) {
        LOGGER.info("Process Delivery Info");
        LOGGER.info(info.toString());
        validateDeliveryInfo(info);
    }
    
    /**
   * The method validates the info
   * @param info Delivery info
     */
    public void validateDeliveryInfo(HashMap<String, String> info) {
    	if (!validatePhoneNumber(info.get("phone"))) {
            throw new InvalidDeliveryInfoException("Phone number is invalid");
        }
        if (!validateName(info.get("name"))) {
            throw new InvalidDeliveryInfoException("Name is invalid");
        }
        if (!validateAddress(info.get("address"))) {
            throw new InvalidDeliveryInfoException("Address is invalid");
        }
        if (info.get("isRush").equals("yes")) {
            if (!validateProvince(info.get("province"))) {
                throw new InvalidDeliveryInfoException("Province is not support rush delivery");
            }
            if (!validateDate(info.get("date"))) {
                throw new InvalidDeliveryInfoException("Date is invalid");
            }
        }
    }

    /**
     * @param phoneNumber The phone number of buyer
     * @return phone number is valid or not
     */
    public boolean validatePhoneNumber(String phoneNumber) {
        // Pham Tuan Hien - 20183527
        // TODO: your work
        if (phoneNumber.length() != 10) return false;

        if (!phoneNumber.startsWith("0")) return false;

        try {
            Integer.parseInt(phoneNumber);
        } catch (NumberFormatException e) {
            return false;
        }

    	return true;
    }

    /**
     * @param name name of buyer
     * @return name is valid or not
     */
    public boolean validateName(String name) {
        // Pham Tuan Hien - 20183527
        // TODO: your work

        if (name == null) return false;

        String regx = "[^A-Za-z ]";
        Pattern pattern = Pattern.compile(regx);
        Matcher matcher = pattern.matcher(name);
        return !matcher.find();
    }

    /**
     * @param address delivery address
     * @return delivery address is valid or not
     */
    public boolean validateAddress(String address) {
        // Pham Tuan Hien - 20183527
        // TODO: your work

        if (address == null) return false;

        String regx = "[^A-Za-z0-9 ]";
        Pattern pattern = Pattern.compile(regx);
        Matcher matcher = pattern.matcher(address);
        return !matcher.find();
    }

    /**
     * @param province delivery province
     * @return province is valid or not
     */
    public boolean validateProvince(String province){
        // Pham Tuan Hien - 20183527
        if (province == null) return false;

        return province.equals("Hà Nội");
    }

    /**
     *
     * @param date delivery time
     * @return delivery time is valid or not
     */
    public boolean validateDate(String date){
        // Pham Tuan Hien - 20183527
        if (date == null) return false;
        System.out.println(date);
        return LocalDate.parse(date).isAfter(LocalDate.now()) || LocalDate.parse(date).isEqual(LocalDate.now());
    }

    /**
     * This method calculates the shipping fees of order
     * @param order Order information
     * @return shippingFee
     */
    public int calculateShippingFee(Order order){
        return shippingFeeCalculator.calculateShippingFee(order);
    }
}
