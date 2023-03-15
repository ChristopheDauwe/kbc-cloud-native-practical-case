package com.ezgroceries.shoppinglist.shoppinglist.domain;

import com.ezgroceries.shoppinglist.groceries.cocktail.domain.Cocktail;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "shopping_list")
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class ShoppingList {

    @Id
    @Column(name = "id", unique = true)
    private UUID id ;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @ManyToMany
    @JoinTable(
            name = "cocktail_shopping_list",
            joinColumns = @JoinColumn(name = "shopping_list_id"),
            inverseJoinColumns = @JoinColumn(name = "cocktail_id"))
    Set<Cocktail> cocktails = new HashSet<>();

    public void addCocktail(Cocktail cocktail){
        this.cocktails.add(cocktail);
    }
}
