package electro.controller;

import electro.model.AddBank;
import electro.model.ApiCallRecord;
import electro.model.BonusTransaction;
import electro.model.User;
import electro.model.userDto.UserDto;
import electro.service.AddBankService;
import electro.service.LoginResponse;
import electro.service.UserPortfolioResponse;
import electro.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping("/user")
@CrossOrigin("*")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    AddBankService addBankService;


    // Add User
    @PostMapping("/add")
    public ResponseEntity<String> addSingleUser(@RequestBody UserDto user, @RequestParam(required = false) String inviteCode) {
        UserDto addedUser = userService.addSingleUser(user);
        if (addedUser == null) {
            return new ResponseEntity<>("User with the same phone number already exists", HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>("User added successfully", HttpStatus.CREATED);
        }
    }
    @PostMapping("/login")
    public LoginResponse loginUser(@RequestBody UserDto userDto , @RequestParam(required = false) String inviteCode) {
        return userService.loginUser(userDto);
        //


    }
    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/getBonus/{userId}")
    public ResponseEntity<String> getBonus(@PathVariable String userId) {
        ResponseEntity<String> bonusResponse = userService.getBonus(userId);
        String bonus = bonusResponse.getBody();
        return new ResponseEntity<>(bonus, HttpStatus.OK);
    }

//    @GetMapping("/bonusRecord/{userId}")
//    public ResponseEntity<ApiCallRecord> bonusRecord(@PathVariable String userId) {
//        Optional<ApiCallRecord> apiCallRecordOptional = userService.bonusRecord(userId);
//
//        if (apiCallRecordOptional.isPresent()) {
//            ApiCallRecord apiCallRecord = apiCallRecordOptional.get();
//            return new ResponseEntity<>(apiCallRecord, HttpStatus.OK);
//        } else {
//            // If the record is not present, you may want to return a different HTTP status code, such as NOT_FOUND
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//    }

    @GetMapping("/bonusRecord/{userId}")
    public ResponseEntity<?> bonusRecord(@PathVariable String userId,
                                         @RequestHeader(name = "TimeZone", defaultValue = "Asia/Kolkata") String clientTimeZone) {
        List<ApiCallRecord> apiCallRecords = userService.bonusRecord(userId);

        if (!apiCallRecords.isEmpty()) {
            // Define a DateTimeFormatter
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd yyyy HH:mm:ss");

            // Get the ZoneId for the client's time zone
            ZoneId clientZoneId = ZoneId.of(clientTimeZone);

            // Create a new list to hold the formatted data
            List<Map<String, Object>> formattedBonusRecords = new ArrayList<>();

            for (ApiCallRecord apiCallRecord : apiCallRecords) {
                Map<String, Object> formattedBonusRecord = new HashMap<>();
                formattedBonusRecord.put("id", apiCallRecord.getId());
                formattedBonusRecord.put("userId", apiCallRecord.getUserId());
                formattedBonusRecord.put("bonusClaimed", apiCallRecord.isBonusClaimed());

                // Convert LocalDateTime to ZonedDateTime and adjust for client's time zone (IST)
                ZonedDateTime bonusClaimTimeIST = apiCallRecord.getBonusClaimTime().atZone(ZoneOffset.UTC).withZoneSameInstant(clientZoneId);
                formattedBonusRecord.put("bonusClaimTime", bonusClaimTimeIST.format(formatter));

                formattedBonusRecord.put("bonus", apiCallRecord.getBonus());
                formattedBonusRecords.add(formattedBonusRecord);
            }

            // Create a response map to structure the response
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("data", formattedBonusRecords);

            return new ResponseEntity<>(responseMap, HttpStatus.OK);
        } else {
            // If the records are not present, return a response indicating that the user ID is not found
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "User ID not found: " + userId);

            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
    }



    @GetMapping("/totalAmount/{userId}")
    public Integer getTotalAmountByUserId(@PathVariable String userId) {
        return userService.getTotalAmountByUserId(userId);
    }



    @PutMapping("/updatePassword/{phoneNumber}")
    public ResponseEntity<String> updatePassword(
            @PathVariable String phoneNumber,
            @RequestBody Map<String, String> request) {

        if (!request.containsKey("password")) {
            return ResponseEntity.badRequest().body("Password not provided in the request body");
        }

        String newPassword = request.get("password");

        String result = userService.updatePassword(phoneNumber, newPassword);
        return ResponseEntity.ok(result);
    }













}

