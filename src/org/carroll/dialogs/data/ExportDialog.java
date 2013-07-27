package org.carroll.dialogs.data;

import java.io.File;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import org.carroll.utils.Exporting;
import org.carroll.utils.Logging;
import org.swing.dialogs.FileChoosingDialog;
import org.swing.dialogs.messages.ErrorMessage;

/**
 * Dialog for exporting progress to a progress report.
 *
 * @author Joel Gallant
 */
public class ExportDialog extends FileChoosingDialog {

    /**
     * Creates dialog.
     */
    public ExportDialog() {
        super("Export progress report", "Export", null, JFileChooser.FILES_AND_DIRECTORIES, null);
    }

    @Override
    protected void setFile(String path) {
        try {
            if (path.contains(".xls")) {
                Exporting.exportTo(new File(path.trim()));
            } else {
                Exporting.exportTo(new File(path + "/PROGRESS REPORT.xls"));
            }
        } catch (IOException ex) {
            new ErrorMessage(ex, "Could not export to file - " + path).createAndViewGUI();
        }
    }
}
