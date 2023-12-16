package electro.repository;

import electro.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {


    Optional<User>findByPhoneNumber(String phoneNumber);
    Optional<User>findByPassword(String password);

//    Optional<User>findByRandom5DiditNumber(String inviteCode);

    User findByRandom5DiditNumber(Integer random5DiditNumber);
//    Optional<User> findByPhoneNumber(String phoneNumber);



}
