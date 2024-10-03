package ru.luckyskeet.sharehub.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.luckyskeet.sharehub.user.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
