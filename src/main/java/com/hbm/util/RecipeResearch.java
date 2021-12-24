package com.hbm.util;

import java.util.HashMap;

import com.hbm.interfaces.IResearchableRecipe;

public class RecipeResearch
{
	public static final HashMap<String, Class<? extends IResearchableRecipe>> recipeClasses = new HashMap<>();
}
