package electro.repository;

import electro.model.Portfolio;
import electro.model.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PortfolioRepository extends JpaRepository<Portfolio,Integer> {
    Portfolio findByUserId( int userId);


    Portfolio findByUser(User existingReferralUser);
}
