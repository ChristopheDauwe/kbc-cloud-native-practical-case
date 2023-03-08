package com.ezgroceries.shoppinglist.groceries.cocktail;

import com.ezgroceries.shoppinglist.groceries.cocktail.client.DrinkResource;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;


@Mapper(componentModel = "spring")
public interface CocktailMapper {

    default Cocktail toCocktail(DrinkResource drinkResource) {

        return new Cocktail(
                UUID.nameUUIDFromBytes(drinkResource.getIdDrink().getBytes()),
                drinkResource.getStrDrink(),
                drinkResource.getStrGlass(),
                drinkResource.getStrInstructions(),
                drinkResource.getStrDrinkThumb(),
                drinkResource.getIngredients()
                );

    }

}
