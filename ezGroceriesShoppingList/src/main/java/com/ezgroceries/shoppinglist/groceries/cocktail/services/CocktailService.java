package com.ezgroceries.shoppinglist.groceries.cocktail.services;

import com.ezgroceries.shoppinglist.groceries.cocktail.CocktailException;
import com.ezgroceries.shoppinglist.groceries.cocktail.CocktailMapper;
import com.ezgroceries.shoppinglist.groceries.cocktail.CocktailResource;
import com.ezgroceries.shoppinglist.groceries.cocktail.client.CocktailDBClient;
import com.ezgroceries.shoppinglist.groceries.cocktail.client.CocktailDBResponse;
import com.ezgroceries.shoppinglist.groceries.cocktail.client.DrinkResource;
import com.ezgroceries.shoppinglist.groceries.cocktail.domain.Cocktail;
import com.ezgroceries.shoppinglist.groceries.cocktail.repo.CocktailRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service("CocktailService")
public class CocktailService {

    private final CocktailDBClient cocktailClient;
    private final CocktailRepository cocktailRepository;

    private final CocktailMapper cocktailMapper;


    public CocktailService(CocktailDBClient cocktailClient, CocktailRepository cocktailRepository, CocktailMapper cocktailMapper) {
        this.cocktailClient = cocktailClient;
        this.cocktailRepository = cocktailRepository;
        this.cocktailMapper = cocktailMapper;
    }

    public Collection<CocktailResource> findAll(String search){
        CocktailDBResponse response = cocktailClient.searchCocktails(search);
        return mergeCocktails(response.getDrinks());
    }

    public Collection<Cocktail> findAllInDB(){

        return cocktailRepository.findAll();
    }

    public CocktailResource save(Cocktail cocktail){
        Optional<Cocktail> cocktailOptional = cocktailRepository.findById(cocktail.getId());

        return cocktailMapper.toCocktailResource(cocktailOptional.orElse(cocktailRepository.save(cocktail)));
    }

    public Cocktail findCocktailById(UUID uuid){
        Optional<Cocktail> cocktailOptional = cocktailRepository.findById(uuid);

        return cocktailOptional.orElseThrow(() -> new CocktailException(String.format("Cocktail with id %s does not exist", uuid)));

    }

    public Collection<CocktailResource> mergeCocktails(Collection<DrinkResource>drinks){
        //Get all the idDrink attributes
       Collection<String> ids=drinks.stream().map(DrinkResource::idDrink).toList();

        //Get all the ones we already have from our DB, use a Map for convenient lookup
        Map<String, Cocktail> existingEntityMap=cocktailRepository.findByIdDrinkIn(ids).stream().collect(Collectors.toMap(Cocktail::getIdDrink, o->o,(o, o2)->o));

        //Stream over all the drinks, map them to the existing ones, persist a new one if not existing
        Map<String, Cocktail> allEntityMap=drinks.stream().map(drinkResource->
                existingEntityMap.getOrDefault(
                    drinkResource.idDrink(),
                    cocktailRepository.save(cocktailMapper.toCocktail(drinkResource)))
        ).collect(Collectors.toMap(Cocktail::getIdDrink, o->o,(o, o2)->o));

        //Merge drinks and our entities, transform to CocktailResource instances
        return mergeAndTransform(drinks,allEntityMap);
    }

    private Collection<CocktailResource> mergeAndTransform(Collection<DrinkResource>drinks,Map<String, Cocktail> allEntityMap){
        return drinks.stream().map(drinkResource->new CocktailResource(
                allEntityMap.get(drinkResource.idDrink()).getId(),
                drinkResource.strDrink(),drinkResource.strGlass(),
                drinkResource.strInstructions(),
                drinkResource.strDrinkThumb(),
                drinkResource.getIngredients())).toList();
    }


}
