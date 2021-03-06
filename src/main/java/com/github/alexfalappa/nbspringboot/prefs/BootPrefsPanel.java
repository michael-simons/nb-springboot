/*
 * Copyright 2016 Alessandro Falappa.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.alexfalappa.nbspringboot.prefs;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.prefs.Preferences;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.openide.util.NbPreferences;

import com.github.alexfalappa.nbspringboot.projects.initializr.InitializrService;

import static com.github.alexfalappa.nbspringboot.projects.initializr.InitializrProjectProps.PREF_INITIALIZR_TIMEOUT;
import static com.github.alexfalappa.nbspringboot.projects.initializr.InitializrProjectProps.PREF_INITIALIZR_URL;

/**
 * Plugin options panel.
 * <p>
 * It is shown under the Java category in Tools/Options dialog.
 *
 * @author Alessandro Falappa
 */
final class BootPrefsPanel extends javax.swing.JPanel implements DocumentListener, ChangeListener {

    private final BootPrefsOptionsPanelController controller;

    BootPrefsPanel(BootPrefsOptionsPanelController controller) {
        this.controller = controller;
        initComponents();
        // listen to changes in form fields and call controller.changed()
        // Register listener on the textFields to detect changes
        txInitializrUrl.getDocument().addDocumentListener(this);
        spInitializrTimeout.addChangeListener(this);
    }

    /** This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this
     * method is always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lInitializr = new javax.swing.JLabel();
        lInitializrUrl = new javax.swing.JLabel();
        txInitializrUrl = new javax.swing.JTextField();
        lInitializrTimeout = new javax.swing.JLabel();
        spInitializrTimeout = new javax.swing.JSpinner();
        lSeconds = new javax.swing.JLabel();

        org.openide.awt.Mnemonics.setLocalizedText(lInitializr, org.openide.util.NbBundle.getMessage(BootPrefsPanel.class, "BootPrefsPanel.lInitializr.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(lInitializrUrl, org.openide.util.NbBundle.getMessage(BootPrefsPanel.class, "BootPrefsPanel.lInitializrUrl.text")); // NOI18N

        txInitializrUrl.setColumns(20);

        org.openide.awt.Mnemonics.setLocalizedText(lInitializrTimeout, org.openide.util.NbBundle.getMessage(BootPrefsPanel.class, "BootPrefsPanel.lInitializrTimeout.text")); // NOI18N

        spInitializrTimeout.setModel(new javax.swing.SpinnerNumberModel(30, 5, 999, 5));

        org.openide.awt.Mnemonics.setLocalizedText(lSeconds, org.openide.util.NbBundle.getMessage(BootPrefsPanel.class, "BootPrefsPanel.lSeconds.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lInitializr)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lInitializrTimeout)
                            .addComponent(lInitializrUrl))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(spInitializrTimeout, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lSeconds)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(txInitializrUrl))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lInitializr)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lInitializrUrl)
                    .addComponent(txInitializrUrl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lInitializrTimeout)
                    .addComponent(spInitializrTimeout, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lSeconds))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    void load() {
        // read settings and initialize GUI
        final Preferences prefs = NbPreferences.forModule(InitializrService.class);
        txInitializrUrl.setText(prefs.get(PREF_INITIALIZR_URL, "http://start.spring.io"));
        spInitializrTimeout.setValue(prefs.getInt(PREF_INITIALIZR_TIMEOUT, 30));
    }

    void store() {
        // store modified settings
        final Preferences prefs = NbPreferences.forModule(InitializrService.class);
        prefs.put(PREF_INITIALIZR_URL, txInitializrUrl.getText());
        prefs.putInt(PREF_INITIALIZR_TIMEOUT, (int) spInitializrTimeout.getValue());
    }

    boolean valid() {
        // check whether form is consistent and complete
        boolean ret = true;
        try {
            URL url = new URL(txInitializrUrl.getText());
        } catch (MalformedURLException ex) {
            ret = false;
        }
        return ret;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel lInitializr;
    private javax.swing.JLabel lInitializrTimeout;
    private javax.swing.JLabel lInitializrUrl;
    private javax.swing.JLabel lSeconds;
    private javax.swing.JSpinner spInitializrTimeout;
    private javax.swing.JTextField txInitializrUrl;
    // End of variables declaration//GEN-END:variables

    @Override
    public void insertUpdate(DocumentEvent e) {
        controller.changed();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        controller.changed();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        controller.changed();
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        controller.changed();
    }
}
