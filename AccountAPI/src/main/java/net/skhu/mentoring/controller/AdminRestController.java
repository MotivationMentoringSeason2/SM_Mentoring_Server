package net.skhu.mentoring.controller;

import net.skhu.mentoring.model.AccountPagination;
import net.skhu.mentoring.service.interfaces.AdminService;
import net.skhu.mentoring.service.interfaces.ExcelUploadService;
import net.skhu.mentoring.vo.FindResultVO;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.security.Principal;

@RestController
@CrossOrigin
@RequestMapping("/AccountAPI/admin")
public class AdminRestController {
    @Autowired
    private AdminService adminService;

    @Autowired
    private ExcelUploadService excelUploadService;

    @GetMapping("account/list")
    public ResponseEntity<?> fetchAccountListWithPagination(Principal principal, HttpServletRequest request, AccountPagination accountPagination){
        return ResponseEntity.ok(FindResultVO.builtToVO(adminService.fetchAccountListWithPagination(principal, request, accountPagination), accountPagination));
    }

    @GetMapping("account/view/{id}")
    public ResponseEntity<?> fetchAccountView(Principal principal, HttpServletRequest request, @PathVariable Long id){
        return adminService.fetchAccountView(principal, request, id);
    }

    @PutMapping("account/chairman/{method}/{id}")
    public ResponseEntity<?> executeSettingChairman(Principal principal, HttpServletRequest request, @PathVariable String method, @PathVariable Long id){
        switch(method){
            case "appoint" :
                return adminService.executeAppointChairman(principal, request, id);
            case "release" :
                return adminService.executeReleaseChairman(principal, request, id);
            default :
                return new ResponseEntity<>("잘못 된 요청입니다. 다시 시도 바랍니다.", HttpStatus.NON_AUTHORITATIVE_INFORMATION);
        }
    }

    @PutMapping("account/mentoring/{user}/{role}")
    public ResponseEntity<?> executeApplicateMentoring(Principal principal, HttpServletRequest request, @PathVariable String user, @PathVariable String role){
        return adminService.executeAppointMentoring(principal, request, user, role);
    }

    @PutMapping("account/student/reset")
    public ResponseEntity<?> executeStudentStatusResetting(Principal principal, HttpServletRequest request){
        return adminService.executeResettingStudentStatus(principal, request);
    }

    @GetMapping("account/options/search_by")
    public ResponseEntity<?> fetchSearchByOptions(Principal principal, HttpServletRequest request){
        return ResponseEntity.ok(adminService.getSearchByModel(principal, request));
    }

    @GetMapping("account/options/order_by")
    public ResponseEntity<?> fetchOrderByOptions(Principal principal, HttpServletRequest request){
        return ResponseEntity.ok(adminService.getOrderByModel(principal, request));
    }

    @PostMapping(value="account/excel/upload", consumes = {"multipart/form-data"})
    public ResponseEntity<?> executeExcelUploadSaving(Principal principal, HttpServletRequest request, @RequestPart("file") MultipartFile multipartFile) throws IOException {
        if(multipartFile.getSize() <= 0)
            return new ResponseEntity<String>("파일 형식이 잘 못 되었습니다.", HttpStatus.NON_AUTHORITATIVE_INFORMATION);
        return excelUploadService.executeExcelUploading(principal, request, multipartFile.getInputStream());
    }

    @GetMapping("account/excel/sample_document")
    public void executeExcelSampleFormDownload(Principal principal, HttpServletRequest request, HttpServletResponse response) throws Exception{
        XSSFWorkbook workbook=excelUploadService.fetchSampleExcelFile();
        String fileName = URLEncoder.encode("신입생_추가_폼.xlsx", "UTF-8");
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename="+fileName+";");
        try (BufferedOutputStream output = new BufferedOutputStream(response.getOutputStream())) {
            workbook.write(output);
        }
    }


}
