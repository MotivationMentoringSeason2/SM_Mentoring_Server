package net.skhu.mentoring.service.implement_objects;

import net.skhu.mentoring.component.JwtTokenProvider;
import net.skhu.mentoring.domain.Account;
import net.skhu.mentoring.domain.Department;
import net.skhu.mentoring.domain.Student;
import net.skhu.mentoring.enumeration.Gender;
import net.skhu.mentoring.enumeration.StudentStatus;
import net.skhu.mentoring.enumeration.UserType;
import net.skhu.mentoring.repository.AccountRepository;
import net.skhu.mentoring.repository.DepartmentRepository;
import net.skhu.mentoring.repository.StudentRepository;
import net.skhu.mentoring.service.interfaces.ExcelUploadService;
import net.skhu.mentoring.util.Encryption;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.extractor.XSSFExcelExtractor;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
public class ExcelUploadServiceImpl implements ExcelUploadService {
    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private boolean tokenValidation(final Principal principal, final HttpServletRequest request) {
        String jwtToken;
        String tokenLoginId;
        boolean hasAbleAccess;
        Account account = accountRepository.findByIdentity(principal.getName()).get();
        if(account.getType().equals(UserType.EMPLOYEE) || account.getType().equals(UserType.PROFESSOR)) {
            hasAbleAccess = true;

        } else {
            Student student = studentRepository.findByIdentity(principal.getName()).get();
            hasAbleAccess = student.getHasChairman();
        }
        if (principal != null && hasAbleAccess) {
            jwtToken = jwtTokenProvider.resolveToken(request);
            tokenLoginId = jwtTokenProvider.getUsername(jwtToken);
        } else {
            return false;
        }
        return principal.getName().equals(tokenLoginId);
    }

    @Override
    public XSSFWorkbook fetchSampleExcelFile() {
        XSSFColor myColor=new XSSFColor(new Color(135,206,250));
        XSSFColor borderColor=new XSSFColor(Color.BLACK);
        XSSFWorkbook workbook=new XSSFWorkbook();

        XSSFCellStyle entityStyle=workbook.createCellStyle();
        XSSFFont entityFont=workbook.createFont();
        entityFont.setFontName("맑은 고딕");
        entityFont.setFontHeightInPoints((short)11);
        entityStyle.setFont(entityFont);
        entityStyle.setAlignment(HorizontalAlignment.CENTER);
        entityStyle.setTopBorderColor(borderColor);
        entityStyle.setBorderTop(BorderStyle.THIN);
        entityStyle.setBottomBorderColor(borderColor);
        entityStyle.setBorderBottom(BorderStyle.THIN);
        entityStyle.setLeftBorderColor(borderColor);
        entityStyle.setBorderLeft(BorderStyle.THIN);
        entityStyle.setRightBorderColor(borderColor);
        entityStyle.setBorderRight(BorderStyle.THIN);
        entityStyle.setFillForegroundColor(myColor);
        entityStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        XSSFCellStyle attributeStyle=workbook.createCellStyle();
        XSSFFont attributeFont=workbook.createFont();
        attributeFont.setFontName("맑은 고딕");
        attributeFont.setFontHeightInPoints((short)10);
        attributeStyle.setFont(attributeFont);
        attributeStyle.setAlignment(HorizontalAlignment.CENTER);
        attributeStyle.setVerticalAlignment(VerticalAlignment.TOP);
        attributeStyle.setTopBorderColor(borderColor);
        attributeStyle.setBorderTop(BorderStyle.THIN);
        attributeStyle.setBottomBorderColor(borderColor);
        attributeStyle.setBorderBottom(BorderStyle.THIN);
        attributeStyle.setLeftBorderColor(borderColor);
        attributeStyle.setBorderLeft(BorderStyle.THIN);
        attributeStyle.setRightBorderColor(borderColor);
        attributeStyle.setBorderRight(BorderStyle.THIN);

        XSSFSheet sheet1=workbook.createSheet("학생_등록_목록");
        XSSFSheet sheet2=workbook.createSheet("학과_번호_일람");

        workbook.getSheet("학생_등록_목록").setColumnWidth(0, 256*12);
        workbook.getSheet("학생_등록_목록").setColumnWidth(1, 256*8);
        workbook.getSheet("학생_등록_목록").setColumnWidth(2, 256*8);
        workbook.getSheet("학생_등록_목록").setColumnWidth(3, 256*8);
        workbook.getSheet("학생_등록_목록").setColumnWidth(4, 256*16);
        workbook.getSheet("학생_등록_목록").setColumnWidth(5, 256*40);
        workbook.getSheet("학생_등록_목록").setColumnWidth(6, 256*40);

        workbook.getSheet("학과_번호_일람").setColumnWidth(0, 256*15);
        workbook.getSheet("학과_번호_일람").setColumnWidth(1, 256*60);

        Row row = sheet1.createRow(0);

        Cell cell = row.createCell(0);
        cell.setCellStyle(entityStyle);
        cell.setCellValue("학번");

        cell = row.createCell(1);
        cell.setCellStyle(entityStyle);
        cell.setCellValue("이름");

        cell = row.createCell(2);
        cell.setCellStyle(entityStyle);
        cell.setCellValue("성별");

        cell = row.createCell(3);
        cell.setCellStyle(entityStyle);
        cell.setCellValue("학년");

        cell = row.createCell(4);
        cell.setCellStyle(entityStyle);
        cell.setCellValue("휴대폰 번호");

        cell = row.createCell(5);
        cell.setCellStyle(entityStyle);
        cell.setCellValue("E-Mail");

        cell = row.createCell(6);
        cell.setCellStyle(entityStyle);
        cell.setCellValue("학과 이름");

        for(int k=1;k<=10;k++) {
            row=sheet1.createRow(k);
            cell = row.createCell(0);
            cell.setCellStyle(attributeStyle);
            cell = row.createCell(1);
            cell.setCellStyle(attributeStyle);
            cell = row.createCell(2);
            cell.setCellStyle(attributeStyle);
            cell = row.createCell(3);
            cell.setCellStyle(attributeStyle);
            cell = row.createCell(4);
            cell.setCellStyle(attributeStyle);
            cell = row.createCell(5);
            cell.setCellStyle(attributeStyle);
            cell = row.createCell(6);
            cell.setCellStyle(attributeStyle);
        }

        row = sheet2.createRow(0);

        cell = row.createCell(0);
        cell.setCellStyle(entityStyle);
        cell.setCellValue("순서");

        cell = row.createCell(1);
        cell.setCellStyle(entityStyle);
        cell.setCellValue("학과이름");

        List<Department> departmentList = departmentRepository.findAll();
        for(int k=1;k<1+departmentList.size();k++) {
            row = sheet2.createRow(k);
            cell = row.createCell(0);
            cell.setCellStyle(attributeStyle);
            cell.setCellValue(k);

            cell = row.createCell(1);
            cell.setCellStyle(attributeStyle);
            cell.setCellValue(departmentList.get(k-1).getName());
        }

        return workbook;
    }

