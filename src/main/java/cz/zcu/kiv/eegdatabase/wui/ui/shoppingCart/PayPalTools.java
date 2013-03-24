package cz.zcu.kiv.eegdatabase.wui.ui.shoppingCart;

import cz.zcu.kiv.eegdatabase.wui.app.session.EEGDataBaseSession;
import cz.zcu.kiv.eegdatabase.wui.components.utils.PageParametersUtils;
import org.apache.wicket.protocol.http.WebApplication;
import urn.ebay.api.PayPalAPI.*;
import urn.ebay.apis.CoreComponentTypes.BasicAmountType;
import urn.ebay.apis.eBLBaseComponents.*;

import java.util.ArrayList;
import java.util.List;

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
            PayPalAPIInterfaceServiceService service = new PayPalAPIInterfaceServiceService(WebApplication.get().getServletContext().getResourceAsStream("/WEB-INF/PayPal.properties"));
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

            details.setOrderDescription("Experiments for total of: " + Double.toString(EEGDataBaseSession.get().getShoppingCart().getTotalPrice()) + " GBP.");

            setExpressCheckoutReq.setSetExpressCheckoutRequestDetails(details);

            SetExpressCheckoutReq expressCheckoutReq = new SetExpressCheckoutReq();
            expressCheckoutReq
                    .setSetExpressCheckoutRequest(setExpressCheckoutReq);

            expressCheckoutReq.toString();

            SetExpressCheckoutResponseType setExpressCheckoutResponse = service
                    .setExpressCheckout(expressCheckoutReq);

            if (setExpressCheckoutResponse.getAck() == AckCodeType.SUCCESS) {
                String token = setExpressCheckoutResponse.getToken();
                //Need to figure out how to load this url from property file
                return ("https://www.sandbox.paypal.com/cgi-bin/webscr?cmd=_express-checkout&token=" + token);
            }
            else {
                return PageParametersUtils.getUrlForPage(PaymentErrorPage.class, null);
            }

        } catch (Exception e) {

        }
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

            if(doCheckoutPaymentResponse == null || doCheckoutPaymentResponse.getAck() != AckCodeType.SUCCESS){
                return false;
            }

            return true;

        } catch (Exception e) {

        }
        return false;
    }
}
