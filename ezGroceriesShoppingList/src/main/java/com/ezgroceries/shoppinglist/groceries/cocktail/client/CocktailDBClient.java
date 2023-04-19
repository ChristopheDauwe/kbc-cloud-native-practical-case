package com.ezgroceries.shoppinglist.groceries.cocktail.client;

import com.ezgroceries.shoppinglist.groceries.cocktail.CocktailMapper;
import com.ezgroceries.shoppinglist.groceries.cocktail.domain.Cocktail;
import com.ezgroceries.shoppinglist.groceries.cocktail.repo.CocktailRepository;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
@FeignClient(name = "cocktailDBClient", url = "https://www.thecocktaildb.com/api/json/v1/1", fallback = CocktailDBClient.CocktailDBClientFallback.class)
public interface CocktailDBClient {



    @GetMapping(value = "search.php")
    CocktailDBResponse searchCocktails(@RequestParam("s") String search);

    @Component
    class CocktailDBClientFallback implements CocktailDBClient {

        private final CocktailRepository cocktailRepository;
        private final CocktailMapper cocktailMapper;

        public CocktailDBClientFallback(CocktailRepository cocktailRepository, CocktailMapper cocktailMapper) {
            this.cocktailRepository = cocktailRepository;
            this.cocktailMapper = cocktailMapper;
        }

        @Override
        public CocktailDBResponse searchCocktails(String search) {
            Collection<Cocktail> cocktailEntities = cocktailRepository.findByNameContainingIgnoreCase(search);

            CocktailDBResponse cocktailDBResponse = new CocktailDBResponse();
            cocktailDBResponse.setDrinks(cocktailEntities.stream().map(cocktailMapper::toDrinkResource).toList());

            return cocktailDBResponse;
        }
    }
}