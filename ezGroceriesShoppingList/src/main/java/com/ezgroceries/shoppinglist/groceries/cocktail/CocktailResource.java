package com.ezgroceries.shoppinglist.groceries.cocktail;


import java.util.Collection;
import java.util.UUID;


/**
 *
 * @param id
 * @param name
 * @param glass
 * @param instructions
 * @param image
 * @param ingredients
 */
public record CocktailResource (UUID id, String name, String glass, String instructions, String image, Collection<String> ingredients){}