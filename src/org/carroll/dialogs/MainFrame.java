package org.carroll.dialogs;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.plaf.metal.MetalBorders;
import org.carroll.data.settings.Settings;
import org.carroll.dialogs.adding.AddCourseDialog;
import org.carroll.dialogs.adding.AddWorkDialog;
import org.carroll.dialogs.data.ExportDialog;
import org.carroll.dialogs.data.OpenDialog;
import org.carroll.dialogs.data.RemoveAllDataDialog;
import org.carroll.dialogs.data.SaveAsDialog;
import org.carroll.dialogs.editing.EditCourseDialog;
import org.carroll.dialogs.editing.EditWorkDialog;
import org.carroll.dialogs.editing.SettingsDialog;
import org.carroll.dialogs.info.AboutDialog;
import org.carroll.dialogs.info.HighSchoolRequirementDialog;
import org.carroll.dialogs.info.LogFileDialog;
import org.carroll.dialogs.info.tutorial.Tutorial;
import org.carroll.dialogs.removing.RemoveCourseDialog;
import org.carroll.dialogs.removing.RemoveWorkDialog;
import org.carroll.internal.InternalPanel;
import org.carroll.internal.panels.CoursesPanel;
import org.carroll.internal.panels.WorkPanel;
import org.carroll.main.update.UpdateCheck;
import org.carroll.utils.Loading;
import org.carroll.utils.Logging;
import org.carroll.utils.Saving;
import org.swing.Dialog;
import org.swing.Listeners.ClosingListener;
import org.swing.Listeners.ResizingListener;
import org.swing.dialogs.messages.ErrorMessage;

/**
 * Main frame of the Bishop Carroll School Tracker. Displays the main window.
 *
 * @author Joel Gallant
 */
public class MainFrame extends Dialog {

    static MainFrame instance;
    Image carrollBird,
            titleImage;
    static JButton toDo = new JButton("To Do"),
            courses = new JButton("Courses"),
            work = new JButton("Work"),
            workload = new JButton("Workload");
    static GridBagConstraints buttonConstraints = new GridBagConstraints();
    static JPanel buttons = new JPanel();
    JLabel carrollIcon,
            title;
    GridBagLayout layout = new GridBagLayout();
    GridBagConstraints constraints = new GridBagConstraints();

    static {
        buttonConstraints.gridx = 0;
        buttonConstraints.gridy = 1;
        buttonConstraints.gridheight = 4;
        buttonConstraints.gridwidth = 1;
        buttonConstraints.fill = GridBagConstraints.BOTH;
        buttonConstraints.weightx = 1;
        buttonConstraints.weighty = 1;
    }

    MainFrame() {
        super("Bishop Carroll School Tracker", WindowConstants.EXIT_ON_CLOSE);
        try {
            carrollBird = ImageIO.read(ClassLoader.getSystemResourceAsStream("org/carroll/images/carroll_icon.png"));
            titleImage = ImageIO.read(ClassLoader.getSystemResourceAsStream("org/carroll/images/title.png"));
        } catch (IOException ex) {
            Logging.log("Could not load title images.");
        }

        addWindowListener(new ClosingListener() {
            @Override
            public void closeWindow() {
                Saving.saveEverything();
                Logging.log("Closing program");
            }
        });
        addComponentListener(new ResizingListener() {
            @Override
            public void windowResizing() {
                carrollIcon.setIcon(getResizedCarrollIcon());
                carrollIcon.setVerticalAlignment(SwingConstants.CENTER);
                carrollIcon.setHorizontalAlignment(SwingConstants.CENTER);
                title.setIcon(getResizedTitleIcon());
                title.setVerticalAlignment(SwingConstants.CENTER);
                title.setHorizontalAlignment(SwingConstants.CENTER);
            }
        });
    }

    /**
     * Returns the singleton instance of the main frame.
     *
     * @return instance of main frame
     */
    public static MainFrame getInstance() {
        if (instance == null) {
            instance = new MainFrame();
        }
        return instance;
    }

