package org.carroll.main;

import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;
import org.carroll.dialogs.MainFrame;
import org.carroll.main.update.UpdateCheck;
import org.carroll.utils.Loading;
import org.carroll.utils.Saving;

/*
 //                                                                            //
 //      ____  _     _                    _____                     _ _        //
 //     |  _ \(_)   | |                  / ____|                   | | |       //
 //     | |_) |_ ___| |__   ___  _ __   | |     __ _ _ __ _ __ ___ | | |       //
 //     |  _ <| / __| '_ \ / _ \| '_ \  | |    / _` | '__| '__/ _ \| | |       //
 //     | |_) | \__ \ | | | (_) | |_) | | |___| (_| | |  | | | (_) | | |       //
 //     |____/|_|___/_| |_|\___/| .__/   \_____\__,_|_|  |_|  \___/|_|_|       //
 //                             | |                                            //
 //                             |_|                                            //
 //       _____                        _   _______             _               //
 //      / ____|    | |               | | |__   __|           | |              //
 //     | (___   ___| |__   ___   ___ | |    | |_ __ __ _  ___| | _____ _ __   //
 //      \___ \ / __| '_ \ / _ \ / _ \| |    | | '__/ _` |/ __| |/ / _ \ '__|  //
 //      ____) | (__| | | | (_) | (_) | |    | | | | (_| | (__|   <  __/ |     //
 //     |_____/ \___|_| |_|\___/ \___/|_|    |_|_|  \__,_|\___|_|\_\___|_|     //
 //                                                                            //
 //                                                                            //
 //                                                                            //
 //      _____                 _                      _   ____                 //
 //     |  __ \               | |                    | | |  _ \         _      //
 //     | |  | | _____   _____| | ___  _ __   ___  __| | | |_) |_   _  (_)     //
 //     | |  | |/ _ \ \ / / _ \ |/ _ \| '_ \ / _ \/ _` | |  _ <| | | |         //
 //     | |__| |  __/\ V /  __/ | (_) | |_) |  __/ (_| | | |_) | |_| |  _      //
 //     |_____/ \___| \_/ \___|_|\___/| .__/ \___|\__,_| |____/ \__, | (_)     //
 //                                   | |                        __/ |         //
 //                                   |_|                       |___/          //
 //                                                                            //
 //----------------------------------------------------------------------------//
 //                                                                            //
 //                _            _    _____       _ _             _             //
 //               | |          | |  / ____|     | | |           | |            //
 //               | | ___   ___| | | |  __  __ _| | | __ _ _ __ | |_           //
 //           _   | |/ _ \ / _ \ | | | |_ |/ _` | | |/ _` | '_ \| __|          //
 //          | |__| | (_) |  __/ | | |__| | (_| | | | (_| | | | | |_           //
 //           \____/ \___/ \___|_|  \_____|\__,_|_|_|\__,_|_| |_|\__|          //
 //                                                                            //
 //----------------------------------------------------------------------------//
 */
/**
 * The main class in the Bishop Carroll School Tracker. Starts the program.
 *
 * @author Joel Gallant
 */
public class BishopCarrollSchoolTracker {

    /**
     * Main method. The beginning to no end.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    UIDefaults defaults = UIManager.getLookAndFeelDefaults();
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
        }
        Loading.loadEverything();
        new Saving.AutoSaving().start();
        MainFrame.getInstance().createAndViewGUI();
        
        UpdateCheck.check();
    }
    
    public static String getVersion() {
        return "1.7.3";
    }
}
