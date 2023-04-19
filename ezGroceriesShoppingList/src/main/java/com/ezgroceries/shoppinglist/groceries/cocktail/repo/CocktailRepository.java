package com.ezgroceries.shoppinglist.groceries.cocktail.repo;

import com.ezgroceries.shoppinglist.groceries.cocktail.domain.Cocktail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.UUID;

@Repository
public interface CocktailRepository extends JpaRepository<Cocktail, UUID> {


    Collection<Cocktail> findByIdDrinkIn(Collection<String> ids);


    Collection<Cocktail> findByNameContainingIgnoreCase(String search);
}
