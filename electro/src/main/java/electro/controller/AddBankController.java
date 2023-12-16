package electro.controller;

import electro.exception.UserAlreadyExistsException;
import electro.model.AddBank;
import electro.service.AddBankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin("*")
@RequestMapping("/bank")

public class AddBankController {
    @Autowired
    AddBankService addBankService;


//    @PostMapping("/addBankDetails/{userId}")
//    public AddBank addBankDetails(@PathVariable String userId, @RequestBody AddBank addBank) {
//        addBank.setUserId(userId);
//        return addBankService.addBank(addBank);
//
//    }


    @GetMapping("/get/{userId}")
    public Optional<AddBank> get(@PathVariable String userId){
        return addBankService.get(userId);
    }





@PostMapping("/addBankDetails/{userId}")
public ResponseEntity<Object> addBankDetails(@PathVariable String userId, @RequestBody AddBank addBank) {
    try {
        addBank.setUserId(userId);
        addBankService.addBank(addBank);
        return new ResponseEntity<>("Bank details added successfully!", HttpStatus.CREATED);
    } catch (UserAlreadyExistsException e) {
        // Handle user already exists exception
        return new ResponseEntity<>("Bank Details Already saved!!", HttpStatus.BAD_REQUEST);
    } catch (Exception e) {
        // Handle other exceptions
        return new ResponseEntity<>("An error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}




}
