package com.example.wavesoffood.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.wavesoffood.GlobalVariables;
import com.example.wavesoffood.R;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.paymentsheet.PaymentSheet;
import com.stripe.android.paymentsheet.PaymentSheetResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class CheckoutFragment extends Fragment {

    private EditText etCardNumber, etExpDate, etCVC;
    private Button btnPay;
    String SECRECT_KEY = "sk_test_51ONOgHIKX7yEjKRLgVFP7P2EGA1ONjpFT8oZ6gvgbrfTM83swtGgNM8UZ7IBVo2GLzZOBZ9FaE36GB2fKtAcQUU200hqdIwNmc";
    String PUBLISH_KEY = "pk_test_51ONOgHIKX7yEjKRLFU90isxeN6P7P0R04ZPkfaBXeoBcYC6jLgfZ5GUEErVfcMnbkpeeS7nL3Pn1zbj3iGHH13V800lcsExHWa";
    PaymentSheet paymentSheet;
    String customerID;
    String ephericalKey;
    String clientSecret;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_checkout, container, false);

        etCardNumber = view.findViewById(R.id.etCardNumber);
        etExpDate = view.findViewById(R.id.etExpDate);
        etCVC = view.findViewById(R.id.etCVC);
        btnPay = view.findViewById(R.id.btnPay);

        PaymentConfiguration.init(getContext(), PUBLISH_KEY);
        paymentSheet = new PaymentSheet(this, this::OnPaymentResult);

//        PaymentConfiguration.init(getContext(),PUBLISH_KEY);
//        paymentSheet = new PaymentSheet(this,paymentSheetResult -> {
//            OnPaymentResult(paymentSheetResult);
//        });
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                "https://api.stripe.com/v1/customers",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            customerID = jsonObject.getString("id");
                            getEphericalKey(customerID);
                            // Continue with the rest of the logic
                            getClientSecret(customerID, ephericalKey);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        // This block of code is executed if there is an error in the request
                        // 'error' parameter contains information about the error
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> header = new HashMap<>();
                header.put("Authorization","Bearer " + SECRECT_KEY);
                return header;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);

        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the method to handle payment
                Paymentflow();
            }
        });

        return view;
    }

    private void OnPaymentResult(PaymentSheetResult paymentSheetResult) {
        if(paymentSheetResult instanceof PaymentSheetResult.Completed){
            Toast.makeText(getContext(),"Payment Successful",Toast.LENGTH_LONG).show();
        }
    }

    private void getEphericalKey(String customerID) {
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                "https://api.stripe.com/v1/ephemeral_keys", // Replace this empty string with the actual URL you want to make the request to
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            ephericalKey = jsonObject.getString("id");
                            getEphericalKey(customerID);
                            getClientSecret(customerID,ephericalKey);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        // This block of code is executed if there is an error in the request
                        // 'error' parameter contains information about the error
                    }
                }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> header = new HashMap<>();
                header.put("Authorization","Bearer " + SECRECT_KEY);
                header.put("Stripe-Version","2023-10-16");
                return header;
            }

            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("customer",customerID);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    private void getClientSecret(String id, String ephericalKey) {
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                "https://api.stripe.com/v1/payment_intents", // Replace this empty string with the actual URL you want to make the request to
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            clientSecret = jsonObject.getString("id");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        // This block of code is executed if there is an error in the request
                        // 'error' parameter contains information about the error
                    }
                }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> header = new HashMap<>();
                header.put("Authorization","Bearer " + SECRECT_KEY);
                return header;
            }@Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("customer",customerID);
                params.put("amount", "10000");
                params.put("currency","usd");
                params.put("automatic_payment_methods[enabled]","true");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    private void Paymentflow() {
        if (customerID != null) {
            paymentSheet.presentWithPaymentIntent(clientSecret,
                    new PaymentSheet.Configuration("ABC Company",
                            new PaymentSheet.CustomerConfiguration(customerID, ephericalKey)));
        } else {
            // Handle the case where customerID is null (e.g., show an error message)
            Toast.makeText(getContext(), "Error: Customer ID is null", Toast.LENGTH_SHORT).show();
        }
    }



    private void updateUIBasedOnCardType(CardType cardType) {
        // Customize this method to update your UI based on the detected card type
        switch (cardType) {
            case VISA:
                Toast.makeText(requireContext(), "Card type: Visa", Toast.LENGTH_SHORT).show();
                // Update your UI for Visa card
                break;
            case MASTERCARD:
                Toast.makeText(requireContext(), "Card type: MasterCard", Toast.LENGTH_SHORT).show();
                // Update your UI for MasterCard
                break;
            case AMEX:
                Toast.makeText(requireContext(), "Card type: American Express", Toast.LENGTH_SHORT).show();
                // Update your UI for American Express
                break;
            case UNKNOWN:
                Toast.makeText(requireContext(), "Card type: Unknown", Toast.LENGTH_SHORT).show();
                // Update your UI for an unknown card type
                break;
        }
    }

    private CardType detectCardType(String cardNumber) {
        // Implement card type detection logic here, e.g., using regex or a library
        // This is a simplified example; you may want to use a more robust solution
        if (cardNumber.startsWith("4")) {
            return CardType.VISA;
        } else if (cardNumber.startsWith("5")) {
            return CardType.MASTERCARD;
        } else if (cardNumber.startsWith("34") || cardNumber.startsWith("37")) {
            return CardType.AMEX;
        } else {
            return CardType.UNKNOWN;
        }
    }

    private enum CardType {
        VISA,
        MASTERCARD,
        AMEX,
        UNKNOWN
    }
}
