//package com.service;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//import com.dao.FakeAreaDAO;
//import com.dao.FakeCategoryDAO;
//import com.dao.FakeRecipeDAO;
//import com.dao.FakeUserDAO;
//import com.request.RecipeRequest;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.sql.Date;
//import java.sql.SQLException;
//
//public class RecipeServiceTest {
//
//    private RecipeService recipeService;
//    private FakeRecipeDAO fakeRecipeDAO;
//
//    @BeforeEach
//    public void setUp() {
//        fakeRecipeDAO = new FakeRecipeDAO();
//        recipeService = new RecipeService(
//                fakeRecipeDAO,
//                new FakeCategoryDAO(true),
//                new FakeAreaDAO(true),
//                new FakeUserDAO(true)
//        );
//    }
//
//    @Test
//    public void testAddRecipeSuccess() {
//        RecipeRequest request = createValidRecipeRequest();
//        assertDoesNotThrow(() -> recipeService.addRecipe(request));
//        assertNotNull(fakeRecipeDAO.lastAddedRecipe);
//        System.out.println("Recipe added: " + fakeRecipeDAO.lastAddedRecipe);
//        assertEquals("Delicious Meal", fakeRecipeDAO.lastAddedRecipe.getName());
//    }
//
//    @Test
//    public void testInvalidCategory() {
//        recipeService = new RecipeService(
//                fakeRecipeDAO,
//                new FakeCategoryDAO(false),
//                new FakeAreaDAO(true),
//                new FakeUserDAO(true)
//        );
//
//        RecipeRequest request = createValidRecipeRequest();
//        Exception ex = assertThrows(SQLException.class, () -> recipeService.addRecipe(request));
//
//        System.out.println(ex.getMessage());
//
//        assertEquals("Category does not exist", ex.getMessage());
//    }
//
//    @Test
//    public void testInvalidArea() {
//        recipeService = new RecipeService(
//                fakeRecipeDAO,
//                new FakeCategoryDAO(true),
//                new FakeAreaDAO(false),
//                new FakeUserDAO(true)
//        );
//
//        RecipeRequest request = createValidRecipeRequest();
//        Exception ex = assertThrows(SQLException.class, () -> recipeService.addRecipe(request));
//
//        System.out.println(ex.getMessage());
//
//        assertEquals("Area does not exist", ex.getMessage());
//    }
//
//    @Test
//    public void testInvalidAccessType() {
//        RecipeRequest request = createValidRecipeRequest();
//        request.setAccessType("PREMIUM"); // Sai accessType
//
//        Exception ex = assertThrows(Exception.class, () -> recipeService.addRecipe(request));
//        System.out.println(ex.getMessage());
//        assertEquals("Invalid access type", ex.getMessage());
//    }
//
//    @Test
//    public void testInvalidRecipedBy() {
//        recipeService = new RecipeService(
//                fakeRecipeDAO,
//                new FakeCategoryDAO(true),
//                new FakeAreaDAO(true),
//                new FakeUserDAO(false)
//        );
//
//        RecipeRequest request = createValidRecipeRequest();
//        Exception ex = assertThrows(SQLException.class, () -> recipeService.addRecipe(request));
//        System.out.println(ex.getMessage());
//        assertEquals("User does not exist", ex.getMessage());
//    }
//
//    // Tạo request hợp lệ để dùng cho các test
//    private RecipeRequest createValidRecipeRequest() {
//        RecipeRequest req = new RecipeRequest();
//        req.setName("Delicious Meal");
//        req.setCategory("Beef");
//        req.setArea("American");
//        req.setInstructions("Some cooking instructions...");
//        req.setImage("meal.jpg");
//        req.setIngredients("ingredient1, ingredient2");
//        req.setPublishedOn(Date.valueOf("2025-05-01"));
//        req.setRecipedBy(1);
//        req.setPrepareTime("15 minutes");
//        req.setCookingTime("30 minutes");
//        req.setYield("4 servings");
//        req.setShortDescription("A very tasty meal.");
//        req.setAccessType("FREE");
//        return req;
//    }
//
//}
