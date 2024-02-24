package com.example.wavesoffood.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
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

    private static final int MY_SOCKET_TIMEOUT_MS =  15000;
    private Button btnPay;
    String SECRECT_KEY = "sk_test_51ONOgHIKX7yEjKRLgVFP7P2EGA1ONjpFT8oZ6gvgbrfTM83swtGgNM8UZ7IBVo2GLzZOBZ9FaE36GB2fKtAcQUU200hqdIwNmc";
    String PUBLISH_KEY = "pk_test_51ONOgHIKX7yEjKRLFU90isxeN6P7P0R04ZPkfaBXeoBcYC6jLgfZ5GUEErVfcMnbkpeeS7nL3Pn1zbj3iGHH13V800lcsExHWa";
    PaymentSheet paymentSheet;
    String customerID;
    String ephericalKey;
    String clientSecret;
    private ProgressBar progressBar;

    private boolean isPaymentInProgress = false;
    private boolean isPaymentSheetUp = false;
    private boolean isButtonClick = false;

    // Payment gateway source video URL --> https://www.youtube.com/watch?v=h12wNuOher4&t=1821s

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_checkout, container, false);

        btnPay = view.findViewById(R.id.btnPay);
        progressBar = view.findViewById(R.id.progressBar);

        // Make the initial request to create a customer
        PaymentConfiguration.init(getContext(),PUBLISH_KEY);

        paymentSheet = new PaymentSheet(this,paymentSheetResult -> {
            OnPaymentResult(paymentSheetResult);
        });
        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPaymentInProgress = true; // Set the flag to true to indicate that the payment process is now in progress
                isButtonClick = true;
                showProgressBar();

                // Call the method to handle payment
                try {
                    if(GlobalVariables.total > 0)
                    {
                        createCustomer();
                    }
                } catch (Exception e) {
                    hideProgressBar();
                    isPaymentInProgress = false; // Set the flag to false in case of an exception
                    throw new RuntimeException(e);
                }

            }
        });

        return view;
    }

    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }


    private void createCustomer() {
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
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new HashMap<>();
                header.put("Authorization", "Bearer " + SECRECT_KEY);
                return header;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    private void OnPaymentResult(PaymentSheetResult paymentSheetResult) {
        hideProgressBar();
        isPaymentInProgress = false;

        if (paymentSheetResult instanceof PaymentSheetResult.Completed) {
            Toast.makeText(getContext(), "Payment Successful", Toast.LENGTH_LONG).show();
        } else if (paymentSheetResult instanceof PaymentSheetResult.Canceled) {
            Toast.makeText(getContext(), "Payment Canceled", Toast.LENGTH_LONG).show();
            isPaymentSheetUp = false;
            isButtonClick = false;
        } else if (paymentSheetResult instanceof PaymentSheetResult.Failed) {
            PaymentSheetResult.Failed failedResult = (PaymentSheetResult.Failed) paymentSheetResult;
            String errorMessage = failedResult.getError().getLocalizedMessage();
            Toast.makeText(getContext(), "Payment Failed: " + errorMessage, Toast.LENGTH_LONG).show();
        }
    }


    private void getEphericalKey(String customerID) {
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                "https://api.stripe.com/v1/ephemeral_keys",
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
        // Set Retry Policy
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        try{
            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            requestQueue.add(stringRequest);
        } catch (Exception e) {
            Log.wtf("Check Req","erorr"+e);
        }

    }

    private void getClientSecret(String id, String ephericalKey) {
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                "https://api.stripe.com/v1/payment_intents",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            clientSecret = jsonObject.getString("client_secret");
                            if (!isPaymentSheetUp && isButtonClick) {
                                Paymentflow();
                                isPaymentSheetUp = true;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
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
                params.put("amount", String.valueOf(GlobalVariables.total).replace(".0","")+"00");
                params.put("currency","lkr");
                params.put("automatic_payment_methods[enabled]","true");
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    private void Paymentflow() {
        if (customerID != null) {
            paymentSheet.presentWithPaymentIntent(clientSecret,
                    new PaymentSheet.Configuration("ABC Company",
                            new PaymentSheet.CustomerConfiguration(customerID, ephericalKey)));
        }
    }

}
