package net.skhu.mentoring.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = {"http://localhost:3000","http://182.209.240.203:81"})
@RequestMapping("/AccountAPI/user")
public class UserRestController {
}
