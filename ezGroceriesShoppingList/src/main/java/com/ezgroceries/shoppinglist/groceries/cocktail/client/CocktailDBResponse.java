package com.ezgroceries.shoppinglist.groceries.cocktail.client;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CocktailDBResponse {

    private List<DrinkResource> drinks;

}