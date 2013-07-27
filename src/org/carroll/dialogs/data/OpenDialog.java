package org.carroll.dialogs.data;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import org.carroll.utils.Loading;
import org.carroll.data.Paths;
import org.carroll.utils.Logging;
import org.swing.dialogs.FileChoosingDialog;

/**
 * File chooser to set a folder as the main config folder.
 *
 * @author Joel Gallant
 */
public class OpenDialog extends FileChoosingDialog {

    /**
     * Creates open dialog.
     */
    public OpenDialog() {
        super("Open file", "Open", Paths.getMain(), JFileChooser.DIRECTORIES_ONLY, new FileFilter() {
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
        Logging.log("Opening folder - "+path);
        Paths.setMain(path);
        Loading.loadEverything();
    }
}
