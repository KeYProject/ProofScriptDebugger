package edu.kit.iti.formal.psdbg.gui.controls;

import edu.kit.iti.formal.psdbg.gui.ProofScriptDebugger;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.web.WebView;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Alexander Weigl
 * @version 1 (19.01.18)
 */
public class AboutDialog implements Initializable {
    private StringProperty programName = new SimpleStringProperty();
    private SimpleStringProperty version = new SimpleStringProperty();
    private SimpleStringProperty subtitle = new SimpleStringProperty();
    private SimpleStringProperty license = new SimpleStringProperty();
    private SimpleStringProperty keyLicense = new SimpleStringProperty();
    private SimpleStringProperty thridPartyLicense = new SimpleStringProperty();
    private SimpleStringProperty aboutText = new SimpleStringProperty();

    private WebView webView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setProgramName(ProofScriptDebugger.NAME + "-" + ProofScriptDebugger.VERSION);
        setSubtitle("KeY Version: " + ProofScriptDebugger.KEY_VERSION);

        try {
            InputStream is = getClass().getResourceAsStream("/about.txt");
            if (is != null)
                aboutText.set(IOUtils.toString(is, "utf-8"));
        } catch (IOException e) {
        }

        try {
            InputStream is = getClass().getResourceAsStream("/THIRD-PARTY.txt");
            if (is != null)
                setThridPartyLicense(IOUtils.toString(is, "utf-8"));
        } catch (IOException e) {
        }

        try {
            InputStream is = getClass().getResourceAsStream("/LICENSE");
            if (is != null)
                setLicense(IOUtils.toString(is, "utf-8"));
        } catch (IOException e) {
        }
    }

    public String getProgramName() {
        return programName.get();
    }

    public void setProgramName(String programName) {
        this.programName.set(programName);
    }

    public StringProperty programNameProperty() {
        return programName;
    }

    public String getVersion() {
        return version.get();
    }

    public void setVersion(String version) {
        this.version.set(version);
    }

    public SimpleStringProperty versionProperty() {
        return version;
    }

    public String getSubtitle() {
        return subtitle.get();
    }

    public void setSubtitle(String subtitle) {
        this.subtitle.set(subtitle);
    }

    public SimpleStringProperty subtitleProperty() {
        return subtitle;
    }

    public String getLicense() {
        return license.get();
    }

    public void setLicense(String license) {
        this.license.set(license);
    }

    public SimpleStringProperty licenseProperty() {
        return license;
    }

    public String getKeyLicense() {
        return keyLicense.get();
    }

    public void setKeyLicense(String keyLicense) {
        this.keyLicense.set(keyLicense);
    }

    public SimpleStringProperty keyLicenseProperty() {
        return keyLicense;
    }

    public String getThridPartyLicense() {
        return thridPartyLicense.get();
    }

    public void setThridPartyLicense(String thridPartyLicense) {
        this.thridPartyLicense.set(thridPartyLicense);
    }

    public SimpleStringProperty thridPartyLicenseProperty() {
        return thridPartyLicense;
    }

    public String getAboutText() {
        return aboutText.get();
    }

    public void setAboutText(String aboutText) {
        this.aboutText.set(aboutText);
    }

    public SimpleStringProperty aboutTextProperty() {
        return aboutText;
    }
}
