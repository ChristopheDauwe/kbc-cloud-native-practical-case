package com.ezgroceries.shoppinglist;

import com.ezgroceries.shoppinglist.groceries.cocktail.CocktailResource;
import com.ezgroceries.shoppinglist.groceries.cocktail.domain.Cocktail;
import com.ezgroceries.shoppinglist.groceries.cocktail.services.CocktailService;
import com.ezgroceries.shoppinglist.shoppinglist.ShoppingListMapper;
import com.ezgroceries.shoppinglist.shoppinglist.ShoppingListResource;
import com.ezgroceries.shoppinglist.shoppinglist.domain.ShoppingList;
import com.ezgroceries.shoppinglist.shoppinglist.repo.ShoppingListRepository;
import com.ezgroceries.shoppinglist.shoppinglist.services.ShoppingListService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@ActiveProfiles("hsqldb")
class ShoppingListResourceControllerTests {

    @Autowired
    private MockMvc mockMvc;

    private static final UUID ID = UUID.randomUUID();

    private static final String NAME = "Bert's birthday";

    private static final String USERNAME = "bert";

    @Autowired
    private ShoppingListMapper shoppingListMapper;
    private static final ShoppingListResource SHOPPINGLISTRESOURCE = new ShoppingListResource(ID, NAME,List.of());
    private static final ShoppingList SHOPPINGLIST = new ShoppingList(ID,NAME,USERNAME,Set.of());

    @Autowired
    private CocktailService cocktailService;

    @Autowired
    private ShoppingListService shoppingListService;

    @Autowired
    private ShoppingListRepository shoppingListRepository;



    @Test
    @Transactional
    @WithMockUser(username = "bert",password = "user")
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

        assertThat(countBeforeSave+1,is(countAfterSave));
        Optional<ShoppingList> shoppingListSaved = shoppingListRepository.findById(getUUIDFromLocationHeader(mvcResult));

        assertThat(shoppingListSaved.isPresent(),is(true));
        assertThat(shoppingListSaved.get().getName(),is(NAME));
        assertThat(shoppingListSaved.get().getCocktails(), hasSize(0));





    }

    @Test
    @Transactional
    @WithMockUser(username = "bert",password = "user")
    void postShoppingListCocktail() throws Exception {

        int countBeforeSave = shoppingListService.findAllInDB().size();

        CocktailResource cocktailResource = cocktailService.save(COCKTAIL);
        ShoppingList shoppingListCreated = shoppingListService.createShoppingList(SHOPPINGLIST);

        MvcResult mvcResult = mockMvc
                .perform(
                        post("/shopping-lists/"+ shoppingListCreated.getId() +"/cocktails").contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtil.convertObjectToJsonBytes(cocktailResource))
                )
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().string("Location",matchesPattern(".*\\/shopping-lists\\/"+UUID_REGEX)))
                .andReturn();

        Optional<ShoppingList> shoppingListOptional = shoppingListRepository.findById(getUUIDFromLocationHeader(mvcResult));
        assertTrue(shoppingListOptional.isPresent());

        ShoppingListResource shoppingListSaved = shoppingListMapper.toShoppingListResource(shoppingListOptional.get());

        int countAfterSave = cocktailService.findAllInDB().size();
        assertThat(countBeforeSave+1,is(countAfterSave));

        assertThat(shoppingListSaved.ingredients(), containsInAnyOrder(cocktailResource.ingredients().toArray()));
        assertThat(shoppingListSaved.ingredients(), hasSize(cocktailResource.ingredients().size()));

        Collection<ShoppingList> shoppingLists = shoppingListService.findAllInDB();
        ShoppingList shoppingList = shoppingLists.stream().skip(countBeforeSave).findFirst().get();

        assertThat(shoppingList.getCocktails(), hasSize(1));
        Cocktail cocktail = shoppingList.getCocktails().iterator().next();
        assertThat(cocktail.getId(), is(cocktailResource.id()));

    }


    @Test
    @Transactional
    @WithMockUser(username = "bert",password = "user")
    void getShoppingListCocktail() throws Exception {
        shoppingListService.createShoppingList(SHOPPINGLIST);

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

    @Test
    @Transactional
    void getShoppingListCocktailNoDataFromOtherUser() throws Exception {
        shoppingListService.createShoppingList(SHOPPINGLIST);

        mockMvc
                .perform(
                        get("/shopping-lists").with(httpBasic("jani","user")).contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*]", hasSize(0)));
    }

    @Test
    @Transactional
    void getShoppingListCocktailNoExistingUser() throws Exception {

        shoppingListService.createShoppingList(SHOPPINGLIST);

        mockMvc
                .perform(
                        get("/shopping-lists").with(httpBasic("bompa","user")).contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isUnauthorized());
    }

    @Test
    @Transactional
    void getShoppingListCocktailNoUser() throws Exception {

        shoppingListService.createShoppingList(SHOPPINGLIST);

        mockMvc
                .perform(
                        get("/shopping-lists").contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isUnauthorized());
    }

    ///hiere

    @Test
    @Transactional
    @WithMockUser(username = "bert",password = "user")
    void getShoppingListCocktailByID() throws Exception {
        ShoppingList shoppingList = shoppingListService.createShoppingList(SHOPPINGLIST);

        mockMvc
                .perform(
                        get("/shopping-lists/"+shoppingList.getId()).contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id",matchesPattern(UUID_REGEX)))
                .andExpect(jsonPath("$.name", is(NAME)))
                .andExpect(jsonPath("$.ingredients", hasSize(0)));
    }

    @Test
    @Transactional
    @WithMockUser(username = "bert",password = "user")
    void getShoppingListCocktailByIDNoDataFromOtherUser() throws Exception {
        ShoppingList shoppingList = shoppingListService.createShoppingList(SHOPPINGLIST);

        MvcResult result = mockMvc
                .perform(
                        get("/shopping-lists/"+shoppingList.getId()).with(httpBasic("jani","user")).contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id",matchesPattern(UUID_REGEX) ))
                .andReturn();

        result.getResponse();
    }

    @Test
    @Transactional
    @WithMockUser(username = "bert",password = "user")
    void getShoppingListCocktailByIDNoExistingUser() throws Exception {

        ShoppingList shoppingList = shoppingListService.createShoppingList(SHOPPINGLIST);

        mockMvc
                .perform(
                        get("/shopping-lists/"+shoppingList.getId()).with(httpBasic("bompa","user")).contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isUnauthorized());
    }

    @Test
    @Transactional
    void getShoppingListCocktailByIDNoUser() throws Exception {

        ShoppingList shoppingList = shoppingListService.createShoppingList(SHOPPINGLIST);

        mockMvc
                .perform(
                        get("/shopping-lists/"+shoppingList.getId()).contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isUnauthorized());
    }
}
