package org.carroll.utils;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import jxl.Workbook;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.write.Blank;
import jxl.write.Formula;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import org.carroll.data.school.AllCourses;
import org.carroll.data.settings.Settings;

/**
 * Exporting utility for making progress reports.
 *
 * @author Joel Gallant
 */
public class Exporting {

    /**
     * Exports a progress report to a file.
     *
     * @param file file to export to
     * @throws IOException thrown when problem writing happens
     */
    public static void exportTo(File file) throws IOException {
        WritableWorkbook workbook = null;
        Collections.sort(AllCourses.getInstance().getElements());
        try {
            Files.createNewFileWithParents(file);
            workbook = Workbook.createWorkbook(file);
            WritableSheet sheet = workbook.createSheet("First Sheet", 0);

            WritableCell[][] data = new WritableCell[25][3 + AllCourses.getInstance().getElements().size()];
            WritableCellFormat[][] formats = new WritableCellFormat[25][3 + AllCourses.getInstance().getElements().size()];

            //Labels
            data[0][0] = new Label(0, 0, "Grade " + Settings.getInstance().get("CurrentGrade").getValue());
            data[4][0] = new Label(4, 0, "Year Plan");
            formats[4][0] = new WritableCellFormat(new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD));
            String[] months = {"September", "October", "November", "December", "January", "February", "March", "April", "May", "June"};
            int month = 0;
            for (int x = 7; x < 25; x += 2) {
                data[x][0] = new Label(x, 0, months[month]);
                formats[x][0] = new WritableCellFormat(new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD));
                month++;
            }
            String[] secondRow = {"Course", "Credits", "Semester", "P", "C", "%", "R", "P", "C", "P", "C", "P", "C", "P",
                "C", "P", "C", "P", "C", "P", "C", "P", "C", "P", "C"};
            for (int x = 0; x < secondRow.length; x++) {
                data[x][1] = new Label(x, 1, secondRow[x]);
            }

            //Courses
            for (int x = 0; x < data.length; x++) {
                for (int y = 2; y < data[x].length - 1; y++) {
                    switch (x) {
                        case (0):
                            data[x][y] = new Label(x, y, AllCourses.getInstance().getElements().get(y - 2).getName());
                            break;
                        case (1):
                            data[x][y] = new jxl.write.Number(x, y, AllCourses.getInstance().getElements().get(y - 2).getCredits());
                            break;
                        case (2):
                            data[x][y] = new jxl.write.Number(x, y, AllCourses.getInstance().getElements().get(y - 2).getSemester());
                            break;
                        case (3):
                            data[x][y] = new jxl.write.Number(x, y, AllCourses.getInstance().getElements().get(y - 2).getUnits());
                            break;
                        case (4):
                            data[x][y] = new jxl.write.Number(x, y, AllCourses.getInstance().getElements().get(y - 2).getCompletedUnits());
                            break;
                        case (5):
                            data[x][y] = new Formula(x, y, "E" + (y + 1) + "/D" + (y + 1) + "*100");
                            break;
                        case (6):
                            data[x][y] = new Formula(x, y, "D" + (y + 1) + "-E" + (y + 1));
                    }
                    if (x > 5) {
                        if (x % 2 == 0) {
                            int monthIndex = (x - 8) / 2;
                            data[x][y] = new jxl.write.Number(x, y, Stats.getUnitsDoneInMonth(monthIndex, data[0][y].getContents()));
                        }
                    }
                }
            }

            //Totals
            char[] alphabet = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
            int end = data[0].length - 1;
            for (int x = 1; x < data.length; x++) {
                data[x][end] = new Formula(x, end, "SUM(" + alphabet[x] + 2 + ":" + alphabet[x] + (end) + ")");
            }
            data[0][end] = new Label(0, end, "Totals");
            data[2][end] = new Blank(2, end);
            data[5][end] = new Formula(5, end, "AVERAGE(F3 : F" + (end) + ")");


            //Borders
            for (int y = 0; y < formats[0].length - 1; y++) {
                if (formats[0][y] == null) {
                    formats[0][y] = new WritableCellFormat();
                }
                if (formats[1][y] == null) {
                    formats[1][y] = new WritableCellFormat();
                }
                formats[0][y].setBorder(Border.RIGHT, BorderLineStyle.MEDIUM);
                formats[1][y].setBorder(Border.RIGHT, BorderLineStyle.MEDIUM);
            }
            for (int x = 6; x < formats.length; x += 2) {
                for (int y = 0; y < formats[x].length - 1; y++) {
                    if (formats[x][y] == null) {
                        formats[x][y] = new WritableCellFormat();
                    }
                    formats[x][y].setBorder(Border.RIGHT, BorderLineStyle.MEDIUM);
                }
            }
            for (int x = 0; x < formats.length; x++) {
                if (formats[x][0] == null) {
                    formats[x][0] = new WritableCellFormat();
                }
                if (formats[x][1] == null) {
                    formats[x][1] = new WritableCellFormat();
                }
                formats[x][0].setBorder(Border.BOTTOM, BorderLineStyle.MEDIUM);
                formats[x][1].setBorder(Border.BOTTOM, BorderLineStyle.MEDIUM);
            }
            for (int x = 0; x < formats.length; x++) {
                if (formats[x][formats[x].length - 2] == null) {
                    formats[x][formats[x].length - 2] = new WritableCellFormat();
                }
                formats[x][formats[x].length - 2].setBorder(Border.BOTTOM, BorderLineStyle.MEDIUM);
            }

            //Sizes
            sheet.setColumnView(0, 35);
            for (int x = 3; x < data.length; x++) {
                sheet.setColumnView(x, 5);
            }

            //Adding
            for (int x = 0; x < data.length; x++) {
                for (int y = 0; y < data[x].length; y++) {
                    if (data[x][y] == null) {
                        data[x][y] = new Blank(x, y);
                    }
                    if (formats[x][y] != null) {
                        data[x][y].setCellFormat(formats[x][y]);
                    }
                    sheet.addCell(data[x][y]);
                }
            }

            workbook.write();

        } catch (WriteException | IOException | NoSuchFieldException ex) {
            throw new IOException(ex.getMessage());
        } finally {
            try {
                workbook.close();
            } catch (WriteException ex) {
                throw new IOException(ex.getMessage());
            }
        }
    }
}
