 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Application.Vue.main;

import java.net.URL;
import java.util.ResourceBundle;

import Application.Database.BadUserError;
import Application.Metier.Project;
import Application.Metier.User;
import Application.Vue.Login.SceneLoginController;
import Application.Vue.ProjectActivityScene.SceneProjectActivityController;
import Application.Vue.ProjectMaterial.SceneProjectMaterialController;
import Application.Vue.UtilsIHM;
import Application.Vue.profilScene.SceneProfileController;
import Application.Vue.projectsScene.SceneProjectsController;
import Application.Vue.techsScene.sceneTechsController;
import javafx.fxml.Initializable;
import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author math7
 */
public class MainController implements Initializable {
    
    @FXML
    private Label roleEmployee;

    @FXML
    private Label namesEmployee;

    @FXML
    private JFXButton projectsWindow;

    @FXML
    private JFXButton customers;

    @FXML
    private JFXButton techsWindow;

    @FXML
    private JFXButton techWindow;
    
    @FXML
    private JFXButton toolsWindow;

    @FXML
    private JFXButton profileWindow;

    @FXML
    private JFXButton deconnectionButton;

    @FXML
    private JFXButton connectionButton;
    
    /**
     * espacement item menu
     */
    @FXML
    private AnchorPane spacerMenu; 
    
    @FXML 
    private BorderPane container;

    // Icon bouton menu 
    @FXML
    private ImageView imgProjects;
  
    @FXML
    private ImageView imgClients;

    @FXML
    private ImageView imgTechs;

    @FXML
    private ImageView imgMateriel;

    @FXML
    private ImageView imgProfil;

    @FXML
    private ImageView imgConect;

    @FXML
    private ImageView imgDisconect;
    
    
    /**
     * Bo??te contenant les boutons Projets, Clients et Techniciens
     */
    @FXML
    private VBox boxMenu1;
    
    /**
     * Bo??te contenant les boutons MonProfil, Connexion et deconnexion
     */
    @FXML
    private VBox boxMenu2;
    
    

    /**
     * Utilisateur courant avec un id par d??faut ?? 0. Par d??faut lors de l'arriv?? sur l'application l'user est d??connect??
     * Son id lui sera attribu?? lors de la connexion suivant l'user qui se connecte (id r??cup??r?? dans la bdd)
     */
    private User currentUser = new User(0);

    /**
     * Attribut qui permet d'utiliser les m??thodes (messages pop-up, erreur etc) de la classe Vue.utils
     */
    private UtilsIHM utilsIHM = new UtilsIHM();
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            disableButtons();
            initFieldsUser();
            connect();
        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Initialise les champs informations "nom et r??le" de l'utilisateur qui 
     * s'est connect??
     */
    public void initFieldsUser(){
        String res = "Vous n'??tes pas connect??";
        if (currentUser.isConnected()){
            res = currentUser.getFirstName() + " " + currentUser.getLastName();
        }
        namesEmployee.setText(res);
    }
    
