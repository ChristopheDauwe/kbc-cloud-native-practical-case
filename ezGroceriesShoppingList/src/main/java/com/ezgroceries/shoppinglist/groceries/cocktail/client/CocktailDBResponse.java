package com.ezgroceries.shoppinglist.groceries.cocktail.client;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class CocktailDBResponse {

    private List<DrinkResource> drinks;

}