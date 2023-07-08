package com.example.demo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * The class that is responsible for building, sending and receiving PayPal orders
 * For more info see: https://developer.paypal.com/docs/api/orders/v2/#orders_create
 */
//The shittiest code at back-end part in whole project
@Service
public class PayPalService {
    private static Logger LOG = LoggerFactory.getLogger(PayPalService.class);
    @Value("${pay_pal.client_id}")
    private String payPalClientId;
    @Value("${pay_pal.client_secret}")
    private String payPalClientSecret;

    private final  ObjectMapper objectMapper = new ObjectMapper();

    public String createOrder(List<String> chosenTimePeriods) throws IOException {
        String jsonToSend = preparePayPalParameters(chosenTimePeriods);

        URL url = new URL("https://api-m.sandbox.paypal.com/v2/checkout/orders");
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        httpConn.setRequestMethod("POST");

        String auth = payPalClientId + ":" + payPalClientSecret;
        byte[] authBytes = auth.getBytes(StandardCharsets.UTF_8);
        String encodedAuth = Base64.getEncoder().encodeToString(authBytes);

        httpConn.setRequestProperty("Authorization", "Basic " + encodedAuth);
        httpConn.setRequestProperty("Content-Type", "application/json");
//        httpConn.setRequestProperty("PayPal-Request-Id", "7b92603e-77ed-4896-8e78-5dea2050476a");

        httpConn.setDoOutput(true);
        OutputStreamWriter writer = new OutputStreamWriter(httpConn.getOutputStream());
        writer.write(jsonToSend);
        writer.flush();
        writer.close();
        httpConn.getOutputStream().close();

        InputStream responseStream = httpConn.getResponseCode() / 100 == 2
                ? httpConn.getInputStream()
                : httpConn.getErrorStream();
        Scanner s = new Scanner(responseStream).useDelimiter("\\A");
        String responseString = s.hasNext() ? s.next() : "";
        LOG.info(responseString);
        return responseString;
    }

 private String preparePayPalParameterAmount(List<String> chosenTimePeriods) throws JsonProcessingException {

     return String.format("\"amount\" :" +
             " { \"value\" : \"%s\" ," +
             " \"currency_code\" : \"ILS\"," +
             "  \"breakdown\" : " +
             "{ \"item_total\" : " +
                "{ \"value\" : \"%s\" ," +
                " \"currency_code\" : \"ILS\" } } } ",chosenTimePeriods.size() * 100,chosenTimePeriods.size() * 100);
 }

private String preparePayPalParameterItems(List<String> chosenTimePeriods) throws JsonProcessingException {
    List<Map<String,String>> resultArray = chosenTimePeriods.stream().map(el->{
        Map<String,String> result = new HashMap<>();
        result.put("name", el);
        result.put("quantity", "1");
        result.put("category", "DIGITAL_GOODS");
        return result;
    }).collect(Collectors.toList());
    LOG.info(objectMapper.writeValueAsString(resultArray));

    Map<String,Map<String,String>> unitAmountMap = new HashMap<>();
    unitAmountMap.put("unit_amount",new HashMap<>());
    unitAmountMap.get("unit_amount").put("currency_code","ILS");
    unitAmountMap.get("unit_amount").put("value","100");

    String unitAmountString = objectMapper.writeValueAsString(unitAmountMap);
    String unit_amount = unitAmountString.substring(1, unitAmountString.length()-1);
    return "\"items\": " + resultArray.stream().map(map-> {
                try {
                    String str = objectMapper.writeValueAsString(map);
                    return str.substring(1,str.length()-1);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            })
            .map(str-> String.format("{ %s, %s }",str,unit_amount))
            .collect(Collectors.joining(", ","[ ", "]"));


}

 private String preparePayPalParameters(List<String> chosenTimePeriods) throws JsonProcessingException {
     Map<String,String> intentMap = Map.of("intent","CAPTURE");
     String intentString = objectMapper.writeValueAsString(intentMap);
     String intent= intentString.substring(1,intentString.length()-1);
     String items = preparePayPalParameterItems(chosenTimePeriods);
     String amount = preparePayPalParameterAmount(chosenTimePeriods);

     String res = String.format("{ %s, \"purchase_units\":[ { %s, %s } ] }",intent,items,amount);
     LOG.info(res);
    return res;

 }
}