    /**
     * Switches the selected button based on a string representation of which
     * button is pressed. Sets the selected button to a different color.
     *
     * @param selected representation of selected button
     */
    public static void switchButton(String selected) {
        toDo.setBackground(selected.equals("todo") ? Color.GRAY : null);
        courses.setBackground(selected.equals("courses") ? Color.GRAY : null);
        work.setBackground(selected.equals("work") ? Color.GRAY : null);
        workload.setBackground(selected.equals("workload") ? Color.GRAY : null);

        getInstance().remove(buttons);
        buttons.removeAll();
        GridBagLayout l = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        buttons.setLayout(l);
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.weighty = 1;
        switch (selected) {
            case "courses":
                c.gridwidth = 3;
                c.gridy = 0;
                c.gridx = 0;
                l.addLayoutComponent(toDo, c);
                c.gridy = 1;
                l.addLayoutComponent(courses, c);
                JButton addCourse = new JButton("Add");
                addCourse.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        new AddCourseDialog().createAndViewGUI();
                    }
                });
                c.gridwidth = 1;
                c.gridy = 2;
                c.weighty = 0;
                l.addLayoutComponent(addCourse, c);
                JButton editCourse = new JButton("Edit");
                editCourse.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        new EditCourseDialog(CoursesPanel.getSelectedWork()).createAndViewGUI();
                    }
                });
                c.gridx = 1;
                l.addLayoutComponent(editCourse, c);
                JButton removeCourse = new JButton("Remove");
                removeCourse.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        new RemoveCourseDialog().createAndViewGUI();
                    }
                });
                c.gridx = 2;
                l.addLayoutComponent(removeCourse, c);
                c.weighty = 1;
                c.gridwidth = 3;
                c.gridx = 0;
                c.gridy = 3;
                l.addLayoutComponent(work, c);
                c.gridy = 4;
                l.addLayoutComponent(workload, c);
                buttons.add(addCourse);
                buttons.add(editCourse);
                buttons.add(removeCourse);
                buttons.add(toDo);
                buttons.add(courses);
                buttons.add(work);
                buttons.add(workload);
                break;
            case "work":
                c.gridwidth = 3;
                c.gridy = 0;
                l.addLayoutComponent(toDo, c);
                c.gridy = 1;
                l.addLayoutComponent(courses, c);
                c.gridy = 2;
                l.addLayoutComponent(work, c);
                JButton addWork = new JButton("Add");
                addWork.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        new AddWorkDialog().createAndViewGUI();
                    }
                });
                c.gridwidth = 1;
                c.weighty = 0;
                c.gridy = 3;
                l.addLayoutComponent(addWork, c);
                JButton editWork = new JButton("Edit");
                editWork.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            new EditWorkDialog(WorkPanel.getSelectedWorkName(), WorkPanel.getSelectedWorkCourse()).createAndViewGUI();
                        } catch (NoSuchFieldException ex) {
                            new ErrorMessage(ex, "Try selecting a piece of work first").createAndViewGUI();
                        }
                    }
                });
                c.gridx = 1;
                l.addLayoutComponent(editWork, c);
                JButton removeWork = new JButton("Remove");
                removeWork.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        new RemoveWorkDialog().createAndViewGUI();
                    }
                });
                c.gridx = 2;
                l.addLayoutComponent(removeWork, c);
                c.gridy = 4;
                c.gridx = 0;
                c.gridwidth = 3;
                c.weighty = 1;
                l.addLayoutComponent(workload, c);
                buttons.add(addWork);
                buttons.add(editWork);
                buttons.add(removeWork);
                buttons.add(toDo);
                buttons.add(courses);
                buttons.add(work);
                buttons.add(workload);
                break;
            default:
                l.addLayoutComponent(toDo, c);
                c.gridy = 1;
                l.addLayoutComponent(courses, c);
                c.gridy = 2;
                l.addLayoutComponent(work, c);
                c.gridy = 3;
                l.addLayoutComponent(workload, c);
                buttons.add(toDo);
                buttons.add(courses);
                buttons.add(work);
                buttons.add(workload);
                break;
        }

        getInstance().add(buttons, buttonConstraints);
    }

    Icon getResizedCarrollIcon() {
        if (getHeight() != 0 && getWidth() != 0) {
            return new ImageIcon(carrollBird.getScaledInstance(getWidth() / 6, getHeight() / 5, Image.SCALE_SMOOTH));
        } else {
            return new ImageIcon(carrollBird.getScaledInstance(170, 130, Image.SCALE_SMOOTH));
        }
    }

    Icon getResizedTitleIcon() {
        if (getHeight() != 0 && getWidth() != 0) {
            return new ImageIcon(titleImage.getScaledInstance((int) (getWidth() * 0.8), getHeight() / 6, Image.SCALE_SMOOTH));
        } else {
            return new ImageIcon(titleImage.getScaledInstance(834, 90, Image.SCALE_SMOOTH));
        }
    }

    @Override
    protected void init() {
        setLayout(layout);
        setIconImage(carrollBird);
        setPreferredSize(new Dimension(1100, 650));

        addPictures();
        addMenuBar();
        addButtons();
        addInternalPanels();
    }

    void addPictures() {
        carrollIcon = new JLabel(getResizedCarrollIcon());
        carrollIcon.setVerticalAlignment(SwingConstants.CENTER);
        carrollIcon.setHorizontalAlignment(SwingConstants.CENTER);
        title = new JLabel(getResizedTitleIcon());
        title.setVerticalAlignment(SwingConstants.CENTER);
        title.setHorizontalAlignment(SwingConstants.CENTER);

        constraints.weightx = 0;
        constraints.weighty = 0;
        constraints.fill = GridBagConstraints.NONE;
        layout.addLayoutComponent(carrollIcon, constraints);
        constraints.gridx = 1;
        layout.addLayoutComponent(title, constraints);

        add(carrollIcon);
        add(title);
    }

    void addMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu file = new JMenu("File"),
                edit = new JMenu("Edit"),
                about = new JMenu("About");

        //File
        JMenuItem open = new JMenuItem("Open"),
                save = new JMenuItem("Save"),
                saveAs = new JMenuItem("Save as"),
                export = new JMenuItem("Export"),
                reload = new JMenuItem("Reset Program"),
                removeData = new JMenuItem("Remove data"),
                saveAndQuit = new JMenuItem("Save and quit");

        KeyStroke openKey = KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK);
        KeyStroke saveKey = KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK);
        KeyStroke quitKey = KeyStroke.getKeyStroke(KeyEvent.VK_Q, KeyEvent.CTRL_DOWN_MASK);
        open.setAccelerator(openKey);
        save.setAccelerator(saveKey);
        saveAndQuit.setAccelerator(quitKey);

        reload.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Loading.loadEverything();
            }
        });
        open.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new OpenDialog().createAndViewGUI();
            }
        });
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Saving.saveEverything();
            }
        });
        saveAs.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new SaveAsDialog().createAndViewGUI();
            }
        });
        export.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ExportDialog().createAndViewGUI();
            }
        });
        removeData.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new RemoveAllDataDialog().createAndViewGUI();
            }
        });
        saveAndQuit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(
                        new WindowEvent(MainFrame.getInstance(), WindowEvent.WINDOW_CLOSING));
            }
        });


        //Edit
        JMenu workMenu = new JMenu("Work"),
                coursesMenu = new JMenu("Courses"),
                currentGrade = new JMenu("Current Grade");
        JMenuItem settings = new JMenuItem("Settings");

        JMenuItem addWork = new JMenuItem("Add work");
        KeyStroke addWorkKey = KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK);
        addWork.setAccelerator(addWorkKey);
        addWork.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AddWorkDialog().createAndViewGUI();
            }
        });

        JMenuItem removeWork = new JMenuItem("Remove work");
        removeWork.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new RemoveWorkDialog().createAndViewGUI();
            }
        });

        JMenuItem addCourse = new JMenuItem("Add course");
        KeyStroke addCourseKey = KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.ALT_DOWN_MASK);
        addCourse.setAccelerator(addCourseKey);
        addCourse.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AddCourseDialog().createAndViewGUI();
            }
        });

        JMenuItem editCourse = new JMenuItem("Edit course");
        editCourse.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new EditCourseDialog().createAndViewGUI();
            }
        });

        JMenuItem removeCourse = new JMenuItem("Remove course");
        removeCourse.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new RemoveCourseDialog().createAndViewGUI();
            }
        });

        JCheckBoxMenuItem grade10 = new JCheckBoxMenuItem("Grade 10"),
                grade11 = new JCheckBoxMenuItem("Grade 11"),
                grade12 = new JCheckBoxMenuItem("Grade 12");
        ButtonGroup grades = new ButtonGroup();

        grades.add(grade10);
        grades.add(grade11);
        grades.add(grade12);
        try {
            int grade = Integer.parseInt(Settings.getInstance().get("CurrentGrade").getValue());
            grade10.setSelected(grade == 10);
            grade11.setSelected(grade == 11);
            grade12.setSelected(grade == 12);
        } catch (NoSuchFieldException ex) {
            Logging.log("Could not set current grade menu item");
        }

        grade10.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Saving.saveEverything();
                    Settings.getInstance().get("CurrentGrade").set("10");
                    Loading.loadData();
                    InternalPanel.COURSES.refresh();
                    InternalPanel.WORK.refresh();
                } catch (NoSuchFieldException ex) {
                }
            }
        });

        grade11.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Saving.saveEverything();
                    Settings.getInstance().get("CurrentGrade").set("11");
                    Loading.loadData();
                    InternalPanel.COURSES.refresh();
                    InternalPanel.WORK.refresh();
                } catch (NoSuchFieldException ex) {
                }
            }
        });

        grade12.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Saving.saveEverything();
                    Settings.getInstance().get("CurrentGrade").set("12");
                    Loading.loadData();
                    InternalPanel.COURSES.refresh();
                    InternalPanel.WORK.refresh();
                } catch (NoSuchFieldException ex) {
                }
            }
        });

        settings.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new SettingsDialog().createAndViewGUI();
            }
        });

        //About

        JMenuItem mininmumHighSchool = new JMenuItem("Requirements for high school"),
                log = new JMenuItem("Log"),
                help = new JMenuItem("Tutorial"),
                update = new JMenuItem("Check for updates"),
                aboutThisProgram = new JMenuItem("About this program");

        mininmumHighSchool.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new HighSchoolRequirementDialog().createAndViewGUI();
            }
        });
        log.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new LogFileDialog().createAndViewGUI();
            }
        });
        help.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Tutorial.startTutorial();
            }
        });
        update.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UpdateCheck.check();
            }
        });
        aboutThisProgram.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AboutDialog().createAndViewGUI();
            }
        });

        file.add(open);
        file.add(save);
        file.add(saveAs);
        file.add(export);
        file.add(reload);
        file.add(removeData);
        file.add(saveAndQuit);

        workMenu.add(addWork);
        workMenu.add(removeWork);

        coursesMenu.add(addCourse);
        coursesMenu.add(editCourse);
        coursesMenu.add(removeCourse);

        currentGrade.add(grade10);
        currentGrade.add(grade11);
        currentGrade.add(grade12);

        edit.add(coursesMenu);
        edit.add(workMenu);
        edit.add(currentGrade);
        edit.add(settings);

        about.add(mininmumHighSchool);
        about.add(log);
        about.add(help);
        about.add(update);
        about.add(aboutThisProgram);

        menuBar.add(file);
        menuBar.add(edit);
        menuBar.add(about);

        setJMenuBar(menuBar);
    }

    void addButtons() {
        toDo.setFocusable(false);
        courses.setFocusable(false);
        work.setFocusable(false);
        workload.setFocusable(false);

        toDo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                InternalPanel.changePanelTo(InternalPanel.TODO);
            }
        });
        courses.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                InternalPanel.changePanelTo(InternalPanel.COURSES);
            }
        });
        work.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                InternalPanel.changePanelTo(InternalPanel.WORK);
            }
        });
        workload.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                InternalPanel.changePanelTo(InternalPanel.WORKLOAD);
            }
        });

        buttons.setLayout(new GridLayout(4, 1));
        buttons.add(toDo);
        buttons.add(courses);
        buttons.add(work);
        buttons.add(workload);

        layout.addLayoutComponent(buttons, buttonConstraints);

        add(buttons);
    }

    void addInternalPanels() {
        MetalBorders.Flush3DBorder border = new MetalBorders.Flush3DBorder();
        InternalPanel.TODO.setBorder(border);
        InternalPanel.COURSES.setBorder(border);
        InternalPanel.WORK.setBorder(border);
        InternalPanel.WORKLOAD.setBorder(border);

        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.weighty = 1;
        constraints.weightx = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridheight = 4;
        constraints.gridwidth = 1;
        layout.addLayoutComponent(InternalPanel.TODO, constraints);
        layout.addLayoutComponent(InternalPanel.COURSES, constraints);
        layout.addLayoutComponent(InternalPanel.WORK, constraints);
        layout.addLayoutComponent(InternalPanel.WORKLOAD, constraints);

        add(InternalPanel.TODO);
        add(InternalPanel.COURSES);
        add(InternalPanel.WORK);
        add(InternalPanel.WORKLOAD);

        InternalPanel.changePanelTo(InternalPanel.TODO);
    }
}
