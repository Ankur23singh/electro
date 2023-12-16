package electro.service;

import electro.model.ApiCallRecord;
import electro.model.BonusTransaction;
import electro.model.Portfolio;
import electro.model.User;
import electro.model.userDto.UserDto;
import electro.repository.ApiCallRecordRepository;
import electro.repository.BonusTransactionRepository;
import electro.repository.PortfolioRepository;
import electro.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Builder
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PortfolioRepository portfolioRepository;

    @Autowired
    BonusTransactionRepository bonusTransactionRepository;

    @Autowired
    ApiCallRecordRepository apiCallRecordRepository;

    private static LocalDate lastClaimDate = null;
    private static boolean bonusClaimed = false;

    public UserDto addSingleUser(UserDto userDto) {
        Optional<User> existingUser = userRepository.findByPhoneNumber(userDto.getPhoneNumber());
        User inviteCode = null;

        if (userDto.getInviteCode() != null) {
            inviteCode = userRepository.findByRandom5DiditNumber(Integer.parseInt(userDto.getInviteCode()));
        }

        if (existingUser.isPresent()) {
            return null;
        } else {
            User user = dtoToEntity(userDto);
            User savedUser = userRepository.save(user);

            if (inviteCode != null) {
                int userId = inviteCode.getId();
                List<User> referralUsers = userRepository.findAllById(Collections.singleton(userId));

                for (User existingReferralUser : referralUsers) {
                    Portfolio existingReferralPortfolio = portfolioRepository.findByUser(existingReferralUser);
                    if (existingReferralPortfolio != null) {
                        String existingReferralStatus = existingReferralPortfolio.getReferralStatus();
                        if ("No Referral Found".equals(existingReferralStatus)) {
                            existingReferralPortfolio.setReferralStatus(userDto.getPhoneNumber());
                        } else {
                            existingReferralPortfolio.setReferralStatus(existingReferralStatus + "," + userDto.getPhoneNumber());
                        }
                        portfolioRepository.save(existingReferralPortfolio);
                    }
                }
            }

            Portfolio portfolio = new Portfolio();
            portfolio.setUser(savedUser);
            initializePortfolio(portfolio);
            portfolioRepository.save(portfolio);

            return entityToDto(savedUser);
        }
    }

    private void initializePortfolio(Portfolio portfolio) {
        portfolio.setTotalAssets(BigDecimal.ZERO);
        portfolio.setTeamIncome(BigDecimal.ZERO);
        portfolio.setTodaysIncome(BigDecimal.ZERO);
        portfolio.setTotalIncome(BigDecimal.ZERO);
        portfolio.setCurrentBalance(BigDecimal.ZERO);
        portfolio.setReferralStatus("No Referral Found");
        portfolio.setBonus(0);
    }

    private User dtoToEntity(UserDto userDto) {
        return User.builder()
                .phoneNumber(userDto.getPhoneNumber())
                .password(userDto.getPassword())
                .inviteCode(userDto.getInviteCode())
                .build();
    }

    private UserDto entityToDto(User savedUser) {
        return UserDto.builder()
                .phoneNumber(savedUser.getPhoneNumber())
                .password(savedUser.getPassword())
                .inviteCode(savedUser.getInviteCode())
                .build();
    }

    public LoginResponse loginUser(UserDto userDto) {
        Optional<User> existingUser = userRepository.findByPhoneNumber(userDto.getPhoneNumber());

        if (existingUser.isPresent() && existingUser.get().getPassword().equals(userDto.getPassword())) {
            int userId = existingUser.get().getId();
            Portfolio portfolio = portfolioRepository.findByUserId(userId);

            if (portfolio != null) {
                return new LoginResponse("User is present, login successful", userId);
            } else {
                return null;
            }
        } else if (existingUser.isPresent()) {
            return new LoginResponse("Incorrect password", null);
        } else {
            return new LoginResponse("Create an account", null);
        }
    }


    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public ResponseEntity<String> getBonus(String userId) {
        LocalDate today = LocalDate.now();
        LocalDateTime currentDateTime = LocalDateTime.now();

        // Get the last bonus claim time for the user
        Optional<ApiCallRecord> lastApiCallRecordOptional = apiCallRecordRepository.findFirstByUserIdOrderByIdDesc(userId);

        if (lastApiCallRecordOptional.isPresent()) {
            ApiCallRecord lastApiCallRecord = lastApiCallRecordOptional.get();

            // Check if the last claim was on the same day
            if (lastApiCallRecord.getBonusClaimTime().toLocalDate().equals(today)) {

                Optional<BonusTransaction> existingTransactionOptional = bonusTransactionRepository.findFirstByUserIdOrderByIdDesc(userId);

                if (existingTransactionOptional.isPresent()) {
                    BonusTransaction existingTransaction = existingTransactionOptional.get();

                    return new ResponseEntity<>("You have already collected the bonus today. " +
                            "Total amount = " + existingTransaction.getAmount() + " rupees.", HttpStatus.OK);
                }
            }
        }

        // Create and save ApiCallRecord for the user
        ApiCallRecord apiCallRecord = new ApiCallRecord();
        apiCallRecord.setUserId(userId);
        apiCallRecord.setBonusClaimed(true);
        apiCallRecord.setBonusClaimTime(currentDateTime);
        apiCallRecord.setBonus(2); // Set bonus amount
        apiCallRecordRepository.save(apiCallRecord);

        // Existing code for BonusTransaction
        Optional<BonusTransaction> existingTransactionOptional = bonusTransactionRepository.findFirstByUserIdOrderByIdDesc(userId);

        if (existingTransactionOptional.isPresent()) {
            BonusTransaction existingTransaction = existingTransactionOptional.get();
            existingTransaction.setAmount(existingTransaction.getAmount() + 2);
            bonusTransactionRepository.save(existingTransaction);

            return new ResponseEntity<>("You have received a bonus of 2 rupees. Total bonus: " +
                    existingTransaction.getAmount() + " rupees.", HttpStatus.OK);
        } else {
            BonusTransaction bonusTransaction = new BonusTransaction();
            bonusTransaction.setTransactionTime(currentDateTime);
            bonusTransaction.setAmount(2);
            bonusTransaction.setUserId(userId);
            bonusTransactionRepository.save(bonusTransaction);

            return new ResponseEntity<>("You have received a bonus of 2 rupees. Total bonus: 2 rupees.", HttpStatus.OK);
        }
    }

    public boolean isUserIdPresent(String userId) {
        return apiCallRecordRepository.existsByUserId(userId);
    }


//    @Transactional
//    public Optional<ApiCallRecord> bonusRecord(String userId) {
//        return apiCallRecordRepository.findByUserId(userId)
//                .findFirst();
//    }

    @Transactional
    public List<ApiCallRecord> bonusRecord(String userId) {
        return apiCallRecordRepository.findByUserId(userId);
    }


    public Integer getTotalAmountByUserId(String userId) {
        return bonusTransactionRepository.getTotalAmountByUserId(userId);
    }

    public String updatePassword(String phoneNumber, String newPassword) {
        // Find the user by phone number
        Optional<User> userOptional = userRepository.findByPhoneNumber(phoneNumber);

        // Check if the user exists
        if (userOptional.isEmpty()) {
            return "User not found";
        }

        // Update the password
        User user = userOptional.get();
        user.setPassword(newPassword);

        // Save the updated user
        userRepository.save(user);

        return "Password updated successfully";
    }




}



