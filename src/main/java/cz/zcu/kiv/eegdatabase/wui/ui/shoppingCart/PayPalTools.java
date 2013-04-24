package cz.zcu.kiv.eegdatabase.wui.ui.shoppingCart;

import cz.zcu.kiv.eegdatabase.wui.app.session.EEGDataBaseSession;
import cz.zcu.kiv.eegdatabase.wui.components.utils.PageParametersUtils;
import org.apache.wicket.protocol.http.WebApplication;
import urn.ebay.api.PayPalAPI.*;
import urn.ebay.apis.CoreComponentTypes.BasicAmountType;
import urn.ebay.apis.eBLBaseComponents.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * PayPal tools to set and execute user's payment via PayPal service.
 * User: jfronek
 * Date: 21.3.2013
 */
public class PayPalTools {
    /**
     * Method for registering a payment on PayPal server. Needs to specify requested information.
     * In future extension might have an input parameter - Currency
     * @return PayPal's URL including token value identifying the payment. (Or ErrorPage URL in case an error occurs).
     */
    public static String setExpressCheckout(){
        // PayPal requires URL to redirect user upon successful payment authorization.
        String confirmURL = PageParametersUtils.getUrlForPage(PayPalConfirmPaymentPage.class, null);
        // PayPal requires URL to redirect user upon canceling the payment authorization.
        String cancelURL = PageParametersUtils.getUrlForPage(ShoppingCartPage.class, null);

        try{
            // PayPal SDK requires property object as a constructor parameter.
            Properties PayPalProperties =  new Properties();
            PayPalProperties.load(WebApplication.get().getServletContext().getResourceAsStream("/WEB-INF/PayPal.properties"));
            PayPalAPIInterfaceServiceService service = new PayPalAPIInterfaceServiceService(PayPalProperties);
            // Generation of setExpressCheckout request.
            SetExpressCheckoutRequestType setExpressCheckoutReq = new SetExpressCheckoutRequestType();
            SetExpressCheckoutRequestDetailsType details = new SetExpressCheckoutRequestDetailsType();

            details.setReturnURL(confirmURL);
            details.setCancelURL(cancelURL);

            List<PaymentDetailsType> payDetails = new ArrayList<PaymentDetailsType>();
            PaymentDetailsType paydtl = new PaymentDetailsType();
            paydtl.setPaymentAction(PaymentActionCodeType.SALE);  // Type needs to be set as SALE
            payDetails.add(paydtl);

            BasicAmountType totalAmount = new BasicAmountType();
            // Currency code
            // For developing purposes set to GBP, might be changes to EUR or an input param in case of multi-currency store
            totalAmount.setCurrencyID(CurrencyCodeType.GBP);
            totalAmount.setValue(Double.toString(EEGDataBaseSession.get().getShoppingCart().getTotalPrice()));

            details.setOrderTotal(totalAmount);
            details.setPaymentDetails(payDetails);

            details.setNoShipping("1");  //No shipping detail to be filled
            details.setAllowNote("0");   //No notes to be accepted

            // HardCoded Strings. Will be replaced when eshop goes live and Experiments are replaced with prepared packages.
            details.setOrderDescription("Experiments for total of: " + Double.toString(EEGDataBaseSession.get().getShoppingCart().getTotalPrice()) + " GBP.");

            setExpressCheckoutReq.setSetExpressCheckoutRequestDetails(details);

            SetExpressCheckoutReq expressCheckoutReq = new SetExpressCheckoutReq();
            expressCheckoutReq
                    .setSetExpressCheckoutRequest(setExpressCheckoutReq);

            SetExpressCheckoutResponseType setExpressCheckoutResponse = service
                    .setExpressCheckout(expressCheckoutReq);

            if (setExpressCheckoutResponse.getAck() == AckCodeType.SUCCESS) {
                String token = setExpressCheckoutResponse.getToken();
                //Reads redirect URL from property file
                return (PayPalProperties.getProperty("service.SetExpressCheckoutRedirectURL") + token);
            }
        } catch (Exception e) {}
        return PageParametersUtils.getUrlForPage(PaymentErrorPage.class, null);
    }

    /**
     * Gets Payer's ID from authorized payment and requests PayPal service to bill user's account.
     * In future extension might have an input parameter - Currency
     * @param token Token identifying payment.
     * @return true in case of successful transaction. false otherwise.
     */
    public static boolean doExpressCheckout(String token){
        try{
            // PayPal SDK requires property object as a constructor parameter.
            Properties PayPalProperties =  new Properties();
            PayPalProperties.load(WebApplication.get().getServletContext().getResourceAsStream("/WEB-INF/PayPal.properties"));
            PayPalAPIInterfaceServiceService service = new PayPalAPIInterfaceServiceService(PayPalProperties);
            // Generation of getExpressCheckout request.
            GetExpressCheckoutDetailsReq req = new GetExpressCheckoutDetailsReq();
            GetExpressCheckoutDetailsRequestType reqType = new GetExpressCheckoutDetailsRequestType(token);
            req.setGetExpressCheckoutDetailsRequest(reqType);
            GetExpressCheckoutDetailsResponseType resp = service.getExpressCheckoutDetails(req);

            if(resp == null || resp.getAck() != AckCodeType.SUCCESS){
                return false;
            }
            // Reads Payer's ID from getExpressCheckout response
            String payerID = resp.getGetExpressCheckoutDetailsResponseDetails().getPayerInfo().getPayerID();

            // Generating doExpressCheckout request to actually bill user's account
            DoExpressCheckoutPaymentRequestType doCheckoutPaymentRequestType = new DoExpressCheckoutPaymentRequestType();
            DoExpressCheckoutPaymentRequestDetailsType details = new DoExpressCheckoutPaymentRequestDetailsType();

            details.setToken(token);
            details.setPayerID(payerID);
            details.setPaymentAction(PaymentActionCodeType.SALE); //Type need to be set as SALE

            List<PaymentDetailsType> payDetails = new ArrayList<PaymentDetailsType>();
            PaymentDetailsType paydtl = new PaymentDetailsType();
            paydtl.setPaymentAction(PaymentActionCodeType.SALE);  //Type need to be set as SALE
            BasicAmountType totalAmount = new BasicAmountType();
            // Currency code
            // For developing purposes set to GBP, might be changes to EUR or an input param in case of multi-currency store
            totalAmount.setCurrencyID(CurrencyCodeType.GBP);
            totalAmount.setValue(Double.toString(EEGDataBaseSession.get().getShoppingCart().getTotalPrice()));
            paydtl.setOrderTotal(totalAmount);
            payDetails.add(paydtl);

            details.setPaymentDetails(payDetails);


            doCheckoutPaymentRequestType.setDoExpressCheckoutPaymentRequestDetails(details);
            DoExpressCheckoutPaymentReq doExpressCheckoutPaymentReq = new DoExpressCheckoutPaymentReq();
            doExpressCheckoutPaymentReq.setDoExpressCheckoutPaymentRequest(doCheckoutPaymentRequestType);

            DoExpressCheckoutPaymentResponseType doCheckoutPaymentResponse = service
                    .doExpressCheckoutPayment(doExpressCheckoutPaymentReq);

            if(doCheckoutPaymentResponse != null && doCheckoutPaymentResponse.getAck() == AckCodeType.SUCCESS){
                return true;
            }
        } catch (Exception e) {}
        return false;
    }
}
