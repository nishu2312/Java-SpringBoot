package com.taskmanager.repo;

import com.taskmanager.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query("SELECT t FROM Task t WHERE t.status = :status")
    Page<Task> findByStatus(@Param("status") String status, Pageable pageable);

    @Query("SELECT t FROM Task t WHERE t.assignedTo.id = :userId")
    Page<Task> findByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT t FROM Task t WHERE t.status = :status AND t.assignedTo.id = :userId")
    Page<Task> findByStatusAndUserId(@Param("status") String status, @Param("userId") Long userId, Pageable pageable);
}
