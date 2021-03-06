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
package com.github.alexfalappa.nbspringboot.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import org.netbeans.modules.maven.NbMavenProjectImpl;
import org.netbeans.modules.maven.configurations.M2ConfigProvider;
import org.netbeans.modules.maven.configurations.M2Configuration;
import org.netbeans.modules.maven.execute.model.NetbeansActionMapping;
import org.netbeans.spi.project.ActionProvider;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle.Messages;

@ActionID(
        category = "Build",
        id = "com.github.alexfalappa.nbspringboot.actions.ReloadAction"
)
@ActionRegistration(
        iconBase = "com/github/alexfalappa/nbspringboot/actions/springboot-logo.png",
        displayName = "#CTL_ReloadAction"
)
@ActionReferences({
    @ActionReference(path = "Menu/BuildProject", position = 57),
    @ActionReference(path = "Toolbars/Build", position = 500),
    @ActionReference(path = "Shortcuts", name = "DS-L")
})
@Messages("CTL_ReloadAction=S&pring Boot Reload")
public final class ReloadAction implements ActionListener {

    public static final String TRIGGER_FILE = ".nbReloadTrigger";
    public static final String PROP_RUN_ARGS = "run.arguments";
    private static final Logger logger = Logger.getLogger(ReloadAction.class.getName());
    private final NbMavenProjectImpl proj;

    public ReloadAction(NbMavenProjectImpl context) {
        this.proj = context;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        File outDir = proj.getProjectWatcher().getOutputDirectory(false);
        M2ConfigProvider cp = proj.getLookup().lookup(M2ConfigProvider.class);
        boolean enabled = false;
        if (cp != null) {
            M2Configuration m2 = cp.getActiveConfiguration();
            List<NetbeansActionMapping> nams = m2.getRawMappings().getActions();
            if (!nams.isEmpty()) {
                for (NetbeansActionMapping nam : nams) {
                    if (nam.getActionName().equals(ActionProvider.COMMAND_RUN)) {
                        enabled = nam.getProperties().containsKey(PROP_RUN_ARGS) && nam.getProperties().get(PROP_RUN_ARGS).contains(
                                TRIGGER_FILE);
                    }
                }
            }
            if (enabled) {
                File f = new File(outDir, TRIGGER_FILE);
                if (outDir.exists()) {
                    try (PrintWriter pw = new PrintWriter(f)) {
                        pw.printf("%1$tF %1$tT", new Date());
                        pw.close();
                        logger.info(String.format("Spring Boot reload triggered"));
                        logger.fine(String.format("Timestamp written in %s", f.getAbsolutePath()));
                    } catch (FileNotFoundException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                } else {
                    logger.warning("No output directory found! Build the project.");
                }
            } else {
                logger.info("Reload disabled!");
            }
        } else {
            logger.warning("No reloading preferences found!");
        }
    }
}
