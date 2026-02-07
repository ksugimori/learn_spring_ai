package com.example.todo.domain.service

import com.example.todo.domain.model.Todo
import com.example.todo.domain.model.User
import com.example.todo.domain.repository.TodoRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
@Transactional(readOnly = true)
class TodoService(
    private val todoRepository: TodoRepository,
) {
    fun findById(id: Long): Todo? {
        return todoRepository.findById(id)
    }

    fun findByUser(user: User): List<Todo> {
        return todoRepository.findByUser(user)
    }

    fun findByUserAndCompleted(
        user: User,
        completed: Boolean,
    ): List<Todo> {
        return todoRepository.findByUserAndCompleted(user, completed)
    }

    fun searchByKeyword(
        user: User,
        keyword: String,
    ): List<Todo> {
        return todoRepository.findByUserAndTitleContaining(user, keyword)
    }

    fun findOverdueTodos(user: User): List<Todo> {
        return todoRepository.findOverdueTodos(user, LocalDate.now())
    }

    fun findTodosByDueDate(
        user: User,
        dueDate: LocalDate,
    ): List<Todo> {
        return todoRepository.findTodosByDueDate(user, dueDate)
    }

    @Transactional
    fun createTodo(
        user: User,
        title: String,
        dueDate: LocalDate?,
    ): Todo {
        val todo = Todo(
            title = title,
            dueDate = dueDate,
            user = user,
        )
        return todoRepository.save(todo)
    }

    @Transactional
    fun updateTodo(
        id: Long,
        user: User,
        title: String,
        dueDate: LocalDate?,
    ): Todo {
        val todo = findById(id)
            ?: throw IllegalArgumentException("Todo not found: $id")

        if (todo.user.id != user.id) {
            throw IllegalArgumentException("Not authorized to update this todo")
        }

        todo.title = title
        todo.dueDate = dueDate

        return todoRepository.save(todo)
    }

    @Transactional
    fun toggleTodo(
        id: Long,
        user: User,
    ): Todo {
        val todo = findById(id)
            ?: throw IllegalArgumentException("Todo not found: $id")

        if (todo.user.id != user.id) {
            throw IllegalArgumentException("Not authorized to update this todo")
        }

        todo.toggle()
        return todoRepository.save(todo)
    }

    @Transactional
    fun deleteTodo(
        id: Long,
        user: User,
    ) {
        val todo = findById(id)
            ?: throw IllegalArgumentException("Todo not found: $id")

        if (todo.user.id != user.id) {
            throw IllegalArgumentException("Not authorized to delete this todo")
        }

        todoRepository.deleteById(id)
    }

    fun findAll(): List<Todo> {
        return todoRepository.findAll()
    }

    fun findWithFiltersAndSort(
        user: User,
        filter: TodoFilter = TodoFilter(),
        sort: TodoSort = TodoSort(),
    ): List<Todo> {
        val todos = todoRepository.findByUser(user)
        val filteredTodos = applyFilters(todos, filter)
        return applySort(filteredTodos, sort)
    }

    fun findWithFilters(
        filter: TodoFilter,
    ): List<Todo> {
        val todos = todoRepository.findAll()
        return applyFilters(todos, filter)
    }

    private fun applyFilters(
        todos: List<Todo>,
        filter: TodoFilter,
    ): List<Todo> {
        var result = todos

        filter.completed?.let { completed ->
            result = result.filter { it.completed == completed }
        }

        filter.dueDateFrom?.let { from ->
            result = result.filter { todo ->
                todo.dueDate?.let { !it.isBefore(from) } ?: false
            }
        }

        filter.dueDateTo?.let { to ->
            result = result.filter { todo ->
                todo.dueDate?.let { !it.isAfter(to) } ?: false
            }
        }

        filter.keyword?.let { keyword ->
            result = result.filter { it.title.contains(keyword, ignoreCase = true) }
        }

        filter.hasNoDueDate?.let { hasNoDueDate ->
            result = if (hasNoDueDate) {
                result.filter { it.dueDate == null }
            } else {
                result.filter { it.dueDate != null }
            }
        }

        return result
    }

    private fun applySort(
        todos: List<Todo>,
        sort: TodoSort,
    ): List<Todo> {
        return when (sort.field) {
            TodoSortField.TITLE -> {
                if (sort.direction == SortDirection.ASC) {
                    todos.sortedBy { it.title }
                } else {
                    todos.sortedByDescending { it.title }
                }
            }
            TodoSortField.DUE_DATE -> {
                if (sort.direction == SortDirection.ASC) {
                    todos.sortedWith(compareBy(nullsLast()) { it.dueDate })
                } else {
                    todos.sortedWith(compareByDescending(nullsLast()) { it.dueDate })
                }
            }
            TodoSortField.CREATED_AT -> {
                if (sort.direction == SortDirection.ASC) {
                    todos.sortedBy { it.createdAt }
                } else {
                    todos.sortedByDescending { it.createdAt }
                }
            }
            TodoSortField.UPDATED_AT -> {
                if (sort.direction == SortDirection.ASC) {
                    todos.sortedBy { it.updatedAt }
                } else {
                    todos.sortedByDescending { it.updatedAt }
                }
            }
            TodoSortField.COMPLETED -> {
                if (sort.direction == SortDirection.ASC) {
                    todos.sortedBy { it.completed }
                } else {
                    todos.sortedByDescending { it.completed }
                }
            }
        }
    }
}
