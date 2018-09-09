package net.skhu.mentoring.controller;

import net.skhu.mentoring.model.AccountPagination;
import net.skhu.mentoring.service.interfaces.AdminService;
import net.skhu.mentoring.vo.FindResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/AccountAPI/admin")
public class AdminRestController {
    @Autowired
    private AdminService adminService;

    @GetMapping("account/list")
    public ResponseEntity<?> fetchAccountListWithPagination(Principal principal, HttpServletRequest request, AccountPagination accountPagination){
        return ResponseEntity.ok(FindResultVO.builtToVO(adminService.fetchAccountListWithPagination(principal, request, accountPagination), accountPagination));
    }

    @GetMapping("account/view/{id}")
    public ResponseEntity<?> fetchAccountView(Principal principal, HttpServletRequest request, @PathVariable("id") Long id){
        return adminService.fetchAccountView(principal, request, id);
    }

    @GetMapping("account/options/search_by")
    public ResponseEntity<?> fetchSearchByOptions(Principal principal, HttpServletRequest request){
        return ResponseEntity.ok(adminService.getSearchByModel(principal, request));
    }

    @GetMapping("account/options/order_by")
    public ResponseEntity<?> fetchOrderByOptions(Principal principal, HttpServletRequest request){
        return ResponseEntity.ok(adminService.getOrderByModel(principal, request));
    }
}
