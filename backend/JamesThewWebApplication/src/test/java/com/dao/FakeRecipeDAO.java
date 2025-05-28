//package com.dao;
//
//import com.request.RecipeRequest;
//
//import java.sql.SQLException;
//
//public class FakeRecipeDAO extends RecipeDAO {
//    public RecipeRequest lastAddedRecipe = null;
//
//    @Override
//    public void addRecipe(RecipeRequest recipe) throws SQLException {
//        this.lastAddedRecipe = recipe;
//        // Giả lập thêm công thức vào DB
//    }
//}