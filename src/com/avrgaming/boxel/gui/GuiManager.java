/*
 ************************************************************************
 * 
 * AVRGAMING LLC
 * __________________
 * 
 *  [2014] AVRGAMING LLC
 *  All Rights Reserved.
 * 
 * NOTICE:  All information contained herein is, and remains
 * the property of AVRGAMING LLC and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to AVRGAMING LLC
 * and its suppliers and may be covered by U.S. and Foreign Patents,
 * patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from AVRGAMING LLC.
 ************************************************************************
 */
package com.avrgaming.boxel.gui;

import com.jme3.niftygui.NiftyJmeDisplay;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.LayerBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.ScreenBuilder;
import de.lessvoid.nifty.controls.button.builder.ButtonBuilder;
import de.lessvoid.nifty.screen.DefaultScreenController;

public class GuiManager {
    
    private NiftyJmeDisplay niftyDisplay;
    
    public GuiManager(NiftyJmeDisplay niftyDisplay) {
        Nifty nifty = niftyDisplay.getNifty();
        nifty.loadStyleFile("nifty-default-styles.xml");
        nifty.loadControlFile("nifty-default-controls.xml");
        
            // <screen>
        nifty.addScreen("Screen_ID", new ScreenBuilder("Hello Nifty Screen"){{
            controller(new DefaultScreenController()); // Screen properties       

            // <layer>
            layer(new LayerBuilder("Layer_ID") {{
                childLayoutVertical(); // layer properties, add more...

                // <panel>
                panel(new PanelBuilder("Panel_ID") {{
                   childLayoutCenter(); // panel properties, add more...               

                    // GUI elements
                    control(new ButtonBuilder("Button_ID", "Hello Nifty"){{
                        alignCenter();
                        valignCenter();
                        height("5%");
                        width("15%");
                    }});

                    //.. add more GUI elements here              

                }});
                // </panel>
              }});
            // </layer>
          }}.build(nifty));
        // </screen>

        nifty.gotoScreen("Screen_ID"); // start the screen
        
    }
    
}
