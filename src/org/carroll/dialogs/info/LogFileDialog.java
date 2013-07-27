package org.carroll.dialogs.info;

import java.awt.Dimension;
import java.io.File;
import java.io.FileNotFoundException;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import org.carroll.utils.Files;
import org.carroll.data.Paths;
import org.main.Timer;
import org.swing.Dialog;
import org.swing.Listeners;

/**
 * Dialog to show the contents on the log file live.
 *
 * @author Joel Gallant
 */
public class LogFileDialog extends Dialog {

    JTextArea textArea;
    volatile static String text = "";
    static boolean show;

    @Override
    protected void init() {
        textArea = new JTextArea();
        add(new JScrollPane(textArea));
        setPreferredSize(new Dimension(600, 400));
        show = true;
        startListener();
        addWindowListener(new Listeners.ClosingListener() {
            @Override
            public void closeWindow() {
                show = false;
            }
        });
    }

    void startListener() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                File log = new File(Paths.getLog());
                while (show) {
                    try {
                        LogFileDialog.text += Files.TextFiles.getStringFromFile(log).substring(text.length());
                    } catch (FileNotFoundException ex) {
                    }
                    Timer.delay(0.3);
                }
            }
        }, "Log file viewer Worker #1").start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (show) {
                    textArea.setText(text);
                    Timer.delay(0.35);
                }
            }
        }, "Log file viewer Worker #2").start();
    }
}
