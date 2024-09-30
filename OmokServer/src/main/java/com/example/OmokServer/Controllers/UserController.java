package com.example.OmokServer.Controllers;

import com.example.OmokServer.DTO.LoginAttemptResponse;
import com.example.OmokServer.DTO.LoginRequest;
import com.example.OmokServer.DTO.LoginResponse;
import com.example.OmokServer.Exceptions.LoginFailException;
import com.example.OmokServer.OmokUser.OmokUser;
import com.example.OmokServer.OmokUser.UserService;
import com.example.OmokServer.Secure.AccessToken;
import com.example.OmokServer.Secure.EncryptUtil;
import com.example.OmokServer.Secure.RefreshToken;
import com.example.OmokServer.Secure.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.crypto.SecretKey;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    //private UserDataService userDataService; 아직 구현되지 않음. 냅둬

    private Map<Long, AvailKey> loginList;

    private static class AvailKey{
        SecretKey key;
        long expiredTime;

        public AvailKey(SecretKey key){
            this.key = key;
            expiredTime = System.currentTimeMillis() + 10000;
        }

        public boolean isAvail(){
            return System.currentTimeMillis() < expiredTime;
        }
    }

    public ResponseEntity<String> register(@RequestParam String username, @RequestParam String password){
        OmokUser user = userService.createUser(username, password);
        return ResponseEntity.ok(String.valueOf(user.getId()));
    }

    @PostMapping("/login-attempt")
    public ResponseEntity<LoginAttemptResponse> loginAttempt(@RequestParam String username){
        OmokUser user = userService.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Invalid username"));
        SecretKey secretKey = EncryptUtil.generateSecretKey();
        AvailKey key = new AvailKey(secretKey);
        loginList.put(user.getId(), key);

        LoginAttemptResponse response = new LoginAttemptResponse();
        response.setKeyData(EncryptUtil.secretKeyToString(secretKey));
        response.setUserId(user.getId());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request){
        Long userId = request.getUserId();
        AvailKey key = loginList.get(userId);
        if(key == null || !key.isAvail())
            throw new LoginFailException("not available key");
        String password = EncryptUtil.decrypt(request.getPassword(), key.key);
        userService.authenticateUser(request.getUsername(), password);
        LoginResponse response = new LoginResponse();
        response.setMessage("Login Success");
        response.setAccessToken(TokenUtil.createAccessToken(userId));
        response.setRefreshToken(TokenUtil.createRefreshToken(userId, request.getDeviceInfo()));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh-access-token")
    public ResponseEntity<AccessToken> refreshAccessToken(@RequestBody RefreshToken token){
        if(token != null){
            AccessToken accessToken = TokenUtil.refreshAccessToken(token);
            if(accessToken != null)
                return ResponseEntity.ok(accessToken);
        }
        return null;
    }
}
