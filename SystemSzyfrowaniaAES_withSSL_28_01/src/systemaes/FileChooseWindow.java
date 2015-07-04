package systemaes;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class FileChooseWindow extends JFrame {
	
	
  @SuppressWarnings("unused")
private JTextField filename = new JTextField(), dir = new JTextField(), sciezka = new JTextField();
  private JLabel lblSciezkaDoPliku = new JLabel("Sciezka do pliku: ");

  
  
  private JButton open = new JButton("Open"), save = new JButton("Save");
  



  public FileChooseWindow() {
    
	
	JPanel p = new JPanel();
    open.addActionListener(new OpenL());
    p.add(open);
    save.addActionListener(new SaveL());
    p.add(save);
    Container cp = getContentPane();
    cp.add(p, BorderLayout.SOUTH);
    dir.setEditable(true);
    filename.setEditable(true);
    //sciezka.setEditable(true);
    p = new JPanel();
    p.setLayout(new GridLayout(3, 1));
    p.add(lblSciezkaDoPliku);
    p.add(filename);
    p.add(dir);
    //p.add();
    cp.add(p, BorderLayout.NORTH);
    
    
  }

  class OpenL implements ActionListener {
    
	  
	  
	  public void actionPerformed(ActionEvent e) {
      JFileChooser c = new JFileChooser();
      // Demonstrate "Open" dialog:
      int rVal = c.showOpenDialog(FileChooseWindow.this);
      if (rVal == JFileChooser.APPROVE_OPTION) {
        filename.setText(c.getSelectedFile().getName());
        dir.setText(c.getCurrentDirectory().toString());
        //sciezka.setText(c.getCurrentDirectory().toString() + c.getSelectedFile().getName());
        //System.out.println(sciezka);
        
      }
      if (rVal == JFileChooser.CANCEL_OPTION) {
        filename.setText("Wcisneles przycisk Cancel");
        dir.setText("");
      }
    }
  }

  class SaveL implements ActionListener  {
    public void actionPerformed(ActionEvent e) {
      //JFileChooser c = new JFileChooser();
      // Demonstrate "Save" dialog:
    	
      }
      
    }
  


  public static void run(JFrame frame, int width, int height) {
    
	  frame.setSize(width, height);
    
	  frame.setVisible(true);
  
  }
}