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
    private val todoRepository: TodoRepository
) {
    fun findById(id: Long): Todo? {
        return todoRepository.findById(id)
    }

    fun findByUser(user: User): List<Todo> {
        return todoRepository.findByUser(user)
    }

    fun findByUserAndCompleted(user: User, completed: Boolean): List<Todo> {
        return todoRepository.findByUserAndCompleted(user, completed)
    }

    fun searchByKeyword(user: User, keyword: String): List<Todo> {
        return todoRepository.findByUserAndTitleContaining(user, keyword)
    }

    fun findOverdueTodos(user: User): List<Todo> {
        return todoRepository.findOverdueTodos(user, LocalDate.now())
    }

    fun findTodosByDueDate(user: User, dueDate: LocalDate): List<Todo> {
        return todoRepository.findTodosByDueDate(user, dueDate)
    }

    @Transactional
    fun createTodo(user: User, title: String, dueDate: LocalDate?): Todo {
        val todo = Todo(
            title = title,
            dueDate = dueDate,
            user = user
        )
        return todoRepository.save(todo)
    }

    @Transactional
    fun updateTodo(id: Long, user: User, title: String, dueDate: LocalDate?): Todo {
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
    fun toggleTodo(id: Long, user: User): Todo {
        val todo = findById(id)
            ?: throw IllegalArgumentException("Todo not found: $id")

        if (todo.user.id != user.id) {
            throw IllegalArgumentException("Not authorized to update this todo")
        }

        todo.toggle()
        return todoRepository.save(todo)
    }

    @Transactional
    fun deleteTodo(id: Long, user: User) {
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
        sort: TodoSort = TodoSort()
    ): List<Todo> {
        // まずユーザーの全Todoを取得
        var todos = todoRepository.findByUser(user)

        // フィルタリング
        filter.completed?.let { completed ->
            todos = todos.filter { it.completed == completed }
        }

        filter.dueDateFrom?.let { from ->
            todos = todos.filter { todo ->
                val dueDate = todo.dueDate
                dueDate != null && !dueDate.isBefore(from)
            }
        }

        filter.dueDateTo?.let { to ->
            todos = todos.filter { todo ->
                val dueDate = todo.dueDate
                dueDate != null && !dueDate.isAfter(to)
            }
        }

        filter.keyword?.let { keyword ->
            todos = todos.filter { it.title.contains(keyword, ignoreCase = true) }
        }

        filter.hasNoDueDate?.let { hasNoDueDate ->
            if (hasNoDueDate) {
                todos = todos.filter { it.dueDate == null }
            } else {
                todos = todos.filter { it.dueDate != null }
            }
        }

        // ソート
        todos = when (sort.field) {
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

        return todos
    }
}
