package org.carroll.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import org.carroll.data.school.AllCourses;
import org.carroll.data.school.AllWork;
import org.carroll.data.school.Workload;
import org.carroll.school.Course;
import org.carroll.school.Work;

/**
 * Importing and exporting methods for using {@link JTable} tables.
 *
 * @author Joel Gallant
 */
public class Excel {

    /**
     * Exports a {@link JTable} to an excel file. If the file does not exists,
     * creates one.
     *
     * @param table table to export
     * @param file destination file
     * @throws IOException thrown when export can not be completed (Ex.
     * FileNotFound)
     */
    public static void exportTableTo(JTable table, File file) throws IOException {
        try {
            Logging.log("Exporting table to " + file.getPath());
            Files.createNewFileWithParents(file);
            WritableWorkbook workbook = Workbook.createWorkbook(file);
            WritableSheet sheet = workbook.createSheet("First Sheet", 0);
            TableModel model = table.getModel();

            for (int i = 0; i < model.getRowCount(); i++) {
                for (int j = 0; j < model.getColumnCount(); j++) {
                    Label cell = new Label(j, i, String.valueOf(model.getValueAt(i, j)));
                    sheet.addCell(cell);
                }
            }

            workbook.write();
            workbook.close();

        } catch (WriteException ex) {
            throw new IOException();
        }
    }

    /**
     * Saves all courses to a file. Gets courses from the AllCourses cache.
     *
     * @param file file to save to
     * @throws IOException thrown when export can not be completed (Ex.
     * FileNotFound)
     */
    public static void saveCoursesToFile(File file) throws IOException {
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnCount(3);
        for (int x = 0; x < AllCourses.getInstance().getElements().size(); x++) {
            String[] row = {
                AllCourses.getInstance().getElements().get(x).getName(),
                String.valueOf(AllCourses.getInstance().getElements().get(x).getCredits()),
                String.valueOf(AllCourses.getInstance().getElements().get(x).getSemester())};
            model.addRow(row);
        }
        if (AllCourses.getInstance().getElements().isEmpty()) {
            file.delete();
        } else {
            exportTableTo(new JTable(model), file);
        }
    }

    /**
     * Saves all work to a file. Gets work from the AllWork cache.
     *
     * @param file file to save to
     * @throws IOException thrown when export can not be completed (Ex.
     * FileNotFound)
     */
    public static void saveWorkToFile(File file) throws IOException {
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnCount(7);
        for (int x = 0; x < AllWork.getInstance().getElements().size(); x++) {
            String[] row = {
                AllWork.getInstance().getElements().get(x).getName(),
                String.valueOf(AllWork.getInstance().getElements().get(x).getUnits()),
                AllWork.getInstance().getElements().get(x).getCourseName(),
                String.valueOf(AllWork.getInstance().getElements().get(x).getOrder()),
                String.valueOf(AllWork.getInstance().getElements().get(x).isComplete()),
                String.valueOf(AllWork.getInstance().getElements().get(x).getMonthComplete()),
                String.valueOf(AllWork.getInstance().getElements().get(x).getWeight())
            };
            model.addRow(row);
        }
        if (AllWork.getInstance().getElements().isEmpty()) {
            file.delete();
        } else {
            exportTableTo(new JTable(model), file);
        }
    }

    /**
     * Saves the current workloads to a file. Gets workloads from Workload
     * cache.
     *
     * @param file file to save to
     * @throws IOException thrown when export can not be completed (Ex.
     * FileNotFound)
     */
    public static void saveWorkloadToFile(File file) throws IOException {
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnCount(1);
        for (int x = 0; x < 10; x++) {
            String[] row = {
                String.valueOf(Workload.getWorkload(x))
            };
            model.addRow(row);
        }
        exportTableTo(new JTable(model), file);
    }

