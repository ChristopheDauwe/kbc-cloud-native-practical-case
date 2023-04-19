package com.ezgroceries.shoppinglist.groceries.cocktail.client;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


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

        if(!strIngredient1.isEmpty()){
            ingr.add(strIngredient1);
        }
        if(!strIngredient2.isEmpty()){
            ingr.add(strIngredient2);
        }
        if(!strIngredient3.isEmpty()){
            ingr.add(strIngredient3);
        }

        return ingr;
    }
}