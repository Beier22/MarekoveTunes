/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.gui.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import mytunes.be.Playlist;
import mytunes.bll.IModel;
import mytunes.bll.Model;

/**
 * FXML Controller class
 *
 * @author mads_
 */
public class PlaylistEditViewController implements Initializable {

    @FXML
    private Button cancelButton;
    @FXML
    private Button saveButton;
    @FXML
    private TextField input;
    
    private ControllerModel controllerModel;
    private boolean isEditing;
    private Playlist playlist;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void pressCancel(ActionEvent event) {
        Stage st = (Stage) cancelButton.getScene().getWindow();
        st.close();
    }

    @FXML
    private void pressSave(ActionEvent event) 
    {
        if (!input.getText().equals(""))
        {
            playlist = isEditing ? playlist : new Playlist();
            playlist.setName(input.getText());
            if(isEditing)
                controllerModel.updatePlaylist(playlist);
            else
                controllerModel.addPlaylist(playlist);

            Stage st = (Stage) saveButton.getScene().getWindow();
            st.close();
        }
    }
    
    public void setControllerModel(ControllerModel model)
    {
        controllerModel = model;
        isEditing = false;
    }
    
    public void setControllerModelWithPlaylist(ControllerModel model,Playlist playlist)
    {
        controllerModel = model;
        isEditing = true;
        this.playlist = playlist;
    }
}
