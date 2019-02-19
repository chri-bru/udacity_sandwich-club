package com.udacity.sandwichclub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

import java.util.List;

public class DetailActivity extends AppCompatActivity {

    // intent handling
    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;

    // UI widget references
    private ImageView ingredientsIv;
    private TextView alsoKnownAsTv;
    private TextView descriptionTv;
    private TextView ingredientsTv;

    // main sandwich
    private Sandwich sandwich;
    private TextView originTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        initUiReferences();
        processIntent();
        populateUI();
    }

    private void initUiReferences() {
        ingredientsIv = findViewById(R.id.sandwich_image);
        alsoKnownAsTv = findViewById(R.id.sandwich_also_known_as);
        descriptionTv = findViewById(R.id.sandwich_description);
        ingredientsTv = findViewById(R.id.sandwich_ingredients);
        originTv = findViewById(R.id.sandwich_origin);
    }

    private void processIntent() {
        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];
        sandwich = JsonUtils.parseSandwichJson(json);
        if (sandwich == null) {
            // Sandwich data unavailable
            closeOnError();
            return;
        }
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI() {
        setTextToTv(alsoKnownAsTv, TextUtils.join(", ", sandwich.getAlsoKnownAs()));
        setTextToTv(descriptionTv, sandwich.getDescription());

        // add listing to text view
        List<String> ingredients = sandwich.getIngredients();

        if (ingredients.size() > 0) {
            String firstIngredient = ingredients.get(0);
            firstIngredient = " - " + firstIngredient;
            ingredients.set(0, firstIngredient);
        }

        setTextToTv(ingredientsTv, TextUtils.join("\n - ", sandwich.getIngredients()));

        // set place of origin
        setTextToTv(originTv, sandwich.getPlaceOfOrigin());

        // image
        Picasso.with(this)
                .load(sandwich.getImage())
                .placeholder(R.mipmap.ic_placeholder)
                .error(R.mipmap.ic_launcher)
                .into(ingredientsIv);

        // title (main name)
        setTitle(sandwich.getMainName());
    }

    private void setTextToTv(TextView tv, String input) {
        if (isNullOrEmpty(input)) {
            input = getString(R.string.detail_unknown);
        }

        tv.setText(input);
    }

    private boolean isNullOrEmpty(String s) {
        return s == null || s.equals("");
    }
}
