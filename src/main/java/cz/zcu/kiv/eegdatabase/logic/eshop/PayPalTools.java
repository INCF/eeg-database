/*******************************************************************************
 * This file is part of the EEG-database project
 * 
 *   ==========================================
 *  
 *   Copyright (C) 2013 by University of West Bohemia (http://www.zcu.cz/en/)
 *  
 *  ***********************************************************************************************************************
 *  
 *   Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 *   the License. You may obtain a copy of the License at
 *  
 *       http://www.apache.org/licenses/LICENSE-2.0
 *  
 *   Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 *   an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 *   specific language governing permissions and limitations under the License.
 *  
 *  ***********************************************************************************************************************
 *  
 *   PayPalTools.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.logic.eshop;

import cz.zcu.kiv.eegdatabase.wui.app.EEGDataBaseApplication;
import cz.zcu.kiv.eegdatabase.wui.app.session.EEGDataBaseSession;
import cz.zcu.kiv.eegdatabase.wui.components.utils.PageParametersUtils;
import cz.zcu.kiv.eegdatabase.wui.ui.shoppingCart.PayPalConfirmPaymentPage;
import cz.zcu.kiv.eegdatabase.wui.ui.shoppingCart.PaymentErrorPage;
import cz.zcu.kiv.eegdatabase.wui.ui.shoppingCart.ShoppingCartPage;

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
        String confirmURL = PageParametersUtils.getUrlForPage(PayPalConfirmPaymentPage.class, null, EEGDataBaseApplication.get().getDomain());
        // PayPal requires URL to redirect user upon canceling the payment authorization.
        String cancelURL = PageParametersUtils.getUrlForPage(ShoppingCartPage.class, null, EEGDataBaseApplication.get().getDomain());

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
            totalAmount.setCurrencyID(CurrencyCodeType.EUR);
            totalAmount.setValue(EEGDataBaseSession.get().getShoppingCart().getTotalPrice().toPlainString());

            details.setOrderTotal(totalAmount);
            details.setPaymentDetails(payDetails);

            details.setNoShipping("1");  //No shipping detail to be filled
            details.setAllowNote("0");   //No notes to be accepted

            // HardCoded Strings. Will be replaced when eshop goes live and Experiments are replaced with prepared packages.
            details.setOrderDescription("Experiments for total of: " + EEGDataBaseSession.get().getShoppingCart().getTotalPrice().toPlainString() + " EUR.");

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
        return PageParametersUtils.getUrlForPage(PaymentErrorPage.class, null, EEGDataBaseApplication.get().getDomain());
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
            totalAmount.setCurrencyID(CurrencyCodeType.EUR);
            totalAmount.setValue(EEGDataBaseSession.get().getShoppingCart().getTotalPrice().toPlainString());
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
