package com.ezgroceries.shoppinglist;

import com.ezgroceries.shoppinglist.groceries.cocktail.CocktailResource;
import com.ezgroceries.shoppinglist.groceries.cocktail.services.CocktailService;
import com.ezgroceries.shoppinglist.shoppinglist.ShoppingListResource;
import com.ezgroceries.shoppinglist.shoppinglist.services.ShoppingListService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.transaction.Transactional;
import java.util.*;


import static com.ezgroceries.shoppinglist.CocktailResourceControllerTests.COCKTAIL;
import static com.ezgroceries.shoppinglist.TestUtil.UUID_REGEX;

import static com.ezgroceries.shoppinglist.TestUtil.getUUIDFromLocationHeader;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@ActiveProfiles("hsqldb")
class ShoppingListResourceControllerTests {

    @Autowired
    private MockMvc mockMvc;

    private static final UUID ID = UUID.randomUUID();

    private static final String NAME = "Stephanie's birthday";

    private static final ShoppingListResource SHOPPINGLISTRESOURCE = new ShoppingListResource(ID, NAME,List.of());

    @Autowired
    private CocktailService cocktailService;

    @Autowired
    private ShoppingListService shoppingListService;

    @Test
    @Transactional
    void postShoppingList() throws Exception {

        int countBeforeSave = shoppingListService.findAll().size();


        MvcResult mvcResult = mockMvc
                .perform(
                        post("/shopping-lists").contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtil.convertObjectToJsonBytes(SHOPPINGLISTRESOURCE))
                )
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().string("Location",matchesPattern(".*\\/shopping-lists\\/"+UUID_REGEX)))
                .andReturn();



        int countAfterSave = shoppingListService.findAll().size();
        ShoppingListResource shoppingListSaved = shoppingListService.findById(getUUIDFromLocationHeader(mvcResult));

        assertThat(shoppingListSaved.name(),is(NAME));
        assertThat(shoppingListSaved.ingredients(), hasSize(0));



        assertThat(countBeforeSave+1,is(countAfterSave));

    }

    @Test
    @Transactional
    void postShoppingListCocktail() throws Exception {

        int countBeforeSave = shoppingListService.findAll().size();
        CocktailResource cocktailResource = cocktailService.save(COCKTAIL);
        ShoppingListResource resource = shoppingListService.createShoppingList(SHOPPINGLISTRESOURCE);

        MvcResult mvcResult = mockMvc
                .perform(
                        post("/shopping-lists/"+ resource.id() +"/cocktails").contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtil.convertObjectToJsonBytes(cocktailResource))
                )
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().string("Location",matchesPattern(".*\\/shopping-lists\\/"+UUID_REGEX)))
                .andReturn();
        ShoppingListResource shoppingListSaved = shoppingListService.findById(getUUIDFromLocationHeader(mvcResult));

        int countAfterSave = shoppingListService.findAll().size();

        assertThat(shoppingListSaved.ingredients(), containsInAnyOrder(cocktailResource.ingredients().toArray()));
        assertThat(shoppingListSaved.ingredients(), hasSize(cocktailResource.ingredients().size()));
    }

    @Test
    @Transactional
    void getShoppingListCocktail() throws Exception {

        ShoppingListResource resource = shoppingListService.createShoppingList(SHOPPINGLISTRESOURCE);

        mockMvc
                .perform(
                        get("/shopping-lists").contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*]", hasSize(1)))
                .andExpect(jsonPath("$.[0].id",matchesPattern(UUID_REGEX)))
                .andExpect(jsonPath("$.[0].name", is(NAME)))
                .andExpect(jsonPath("$.[0].ingredients", hasSize(0)));
    }
}