    @Override
    @Transactional
    public ResponseEntity<String> executeExcelUploading(final Principal principal, final HttpServletRequest request, final InputStream is) throws EncryptedDocumentException, IOException {
        if(!this.tokenValidation(principal, request)) return new ResponseEntity<>("파일 업로딩 권한이 없습니다. 다시 시도 바랍니다.", HttpStatus.NON_AUTHORITATIVE_INFORMATION);

        XSSFWorkbook workbook=new XSSFWorkbook(is);

        XSSFExcelExtractor extractor=new XSSFExcelExtractor(workbook);
        extractor.setFormulasNotResults(false);
        extractor.setIncludeSheetNames(false);

        Sheet sheet=workbook.getSheetAt(0);
        int rowCount=sheet.getPhysicalNumberOfRows();

        for(int k=1;k<rowCount;k++) {
            Row row=sheet.getRow(k);
            if(row.getCell(0)==null) break;

            Student student=new Student();
            student.setStatus(StudentStatus.NORMAL);
            student.setHasChairman(false);
            student.setType(UserType.STUDENT);

            // 엑셀에서 학번 입력하면 20+1E89000 이런 식으로 나오기 때문에 숫자로 변환할 수 밖에 없습니다...
            Cell cell = row.getCell(0);
            BigDecimal identity = new BigDecimal(cell.getNumericCellValue());

            Optional<Student> tmpStudent = studentRepository.findByIdentity(identity.toString());
            if(tmpStudent.isPresent())
                return new ResponseEntity<String>("회원 데이터베이스에 이미 존재하는 학생 이름이 Excel 파일에 있습니다. 다시 시도 바랍니다.", HttpStatus.NON_AUTHORITATIVE_INFORMATION);
            else {
                student.setIdentity(identity.toString());
            }

            cell=row.getCell(1);
            student.setName(cell.getRichStringCellValue().toString());

            cell=row.getCell(2);
            student.setGender(cell.getRichStringCellValue().toString().contains("남") ? Gender.MALE : Gender.FEMALE);

            cell=row.getCell(3);
            student.setGrade((int) cell.getNumericCellValue());

            cell=row.getCell(4);
            student.setName(cell.getRichStringCellValue().toString());
            student.setPassword(Encryption.encrypt(String.format("a%s", cell.getRichStringCellValue().toString().substring(9, 13)), Encryption.MD5));

            cell=row.getCell(5);
            student.setEmail(cell.getRichStringCellValue().toString());

            cell=row.getCell(6);
            Optional<Department> department = departmentRepository.findByName(cell.getRichStringCellValue().toString());
            if(department.isPresent()){
                student.setDepartment(department.get());
            } else {
                return new ResponseEntity<String>("실존하지 않는 학과 이름을 입력하셨습니다. 다시 시도 바랍니다.", HttpStatus.NON_AUTHORITATIVE_INFORMATION);
            }

            if(accountRepository.findByNameAndEmail(student.getName(), student.getEmail()).isPresent())
                return new ResponseEntity<String>(String.format("다음과 같은 데이터를 가진 회원이 존재합니다. 다시 시도 바랍니다. 이름 : %s - 이메일 : %s", student.getName(), student.getEmail()), HttpStatus.NON_AUTHORITATIVE_INFORMATION);
            Student std = studentRepository.save(student);
            if(std.getId() == null)
                return new ResponseEntity<String>("서버 측 데이터베이스 오류입니다. 다시 시도 바랍니다.", HttpStatus.NON_AUTHORITATIVE_INFORMATION);
        }
        return ResponseEntity.ok("Excel 파일를 이용한 학생 업로딩 작업이 완료 되었습니다. 회원 목록으로 이동합니다.");
    }
}
