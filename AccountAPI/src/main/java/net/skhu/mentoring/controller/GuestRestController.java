package net.skhu.mentoring.controller;

import net.skhu.mentoring.exception.CustomException;
import net.skhu.mentoring.model.LoginModel;
import net.skhu.mentoring.service.interfaces.TokenLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/AccountAPI/guest")
public class GuestRestController {
    @Autowired
    private TokenLoginService tokenLoginService;

    @PostMapping("login")
    public ResponseEntity<String> tokenLogin(@RequestBody LoginModel loginModel){
        try {
            return ResponseEntity.ok(tokenLoginService.tokenLogin(loginModel.getIdentity(), loginModel.getPassword()));
        } catch(CustomException e){
            return new ResponseEntity(e.getMessage(), e.getHttpStatus());
        }
    }
}
