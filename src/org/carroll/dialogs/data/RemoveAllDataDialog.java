package org.carroll.dialogs.data;

import java.io.File;
import org.carroll.utils.Files;
import org.carroll.utils.Loading;
import org.carroll.data.Paths;
import org.carroll.utils.Logging;
import org.swing.dialogs.VerifyDialog;

/**
 * Verifies with the user to make sure they want to delete all data.
 *
 * @author Joel Gallant
 */
public class RemoveAllDataDialog extends VerifyDialog {

    /**
     * Creates new remove all data dialog.
     */
    public RemoveAllDataDialog() {
        super("Remove all data?", "Are you sure you want to remove all data and settings?");
    }

    @Override
    public void yesAction() {
        Logging.log("Deleting all data.");
        Files.deleteAllInDirectory(new File(Paths.getMain()));
        Loading.loadEverything();
    }

    @Override
    public void noAction() {
    }
}
