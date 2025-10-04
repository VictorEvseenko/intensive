package ru.aston.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.aston.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>{
}
