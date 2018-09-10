package net.skhu.mentoring.service.interfaces;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;

public interface ExcelUploadService {
    public XSSFWorkbook fetchSampleExcelFile();

    @Transactional
    public ResponseEntity<String> executeExcelUploading(final Principal principal, final HttpServletRequest request, final InputStream is) throws EncryptedDocumentException, IOException;
}
