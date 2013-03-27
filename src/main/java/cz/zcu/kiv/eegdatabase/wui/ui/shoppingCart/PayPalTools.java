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
 * Created with IntelliJ IDEA.
 * User: jfronek
 * Date: 21.3.13
 * Time: 19:34
 * To change this template use File | Settings | File Templates.
 */
public class PayPalTools {

    //In future extension might have an input parameter - Currency
    public static String setExpressCheckout(){
        String confirmURL = PageParametersUtils.getUrlForPage(PayPalConfirmPaymentPage.class, null);
        String cancelURL = PageParametersUtils.getUrlForPage(ShoppingCartPage.class, null);

        try{
            Properties PayPalProperties =  new Properties();
            PayPalProperties.load(WebApplication.get().getServletContext().getResourceAsStream("/WEB-INF/PayPal.properties"));
            PayPalAPIInterfaceServiceService service = new PayPalAPIInterfaceServiceService(PayPalProperties);
            SetExpressCheckoutRequestType setExpressCheckoutReq = new SetExpressCheckoutRequestType();
            SetExpressCheckoutRequestDetailsType details = new SetExpressCheckoutRequestDetailsType();

            details.setReturnURL(confirmURL);
            details.setCancelURL(cancelURL);

            List<PaymentDetailsType> payDetails = new ArrayList<PaymentDetailsType>();
            PaymentDetailsType paydtl = new PaymentDetailsType();
            paydtl.setPaymentAction(PaymentActionCodeType.SALE);
            payDetails.add(paydtl);

            BasicAmountType totalAmount = new BasicAmountType();
            totalAmount.setCurrencyID(CurrencyCodeType.GBP);
            totalAmount.setValue(Double.toString(EEGDataBaseSession.get().getShoppingCart().getTotalPrice()));

            details.setOrderTotal(totalAmount);
            details.setPaymentDetails(payDetails);

            details.setNoShipping("1");
            details.setAllowNote("0");

            //HardCoded String. Will be replaced when eshop goes live and Experiments are replaced with prepared packages.
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

    //In future extension might have an input parameter - Currency
    public static boolean doExpressCheckout(String token){
        try{
            PayPalAPIInterfaceServiceService service = new PayPalAPIInterfaceServiceService(WebApplication.get().getServletContext().getResourceAsStream("/WEB-INF/PayPal.properties"));
            GetExpressCheckoutDetailsReq req = new GetExpressCheckoutDetailsReq();
            GetExpressCheckoutDetailsRequestType reqType = new GetExpressCheckoutDetailsRequestType(token);
            req.setGetExpressCheckoutDetailsRequest(reqType);
            GetExpressCheckoutDetailsResponseType resp = service.getExpressCheckoutDetails(req);

            if(resp == null || resp.getAck() != AckCodeType.SUCCESS){
                return false;
            }

            String payerID = resp.getGetExpressCheckoutDetailsResponseDetails().getPayerInfo().getPayerID();

            DoExpressCheckoutPaymentRequestType doCheckoutPaymentRequestType = new DoExpressCheckoutPaymentRequestType();
            DoExpressCheckoutPaymentRequestDetailsType details = new DoExpressCheckoutPaymentRequestDetailsType();

            details.setToken(token);
            details.setPayerID(payerID);
            details.setPaymentAction(PaymentActionCodeType.SALE);

            List<PaymentDetailsType> payDetails = new ArrayList<PaymentDetailsType>();
            PaymentDetailsType paydtl = new PaymentDetailsType();
            paydtl.setPaymentAction(PaymentActionCodeType.SALE);
            BasicAmountType totalAmount = new BasicAmountType();
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
