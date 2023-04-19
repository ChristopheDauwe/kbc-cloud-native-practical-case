package com.ezgroceries.shoppinglist.groceries.cocktail.client;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Collection;


/**
 *
 * @param idDrink
 * @param strDrink
 * @param strGlass
 * @param strInstructions
 * @param strDrinkThumb
 * @param strIngredient1
 * @param strIngredient2
 * @param strIngredient3
 */
public record DrinkResource (String idDrink, String strDrink, String strGlass, String strInstructions, String strDrinkThumb,
     String strIngredient1,
     String strIngredient2,
     String strIngredient3){



    public Collection<String> getIngredients() {
        Collection<String> ingr = new ArrayList<>();

        if(StringUtils.isNotEmpty(strIngredient1)){
            ingr.add(strIngredient1);
        }
        if(StringUtils.isNotEmpty(strIngredient2)){
            ingr.add(strIngredient2);
        }
        if(StringUtils.isNotEmpty(strIngredient3)){
            ingr.add(strIngredient3);
        }

        return ingr;
    }
}