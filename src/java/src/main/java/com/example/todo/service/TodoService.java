package com.example.todo.service;

import com.example.todo.model.Todo;
import com.example.todo.repository.TodoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TodoService {

    private final TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public List<Todo> findAll() {
        return todoRepository.findAllByOrderByCreatedAtDesc();
    }

    public List<Todo> findByCompleted(boolean completed) {
        return todoRepository.findByCompletedOrderByCreatedAtDesc(completed);
    }

    public Optional<Todo> findById(Long id) {
        return todoRepository.findById(id);
    }

    public Todo save(Todo todo) {
        return todoRepository.save(todo);
    }

    public Todo update(Long id, Todo updated) {
        Todo existing = todoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Todo not found: " + id));
        existing.setTitle(updated.getTitle());
        existing.setDescription(updated.getDescription());
        existing.setCompleted(updated.isCompleted());
        return todoRepository.save(existing);
    }

    public void toggleCompleted(Long id) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Todo not found: " + id));
        todo.setCompleted(!todo.isCompleted());
        todoRepository.save(todo);
    }

    public void deleteById(Long id) {
        if (todoRepository.existsById(id)) {
            todoRepository.deleteById(id);
        }
    }
}
