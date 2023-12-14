package com.example.wavesoffood.Fragments;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.wavesoffood.Adapters.ItemAdapter;
import com.example.wavesoffood.Database.DAO;

import com.example.wavesoffood.Database.DishDTO;
import com.example.wavesoffood.R;
import com.example.wavesoffood.models.JsonModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

    public RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        // Make the API request
        // Make the API request after 60 seconds
        Handler handler = new Handler();
//        Runnable apiRunnable = new Runnable() {
//            @Override
//            public void run() {
//                // Make the API request
//                new FetchDataTask().execute();
//
//                // Schedule the next API request after 60 seconds
//                handler.postDelayed(this, 600000); // 60 seconds in milliseconds
//            }
//        };

// Make the first API request immediately
//        apiRunnable.run();

        recyclerView = view.findViewById(R.id.recycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        // Inflate the layout for this fragment
        return view;

    }
    private class FetchDataTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL("https://foodiefetch.p.rapidapi.com/swiggy?query=grandamas%20cafe%20pune");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                // Set request headers
                urlConnection.setRequestProperty("X-RapidAPI-Key", "b04979f06cmsh58fcd0523fb3c4cp168fd1jsn5cc41584eaf3");
                urlConnection.setRequestProperty("X-RapidAPI-Host", "foodiefetch.p.rapidapi.com");

                // Read the response
                BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                reader.close();

                // Return the response string
                return stringBuilder.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                // Handle the data result here
                try {
                    ArrayList<JsonModel> jsonModels = new ArrayList<>();
                    JSONObject jsonObject = new JSONObject(result);
                    List<DishDTO> dishesList = new ArrayList<>();
                    List<String> cuisinesList = new ArrayList<>();

                    // Extracting data key
                    JSONObject data = jsonObject.getJSONObject("data");
                    JSONObject about = data.getJSONObject("About");

                    // Creating a new JsonModel object and setting its fields
                    JsonModel.About aboutJsonModel = new JsonModel.About();
                    aboutJsonModel.setName(about.getString("Name"));
                    aboutJsonModel.setRatings(about.getString("Ratings"));
                    aboutJsonModel.setLocality(about.getString("Locality"));
                    aboutJsonModel.setAreaName(about.getString("AreaName"));
                    aboutJsonModel.setCity(about.getString("City"));

                    // Handling Cuisines array
                    JSONArray cuisinesArray = about.getJSONArray("Cuisines");

                    for (int i = 0; i < cuisinesArray.length(); i++) {
                        cuisinesList.add(cuisinesArray.getString(i));
                    }
                    aboutJsonModel.setCuisines(cuisinesList);
                    // Extracting Menu key
                    JSONArray menuArray = data.getJSONArray("Menu");
                    JsonModel.MenuCategory menuJsonModel = new JsonModel.MenuCategory();
                    menuJsonModel.setTitle(menuArray.getJSONObject(0).getString("title"));

                    // Handling Dishes array
                    JSONArray dishesArray = menuArray.getJSONObject(0).getJSONArray("Dishes");

                    for (int i = 0; i < dishesArray.length(); i++) {

                        JSONObject dishObject = dishesArray.getJSONObject(i);
                        DishDTO dish = new DishDTO();
                        dish.setName(dishObject.getString("name"));
                        dish.setDescription(dishObject.getString("description"));
                        dish.setInStock(dishObject.getBoolean("inStock"));
                        dish.setVeg(dishObject.getBoolean("isVeg"));
                        dish.setPrice(dishObject.getDouble("price"));
                        dishesList.add(dish);
//                        DAO.create(dish, DishDTO.class);
                    }
//                    menuJsonModel.setDishes(dishesList);
                    ItemAdapter itemAdapter = new ItemAdapter(getContext(),dishesList);
                    recyclerView.setAdapter(itemAdapter);
                    itemAdapter.notifyDataSetChanged();


                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            } else {
                Toast.makeText(getContext(), "API Error", Toast.LENGTH_LONG).show();
            }
        }
    }
}