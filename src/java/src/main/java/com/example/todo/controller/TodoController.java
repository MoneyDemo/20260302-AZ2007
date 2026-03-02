package com.example.todo.controller;

import com.example.todo.model.Todo;
import com.example.todo.service.TodoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/")
public class TodoController {

    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping
    public String index(@RequestParam(value = "filter", defaultValue = "all") String filter,
                        Model model) {
        switch (filter) {
            case "active":
                model.addAttribute("todos", todoService.findByCompleted(false));
                break;
            case "completed":
                model.addAttribute("todos", todoService.findByCompleted(true));
                break;
            default:
                model.addAttribute("todos", todoService.findAll());
                break;
        }
        model.addAttribute("filter", filter);
        model.addAttribute("newTodo", new Todo());
        return "index";
    }

    @PostMapping("/add")
    public String addTodo(@Valid @ModelAttribute("newTodo") Todo todo,
                          BindingResult result,
                          RedirectAttributes redirectAttributes,
                          Model model) {
        if (result.hasErrors()) {
            model.addAttribute("todos", todoService.findAll());
            model.addAttribute("filter", "all");
            return "index";
        }
        todoService.save(todo);
        redirectAttributes.addFlashAttribute("successMessage", "Todo added successfully!");
        return "redirect:/";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Todo todo = todoService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid todo id: " + id));
        model.addAttribute("todo", todo);
        return "edit";
    }

    @PostMapping("/edit/{id}")
    public String updateTodo(@PathVariable Long id,
                             @Valid @ModelAttribute("todo") Todo todo,
                             BindingResult result,
                             RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "edit";
        }
        todoService.update(id, todo);
        redirectAttributes.addFlashAttribute("successMessage", "Todo updated successfully!");
        return "redirect:/";
    }

    @PostMapping("/toggle/{id}")
    public String toggleTodo(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        todoService.toggleCompleted(id);
        redirectAttributes.addFlashAttribute("successMessage", "Todo status updated!");
        return "redirect:/";
    }

    @PostMapping("/delete/{id}")
    public String deleteTodo(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        todoService.deleteById(id);
        redirectAttributes.addFlashAttribute("successMessage", "Todo deleted successfully!");
        return "redirect:/";
    }
}
