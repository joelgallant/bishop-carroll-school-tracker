package org.carroll.main.update;

import java.awt.Desktop;
import java.awt.Dimension;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import javax.swing.JProgressBar;
import org.carroll.main.BishopCarrollSchoolTracker;
import org.carroll.utils.Files;
import org.swing.Dialog;
import org.swing.dialogs.VerifyDialog;
import org.swing.dialogs.messages.ErrorMessage;

/**
 * Class used to ensure user is using the most recent version of the program.
 *
 * @author joel
 */
public class UpdateCheck {

    public static void check() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                URL website;
                try {
                    website = new URL("http://sourceforge.net/projects/bishopcaroll/files/version.txt");
                    ReadableByteChannel rbc = Channels.newChannel(website.openStream());
                    File version = File.createTempFile("version", ".txt");
                    FileOutputStream fos = new FileOutputStream(version);
                    fos.getChannel().transferFrom(rbc, 0, 1 << 24);
                    String versionNum = Files.TextFiles.getStringFromFile(version);
                    if (!versionNum.trim().equals(BishopCarrollSchoolTracker.getVersion())) {
                        new VerifyDialog("Out of date", "You are using version " + BishopCarrollSchoolTracker.getVersion()
                                + "<br>and version " + versionNum + " is out. Update?") {
                            @Override
                            public void yesAction() {
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (System.getProperty("os.name").contains("Win")) {
                                            if (Desktop.isDesktopSupported()) {
                                                try {
                                                    Desktop.getDesktop().browse(new URI("http://sourceforge.net/projects/bishopcaroll/files/latest/download"));
                                                } catch (URISyntaxException | IOException ex) {
                                                    new ErrorMessage(ex).createAndViewGUI();
                                                }
                                            }
                                        } else {
                                            try {
                                                String path = BishopCarrollSchoolTracker.class.getProtectionDomain().getCodeSource().getLocation().getPath();
                                                final String decodedPath = URLDecoder.decode(path, "UTF-8");
                                                URL newVersion;
                                                if (decodedPath.contains("jdk6")) {
                                                    newVersion = new URL("http://sourceforge.net/projects/bishopcaroll/files/updating/jdk6.jar");
                                                } else {
                                                    newVersion = new URL("http://sourceforge.net/projects/bishopcaroll/files/updating/jdk7.jar");
                                                }
                                                URLConnection version = newVersion.openConnection();
                                                final File temp = File.createTempFile("temp", ".jar");
                                                final int length = version.getContentLength();
                                                final InputStream is = version.getInputStream();
                                                final FileOutputStream fos = new FileOutputStream(temp);
                                                Dialog d = new Dialog("Updating") {
                                                    JProgressBar bar = new JProgressBar(0, length);

                                                    @Override
                                                    protected void init() {
                                                        new Thread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                try {
                                                                    byte[] buf = new byte[1024];
                                                                    int len;
                                                                    int progress = 0;
                                                                    while ((len = is.read(buf)) > 0) {
                                                                        fos.write(buf, 0, len);
                                                                        progress += len;
                                                                        bar.setValue(progress);
                                                                    }

                                                                    fos.close();
                                                                    is.close();

                                                                } catch (IOException ex) {
                                                                    new ErrorMessage(ex, "Could not copy file to temp").createAndViewGUI();
                                                                }

                                                                bar.setValue(0);

                                                                try {
                                                                    try (FileInputStream t = new FileInputStream(temp); FileOutputStream o = new FileOutputStream(new File(decodedPath))) {

                                                                        bar.setMaximum(temp.toURI().toURL().openConnection().getContentLength());

                                                                        byte[] buf = new byte[1024];
                                                                        int len;
                                                                        int progress = 0;
                                                                        while ((len = t.read(buf)) > 0) {
                                                                            o.write(buf, 0, len);
                                                                            progress += len;
                                                                            bar.setValue(progress);
                                                                        }
                                                                    }
                                                                } catch (IOException ex) {
                                                                    new ErrorMessage(ex, "Could not copy temp file to " + decodedPath).createAndViewGUI();
                                                                }

                                                                try {
                                                                    Runtime.getRuntime().exec("java -jar " + decodedPath);
                                                                } catch (IOException ex) {
                                                                }
                                                                System.exit(0);
                                                            }
                                                        }).start();
                                                        add(bar);
                                                        setPreferredSize(new Dimension(200, 50));
                                                    }
                                                };
                                                d.createAndViewGUI();
                                            } catch (UnsupportedEncodingException ex) {
                                                new ErrorMessage(ex, "Could not retreive current working directory. Could not update.").createAndViewGUI();
                                            } catch (MalformedURLException ex1) {
                                                new ErrorMessage(ex1, "Could not retreive newest version.").createAndViewGUI();
                                            } catch (IOException ex2) {
                                                new ErrorMessage(ex2, "Check internet connection.").createAndViewGUI();
                                            }
                                        }
                                    }
                                }, "Update Worker").start();
                            }

                            @Override
                            public void noAction() {
                            }
                        }.createAndViewGUI();
                    }
                } catch (IOException ex) {
                    new ErrorMessage(ex, "Check internet connection.").createAndViewGUI();
                }
            }
        }, "Update Checker").start();
    }
}
