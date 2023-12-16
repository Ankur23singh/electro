package electro.controller;

import electro.model.Portfolio;
import electro.model.User;
import electro.repository.PortfolioRepository;
import electro.repository.UserRepository;
import electro.service.PortfolioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/portfolio")
public class PortfolioController {

    @Autowired
    private PortfolioService portfolioService;

    @Autowired
    private PortfolioRepository  portfolioRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/{userId}")
    public ResponseEntity<Portfolio> getPortfolioByUserId(@PathVariable int userId) {
        Portfolio portfolio = portfolioService.getPortfolioByUserId(userId);
        if (portfolio == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(portfolio, HttpStatus.OK);
    }

//    @PostMapping("/create")
//    public Portfolio createPortfolio(@RequestBody Portfolio portfolio) {
//        // Save the Portfolio data to the database
//        return portfolioRepository.save(portfolio);
//    }

    @PostMapping("/create")
    public Portfolio createPortfolio(@RequestBody Portfolio request) {
        User user = new User();
        user.setPhoneNumber(request.getUser().getPhoneNumber());
        user.setPassword(request.getUser().getPassword());
        user = userRepository.save(user);

        Portfolio portfolio = new Portfolio();
        portfolio.setUser(user);
        portfolio.setTotalAssets(request.getTotalAssets());
        portfolio.setTotalIncome(request.getTeamIncome());
        portfolio.setTodaysIncome(request.getTodaysIncome());
        portfolio.setTotalIncome(request.getTotalIncome());
        portfolio.setCurrentBalance(request.getCurrentBalance());

        return portfolioRepository.save(portfolio);
    }

//    @PatchMapping ("/{id}")
//    public ResponseEntity<Portfolio> updatePortfolio(@PathVariable int id, @RequestBody Portfolio updatedPortfolio) {
//        Portfolio updated = portfolioService.updatePortfolio(id, updatedPortfolio);
//        if (updated == null) {
//            return ResponseEntity.notFound().build();
//        }
//        return ResponseEntity.ok(updated);
//    }
@PatchMapping("/{id}")
public ResponseEntity<Portfolio> updatePortfolio(@PathVariable int id, @RequestBody Map<String, Object> updates) {
    Portfolio updated = portfolioService.partialUpdatePortfolio(id, updates);
    if (updated == null) {
        return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(updated);
}

    public Portfolio partialUpdatePortfolio(int id, Map<String, Object> updates) {
        Portfolio existingPortfolio = portfolioRepository.findById(id).orElse(null);

        if (existingPortfolio == null) {
            return null;
        }

        // Update only the specified fields
        if (updates.containsKey("totalAssets")) {
            existingPortfolio.setTotalAssets((BigDecimal) updates.get("totalAssets"));
        }

        if (updates.containsKey("teamIncome")) {
            existingPortfolio.setTeamIncome((BigDecimal) updates.get("teamIncome"));
        }

        if (updates.containsKey("todaysIncome")) {
            existingPortfolio.setTodaysIncome((BigDecimal) updates.get("todaysIncome"));
        }

        if (updates.containsKey("totalIncome")) {
            existingPortfolio.setTotalIncome((BigDecimal) updates.get("totalIncome"));
        }

        if (updates.containsKey("currentBalance")) {
            existingPortfolio.setCurrentBalance((BigDecimal) updates.get("currentBalance"));
        }

        if (updates.containsKey("referralStatus")) {
            existingPortfolio.setReferralStatus((String) updates.get("referralStatus"));
        }

        if (updates.containsKey("bonus")) {
            existingPortfolio.setBonus((int) updates.get("bonus"));
        }

        // Save the updated portfolio
        return portfolioRepository.save(existingPortfolio);
    }

}
