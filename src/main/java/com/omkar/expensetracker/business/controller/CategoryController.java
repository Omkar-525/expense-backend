package com.omkar.expensetracker.business.controller;


import com.omkar.expensetracker.business.service.CategoryService;
import com.omkar.expensetracker.infra.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public List<Category> getCategory(){return categoryService.getAllCategory();
    }

}
