package controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class PlaceOrderControllerTest {

    private PlaceOrderController placeOrderController;

    @BeforeEach
    void setUp() throws Exception {
        placeOrderController = new PlaceOrderController();
    }

    @ParameterizedTest
    @CsvSource({
            "0192818181,true",
            "0124,false",
            "abc123,false",
            "2019129422,false"
    })
    void validatePhoneNumber(String phone, boolean expected) {
        // Pham Tuan Hien - 20183527
        boolean isValided = placeOrderController.validatePhoneNumber(phone);
        assertEquals(expected, isValided);
    }
    @ParameterizedTest
    @CsvSource({
            "nguyenlm,true",
            "nguyen01234,false",
            "$#nguyen,false",
            ",false"
    })
    void validateName(String name, boolean expected) {
        // Pham Tuan Hien - 20183527
        boolean isValided = placeOrderController.validateName(name);
        assertEquals(expected, isValided);
    }
    @ParameterizedTest
    @CsvSource({
            "hanoi,true",
            "so 1 Hai Ba Trung Ha Noi,true",
            "$# Ha Noi,false",
            ",false"
    })
    void validateAddress(String address, boolean expected) {
        // Pham Tuan Hien - 20183527
        boolean isValided = placeOrderController.validateAddress(address);
        assertEquals(expected, isValided);
    }
    @ParameterizedTest
    @CsvSource({
            "Hà Nội,true",
            "Bắc Giang,false",
            "123ab,false",
            ",false"
    })
    void validateProvince(String province, boolean expected) {
        // Pham Tuan Hien - 20183527
        boolean isValided = placeOrderController.validateProvince(province);
        assertEquals(expected, isValided);
    }
    @ParameterizedTest
    @CsvSource({
            "2022-12-22,true",
            "2020-12-22,false",
            ",false"
    })
    void validateDate(String date, boolean expected) {
        // Pham Tuan Hien - 20183527
        boolean isValided = placeOrderController.validateDate(date);
        assertEquals(expected, isValided);
    }

}