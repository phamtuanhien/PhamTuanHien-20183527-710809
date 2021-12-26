package entity.payment;
// Pham Tuan Hien - 20183527
public class DomesticDebitCard extends PaymentCard {
    private String cardCode;
    private String owner;
    private String issuingBank;
    private String validFromDate;

    public DomesticDebitCard(String cardCode, String owner, String issuingBank, String validFromDate) {
        super();
        this.cardCode = cardCode;
        this.owner = owner;
        this.issuingBank = issuingBank;
        this.validFromDate = validFromDate;
    }
}
