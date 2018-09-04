package net.skhu.mentoring.controller;

import net.skhu.mentoring.exception.CustomException;
import net.skhu.mentoring.service.interfaces.CommonService;
import net.skhu.mentoring.vo.PrincipalVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/AccountAPI/common")
public class CommonRestController {
    @Autowired
    private CommonService commonService;

    @GetMapping("denied")
    public ResponseEntity<String> denied() {
        return new ResponseEntity<String>("Authorization Roles Denied 403 Error", HttpStatus.FORBIDDEN);
    }

    @GetMapping("principal")
    public ResponseEntity<?> fetchCurrentPrincipal(HttpServletRequest request, Principal principal) {
        try {
            return new ResponseEntity<PrincipalVO>(commonService.fetchCurrentPrincipal(principal, request), HttpStatus.OK);
        } catch (CustomException e) {
            return new ResponseEntity(e.getMessage(), e.getHttpStatus());
        }
    }

    @DeleteMapping("logout")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return new ResponseEntity<Void>(HttpStatus.OK);
    }
}