    /**
     * Returns the {@link TableModel} representation of an excel file.
     *
     * @param file file to get representation from
     * @return representation of the file
     * @throws IOException thrown when problems occur while converting to
     * {@link TableModel}
     */
    public static TableModel getTableModelFrom(File file) throws IOException {
        try {
            Logging.log("Importing table from " + file.getPath());
            Workbook workbook = Workbook.getWorkbook(file);
            final Sheet sheet = workbook.getSheet(0);
            DefaultTableModel model = new DefaultTableModel(sheet.getRows(), sheet.getColumns());
            for (int y = 0; y < sheet.getRows(); y++) {
                for (int x = 0; x < sheet.getColumns(); x++) {
                    Cell cell = sheet.getCell(x, y);
                    model.setValueAt(cell.getContents(), y, x);
                }
            }
            return model;
        } catch (BiffException | IndexOutOfBoundsException ex) {
            throw new IOException();
        }
    }

    /**
     * Returns the {@link TableModel} representation of an excel file.
     *
     * @param file {@link InputStream} to get representation from
     * @return representation of the file
     * @throws IOException thrown when problems occur while converting to
     * {@link TableModel}
     */
    public static TableModel getTableModelFrom(InputStream file) throws IOException {
        try {
            Logging.log("Importing table from input stream");
            Workbook workbook = Workbook.getWorkbook(file);
            final Sheet sheet = workbook.getSheet(0);
            DefaultTableModel model = new DefaultTableModel(sheet.getRows(), sheet.getColumns());
            for (int y = 0; y < sheet.getRows(); y++) {
                for (int x = 0; x < sheet.getColumns(); x++) {
                    Cell cell = sheet.getCell(x, y);
                    model.setValueAt(cell.getContents(), y, x);
                }
            }
            return model;
        } catch (BiffException | IndexOutOfBoundsException ex) {
            throw new IOException();
        }
    }

    /**
     * Returns an {@link ArrayList} of courses from a particular file.
     *
     * @param file file courses were saved to
     * @return {@link ArrayList} of courses
     * @throws IOException thrown when problems occur while converting to
     * {@link TableModel}
     */
    public static ArrayList<Course> getCoursesFromFile(File file) throws IOException {
        ArrayList<Course> courses = new ArrayList<>();
        final JTable table = new JTable(getTableModelFrom(file));
        for (int x = 0; x < table.getRowCount(); x++) {
            final int row = x;
            try {
                Course newCourse = new Course() {
                    @Override
                    protected String name() {
                        return table.getValueAt(row, 0).toString();
                    }

                    @Override
                    protected int credits() {
                        return Integer.parseInt(table.getValueAt(row, 1).toString());
                    }

                    @Override
                    protected int semester() {
                        return Integer.parseInt(table.getValueAt(row, 2).toString());
                    }
                };
                courses.add(newCourse);
            } catch (Exception ex) {
            }
        }
        return courses;
    }

    /**
     * Returns an {@link ArrayList} of work from a particular file.
     *
     * @param file file work was saved to
     * @return {@link ArrayList} of work
     * @throws IOException thrown when problems occur while converting to
     * {@link TableModel}
     */
    public static ArrayList<Work> getWorkFromFile(File file) throws IOException {
        ArrayList<Work> work = new ArrayList<>();
        final JTable table = new JTable(getTableModelFrom(file));
        for (int x = 0; x < table.getRowCount(); x++) {
            final int row = x;
            try {
                Work newWork = new Work(table.getValueAt(row, 2).toString()) {
                    @Override
                    protected String name() {
                        return table.getValueAt(row, 0).toString();
                    }

                    @Override
                    protected int units() {
                        return Integer.parseInt(table.getValueAt(row, 1).toString());
                    }

                    @Override
                    protected int order() {
                        return Integer.parseInt(table.getValueAt(row, 3).toString());
                    }

                    @Override
                    protected int weight() {
                        return Integer.parseInt(table.getValueAt(row, 6).toString());
                    }
                };
                newWork.setComplete(Boolean.parseBoolean(table.getValueAt(row, 4).toString()), Integer.parseInt(table.getValueAt(row, 5).toString()));
                work.add(newWork);
            } catch (Exception ex) {
            }
        }
        return work;
    }
}