    @FXML
    public void loadSceneCustomers() throws IOException {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Application/Vue/customersScene/sceneCustomers.fxml"));
            Pane tempPane = fxmlLoader.load();
            container.setCenter(tempPane);
            setBtnMenuIsActive(customers); //colorie le bouton actif
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Evenement d??clench??e lorsque l'utilisateur demande ?? voir les projets
     * Il doit ??tre connect?? sinon une exception de type badUserErr est lev??e
     * @throws IOException
     */
    @FXML
    public void projects() throws IOException{
        try{
            if (!currentUser.isConnected()){
                BadUserError badUserErr = new BadUserError();
                utilsIHM.afficherErreur(badUserErr.getMessage());
                throw badUserErr;
            }
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Application/Vue/projectsScene/sceneProjects.fxml"));
                SceneProjectsController controller = new SceneProjectsController(this);
                fxmlLoader.setController(controller);
                Pane tempPane = fxmlLoader.load();
                container.setCenter(tempPane);
                setBtnMenuIsActive(projectsWindow);//colorie le bouton actif
            } catch (IOException e) {
                e.printStackTrace();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    
    @FXML
    public void projectActivities(Project projet) throws IOException{
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Application/Vue/ProjectActivityScene/SceneProjectActivity.fxml"));            
            SceneProjectActivityController controller = new SceneProjectActivityController(this, projet);
            fxmlLoader.setController(controller);
            container.setCenter(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    public void projectMaterial(Project projet) throws IOException{
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Application/Vue/ProjectMaterial/SceneProjectMaterial.fxml"));            
            SceneProjectMaterialController controller = new SceneProjectMaterialController(this, projet);
            fxmlLoader.setController(controller);
            container.setCenter(fxmlLoader.load());
            System.out.println("PRojectMaterial OK");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    

    /**
     * Evenement d??clench?? lorsque l'utilisateur demande ?? se connecter (bouton de connexion)
     * L'utilisateur ne doit pas ??tre d??j?? connect?? sinon un msg d'erreur lui est adress??
     * @throws IOException
     * @autor David Golay
     */
    @FXML
    public void connect() throws IOException, ClassNotFoundException, SQLException{
        if (currentUser.isConnected()){
            Exception e = new Exception("Utilisateur d??j?? connect??");
            utilsIHM.afficherErreur("Vous ??tes d??j?? connect??!");
        }
        else {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Application/Vue/Login/sceneLogin.fxml"));
                SceneLoginController controller = new SceneLoginController(currentUser);
                fxmlLoader.setController(controller);
                Pane tempPane = fxmlLoader.load();
                container.setCenter(tempPane);
                controller.setMainController(this);
                setBtnMenuIsActive(connectionButton); //colorie le bouton actif
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Evenement d??clench?? lorsque l'utilisateur demander ?? se d??connecter (bouton de d??connexion)
     * Si il est connect?? elle appelle la m??thode Deconnection() de l'attribut currentUser
     * et redirige l'utilisateur vers la sc??ne de login
     * Si l'utilisateur n'est pas connect?? un msg d'erreur lui est adress??
     * @autor Mathis Poncet
     * @throws IOException
     */
    @FXML
    public void disconnect() throws IOException, ClassNotFoundException, SQLException{
        if (!currentUser.isConnected()) {
            BadUserError badUserErr = new BadUserError("Vous devez ??tre connect?? afin de vous d??connecter");
            utilsIHM.afficherErreur(badUserErr.getMessage());
        }
        else {
            currentUser.Disconnect();
            //redirection var la page de login
            try {
                disableButtons();
                namesEmployee.setText("Vous n'??tes pas connect??");
                connect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Evenement d??clench?? lorsque l'utilisateur demande ?? lister les techniciens (clique sur Techniciens)
     * Il doit ??tre connect?? et ??tre chef de projet sinon une exception de type BadUserError est lev??e
     * @throws IOException
     */
    @FXML
    public void viewTechs() throws IOException {
        try {
            if (!currentUser.isConnected()) {
                BadUserError badUserErr = new BadUserError();
                utilsIHM.afficherErreur(badUserErr.getMessage());
                throw new BadUserError();
            }
            else {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClass().getResource("/Application/Vue/techsScene/sceneTechs.fxml"));
                    sceneTechsController controller = new sceneTechsController(this);
                    fxmlLoader.setController(controller);
                    Pane tempPane = fxmlLoader.load();
                    container.setCenter(tempPane);
                    setBtnMenuIsActive(techsWindow); //colorie le bouton actif
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Ev??nement d??clench?? lorsque l'utilisateur demande ?? modifier son profil (clique sur Mon profil)
     * Il doit ??tre connect?? sinon un msg d'erreur lui est adress??
     * @throws IOException
     */
    @FXML
    public void editProfile() throws IOException, ClassNotFoundException, SQLException{
        if (!currentUser.isConnected()){
            BadUserError badUserErr = new BadUserError("Vous devez ??tre connect?? pour visualiser cette page");
            utilsIHM.afficherErreur(badUserErr.getMessage());
        }else {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Application/Vue/profilScene/sceneProfil.fxml"));
                SceneProfileController controller = new SceneProfileController(currentUser, this);
                fxmlLoader.setController(controller);
                Pane tempPane = fxmlLoader.load();
                container.setCenter(tempPane);
                setBtnMenuIsActive(profileWindow); //colorie le bouton actif
            } catch (IOException e) {
                utilsIHM.afficherErreur("Impossible de charger la page monProfil");
                e.printStackTrace();
            }catch (SQLException ex){
                utilsIHM.afficherErreur(ex.getLocalizedMessage());
                ex.printStackTrace();
            }
        }
    }
    
    /**
     * Quand l'utilisateur est pas connect?? il y a que la connexion qui est
     * visible
     */
    public void disableButtons(){
        if (!currentUser.isConnected()){
            boxMenu1.getChildren().clear();
            boxMenu1.getChildren().addAll(connectionButton);
        }
    }
    
    /**
     * Quand l'utilisateur est connect?? les boutons deviennent visibles
     * visible
     */
    public void enableButtons(){
        if (currentUser.isConnected()){
            boxMenu1.getChildren().clear();
            boxMenu1.getChildren().addAll(projectsWindow, customers, techsWindow, toolsWindow, spacerMenu, profileWindow, deconnectionButton);
        }
    }
    
    /**
     * Permet de colorier le bouton actif du menu 
     * d??colorie tous les boutons du menu non-actifs
     * @param button bouton du menu qui est press??
     */
    private void setBtnMenuIsActive(JFXButton button){
        for (int i = 0; i < boxMenu1.getChildren().size(); i++) {
            if(button == boxMenu1.getChildren().get(i)){
                //button.setStyle("-fx-background-color: #14202B");
                button.getStyleClass().add("menu-btn-selected");
            }
            else{
                boxMenu1.getChildren().get(i).getStyleClass().remove("menu-btn-selected");
                //boxMenu1.getChildren().get(i).setStyle("");
            }
        }   
    }    
}
