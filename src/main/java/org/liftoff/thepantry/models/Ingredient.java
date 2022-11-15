package org.liftoff.thepantry.models;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotBlank;

@Entity
public class Ingredient extends AbstractEntity {

    @NotBlank(message = "Ingredient name is required")
    private String name;

    @OneToOne
    @JoinColumn(name = "id")
    private RecipeIngredient recipeIngredient;

    public Ingredient() {
    }


    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
        // getters and setters
    }
    public RecipeIngredient getRecipeIngredient() {
        return recipeIngredient;
    }

    public void setRecipeIngredient(RecipeIngredient recipeIngredient) {
        this.recipeIngredient = recipeIngredient;
    }
        @Override
        public String toString() {
            return name;
        }

}
