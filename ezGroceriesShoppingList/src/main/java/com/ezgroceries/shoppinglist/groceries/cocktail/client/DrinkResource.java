package com.ezgroceries.shoppinglist.groceries.cocktail.client;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class DrinkResource {
    private String idDrink;
    private String strDrink;
    private String strGlass;
    private String strInstructions;
    private String strDrinkThumb;
    private String strIngredient1;
    private String strIngredient2;
    private String strIngredient3;

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