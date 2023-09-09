package com.omkar.expensetracker.business.service.impl;

import com.omkar.expensetracker.business.service.CategoryService;
import com.omkar.expensetracker.infra.entity.Category;
import com.omkar.expensetracker.infra.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<Category> getAllCategory() {
        return categoryRepository.findAll();
    }
}
