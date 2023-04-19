package com.ezgroceries.shoppinglist.shoppinglist.repo;

import com.ezgroceries.shoppinglist.shoppinglist.domain.ShoppingList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ShoppingListRepository extends JpaRepository<ShoppingList, UUID> {


    List<ShoppingList> findAllByUsername(String username);
}
