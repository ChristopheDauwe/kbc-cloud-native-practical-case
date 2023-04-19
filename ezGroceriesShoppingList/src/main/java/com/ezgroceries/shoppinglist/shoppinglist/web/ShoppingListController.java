package com.ezgroceries.shoppinglist.shoppinglist.web;

import com.ezgroceries.shoppinglist.groceries.cocktail.CocktailResource;
import com.ezgroceries.shoppinglist.shoppinglist.ShoppingListMapper;
import com.ezgroceries.shoppinglist.shoppinglist.ShoppingListResource;
import com.ezgroceries.shoppinglist.shoppinglist.domain.ShoppingList;
import com.ezgroceries.shoppinglist.shoppinglist.services.ShoppingListService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.security.Principal;
import java.util.Collection;
import java.util.UUID;

@RestController("ShoppingListController")
public class ShoppingListController {

    private static final Logger log = LoggerFactory.getLogger(ShoppingListController.class);

    private final ShoppingListMapper shoppingListMapper;

    private final ShoppingListService shoppingListService;

    public ShoppingListController(ShoppingListMapper shoppingListMapper, ShoppingListService shoppingListService) {
        this.shoppingListMapper = shoppingListMapper;
        this.shoppingListService = shoppingListService;
    }

    @PostMapping(value = "/shopping-lists")
    public ResponseEntity<Void> createShoppingList(@RequestBody ShoppingListResource newShoppingListResource, Principal principal) {
        ShoppingList shoppingList = shoppingListService.createShoppingList(shoppingListMapper.toShoppingList(newShoppingListResource,principal.getName()));

        return entityWithLocation(shoppingList.getId());
    }

    @PostMapping(value = "/shopping-lists/{shoppingListId}/cocktails")
    public ResponseEntity<Void> addCocktailToShoppingList(@PathVariable("shoppingListId") UUID shoppingListID, @RequestBody CocktailResource cocktailResource) {

            shoppingListService.addCocktail(shoppingListID, cocktailResource);

            URI location = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/shopping-lists/{id}")
                    .buildAndExpand(shoppingListID)
                    .toUri();
            return ResponseEntity.created(location).contentType(MediaType.APPLICATION_JSON).build();
    }


    @GetMapping(value = "/shopping-lists")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("isFullyAuthenticated()")
    public Collection<ShoppingListResource> getAllShoppingList(Principal principal) {
        return shoppingListMapper.toShoppingListResources(shoppingListService.findAllByUsername(principal.getName()));
    }

    @GetMapping(value = "/shopping-lists/{shoppingListId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("isFullyAuthenticated()")
    public ShoppingListResource getShoppingListById(@PathVariable("shoppingListId") UUID shoppingListID) {
        return shoppingListMapper.toShoppingListResource(shoppingListService.findById(shoppingListID));
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
