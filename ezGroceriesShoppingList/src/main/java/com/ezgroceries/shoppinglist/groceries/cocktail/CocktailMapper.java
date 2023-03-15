package com.ezgroceries.shoppinglist.groceries.cocktail;

import com.ezgroceries.shoppinglist.groceries.cocktail.client.DrinkResource;
import com.ezgroceries.shoppinglist.groceries.cocktail.domain.Cocktail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;


@Mapper(componentModel = "spring")
public interface CocktailMapper {

   default Cocktail toCocktail(DrinkResource drinkResource) {

        return new Cocktail(
                UUID.randomUUID(),
                drinkResource.getIdDrink(),
                drinkResource.getStrDrink(),
                drinkResource.getIngredients()
                );

    }

    default CocktailResource toCocktailResource(Cocktail cocktail){
        return new CocktailResource(
                cocktail.getId(),
                cocktail.getName(),
                "glass",
                "instructions",
                "image",
                cocktail.getIngredients()
        );
    }
}
