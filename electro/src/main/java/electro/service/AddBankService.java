package electro.service;

import electro.exception.UserAlreadyExistsException;
import electro.model.AddBank;
import electro.repository.AddBankRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AddBankService {
    @Autowired
    AddBankRepository addBankRepository;


    public AddBank addBank(AddBank addBank) {
        // Check if user already present in the database
        String userId = addBank.getUserId();
        Optional<AddBank> existingUser = addBankRepository.findByUserId(userId);

        if (existingUser.isPresent()) {
            // User already present, handle accordingly (throw exception or return a message)
            throw new UserAlreadyExistsException("User already present!");
        }

        // User not present, save to the database
        return addBankRepository.save(addBank);
    }


    public Optional<AddBank> get(String userId) {
        return addBankRepository.findByUserId(userId);
    }
}
