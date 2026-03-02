package com.example.todo;

import com.example.todo.model.Todo;
import com.example.todo.repository.TodoRepository;
import com.example.todo.service.TodoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class TodoApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TodoService todoService;

    @Autowired
    private TodoRepository todoRepository;

    @BeforeEach
    void setUp() {
        todoRepository.deleteAll();
    }

    @Test
    void contextLoads() {
    }

    @Test
    void indexPageLoads() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("todos", "newTodo", "filter"));
    }

    @Test
    void addTodoSuccessfully() throws Exception {
        mockMvc.perform(post("/add")
                        .param("title", "Test Todo")
                        .param("description", "Test Description"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        assertThat(todoRepository.count()).isEqualTo(1);
        assertThat(todoRepository.findAll().get(0).getTitle()).isEqualTo("Test Todo");
    }

    @Test
    void addTodoWithBlankTitleFails() throws Exception {
        mockMvc.perform(post("/add")
                        .param("title", "")
                        .param("description", "Some description"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));

        assertThat(todoRepository.count()).isZero();
    }

    @Test
    void toggleTodoCompleted() throws Exception {
        Todo todo = todoService.save(new Todo("Toggle me", ""));
        assertThat(todo.isCompleted()).isFalse();

        mockMvc.perform(post("/toggle/" + todo.getId()))
                .andExpect(status().is3xxRedirection());

        Todo updated = todoRepository.findById(todo.getId()).orElseThrow();
        assertThat(updated.isCompleted()).isTrue();
    }

    @Test
    void deleteTodo() throws Exception {
        Todo todo = todoService.save(new Todo("Delete me", ""));

        mockMvc.perform(post("/delete/" + todo.getId()))
                .andExpect(status().is3xxRedirection());

        assertThat(todoRepository.existsById(todo.getId())).isFalse();
    }

    @Test
    void editPageLoads() throws Exception {
        Todo todo = todoService.save(new Todo("Edit me", "Edit description"));

        mockMvc.perform(get("/edit/" + todo.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("edit"))
                .andExpect(model().attributeExists("todo"));
    }

    @Test
    void updateTodoSuccessfully() throws Exception {
        Todo todo = todoService.save(new Todo("Old Title", "Old Desc"));

        mockMvc.perform(post("/edit/" + todo.getId())
                        .param("title", "New Title")
                        .param("description", "New Desc")
                        .param("completed", "true"))
                .andExpect(status().is3xxRedirection());

        Todo updated = todoRepository.findById(todo.getId()).orElseThrow();
        assertThat(updated.getTitle()).isEqualTo("New Title");
        assertThat(updated.isCompleted()).isTrue();
    }

    @Test
    void filterByActiveReturnsOnlyPending() throws Exception {
        todoService.save(new Todo("Active Todo", ""));
        Todo completed = todoService.save(new Todo("Completed Todo", ""));
        todoService.toggleCompleted(completed.getId());

        mockMvc.perform(get("/?filter=active"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("filter", "active"));

        assertThat(todoService.findByCompleted(false)).hasSize(1);
        assertThat(todoService.findByCompleted(true)).hasSize(1);
    }
}
