package com.example.wavesoffood.Fragments;

import android.database.SQLException;
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

import com.example.wavesoffood.Database.DishDTO;
import com.example.wavesoffood.Database.OrmliteHelper;
import com.example.wavesoffood.GlobalVariables;
import com.example.wavesoffood.R;
import com.example.wavesoffood.models.JsonModel;

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

    private RecyclerView recyclerView;
    public static ItemAdapter itemAdapter;
    private OrmliteHelper ormliteHelper;
    List<DishDTO> dishesList = new ArrayList<>();
    List<String> cuisinesList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        // Make the API request
        // Make the API request after 60 seconds
        Handler handler = new Handler();
        ormliteHelper = new OrmliteHelper(getContext());
        Runnable apiRunnable = new Runnable() {
            @Override
            public void run() {
                // Make the API request
                new FetchDataTask().execute();

                // Schedule the next API request after 60 seconds
                handler.postDelayed(this, 600000); // 60 seconds in milliseconds
            }
        };

// Make the first API request immediately
        apiRunnable.run();

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

            result = "{\"data\":{\"About\":{\"Name\":\"Grandmama's Cafe\",\"Ratings\":\"--\",\"Locality\":\"Vashi\",\"AreaName\":\"Vashi\",\"City\":\"Mumbai\",\"Cuisines\":[\"Continental\",\"Indian\"]},\"Menu\":[{\"title\":\"Recommended\",\"Dishes\":[{\"name\":\"Cottage Cheese \\u0026 Chipotle Burger\",\"description\":\"Crispy Cottage Cheese Patty With Mexican Chipotle Chilli.  Served With French Fries And Coleslaw.\",\"inStock\":false,\"isVeg\":true,\"price\":380},{\"name\":\"Keema Pav\",\"description\":\"Minced Mutton Curry Cooked With Onions And Tomatoes, Seasoned With Indian Spices.\",\"inStock\":false,\"isVeg\":false,\"price\":410},{\"name\":\"Bombay Grill Sandwich\",\"description\":\"Masala Potatoes With Mint Chutney And Our Very Own Amul Cheese.  Suggested Bread - Basil Panini.\",\"inStock\":false,\"isVeg\":true,\"price\":285},{\"name\":\"The Big Mouth Veggie Sandwich\",\"description\":\"Loaded Sandwich With Bell Peppers, Tomatoes, Onions, Olives, Gherkins, Lettuce, Topped With Cheddar, Honey Mustard And Mayonnaise.\",\"inStock\":false,\"isVeg\":true,\"price\":350},{\"name\":\"Nutrition Power House Salad\",\"description\":\"Rocket Leaves Tossed In A Duet Of Quinoa And Barley In Goat Cheese And Lemon Dressing.\",\"inStock\":false,\"isVeg\":true,\"price\":310},{\"name\":\"Grilled Chicken Caesar Salad\",\"description\":\"A Caesar Salad Is A Green Salad Of Romaine Lettuce \\u0026 Crostini Dressed With Lime Juice , Egg Yolks\",\"inStock\":false,\"isVeg\":false,\"price\":390}]},{\"title\":\"Swiggy Exclusive Combos\",\"Dishes\":[{\"name\":\"Chicken Shawarma + Grandmama'S Masala Fries\",\"inStock\":false,\"isVeg\":false,\"price\":440},{\"name\":\"Garlic Bread + Spaghetti Aglio Olio\",\"inStock\":false,\"isVeg\":true,\"price\":490},{\"name\":\"Garlic Bread + Grandmama'S Special Pizza\",\"description\":\"Pizza Is Topped With Sundried Tomatoes, Olives, Onions \\u0026 Bell Peppers\",\"inStock\":false,\"isVeg\":true,\"price\":570},{\"name\":\"Chicken Shawarma + Hummus\",\"description\":\"Served With Paprika \\u0026 Pita.\",\"inStock\":false,\"isVeg\":false,\"price\":530}]},{\"title\":\"Veg\",\"Dishes\":[{\"name\":\"The Veggie One Burger\",\"description\":\"In House Spiced Vegetable Burger.  Served With French Fries And Coleslaw.\",\"inStock\":false,\"isVeg\":true,\"price\":340},{\"name\":\"Cottage Cheese \\u0026 Chipotle Burger\",\"description\":\"Crispy Cottage Cheese Patty With Mexican Chipotle Chilli.  Served With French Fries And Coleslaw.\",\"inStock\":false,\"isVeg\":true,\"price\":380},{\"name\":\"Vegan The Veggie One Burger\",\"inStock\":false,\"isVeg\":true,\"price\":440}]},{\"title\":\"Chicken\",\"Dishes\":[{\"name\":\"The All American Burger\",\"description\":\"Juicy Tenderloin Patty Topped With Crispy Bacon And Melted Cheddar.  Served With French Fries And Coleslaw.\",\"inStock\":false,\"isVeg\":false,\"price\":420},{\"name\":\"Firecraker Chicken Burger\",\"description\":\"Burger Loaded With Asian Style Pepper Chicken , Mayonnaise , Sichuan Sauce , Onions ,\\u0026 Tomato Slices .\",\"inStock\":false,\"isVeg\":false,\"price\":380}]},{\"title\":\"Bombay Grandmama's Specials\",\"Dishes\":[{\"name\":\"Rajma Chawal\",\"description\":\"Kidney Beans Slow Cooked In A Rich Tomato Gravy, Served Wit Rice And Crispy Fryums.\",\"inStock\":false,\"isVeg\":true,\"price\":310},{\"name\":\"Bibiji Ki Achari Khichdi\",\"description\":\"Legumes And Rice Cooked Together With Whole Spices Aromatics And Desi Ghee.\",\"inStock\":false,\"isVeg\":true,\"price\":240},{\"name\":\"Farida Motiwala Dhansak\",\"description\":\"A Parsi Zoroastiran Delicacy Made With Lentil And Vegetables Served With Brown Rice.\",\"inStock\":false,\"isVeg\":true,\"price\":310},{\"name\":\"Keema Pav\",\"description\":\"Minced Mutton Curry Cooked With Onions And Tomatoes, Seasoned With Indian Spices.\",\"inStock\":false,\"isVeg\":false,\"price\":410},{\"name\":\"Farida Motiwala Dhansak Mutton\",\"description\":\"A Parsi Zoroastrian Delicacy Made With Lentil \\u0026 Vegetables Served With Brown Rice\",\"inStock\":false,\"isVeg\":false,\"price\":470},{\"name\":\"Tradtional Goan Curry Fish\",\"inStock\":false,\"isVeg\":false,\"price\":450},{\"name\":\"Tradtional Goan Curry Prawns\",\"inStock\":false,\"isVeg\":false,\"price\":490},{\"name\":\"Thai Basil \\u0026 Brown Rice Bowl Tenderloin\",\"inStock\":false,\"isVeg\":false,\"price\":490},{\"name\":\"Thai Basil \\u0026 Brown Rice Bowl Prwans\",\"inStock\":false,\"isVeg\":false,\"price\":510},{\"name\":\"Thai Basil \\u0026 Brown Rice Bowl Chicken\",\"inStock\":false,\"isVeg\":false,\"price\":450},{\"name\":\"Thai Basil \\u0026 Brown Rice Bowl Mahi Mahi\",\"inStock\":false,\"isVeg\":false,\"price\":460},{\"name\":\"Thai Basil \\u0026 Brown Rice Bowl Tofu\",\"inStock\":false,\"isVeg\":true,\"price\":410},{\"name\":\"Burmese Khao Suey Tenderloin\",\"inStock\":false,\"isVeg\":false,\"price\":490},{\"name\":\"Burmese Khao Suey Prawns\",\"inStock\":false,\"isVeg\":false,\"price\":510},{\"name\":\"Burmese Khao Suey Chicken\",\"inStock\":false,\"isVeg\":false,\"price\":450},{\"name\":\"Burmese Khao Suey Mahi Mahi\",\"inStock\":false,\"isVeg\":false,\"price\":460},{\"name\":\"Burmese Khao Suey Tofu\",\"inStock\":false,\"isVeg\":true,\"price\":410},{\"name\":\"Big Veggie \\u0026 Crispy Cashew Bowl Tenderloin\",\"inStock\":false,\"isVeg\":false,\"price\":490},{\"name\":\"Big Veggie \\u0026 Crispy Cashew Bowl Prawns\",\"inStock\":false,\"isVeg\":false,\"price\":510},{\"name\":\"Big Veggie \\u0026 Crispy Cashew Bowl Chicken\",\"inStock\":false,\"isVeg\":false,\"price\":450},{\"name\":\"Big Veggie \\u0026 Crispy Cashew Bowl Mahi Mahi\",\"inStock\":false,\"isVeg\":true,\"price\":460},{\"name\":\"Big Veggie \\u0026 Crispy Cashew Bowl Tofu\",\"inStock\":false,\"isVeg\":true,\"price\":410},{\"name\":\"Green Thai Curry Tenderloin\",\"inStock\":false,\"isVeg\":false,\"price\":490},{\"name\":\"Green Thai Curry Prawns\",\"inStock\":false,\"isVeg\":false,\"price\":510},{\"name\":\"Green Thai Curry Chicken\",\"inStock\":false,\"isVeg\":false,\"price\":450},{\"name\":\"Green Thai Curry Mahi Mahi\",\"inStock\":false,\"isVeg\":false,\"price\":460},{\"name\":\"Vegan Volcano Jambalaya\",\"inStock\":false,\"isVeg\":true,\"price\":340},{\"name\":\"Vegan Rajma Chawal \\u0026 Gun Powder\",\"inStock\":false,\"isVeg\":true,\"price\":310},{\"name\":\"Vegan Bibiji Ki Achari Kichadi\",\"inStock\":false,\"isVeg\":true,\"price\":240},{\"name\":\"Vegan Baked Veggie \\u0026 Crispy Cashew Bowl\",\"inStock\":false,\"isVeg\":true,\"price\":410},{\"name\":\"Vegan Pesto Basilico\",\"inStock\":false,\"isVeg\":true,\"price\":400},{\"name\":\"Vegan Penne Marinara\",\"inStock\":false,\"isVeg\":true,\"price\":400},{\"name\":\"Homestyle Makhani Chicken\",\"description\":\"Homestyle Makhni Served With Rice.\",\"inStock\":false,\"isVeg\":false,\"price\":350},{\"name\":\"Homestyle Makhani Paneer\",\"description\":\"Homestyle Makhni Served With Rice.\",\"inStock\":false,\"isVeg\":true,\"price\":350},{\"name\":\"My Hubbys Chicken Curry Rice\",\"description\":\"Homestyle Chicken Gravy Cooked In A Yoghurt \\u0026 Khas Khas Gravy \\u0026 Whole Spices , Served With Jeera Rice\",\"inStock\":false,\"isVeg\":false,\"price\":320},{\"name\":\"Goan Chorizo Pav\",\"description\":\"Smoked \\u0026 Spiced  Goan Pork Sausages Cooked With Potatoes \\u0026 Onions\",\"inStock\":false,\"isVeg\":false,\"price\":380},{\"name\":\"Green Thai Curry Tofu\",\"description\":\"A Green Thai Curry Preperation Made With Coconut , Vegetables , Tofu \\u0026 Thai Brinjal.\",\"inStock\":false,\"isVeg\":true,\"price\":410}]},{\"title\":\"Shawarmas\",\"Dishes\":[{\"name\":\"Falafel Shawarma\",\"description\":\"Served In Pita Bread With Pickled Vegetables, Garlic Mayo, Spicy Harissa Sauce, Tzatziki And Hummus.\",\"inStock\":false,\"isVeg\":true,\"price\":220},{\"name\":\"Chicken Shawarma\",\"description\":\"Served In Pita Bread With Pickled Vegetables, Garlic Mayo, Spicy Harissa Sauce, Tzatziki And Hummus.\",\"inStock\":false,\"isVeg\":false,\"price\":260},{\"name\":\"Vegan Falafel Shawarma\",\"inStock\":false,\"isVeg\":true,\"price\":220}]},{\"title\":\"Veg\",\"Dishes\":[{\"name\":\"Bombay Grill Sandwich\",\"description\":\"Masala Potatoes With Mint Chutney And Our Very Own Amul Cheese.  Suggested Bread - Basil Panini.\",\"inStock\":false,\"isVeg\":true,\"price\":285},{\"name\":\"Vegan Big Mouth Veggie Sandwich\",\"inStock\":false,\"isVeg\":true,\"price\":450},{\"name\":\"Vegan The Bombay Grill Sandwich\",\"inStock\":false,\"isVeg\":true,\"price\":380},{\"name\":\"The Big Mouth Veggie Sandwich\",\"description\":\"Loaded Sandwich With Bell Peppers, Tomatoes, Onions, Olives, Gherkins, Lettuce, Topped With Cheddar, Honey Mustard And Mayonnaise.\",\"inStock\":false,\"isVeg\":true,\"price\":350},{\"name\":\"French Onion Cheese Grill Sandwich\",\"description\":\"Toasted Brown Bread Loaded With Carramelized Onions , Grilled Tomatoes , Grilled Zucchini \\u0026 Melted Emmenthal Cheese\",\"inStock\":false,\"isVeg\":true,\"price\":290},{\"name\":\"Sloppy Joe Bbq Paneer Sandwich\",\"description\":\"Sour Dough Bauguette Filled With Crumbled Bbq Cottage Cheese \\u0026 Pickled Vegetables . Gratinated With Scarmoza Cheese\",\"inStock\":false,\"isVeg\":true,\"price\":320}]},{\"title\":\"Chicken\",\"Dishes\":[{\"name\":\"Grilled Chicken Salad Sandwich\",\"description\":\"Chicken ,Homemade Mayo,Shredded Lettuce Honeymustard Served On Pannini\",\"inStock\":false,\"isVeg\":false,\"price\":320},{\"name\":\"Sloppy Joe Bbq Tenderloin Sandwich\",\"description\":\"Sour Dough Baguette Filled With Roasted Bbq Tenderloin Slivers \\u0026 Pickled Vegetables . Gratinated With Scarmoza Cheese Baby Gherkins , Cocktail Onions ,Jalapenos .\",\"inStock\":false,\"isVeg\":false,\"price\":360}]},{\"title\":\"Soups\",\"Dishes\":[{\"name\":\"Mushroom Cappuccino\",\"description\":\"A Puree Soup Prepared With Fresh Button Mushrooms ,Fresh Cream\\u0026 Flavoured With Dry Porcini\",\"inStock\":false,\"isVeg\":true,\"price\":210},{\"name\":\"Homestyle Tomato Soup\",\"description\":\"Tomato Soup Sourdough Crostini,Dollop Of Cream\",\"inStock\":false,\"isVeg\":true,\"price\":180},{\"name\":\"Barley, Chicken \\u0026 Coconut Chowder\",\"description\":\"A Classic American Soup Prepared With A Strong Chicken Broth \\u0026 Cubed Potatoes\",\"inStock\":false,\"isVeg\":false,\"price\":240},{\"name\":\"Vegan Home Style Tomato Soup\",\"inStock\":false,\"isVeg\":true,\"price\":180}]},{\"title\":\"Salads\",\"Dishes\":[{\"name\":\"Nutrition Power House Salad\",\"description\":\"Rocket Leaves Tossed In A Duet Of Quinoa And Barley In Goat Cheese And Lemon Dressing.\",\"inStock\":false,\"isVeg\":true,\"price\":310},{\"name\":\"Roasted Beetroot Salad\",\"description\":\"Rocket Leaves, Candid Walnuts, Roasted Beetroot And Crumbled Feta Cheese In A Lemon Balsamic Dressing.\",\"inStock\":false,\"isVeg\":true,\"price\":290},{\"name\":\"Grilled Chicken Caesar Salad\",\"description\":\"A Caesar Salad Is A Green Salad Of Romaine Lettuce \\u0026 Crostini Dressed With Lime Juice , Egg Yolks\",\"inStock\":false,\"isVeg\":false,\"price\":390},{\"name\":\"Vegan Greek Sesame Salad\",\"inStock\":false,\"isVeg\":true,\"price\":380},{\"name\":\"Vegan Quiona \\u0026 Hummus Salad\",\"inStock\":false,\"isVeg\":true,\"price\":310},{\"name\":\"Burrata Rocket Salad\",\"description\":\"Rocket Leaves, Breamy Burratta ,Cherry Tomatoes ,Caramalised Nuts Tossed Roasted Balsamic Reduction\",\"inStock\":false,\"isVeg\":true,\"price\":380},{\"name\":\"Quinoa \\u0026 Hummus Salad\",\"description\":\"Microgreens Citrus Fruits Pomogranate Pearls Fried Leeks Tossed With Sesame Dressing Over Hummus\",\"inStock\":false,\"isVeg\":true,\"price\":310},{\"name\":\"Watermelon \\u0026 Feta Salad\",\"description\":\"Stacked  Fresh Water Melon Squares Served With Rocket Lettuce \\u0026 A Drizzle Of Coke \\u0026 Balsamic Reduction\",\"inStock\":false,\"isVeg\":true,\"price\":350},{\"name\":\"Greek Sesame Salad\",\"description\":\"Greek Feta Cheese,Olives,Turnips,Cherry Tomatoes,Cucumber,Bell Peppers Tossed With Roasted Sesame Dressing\",\"inStock\":false,\"isVeg\":true,\"price\":300}]},{\"title\":\"Veg\",\"Dishes\":[{\"name\":\"Original Classic Hummus\",\"inStock\":false,\"isVeg\":true,\"price\":270},{\"name\":\"Patatas Bravas\",\"description\":\"Baby Potato Flash Fried And Served With Marinara Sauce And Mayo.\",\"inStock\":false,\"isVeg\":true,\"price\":220},{\"name\":\"Ping Pong Cottage Cheese\",\"description\":\"Panko Herb Crusted Cottage Cheese Balls Served With Marinara Sauce.\",\"inStock\":false,\"isVeg\":true,\"price\":320},{\"name\":\"Garlic Bread\",\"inStock\":false,\"isVeg\":true,\"price\":160},{\"name\":\"Jalapeno Mac \\u0026 Cheese Sticks\",\"description\":\"Bite Sized Fingers Of Your Favourite Mac \\u0026 Cheese, Spiked With Jalapenos.\",\"inStock\":false,\"isVeg\":true,\"price\":310},{\"name\":\"Trash Can Nachos\",\"description\":\"Piled Up Tortilla Chips, Refried Beans, Jalapenos, Olives, Bell Peppers Topped With Our Special Cheese Sauce, Sour Cream And Spicy Salsa.\",\"inStock\":false,\"isVeg\":true,\"price\":390},{\"name\":\"Sweet Potato Gnocchi\",\"description\":\"Reacheado Butter,Goat Cheese Sauce.\",\"inStock\":false,\"isVeg\":true,\"price\":240},{\"name\":\"Brie On Khari\",\"description\":\"Pineapple Compote \\u0026 Brie Cheese Triangles Served On Home  Made Khari ( Puffs ) \\u0026 Drizzled With Port Wine Reduction\",\"inStock\":false,\"isVeg\":true,\"price\":350},{\"name\":\"Mushroom Peri Peri\",\"description\":\"Peri Peri Marinated Button Mushrooms Panseared \\u0026 Served With Buttered Fresh King Oyster Mushroom \\u0026 Fresh Shimji Mushrooms(Japanese ) . Garnished With Micro Greens\",\"inStock\":false,\"isVeg\":true,\"price\":270},{\"name\":\"Potato Bombas\",\"description\":\"Home Made Fried Potato Balls Stuffed With A Spicy Chilli Jam Made From Bell Peppers ,Vinegar , Green Chillies \\u0026 Brown Sugar . Served With A Red Pepper Coulis\",\"inStock\":false,\"isVeg\":true,\"price\":190},{\"name\":\"Roasted Cauliflower\",\"description\":\"Cauliflower Marinated In A Sriracha \\u0026 Maple Sauce , Then Baked \\u0026 Served Whole Tossed  With Sriracha  Maple Sauce.\",\"inStock\":false,\"isVeg\":true,\"price\":190},{\"name\":\"Vegan Guacamole \\u0026 Nachos\",\"inStock\":false,\"isVeg\":true,\"price\":510},{\"name\":\"Vegan The Broccoli Dipping Bowl\",\"inStock\":false,\"isVeg\":true,\"price\":260},{\"name\":\"Vegan Avocado On Whole Wheat\",\"inStock\":false,\"isVeg\":true,\"price\":520},{\"name\":\"Vegan Mushroom Peri Peri\",\"inStock\":false,\"isVeg\":true,\"price\":270},{\"name\":\"Vegan Super Cheesy Garlic Bread\",\"inStock\":false,\"isVeg\":true,\"price\":310},{\"name\":\"Vegan Sourdough Chilly Cheese Toast\",\"inStock\":false,\"isVeg\":true,\"price\":310},{\"name\":\"Vegan Bloom Bread\",\"inStock\":false,\"isVeg\":true,\"price\":340},{\"name\":\"Vegan Truffle Mushroom Hummus\",\"inStock\":false,\"isVeg\":true,\"price\":360},{\"name\":\"Vegan Red Pepper Hummus\",\"inStock\":false,\"isVeg\":true,\"price\":290},{\"name\":\"Vegan Original Classic Hummus\",\"inStock\":false,\"isVeg\":true,\"price\":270},{\"name\":\"Vegan Grandmama'S Masala Fries\",\"inStock\":false,\"isVeg\":true,\"price\":180},{\"name\":\"Vegan Gunpowder Tuk Fries\",\"inStock\":false,\"isVeg\":true,\"price\":180},{\"name\":\"Vegan Simply Salted Fries\",\"inStock\":false,\"isVeg\":true,\"price\":160},{\"name\":\"Avocado On Whole Wheat\",\"description\":\"Sliced Avocado,Basil Pesto,Tomatoes Redchilli Flakes.\",\"inStock\":false,\"isVeg\":true,\"price\":520},{\"name\":\"Tomato Chilli Arancini\",\"description\":\"Traditional Italian Crispy Rice Balls Flavoured With Tomato Chilli Sauce ,Served With Roasted Pepper Coulis\",\"inStock\":false,\"isVeg\":true,\"price\":250},{\"name\":\"Broccoli Dipping Bowl\",\"description\":\"Kokum Broth,Green \\u0026 Red Chillies Served With Crispy Pav\",\"inStock\":false,\"isVeg\":true,\"price\":260},{\"name\":\"Parmesan \\u0026 Truffle Fries\",\"description\":\"French Fries Drizzled With Truffle Oil \\u0026 Parmesean Cheese\",\"inStock\":false,\"isVeg\":true,\"price\":310},{\"name\":\"Volcano Fries\",\"description\":\"Sizzilling Cheese \\u0026 Spicy Sauce.\",\"inStock\":false,\"isVeg\":true,\"price\":230},{\"name\":\"Sour Dough Chilli Cheese Toast\",\"description\":\"Sour Dough Baguette Toast  Made With A Topping Of Processed Cheese , Chopped Chilles \\u0026 Coriander \\u0026 Then Its Gartinated\",\"inStock\":false,\"isVeg\":true,\"price\":210},{\"name\":\"Super Cheesy Garlic Bread\",\"description\":\"Garlic Bread Made With Home Made Panini \\u0026 Laced With White Cheddar Cheese , Yellow Cheddar ,Mozarella Cheese \\u0026 Gratinated\",\"inStock\":false,\"isVeg\":true,\"price\":210},{\"name\":\"Classic Bruschetta\",\"description\":\"Basil,Olives,Parmesean Cheese \\u0026 Tomatoes.\",\"inStock\":false,\"isVeg\":true,\"price\":240},{\"name\":\"Bloom Bread\",\"description\":\"Cheddar,Mozzarella Cheese \\u0026 Roasted Garlic\",\"inStock\":false,\"isVeg\":true,\"price\":240},{\"name\":\"Truffle Mushroom Hummus\",\"inStock\":false,\"isVeg\":true,\"price\":360},{\"name\":\"Red Pepper Hummus\",\"inStock\":false,\"isVeg\":true,\"price\":290}]},{\"title\":\"Seafood\",\"Dishes\":[{\"name\":\"Butter Garlic Prawns\",\"description\":\"Prawns Tossed In Sauteed Garlic \\u0026 Buttered White Wine Sauce , Served With Garlic Butter \\u0026 Crispy Pav\",\"inStock\":false,\"isVeg\":false,\"price\":460},{\"name\":\"Prawns Dipping Bowl\",\"description\":\"Kokum Broth,Green \\u0026 Red Chillies Served With Crispy Pav\",\"inStock\":false,\"isVeg\":false,\"price\":460}]},{\"title\":\"Chicken\",\"Dishes\":[{\"name\":\"Creamy Mushroom Kejriwal\",\"description\":\"Bulls Eye Egg ( Sunny Side Egg )Served In A Burger Bun With Creamy Mushroom Duxell \\u0026 Green Chilli Cheese Sauce\",\"inStock\":false,\"isVeg\":false,\"price\":310},{\"name\":\"Lamb Bolognese Kejriwal\",\"description\":\"Bulls Eye Eggs( Sunny Side Egg ) Served In A Burger Bun With Spiced Mutton Kheema \\u0026Green Chilli  Cheese Sauce .\",\"inStock\":false,\"isVeg\":false,\"price\":390},{\"name\":\"Asian Style Pepper Chicken\",\"description\":\"Marinated Tender Chicken Slivers In Asian Spices , Deep Fried To A Crispy Texture \\u0026 Served With Schezwan  Sauce\",\"inStock\":false,\"isVeg\":false,\"price\":320},{\"name\":\"Lamb Keema Hummus\",\"inStock\":false,\"isVeg\":false,\"price\":360},{\"name\":\"Lebanese Spiced Chicken Hummus\",\"inStock\":false,\"isVeg\":false,\"price\":320},{\"name\":\"Sriracha \\u0026 Maple Wings\",\"description\":\"Home Made Crispy Chicken Wings Tossed In Siracha Sauce \\u0026 Maple Syrup , Glazed With Soft Butter\",\"inStock\":false,\"isVeg\":false,\"price\":300},{\"name\":\"Barbeque Glaze Wings\",\"description\":\"Chicken Wings Tossed In Barbecue Sauce \\u0026 A Dash Of Demi Glaze\",\"inStock\":false,\"isVeg\":false,\"price\":300},{\"name\":\"Grandmama'S Masal Rub Wings\",\"description\":\"Home Made Crispy Chicken Wings Sprinkled With Gc Special Rub\",\"inStock\":false,\"isVeg\":false,\"price\":260}]},{\"title\":\"Veg\",\"Dishes\":[{\"name\":\"Margherita Pizza\",\"description\":\"Homemade Tomato Sauce Basil With Stringy Mozzarella.\",\"inStock\":false,\"isVeg\":true,\"price\":310},{\"name\":\"Four Cheese Pizza\",\"description\":\"Yellow Cheddar, Mozzarella, Creamy Feta And American.\",\"inStock\":false,\"isVeg\":true,\"price\":425},{\"name\":\"Barbeque Paneer Pizza\",\"description\":\"Barbeque Paneer With Stringy Mozzarella, Jalapenos And Bell Peppers.\",\"inStock\":false,\"isVeg\":true,\"price\":410},{\"name\":\"Grandmama'S Special Pizza\",\"description\":\"Cheese Pizza Topped With Sundried Tomatoes, Olives, Onions And Bell Peppers.\",\"inStock\":false,\"isVeg\":true,\"price\":410},{\"name\":\"Vegan Peri Peri Mushroom Pizza\",\"inStock\":false,\"isVeg\":true,\"price\":510},{\"name\":\"Vegan Truffle Shuffle Pizza\",\"inStock\":false,\"isVeg\":true,\"price\":610},{\"name\":\"Vegan Grandmama'S Special Pizza\",\"inStock\":false,\"isVeg\":true,\"price\":510},{\"name\":\"Vegan Bombay Pizza\",\"inStock\":false,\"isVeg\":true,\"price\":430},{\"name\":\"Vegan Margherita Pizza\",\"inStock\":false,\"isVeg\":true,\"price\":410},{\"name\":\"Peri Peri Paneer Pizza\",\"description\":\"Cottage Cheese,Bellpeppers,Tomatoes \\u0026 Onions.\",\"inStock\":false,\"isVeg\":true,\"price\":410},{\"name\":\"Truffle Shuffle Pizza\",\"description\":\"Pizza Prepared With Assorted Mushrooms \\u0026 Truffle Oil Drizzle\",\"inStock\":false,\"isVeg\":true,\"price\":510},{\"name\":\"Bombay Pizza\",\"description\":\"Spiced Potato Ball Amul Cheese Mozerella,Bell Peppers Onions\",\"inStock\":false,\"isVeg\":true,\"price\":330}]},{\"title\":\"Non-Veg\",\"Dishes\":[{\"name\":\"Barbeque Chicken Pizza\",\"description\":\"Barbeque Chicken With Stringy Mozzarella, Caramelised Onions And Bell Peppers.\",\"inStock\":false,\"isVeg\":false,\"price\":420},{\"name\":\"Spicy Chicken Sausage Pizza\",\"description\":\"Chicken Sausage, Roasted Garlic And Chilli Oil For A Whole Lot Of Spice.\",\"inStock\":false,\"isVeg\":false,\"price\":420},{\"name\":\"Pepperoni Pizza\",\"description\":\"The Italian Classic.\",\"inStock\":false,\"isVeg\":false,\"price\":525},{\"name\":\"Goan Chorizo Pizza\",\"description\":\"Pizza Topped With Pork Sausages,Goan Spices,Toddy Venigar\",\"inStock\":false,\"isVeg\":false,\"price\":420},{\"name\":\"Double Cheese Quadruple Meat Pizza\",\"description\":\"Pizza Topped With Pork Chorizo , Buff Burger Mix , Chicken Sausage\\u0026 Pulled Chicken\",\"inStock\":false,\"isVeg\":false,\"price\":590},{\"name\":\"Bbq Tenderloin Pizza\",\"description\":\"Pizza Topped With Barbeque Marinated Tenderloin Sliver \\u0026 Home Made Carmelized Onions\",\"inStock\":false,\"isVeg\":false,\"price\":480}]},{\"title\":\"Veg\",\"Dishes\":[{\"name\":\"The Grandma Way Mac N Cheese\",\"description\":\"American Grandmama Style Pasta In A Gooey In Your Face Cheesy Sauce.\",\"inStock\":false,\"isVeg\":true,\"price\":350},{\"name\":\"Vegan Spaghetti Aglio Olio\",\"inStock\":false,\"isVeg\":true,\"price\":410},{\"name\":\"Spicy Bird Eye Chilli Mac N Cheese\",\"description\":\"Macaroni In Cheesy Grandmamas Sauce , Flavoured With Smoked Paprika Powder \\u0026 Fresh Bird Eye Chillies \\u0026 Served With Extra Gratinated Parmesan \\u0026 Panko .\",\"inStock\":false,\"isVeg\":true,\"price\":350},{\"name\":\"Spaghetti Aglio Olio\",\"description\":\"Pasta Served With A Hint Of Chilli, Garlic, Basil And Parmesan.\",\"inStock\":false,\"isVeg\":true,\"price\":330},{\"name\":\"Mushroom \\u0026 Truffle Mac N Cheese\",\"description\":\"Creamy Mushroom Ragout With Infused Truffle Oil Topped With Gc Mac N Cheese , And Crusted With Extra Gratinated Parmesan Cheese \\u0026 Panko.\",\"inStock\":false,\"isVeg\":true,\"price\":460},{\"name\":\"Goat Cheese Agnolotti\",\"description\":\"Stuffed Pasta Tossedin A  Butterfly Jalapeno Sauce.\",\"inStock\":false,\"isVeg\":true,\"price\":390},{\"name\":\"Rigatoni Quattro Formaggi\",\"description\":\"Cyliderical Pasta Cooked In House Four Cheese Sauce .\",\"inStock\":false,\"isVeg\":true,\"price\":380},{\"name\":\"Penne Marinara\",\"description\":\"A Cylindrical Commercial Pasta Cooked In A Classic Tomato \\u0026 Basil Sauce .Garnished With Olive Oil \\u0026 Parmesan Cheese .\",\"inStock\":false,\"isVeg\":true,\"price\":320},{\"name\":\"Pesto Basilico\",\"description\":\"Penne Pasta Cooked In A Fresh Basil Pesto With  Vegetable .Enriched Wirh Fresh Cream \\u0026 Parmesan Cheese\",\"inStock\":false,\"isVeg\":true,\"price\":320},{\"name\":\"Pumpkin \\u0026 Sage Ravioli\",\"description\":\"A Home Made Stuffed Pasta Made From Pumpkin , \\u0026 Candied Sugar , Served With A Sage Butter Sauce\",\"inStock\":false,\"isVeg\":true,\"price\":310},{\"name\":\"Ragitoni Mushroom Ragout\",\"description\":\"Rigatoni Pasta Cooked In A Redwine Based Creamy Mushroom Stew\",\"inStock\":false,\"isVeg\":true,\"price\":350}]},{\"title\":\"Chicken\",\"Dishes\":[{\"name\":\"Buffalo Chicken Mac N Cheese\",\"description\":\"Pulled Chicken In Buffalo Sauce Topped With Gc Mac N Cheese , Topped With Extra Gratinated Parmesan Cheese \\u0026 Panko\",\"inStock\":false,\"isVeg\":false,\"price\":410},{\"name\":\"Chicken Bolognese\",\"description\":\"Pasta Cooked In Chicken ,Tomato \\u0026 Demi Glaze Sauce.\",\"inStock\":false,\"isVeg\":false,\"price\":390},{\"name\":\"Spaghetti Carbonara\",\"description\":\"Traditional Carbonara Tossed With Parmesan And Bacon\",\"inStock\":false,\"isVeg\":false,\"price\":410}]},{\"title\":\"Veg Mains\",\"Dishes\":[{\"name\":\"Sundried Tomato Risotto\",\"description\":\"Creamy Arborio Rice In Tomato Sauce Topped With Italian Sundried Tomatoes.\",\"inStock\":false,\"isVeg\":true,\"price\":410},{\"name\":\"Green Garlic Risotto\",\"description\":\"Creamy Chive Pesto Sauce With Crispy Lotus Stem.\",\"inStock\":false,\"isVeg\":true,\"price\":390},{\"name\":\"Volcano Jambalaya\",\"description\":\"Rice Prepared With Mixed Vegetables.\",\"inStock\":false,\"isVeg\":true,\"price\":340},{\"name\":\"Pan Grilled Cottage Cheese Steak\",\"description\":\"Cranberries, Cashew Nuts, Roasted Pepper ,Creem Sauce Buttered Beans ,Tomato Pelaf.\",\"inStock\":false,\"isVeg\":true,\"price\":410},{\"name\":\"Wild Mushroom Rissoto\",\"description\":\"Creamy Mushroom Risotto Served With Nut Brown Butter\",\"inStock\":false,\"isVeg\":true,\"price\":410},{\"name\":\"Mushroom Stroganoff Pot Pie\",\"description\":\"Fresh Japanese Mushrooms \\u0026 Button Mushrooms Cooked In A Fresh Cream \\u0026 Whisky Sauce , Topped With A Round Puff Pastry As An Accompaniement\",\"inStock\":false,\"isVeg\":true,\"price\":450}]},{\"title\":\"Chicken Mains\",\"Dishes\":[{\"name\":\"Grandmama'S Roast Chicken\",\"description\":\"Classic Roast Chicken, Mixed Spring Vegetables Served With Rosemary Mash And Pan Jus.\",\"inStock\":false,\"isVeg\":false,\"price\":490},{\"name\":\"Homestyle Grilled Chicken\",\"description\":\"Grilled Chicken Breast With Red Wine Jus, And Mash Potato\",\"inStock\":false,\"isVeg\":false,\"price\":450},{\"name\":\"Spaghetti Meatballs\",\"description\":\"Spaghetti Tossed In A Lamb Meat Ball \\u0026 Light Tomato Sauce . Garnished With Parmesann Cheese \\u0026 Fresh Herbs\",\"inStock\":false,\"isVeg\":false,\"price\":480},{\"name\":\"Parmesan Crusted Mahi Mahi\",\"description\":\"Spicy Paprica Crust Berreu Blanc, Brown Rice Risotto\",\"inStock\":false,\"isVeg\":false,\"price\":460},{\"name\":\"Steak \\u0026 Potatoes\",\"description\":\"Grilled Tenderloin Medallions Served With Buttery Mashed Potatoes ,Broccoli  \\u0026 Demiglaze\",\"inStock\":false,\"isVeg\":false,\"price\":450}]},{\"title\":\"Lamb Main\",\"Dishes\":[{\"name\":\"Shepherd'S Pie\",\"description\":\"Rich Lamb Bolognese Served In A Skillet With A Gratinated  Mashed Potato Topping ,\\u0026 Served With Garlic Bread.\",\"inStock\":false,\"isVeg\":false,\"price\":480}]},{\"title\":\"Desserts\",\"Dishes\":[{\"name\":\"Grandmama'S Biscuit Cake\",\"description\":\"Our Own Take On The Grand Old English Biscuit Cake.\",\"inStock\":false,\"isVeg\":true,\"price\":240},{\"name\":\"Mudcake\",\"description\":\"Deliciously Moist And Rich, This Is Definitely The Best Cake For Chocolate Lovers.\",\"inStock\":false,\"isVeg\":true,\"price\":240},{\"name\":\"Hazelnut Tian\",\"description\":\"Grandmama'S Special Chocolate Sponge Pyramid Cake Soaked In The Richness Of Hazelnut And Ferrero Rocher.\",\"inStock\":false,\"isVeg\":true,\"price\":280},{\"name\":\"Statuario Marble Cheese Cake\",\"description\":\"Cremy Set Charcoal Swirl Cheesecake Served With Californian Sour Cherry Compote And A Charcoal Tuile.\",\"inStock\":false,\"isVeg\":false,\"price\":250},{\"name\":\"The Best Chocolate Mousse\",\"description\":\"Decadent And Rich Chocolate Mousse. A Recommended Indulgence\",\"inStock\":false,\"isVeg\":true,\"price\":240},{\"name\":\"The Chocolate Ball\",\"description\":\"Tempered Chocolate Balls,Chocolate Mousse, Chocolate Brownie, Cookie Crumble,Hot Chocolate Fudge Sauce.\",\"inStock\":false,\"isVeg\":false,\"price\":310},{\"name\":\"Hot Skillet Cherry Apple Pie\",\"description\":\"Caramelized Apples, Cinnamon, Californian Cherries,Crisp Pie\",\"inStock\":false,\"isVeg\":true,\"price\":280},{\"name\":\"Royal Blue Velvet Cake\",\"description\":\"Soft Cream Cheese, Edible Blue Dust ,Sour Cherry Compote,Crunchy Crumble\",\"inStock\":false,\"isVeg\":true,\"price\":240}]},{\"title\":\"Extras\",\"Dishes\":[{\"name\":\"Add Chicken\",\"inStock\":false,\"isVeg\":false,\"price\":95},{\"name\":\"Add Cheese\",\"inStock\":false,\"isVeg\":true,\"price\":80},{\"name\":\"Chciken Sausages\",\"inStock\":false,\"isVeg\":false,\"price\":160},{\"name\":\"Sauteed Mushroom\",\"inStock\":false,\"isVeg\":true,\"price\":140},{\"name\":\"Potato Wedges\",\"inStock\":false,\"isVeg\":true,\"price\":160},{\"name\":\"Grandmama'S Masala Fries\",\"inStock\":false,\"isVeg\":true,\"price\":180},{\"name\":\"Simply Salted Fries\",\"description\":\"Crispy Salted Macains Fries\",\"inStock\":false,\"isVeg\":true,\"price\":160},{\"name\":\"Mash Potatoes\",\"inStock\":false,\"isVeg\":true,\"price\":110},{\"name\":\"Add Prawns\",\"inStock\":false,\"isVeg\":false,\"price\":180},{\"name\":\"Add Bacon\",\"inStock\":false,\"isVeg\":false,\"price\":170},{\"name\":\"Vegan Roasted Cauliflower\",\"inStock\":false,\"isVeg\":true,\"price\":190},{\"name\":\"Vegan Garlic Bread\",\"inStock\":false,\"isVeg\":true,\"price\":160},{\"name\":\"Extra Pita Bread\",\"inStock\":false,\"isVeg\":true,\"price\":60},{\"name\":\"Extra Pav\",\"inStock\":false,\"isVeg\":true,\"price\":60},{\"name\":\"Extra Rice\",\"inStock\":false,\"isVeg\":true,\"price\":80},{\"name\":\"Add Tenderloin\",\"inStock\":false,\"isVeg\":false,\"price\":150},{\"name\":\"Add Exotic Vegetable\",\"inStock\":false,\"isVeg\":true,\"price\":90},{\"name\":\"Gunpowder Tuk Fries\",\"description\":\"Crispy Potato Wedges Dregged With Mallipuram Gun Powder Spices\",\"inStock\":false,\"isVeg\":true,\"price\":180}]},{\"title\":\"Veg\",\"Dishes\":[{\"name\":\"Honey Mustard Wings\",\"description\":\"Chicken Wings Tossed In Homade Honey Mustard Sauce\",\"inStock\":false,\"isVeg\":false,\"price\":280}]},{\"title\":\"Veg\",\"Dishes\":[{\"name\":\"Mahi Mahi Fish Fingers\",\"description\":\"Lemom \\u0026 Dijon Mustard Marinated Mahi Mahi  Fish Fingers Breaded \\u0026 Deep Fried , Served Along With A Classic  Tartar Sauce\",\"inStock\":false,\"isVeg\":false,\"price\":40}]}],\"URL\":\"https://www.swiggy.com/restaurants/grandmamas-cafe-vashi-mumbai-118547\"}}\n";


            if (result != null) {
                // Handle the data result here
                try {

                    GlobalVariables.cartList.clear();

                    JSONObject jsonObject = new JSONObject(result);

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
                        dish.setId(i);
                        dish.setQty(0);
                        dish.setName(dishObject.getString("name"));
                        dish.setDescription(dishObject.getString("description"));
                        dish.setInStock(dishObject.getBoolean("inStock"));
//                        dish.setVeg(dishObject.getBoolean("isVeg"));
                        dish.setPrice(dishObject.getDouble("price"));
                        dishesList.add(dish);
                        try {
                            ormliteHelper.createOrUpdate( dish );
                        }
                        catch (SQLException | java.sql.SQLException throwables) {
                            throwables.printStackTrace();
                            Toast.makeText( getContext(),throwables.getMessage(),Toast.LENGTH_LONG ).show();
                        }
//                        DAO.txn.beginTransaction();
//                        DAO.create(dish, DishDTO.class);
//                        DAO.txn.setTransactionSuccessful();
//                        DAO.txn.endTransaction();
                    }
//                    menuJsonModel.setDishes(dishesList);
                    itemAdapter = new ItemAdapter(getContext(),dishesList);
                    recyclerView.setAdapter(itemAdapter);
                    itemAdapter.notifyDataSetChanged();


                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            } else {
                try {
                    dishesList = ormliteHelper.getAll( DishDTO.class );
                } catch (java.sql.SQLException e) {
                    throw new RuntimeException(e);
                }
                itemAdapter = new ItemAdapter(getContext(),dishesList);
                recyclerView.setAdapter(itemAdapter);
                itemAdapter.notifyDataSetChanged();

                Toast.makeText(getContext(), "API Error", Toast.LENGTH_LONG).show();
            }
        }
    }
}