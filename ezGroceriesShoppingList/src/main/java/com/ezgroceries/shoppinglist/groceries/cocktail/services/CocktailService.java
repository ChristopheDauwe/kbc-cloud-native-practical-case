package com.ezgroceries.shoppinglist.groceries.cocktail.services;

import com.ezgroceries.shoppinglist.groceries.cocktail.Cocktail;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service("CocktailService")
public class CocktailService {

    private final Cocktail MARGERITA = new Cocktail(UUID.randomUUID(),"Margerita","Cocktail glass",
            "Rub the rim of the glass with the lime slice to make the salt stick to it. Take care to moisten..",
            "https://www.thecocktaildb.com/images/media/drink/wpxpvu1439905379.jpg",
            List.of("Tequila",
                    "Triple sec",
                    "Lime juice",
                    "Salt"));

    private final Cocktail BLUE_MARGERITA = new Cocktail(UUID.randomUUID(),"Blue Margerita","Cocktail glass",
            "Rub rim of cocktail glass with lime juice. Dip rim in coarse salt..",
            "https://www.thecocktaildb.com/images/media/drink/qtvvyq1439905913.jpg",
            List.of("Tequila",
                    "Blue Curacao",
                    "Lime juice",
                    "Salt"));

    public List<Cocktail> findAll(){
        return List.of(MARGERITA,BLUE_MARGERITA);
    }
}
