package daltonfinal;

import javax.swing.*;

public class ToolComboBox extends JComboBox{
    
    private String name;    //name field
    private String[] types; //types field
    
    //constructor for toolbox
    public ToolComboBox(String n, String[] l) {
        name = n;
        //model for combo box
        setModel(new DefaultComboBoxModel<String>() {
            private static final long serialVersionUID = 1L;
            boolean selectionAllowed = true;
            //allows for a default item that cant be reselected
            @Override
            public void setSelectedItem(Object o) {
                if(!name.equals(o)) {
                    super.setSelectedItem(o);
                }
                else if (selectionAllowed) {
                    selectionAllowed = false;
                    super.setSelectedItem(o);
                }
            }
        });
        //add the items for the combo box
        addItem(n);
        this.types = l;
        for (String i : l) {
            this.addItem(i);
        }
    }
    
    //getter for name
    @Override
    public String getName() {
        return name;
    }
       
}
