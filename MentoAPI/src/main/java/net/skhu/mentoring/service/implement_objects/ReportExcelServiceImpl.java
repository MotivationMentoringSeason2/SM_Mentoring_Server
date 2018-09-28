package net.skhu.mentoring.service.implement_objects;

import net.skhu.mentoring.domain.ClassPhoto;
import net.skhu.mentoring.domain.Report;
import net.skhu.mentoring.domain.Schedule;
import net.skhu.mentoring.domain.Team;
import net.skhu.mentoring.enumeration.ResultStatus;
import net.skhu.mentoring.repository.ClassPhotoRepository;
import net.skhu.mentoring.repository.ReportRepository;
import net.skhu.mentoring.repository.TeamRepository;
import net.skhu.mentoring.service.interfaces.ReportExcelService;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReportExcelServiceImpl implements ReportExcelService {
    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private ClassPhotoRepository classPhotoRepository;

    private static void createCell(Row row, int column, String valueString, XSSFCellStyle cellStyle, short height) {
        Cell cell = row.createCell(column);
        cell.setCellValue(valueString);
        cell.setCellStyle(cellStyle);
        if(height>0) {
            row.setHeight(height);
        }
    }

    @Override
    public XSSFWorkbook fetchReportMultiWorkbook(final Long teamId, final String realName) throws IOException {
        Optional<Team> team = teamRepository.findById(teamId);
        if(team.isPresent()) {
            List<Report> reports = reportRepository.findByScheduleStatusAndScheduleTeam(ResultStatus.PERMIT, team.get());
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat timeFormat = new SimpleDateFormat("a hh:mm");

            XSSFColor titleColor = new XSSFColor(new Color(152, 251, 152));
            XSSFColor myColor = new XSSFColor(new Color(135, 206, 250));
            XSSFColor borderColor = new XSSFColor(Color.BLACK);
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFCellStyle titleStyle = workbook.createCellStyle();
            XSSFFont font = workbook.createFont();
            font.setFontName("맑은 고딕");
            font.setFontHeightInPoints((short) 20);
            titleStyle.setFont(font);
            titleStyle.setAlignment(HorizontalAlignment.CENTER);
            titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            titleStyle.setTopBorderColor(borderColor);
            titleStyle.setBorderTop(BorderStyle.THIN);
            titleStyle.setBottomBorderColor(borderColor);
            titleStyle.setBorderBottom(BorderStyle.THIN);
            titleStyle.setLeftBorderColor(borderColor);
            titleStyle.setBorderLeft(BorderStyle.THIN);
            titleStyle.setRightBorderColor(borderColor);
            titleStyle.setBorderRight(BorderStyle.THIN);
            titleStyle.setFillForegroundColor(titleColor);
            titleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            XSSFCellStyle entityStyle = workbook.createCellStyle();
            XSSFFont entityFont = workbook.createFont();
            entityFont.setFontName("맑은 고딕");
            entityFont.setFontHeightInPoints((short) 11);
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

            XSSFCellStyle attributeStyle = workbook.createCellStyle();
            XSSFFont attributeFont = workbook.createFont();
            attributeFont.setFontName("맑은 고딕");
            attributeFont.setFontHeightInPoints((short) 10);
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
            attributeStyle.setWrapText(true);

            for (int k=0;k<reports.size();k++) {
                Report report = reports.get(k);
                Team tmpTeam = team.get();
                Schedule schedule = report.getSchedule();
                Optional<ClassPhoto> classPhoto = classPhotoRepository.findByReport(reports.get(k));
                LocalDateTime presentDate = report.getPresentDate();
                XSSFSheet sheet = workbook.createSheet(String.format("%d%02d%02d_보고서", presentDate.getMonthValue(), presentDate.getDayOfMonth()));

                if(classPhoto.isPresent()) {
                    Row row = sheet.createRow(0);
                    createCell(row, 0, "멘토링 보고서", titleStyle, (short) 480);
                    createCell(row, 1, "", titleStyle, (short) 0);
                    createCell(row, 2, "", titleStyle, (short) 0);
                    createCell(row, 3, "", titleStyle, (short) 0);
                    workbook.getSheetAt(k).addMergedRegion(new CellRangeAddress(0, 0, 0, 3));

                    row = sheet.createRow(1);
                    createCell(row, 0, "멘토 학번", entityStyle, (short) 360);
                    createCell(row, 1, tmpTeam.getMento(), attributeStyle, (short) 0);
                    createCell(row, 2, "멘토 이름", entityStyle, (short) 0);
                    createCell(row, 3, realName, attributeStyle, (short) 0);

                    row = sheet.createRow(2);
                    createCell(row, 0, "팀 이름", entityStyle, (short) 360);
                    createCell(row, 1, tmpTeam.getName(), attributeStyle, (short) 0);
                    createCell(row, 2, "수업 방식", entityStyle, (short) 0);
                    createCell(row, 3, schedule.getMethod(), attributeStyle, (short) 0);

                    row = sheet.createRow(3);
                    createCell(row, 0, "제출 일자", entityStyle, (short) 360);
                    createCell(row, 1, dateFormat.format(report.getPresentDate()), attributeStyle, (short) 0);
                    createCell(row, 2, "진행 장소", entityStyle, (short) 0);
                    createCell(row, 3, report.getClassPlace(), attributeStyle, (short) 0);

                    row = sheet.createRow(4);
                    createCell(row, 0, "진행 일자", entityStyle, (short) 360);
                    createCell(row, 1, schedule.getStartDate().toString(), attributeStyle, (short) 0);
                    createCell(row, 2, "진행 시간", entityStyle, (short) 0);
                    createCell(row, 3, String.format("%s ~ %s", timeFormat.format(schedule.getStartDate()), timeFormat.format(schedule.getEndDate())), attributeStyle, (short) 0);

                    row = sheet.createRow(5);
                    createCell(row, 0, "결석 인원", entityStyle, (short) 360);
                    createCell(row, 1, report.getAbsentPerson(), attributeStyle, (short) 0);
                    createCell(row, 2, "", attributeStyle, (short) 0);
                    createCell(row, 3, "", attributeStyle, (short) 0);
                    workbook.getSheetAt(k).addMergedRegion(new CellRangeAddress(5, 5, 1, 3));

                    row = sheet.createRow(6);
                    createCell(row, 0, "멘토링 주제 목록", entityStyle, (short) 360);
                    createCell(row, 1, "", entityStyle, (short) 0);
                    createCell(row, 2, "", entityStyle, (short) 0);
                    createCell(row, 3, "", entityStyle, (short) 0);
                    workbook.getSheetAt(k).addMergedRegion(new CellRangeAddress(6, 6, 0, 3));

                    row = sheet.createRow(7);
                    createCell(row, 0,
                            tmpTeam.getSubjects().stream()
                                    .map(subject -> subject.getName())
                                    .collect(Collectors.joining(", "))
                            , attributeStyle, (short) 720);
                    createCell(row, 1, "", attributeStyle, (short) 0);
                    createCell(row, 2, "", attributeStyle, (short) 0);
                    createCell(row, 3, "", attributeStyle, (short) 0);
                    workbook.getSheetAt(k).addMergedRegion(new CellRangeAddress(7, 7, 0, 3));

                    row = sheet.createRow(8);
                    createCell(row, 0, "수업 주제", entityStyle, (short) 360);
                    createCell(row, 1, report.getClassSubject(), attributeStyle, (short) 0);
                    createCell(row, 2, "", attributeStyle, (short) 0);
                    createCell(row, 3, "", attributeStyle, (short) 0);
                    workbook.getSheetAt(k).addMergedRegion(new CellRangeAddress(8, 8, 1, 3));

                    row = sheet.createRow(9);
                    createCell(row, 0, "수업 진행 내용 요약문", entityStyle, (short) 360);
                    createCell(row, 1, "", entityStyle, (short) 0);
                    createCell(row, 2, "", entityStyle, (short) 0);
                    createCell(row, 3, "", entityStyle, (short) 0);
                    workbook.getSheetAt(k).addMergedRegion(new CellRangeAddress(9, 9, 0, 3));

                    row = sheet.createRow(10);
                    createCell(row, 0, report.getClassBriefing(), attributeStyle, (short) 2160);
                    createCell(row, 1, "", attributeStyle, (short) 0);
                    createCell(row, 2, "", attributeStyle, (short) 0);
                    createCell(row, 3, "", attributeStyle, (short) 0);
                    workbook.getSheetAt(k).addMergedRegion(new CellRangeAddress(10, 10, 0, 3));

                    row = sheet.createRow(11);
                    createCell(row, 0, "멘토링 인증 사진", entityStyle, (short) 360);
                    createCell(row, 1, "", entityStyle, (short) 0);
                    createCell(row, 2, "", entityStyle, (short) 0);
                    createCell(row, 3, "", entityStyle, (short) 0);
                    workbook.getSheetAt(k).addMergedRegion(new CellRangeAddress(11, 11, 0, 3));

                    ClassPhoto tmpClassPhoto = classPhoto.get();
                    InputStream inputImage = new ByteArrayInputStream(tmpClassPhoto.getFileData());
                    BufferedImage uploadImage = ImageIO.read(inputImage);
                    int infix = tmpClassPhoto.getFileName().lastIndexOf('.');
                    String ext = tmpClassPhoto.getFileName().substring(infix + 1);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    if (!ext.equals("png")) {
                        ImageIO.write(uploadImage, "png", baos);
                    } else {
                        baos.write(tmpClassPhoto.getFileData());
                    }
                    byte[] bytes = baos.toByteArray();
                    int pictureIdx = workbook.addPicture(bytes, Workbook.PICTURE_TYPE_PNG);
                    inputImage.close();
                    double imageHeight = 756 * uploadImage.getHeight() / uploadImage.getWidth() / 29;

                    for (short a = 12; a < 12 + (short) imageHeight; a++) {
                        row = sheet.createRow(a);
                        createCell(row, 0, "", attributeStyle, (short) 354);
                        createCell(row, 1, "", attributeStyle, (short) 0);
                        createCell(row, 2, "", attributeStyle, (short) 0);
                        createCell(row, 3, "", attributeStyle, (short) 0);
                    }
                    CreationHelper helper = workbook.getCreationHelper();
                    XSSFDrawing drawing = sheet.createDrawingPatriarch();
                    ClientAnchor anchor = helper.createClientAnchor();
                    anchor.setRow1(12);
                    anchor.setCol1(0);
                    anchor.setRow2(12 + (short) imageHeight);
                    anchor.setCol2(4);
                    final Picture pict = drawing.createPicture(anchor, pictureIdx);
                    workbook.getSheetAt(k).addMergedRegion(new CellRangeAddress(12, 12 + (short) imageHeight - 1, 0, 3));
                    pict.resize(1);
                }
            }
            return workbook;
        } else return null;
    }
}
