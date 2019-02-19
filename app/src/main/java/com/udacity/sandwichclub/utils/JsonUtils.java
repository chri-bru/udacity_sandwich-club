package com.udacity.sandwichclub.utils;

import com.udacity.sandwichclub.model.Sandwich;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    // string JSON elements
    public static final String PLACE_OF_ORIGIN = "placeOfOrigin";
    public static final String DESCRIPTION = "description";
    public static final String IMAGE = "image";

    // array JSON elements
    public static final String INGREDIENTS = "ingredients";

    // nested JSON elements
    public static final String NAME = "name";
    public static final String MAIN_NAME = "mainName";
    public static final String ALSO_KNOWN_AS = "alsoKnownAs";
    
    private static final String FALLBACK = "";

    public static Sandwich parseSandwichJson(String json) {
        Sandwich sandwich = new Sandwich();

        try {
            JSONObject sandwichJson = new JSONObject(json);

            // get all string JSON properties
            String placeOfOrigin = sandwichJson.optString(PLACE_OF_ORIGIN, FALLBACK);
            String description = sandwichJson.optString(DESCRIPTION, FALLBACK);
            String imageUrl = sandwichJson.optString(IMAGE, FALLBACK);

            // get all array properties
            JSONArray ingredients = sandwichJson.optJSONArray(INGREDIENTS);

            // get nested properties
            JSONObject sandwichName = sandwichJson.optJSONObject(NAME);
            String mainName = "";
            JSONArray alsoKnownAs = null;
            if (sandwichName != null) {
                mainName = sandwichName.optString(MAIN_NAME);
                alsoKnownAs = sandwichName.optJSONArray(ALSO_KNOWN_AS);
            }

            // populate model
            sandwich.setPlaceOfOrigin(placeOfOrigin);
            sandwich.setDescription(description);
            sandwich.setImage(imageUrl);
            sandwich.setIngredients(jsonArrayToList(ingredients));
            sandwich.setMainName(mainName);
            sandwich.setAlsoKnownAs(jsonArrayToList(alsoKnownAs));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return sandwich;
    }

    /**
     * Helper method to convert JSONArray to a list
     * @param jsonArray JSONArray to convert
     * @return a list of Strings contained in the jsonArray
     * @throws JSONException
     */
    private static List<String> jsonArrayToList(JSONArray jsonArray) throws JSONException {
        List<String> result = new ArrayList<>();

        if (jsonArray == null) {
            return result;
        }

        for (int index = 0; index < jsonArray.length(); index++) {
            result.add(jsonArray.optString(index));
        }

        return result;
    }
}
