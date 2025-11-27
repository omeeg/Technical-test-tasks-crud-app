package com.example.dts.data.repositories;

import com.example.dts.data.entities.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//Repository to call on TaskEntities from DB
@Repository
public interface TaskEntityRepository extends JpaRepository<TaskEntity, Long> {

}
