package views.screen.payment;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import controller.PaymentController;
import entity.cart.Cart;
import common.exception.PlaceOrderException;
import entity.invoice.Invoice;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import utils.Configs;
import views.screen.BaseScreenHandler;
import views.screen.popup.PopupScreen;

public class PaymentScreenHandler extends BaseScreenHandler {

	@FXML
	private Button btnConfirmPayment;

	@FXML
	private ImageView loadingImage;

	private Invoice invoice;

	private String paymentMethod = "Credit Card";

	public PaymentScreenHandler(Stage stage, String screenPath, int amount, String contents) throws IOException {
		super(stage, screenPath);
	}

	public PaymentScreenHandler(Stage stage, String screenPath, Invoice invoice) throws IOException {
		super(stage, screenPath);
		this.invoice = invoice;
		// Pham Tuan Hien - 20183527
		ToggleGroup group = new ToggleGroup();
		creditBtn.setToggleGroup(group);
		creditBtn.setSelected(true);
		domesticBtn.setToggleGroup(group);

		group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
			@Override
			public void changed(ObservableValue<? extends Toggle> observableValue, Toggle toggle, Toggle t1) {
				RadioButton btn = (RadioButton) group.getSelectedToggle();
				paymentMethod = btn.getText();
				if (btn.getText().equals("Credit Card")) {
					creditVBox.setVisible(true);
					domesticVBox.setVisible(false);
				}
				if (btn.getText().equals("Domestic Debit Card")) {
					creditVBox.setVisible(false);
					domesticVBox.setVisible(true);
				}


			}
		});
		
		btnConfirmPayment.setOnMouseClicked(e -> {
			try {
				confirmToPayOrder();
				((PaymentController) getBController()).emptyCart();
			} catch (Exception exp) {
				System.out.println(exp.getStackTrace());
			}
		});
	}

	@FXML
	private Label pageTitle;

	@FXML
	private TextField cardNumber;

	@FXML
	private TextField holderName;

	@FXML
	private TextField expirationDate;

	@FXML
	private TextField securityCode;

	@FXML
	private RadioButton creditBtn;

	@FXML
	private RadioButton domesticBtn;

	@FXML
	private VBox creditVBox;

	@FXML
	private VBox domesticVBox;

	@FXML
	private TextField domesticCardNumber;

	@FXML
	private TextField domesticHolderName;

	@FXML
	private TextField domesticValidFromDate;

	@FXML
	private TextField domesticIssuingBank;


	void confirmToPayOrder() throws IOException{
		String contents = "pay order";
		PaymentController ctrl = (PaymentController) getBController();
		Map<String, String> response = new HashMap<>();

		if (paymentMethod.equals("Credit Card")) {
			response = ctrl.payOrderByCreditCard(invoice.getAmount(), contents, cardNumber.getText(), holderName.getText(),
					expirationDate.getText(), securityCode.getText());
		} else if (paymentMethod.equals("Domestic Debit Card")) {
			response = ctrl.payOrderByDomesticDebitCard(invoice.getAmount(), contents, domesticCardNumber.getText(), domesticHolderName.getText(),
					domesticValidFromDate.getText(), domesticIssuingBank.getText());
		}

		BaseScreenHandler resultScreen = new ResultScreenHandler(this.stage, Configs.RESULT_SCREEN_PATH, response.get("RESULT"), response.get("MESSAGE") );
		resultScreen.setPreviousScreen(this);
		resultScreen.setHomeScreenHandler(homeScreenHandler);
		resultScreen.setScreenTitle("Result Screen");
		resultScreen.show();
	}

}