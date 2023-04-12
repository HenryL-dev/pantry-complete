package org.liftoff.thepantry.controllers;

import org.liftoff.thepantry.data.RecipeRepository;
import org.liftoff.thepantry.models.Recipe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("admin/mealPlanner")
public class AdminMealPlannerController {

    @Autowired
    private RecipeRepository recipeRepository;

    @GetMapping("")
    public String index(Model model) {
        model.addAttribute("plannedMeals", "mealPlanner" );
        model.addAttribute(new Recipe());
        return "admin/mealPlanner/index";
    }
}
