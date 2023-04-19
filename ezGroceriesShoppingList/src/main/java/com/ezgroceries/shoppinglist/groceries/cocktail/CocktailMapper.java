package com.ezgroceries.shoppinglist.groceries.cocktail;

import com.ezgroceries.shoppinglist.groceries.cocktail.client.DrinkResource;
import com.ezgroceries.shoppinglist.groceries.cocktail.domain.Cocktail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.UUID;


@Mapper(componentModel = "spring")
public interface CocktailMapper {

   default Cocktail toCocktail(DrinkResource drinkResource) {

        return new Cocktail(
                UUID.randomUUID(),
                drinkResource.idDrink(),
                drinkResource.strDrink(),
                drinkResource.strGlass(),
                drinkResource.strInstructions(),
                drinkResource.strDrinkThumb(),
                drinkResource.getIngredients()
                );

    }

    default DrinkResource toDrinkResource(Cocktail cocktail) {
        List<String> ingredients = cocktail.getIngredients().stream().toList();

        String ingredient1 = "";
        String ingredient2 = "";
        String ingredient3 = "";

        try{
            ingredient1 = ingredients.get(0);
            ingredient2 = ingredients.get(1);
            ingredient3 = ingredients.get(2);
        }catch (IndexOutOfBoundsException ex){

        }


        return new DrinkResource(cocktail.getIdDrink(), cocktail.getName(), cocktail.getGlass(), cocktail.getInstructions(), cocktail.getImageURL(),ingredient1,ingredient2,ingredient3 );

    }

    default CocktailResource toCocktailResource(Cocktail cocktail){
        return new CocktailResource(
                cocktail.getId(),
                cocktail.getName(),
                cocktail.getGlass(),
                cocktail.getInstructions(),
                cocktail.getImageURL(),
                cocktail.getIngredients()
        );
    }
}
