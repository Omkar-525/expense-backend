package com.omkar.expensetracker.business.service;

import com.omkar.expensetracker.infra.entity.Category;
import org.springframework.stereotype.Service;

import java.util.List;


public interface CategoryService {

     List<Category> getAllCategory();
}
