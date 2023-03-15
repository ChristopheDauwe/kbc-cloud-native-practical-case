package com.ezgroceries.shoppinglist.shoppinglist.web;

import com.ezgroceries.shoppinglist.groceries.cocktail.CocktailResource;
import com.ezgroceries.shoppinglist.shoppinglist.ShoppingListResource;
import com.ezgroceries.shoppinglist.shoppinglist.services.ShoppingListService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@RestController("ShoppingListController")
public class ShoppingListController {

    private static final Logger log = LoggerFactory.getLogger(ShoppingListController.class);


    private final ShoppingListService shoppingListService;

    public ShoppingListController(ShoppingListService shoppingListService) {
        this.shoppingListService = shoppingListService;
    }

    @PostMapping(value = "/shopping-lists")
    public ResponseEntity<Void> createShoppingList(@RequestBody ShoppingListResource newShoppingListResource) {
        ShoppingListResource shoppingListResource = shoppingListService.createShoppingList(newShoppingListResource);

        return entityWithLocation(shoppingListResource.id());
    }

    @PostMapping(value = "/shopping-lists/{shoppingListId}/cocktails")
    public ResponseEntity<Void> addCocktailToShoppingList(@PathVariable("shoppingListId") UUID shoppingListID, @RequestBody CocktailResource cocktailResource) {
        ShoppingListResource shoppingListResource = shoppingListService.findById(shoppingListID);




            shoppingListService.addCocktail(shoppingListResource, cocktailResource);

            URI location = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/shopping-lists/{id}")
                    .buildAndExpand(shoppingListID)
                    .toUri();
            return ResponseEntity.created(location).contentType(MediaType.APPLICATION_JSON).build();
    }


    @GetMapping(value = "/shopping-lists")
    @ResponseStatus(HttpStatus.OK)
    public Collection<ShoppingListResource> getAllShoppingList() {


        return shoppingListService.findAll();
    }

    /**
     * Return a response with the location of the new resource.
     *
     * Suppose we have just received an incoming URL of, say,
     * http://localhost:8080/accounts and resourceId
     * is "12345". Then the URL of the new resource will be
     * http://localhost:8080/accounts/12345.
     */
    private ResponseEntity<Void> entityWithLocation(Object resourceId) {

        // Determines URL of child resource based on the full URL of the given
        // request, appending the path info with the given resource Identifier
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path("/{resourceId}")
                .buildAndExpand(resourceId)
                .toUri();

        // Return an HttpEntity object - it will be used to build the
        // HttpServletResponse
        return ResponseEntity.created(location).contentType(MediaType.APPLICATION_JSON).build();
    }

}
