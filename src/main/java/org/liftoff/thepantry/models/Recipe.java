package org.liftoff.thepantry.models;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
public class Recipe extends AbstractEntity {

    @NotBlank(message = "Recipe Name is required")
    @Size(max = 150)
    private String name;

    @Size(max = 1000)
    private String description;

    @Size(max = 3000)
    private String instructions;

    private String image;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;


    @OneToOne
    @JoinColumn(name = "id")
    private RecipeIngredient recipeIngredient;

    public Recipe() {
    }

    public Recipe(String name, String description, String instructions, String image, User user) {
        super();
        this.name = name;
        this.description = description;
        this.instructions = instructions;
        this.image = image;
        this.user = user;
    }

    // getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        if (image != null && image.isEmpty()) {
            image = null;
        }
        this.image = image;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}