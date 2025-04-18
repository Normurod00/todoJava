package com.pulatov.todoapp.controllers;

import com.pulatov.todoapp.model.TodoItem;
import com.pulatov.todoapp.repositories.TodoItemRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller //<- называется анотацией
public class TodoController implements CommandLineRunner {

    private final TodoItemRepository todoItemRepository;

    public TodoController(TodoItemRepository todoItemRepository) {
        this.todoItemRepository = todoItemRepository;
    }

    @GetMapping
    public String index(Model model) {

        List<TodoItem> allToodos = todoItemRepository.findAll();
        model.addAttribute("allToodos", allToodos);
        model.addAttribute("newTodo", new TodoItem());
        return "index";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute TodoItem todoItem) {
        todoItemRepository.save(todoItem);

        return "redirect:/";
    }

    @PostMapping("/delete/{id}")
    public String removeAllItems(@PathVariable("id") Long id) {
        todoItemRepository.deleteById(id);

        return "redirect:/";
    }

    @PostMapping("/removeAll")
    public String deleteTodoItem() {
        todoItemRepository.deleteAll();
        return "redirect:/";
    }

    @PostMapping("/search")
    public String searchTodoItems(@RequestParam("searchTerm") String searchTerm, Model model) {
        List<TodoItem> allItems = todoItemRepository.findAll();
        List<TodoItem> searchResults = new ArrayList<>();
        for (TodoItem item : allItems) {
            if (item.getTitle().toLowerCase().contains(searchTerm.toLowerCase())) {
                searchResults.add(item);
            }
        }

        model.addAttribute("allToodos", searchResults);
        model.addAttribute("newTodo", new TodoItem());
        model.addAttribute("searchTerm", searchTerm);

        return "index";
    }


    @Override //метод ран запусткается после того как программа запустилась
    public void run(String... args) throws Exception {
        todoItemRepository.save(new TodoItem("Item1"));
        todoItemRepository.save(new TodoItem("Item2"));
        todoItemRepository.save(new TodoItem("Item 3"));
    }
}
