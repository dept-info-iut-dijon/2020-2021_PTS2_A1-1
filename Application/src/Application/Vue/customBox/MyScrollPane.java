/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Application.Vue.customBox;

import Application.Database.ProjectDao;
import Application.Metier.Project;
import Application.Metier.ProjectStatus;
import Application.Metier.Tech;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
/**
 *
 * @author David
 */
public class MyScrollPane extends ScrollPane{
    private MyStyle style;
    private ArrayList <MyCustomBox> listCustomBox = new ArrayList();
    
    public MyScrollPane(MyStyle style) {
        this.style = style;
        this.setPadding(new Insets(10,10,10,10)); //top, right, bottom, left 
    }
  
    public void findBox(MyCustomBox box) {
        for (int i = 0; i < this.listCustomBox.size(); i++) {
            this.listCustomBox.get(i).closeBox();
        }
    }

    public MyRowBox generateMainTechRow(Tech t) { 
        // Génération de la ligne du nom du technicien
        String techFullName = t.getFirstName()+" "+t.getLastName();
        MyRowBox mainTechRow = new MyRowBox(techFullName, this.style);           
        mainTechRow.generateLineBoxRow();
        return mainTechRow;
    }
      
    public MyRowBox generateMainProjectRow(Project p) { 
        // Génération de la ligne du nom du technicien
        String pName = p.getName();
        MyRowBox mainTechRow = new MyRowBox(pName, this.style);           
        mainTechRow.generateLineBoxRow();
        return mainTechRow;
    }
    
    public MyRowBox generateItemBoxTech(Tech tech) {
        // création du container d'Items
        MyRowBox itemTechRow = new MyRowBox("", this.style);

        // Génération des Item de la boite
        String totalSkills = String.valueOf(tech.GetSkills().size());
        String grade = tech.getGrade();
        String euro = Character.toString ((char) 8364);
        String cout = tech.getCoutHoraire() + euro + " /h";
        ArrayList<ItemVBox> itemBoxList = new ArrayList();         
        ItemVBox i1 = new ItemVBox("Compétences", totalSkills, this.style);
        ItemVBox i2 = new ItemVBox("Grade", grade , this.style);
        ItemVBox i3 = new ItemVBox("Coût horaire", cout , this.style);
        itemBoxList.add(i1);
        itemBoxList.add(i2);
        itemBoxList.add(i3);
        itemTechRow.generateItemVBoxRow(itemBoxList, Priority.NEVER); //ajout des items à la boite d'item

        //Génération d'un Item Bouton
        MyButtonTech btn = new MyButtonTech("Voir détails", tech, this.style);
        btn.addIconButton("CRAYON");
        itemTechRow.addButtonToRowBox(btn);
        return itemTechRow;
    }
    
    public MyRowBox generateItemBoxProject(Project p) {
        // création du container d'Items
        MyRowBox projetItems = new MyRowBox("", this.style);

        // Génération des Item de la boite
        String client = p.getName();
        String statut = "<statut>";
        statut = p.getStatusString();
        String dateCommande = "00/00/0000";
        String totalActivite = String.valueOf(p.getActivities().size());
        ArrayList<ItemVBox> itemBoxList = new ArrayList();
        ItemVBox i1 = new ItemVBox("Statut", statut, this.style);
        ItemVBox i2 = new ItemVBox("Client", client, this.style);
        ItemVBox i3 = new ItemVBox("Commande", dateCommande , this.style);          
        itemBoxList.add(i1);
        itemBoxList.add(i2);
        itemBoxList.add(i3);
        projetItems.generateItemVBoxRow(itemBoxList, Priority.NEVER); //ajout des items à la boite d'item
        
        AnchorPane AP1 = new AnchorPane();
        projetItems.getChildren().add(AP1);
        AP1.setPrefWidth(40);
       
        MyRowBox activityItems = new MyRowBox("", this.style);
        ArrayList<ItemVBox> itemBoxListActitvity = new ArrayList();
        ItemVBox ia1 = new ItemVBox("Activités", totalActivite, this.style);
        itemBoxListActitvity.add(ia1);
        activityItems.addSubItemBox(itemBoxListActitvity); //ajout des items à la boite d'item
        projetItems.getChildren().add(activityItems); 
        //activityItems.setPrefWidth(400);
        activityItems.setAlignment(Pos.CENTER_LEFT);
        AnchorPane AP2 = new AnchorPane();
        projetItems.getChildren().add(AP2);
        AP2.setPrefWidth(40);
        //Génération d'un Item Bouton
        MyButtonProject btn = new MyButtonProject("Voir détails", p, this.style);
        btn.addIconButton("CRAYON");
        projetItems.addButtonToRowBox(btn);
        HBox.setHgrow(activityItems, Priority.ALWAYS);
        return projetItems;
    } 
    
    public MyStyle getSPStyle() {
        return this.style;
    }
    
    public ArrayList getCustomBoxList() {
        return this.listCustomBox;
    }

}

