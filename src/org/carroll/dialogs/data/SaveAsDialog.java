package org.carroll.dialogs.data;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import org.carroll.data.Paths;
import org.carroll.utils.Saving;
import org.carroll.utils.Logging;
import org.swing.dialogs.FileChoosingDialog;

/**
 * File chooser for temporarily saving data and settings to a folder.
 *
 * @author Joel Gallant
 */
public class SaveAsDialog extends FileChoosingDialog {

    /**
     * Creates new save as dialog.
     */
    public SaveAsDialog() {
        super("Save as", "Save", Paths.getMain(), JFileChooser.DIRECTORIES_ONLY, new FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.isDirectory();
            }

            @Override
            public String getDescription() {
                return "Directories only";
            }
        });
    }

    @Override
    protected void setFile(String path) {
        Logging.log("Saving configuration to file - "+path);
        String oldPath = Paths.getMain();
        Paths.setMain(path);
        Saving.saveEverything();
        Paths.setMain(oldPath);
    }
}
