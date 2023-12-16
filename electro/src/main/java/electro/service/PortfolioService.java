package electro.service;

import electro.model.Portfolio;
import electro.repository.PortfolioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

@Service
public class PortfolioService {

    @Autowired
    private PortfolioRepository portfolioRepository;

    public Portfolio getPortfolioByUserId(int userId) {
        return portfolioRepository.findByUserId(userId);
    }


//    public Portfolio updatePortfolio(int id, Portfolio updatedPortfolio) {
//        Portfolio existingPortfolio = portfolioRepository.findById(id).orElse(null);
//
//        if (existingPortfolio == null) {
//            return null;
//        }
//
//        // Update the fields you want to allow being updated
//        existingPortfolio.setTotalAssets(updatedPortfolio.getTotalAssets());
//        existingPortfolio.setTeamIncome(updatedPortfolio.getTeamIncome());
//        existingPortfolio.setTodaysIncome(updatedPortfolio.getTodaysIncome());
//        existingPortfolio.setTotalIncome(updatedPortfolio.getTotalIncome());
//        existingPortfolio.setCurrentBalance(updatedPortfolio.getCurrentBalance());
//        existingPortfolio.setReferralStatus(updatedPortfolio.getReferralStatus());
//        existingPortfolio.setBonus(updatedPortfolio.getBonus());
//
//        // Save the updated portfolio
//        return portfolioRepository.save(existingPortfolio);
//    }

//    public Portfolio partialUpdatePortfolio(int id, Map<String, Object> updates) {
//        Portfolio existingPortfolio = portfolioRepository.findById(id).orElse(null);
//
//        if (existingPortfolio == null) {
//            return null;
//        }
//
//        // Update only the specified fields
//        if (updates.containsKey("totalAssets")) {
//            existingPortfolio.setTotalAssets((BigDecimal) updates.get("totalAssets"));
//        }
//
//        if (updates.containsKey("teamIncome")) {
//            existingPortfolio.setTeamIncome((BigDecimal) updates.get("teamIncome"));
//        }
//
//        if (updates.containsKey("todaysIncome")) {
//            existingPortfolio.setTodaysIncome((BigDecimal) updates.get("todaysIncome"));
//        }
//
//        if (updates.containsKey("totalIncome")) {
//            existingPortfolio.setTotalIncome((BigDecimal) updates.get("totalIncome"));
//        }
//
//        if (updates.containsKey("currentBalance")) {
//            existingPortfolio.setCurrentBalance((BigDecimal) updates.get("currentBalance"));
//        }
//
//        if (updates.containsKey("referralStatus")) {
//            existingPortfolio.setReferralStatus((String) updates.get("referralStatus"));
//        }
//
//        if (updates.containsKey("bonus")) {
//            existingPortfolio.setBonus((int) updates.get("bonus"));
//        }
//
//        // Save the updated portfolio
//        return portfolioRepository.save(existingPortfolio);
//    }

    public Portfolio partialUpdatePortfolio(int id, Map<String, Object> updates) {
        Portfolio existingPortfolio = portfolioRepository.findById(id).orElse(null);

        if (existingPortfolio == null) {
            return null;
        }

        // Update only the specified fields
        if (updates.containsKey("totalAssets")) {
            Object totalAssetsObject = updates.get("totalAssets");
            if (totalAssetsObject instanceof BigDecimal) {
                existingPortfolio.setTotalAssets((BigDecimal) totalAssetsObject);
            } else if (totalAssetsObject instanceof Double) {
                existingPortfolio.setTotalAssets(BigDecimal.valueOf((Double) totalAssetsObject));
            }
        }

        if (updates.containsKey("teamIncome")) {
            Object teamIncomeObject = updates.get("teamIncome");
            if (teamIncomeObject instanceof BigDecimal) {
                existingPortfolio.setTeamIncome((BigDecimal) teamIncomeObject);
            } else if (teamIncomeObject instanceof Double) {
                existingPortfolio.setTeamIncome(BigDecimal.valueOf((Double) teamIncomeObject));
            }
        }

        if (updates.containsKey("todaysIncome")) {
            Object todaysIncomeObject = updates.get("todaysIncome");
            if (todaysIncomeObject instanceof BigDecimal) {
                existingPortfolio.setTodaysIncome((BigDecimal) todaysIncomeObject);
            } else if (todaysIncomeObject instanceof Double) {
                existingPortfolio.setTodaysIncome(BigDecimal.valueOf((Double) todaysIncomeObject));
            }
        }

        if (updates.containsKey("totalIncome")) {
            Object totalIncomeObject = updates.get("totalIncome");
            if (totalIncomeObject instanceof BigDecimal) {
                existingPortfolio.setTotalIncome((BigDecimal) totalIncomeObject);
            } else if (totalIncomeObject instanceof Double) {
                existingPortfolio.setTotalIncome(BigDecimal.valueOf((Double) totalIncomeObject));
            }
        }

        if (updates.containsKey("currentBalance")) {
            Object currentBalanceObject = updates.get("currentBalance");
            if (currentBalanceObject instanceof BigDecimal) {
                existingPortfolio.setCurrentBalance((BigDecimal) currentBalanceObject);
            } else if (currentBalanceObject instanceof Double) {
                existingPortfolio.setCurrentBalance(BigDecimal.valueOf((Double) currentBalanceObject));
            }
        }

        if (updates.containsKey("referralStatus")) {
            Object referralStatusObject = updates.get("referralStatus");
            if (referralStatusObject instanceof String) {
                existingPortfolio.setReferralStatus((String) referralStatusObject);
            }
        }

        if (updates.containsKey("bonus")) {
            Object bonusObject = updates.get("bonus");
            if (bonusObject instanceof Integer) {
                existingPortfolio.setBonus((Integer) bonusObject);
            }
        }

        // Save the updated portfolio
        return portfolioRepository.save(existingPortfolio);
    }



}
