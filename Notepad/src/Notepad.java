import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import static javax.swing.JOptionPane.showMessageDialog;

public class Notepad extends JFrame {
    public  Notepad(){
        this.setVisible(true);
        this.setSize(500,600);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setBounds((Toolkit.getDefaultToolkit().getScreenSize().width-this.getWidth())/2,(Toolkit.getDefaultToolkit().getScreenSize().height-this.getHeight())/2,this.getWidth(),this.getHeight());



        initComponents();

        this.getContentPane().add(panel1);

    }

    private void initComponents(){

        this.setJMenuBar(menuBar);
        JMenu file= menuBar.add(new JMenu("File"));
        JMenuItem newFile= file.add(new JMenuItem("New"));
        newFile.setAccelerator(KeyStroke.getKeyStroke("ctrl N"));
        newFile.setMnemonic('N');
        file.addSeparator();
        JMenuItem openFile= file.add(new JMenuItem("Open"));
        openFile.setAccelerator(KeyStroke.getKeyStroke("ctrl O"));
        openFile.setMnemonic('O');
        file.addSeparator();
        JMenuItem saveFile =  file.add(new JMenuItem("Save"));
        saveFile.setAccelerator(KeyStroke.getKeyStroke("ctrl S"));
        saveFile.setMnemonic('S');
        file.addSeparator();
        JMenuItem saveAsFile =  file.add(new JMenuItem("Save as"));
        saveAsFile.setToolTipText("Save file on disc as txt file");
        file.addSeparator();
        JMenuItem closeFile =  file.add(new JMenuItem("Close file"));
        closeFile.setAccelerator(KeyStroke.getKeyStroke("ctrl C"));
        closeFile.setMnemonic('C');



        newFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                newFileHandler();
            }
        });

        openFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openFileHandler();
            }
        });

        saveFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveFileHandler();
            }
        });

        saveAsFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveAsFileHandler();
            }
        });

        closeFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                closeFileHandler();
            }
        });

        panel1.add(scrollPane);


    }

    private void newFileHandler(){
        saveFileHandler();
        textArea1.setText("");
        this.setTitle("");

    }

    private void openFileHandler(){
        if (!textArea1.getText().equals(""))
            newFileHandler();
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(null);
        RandomAccessFile raf = null;
        if (result==JFileChooser.APPROVE_OPTION){
            try{
                File file = new File(fileChooser.getSelectedFile().getAbsolutePath());
                String title = fileChooser.getName(file);
                title=title.replace(".txt","");
                this.setTitle(title);
                raf = new RandomAccessFile(file, "rwd");
                String line = "";
                while ((line=raf.readLine())!=null){
                    textArea1.setText(textArea1.getText()+line);

                }


            }catch (Exception e){
                System.out.println("Exception");

            }

        }

    }

    private void saveFileHandler(){
        int option = JOptionPane.showConfirmDialog(rootPane,"Do you want save changes?","Saving changes",JOptionPane.YES_NO_OPTION);
        if (option == 0) {
            if (this.getTitle().equals("")) {
                saveAsFileHandler();
            } else {
                String path = getTitle()+".txt";
                RandomAccessFile raf = null;
                File file = new File (path);

                try {
                    file.createNewFile();
                    raf = new RandomAccessFile(file,"rwd");
                    raf.writeBytes(textArea1.getText());
                    raf.close();

                } catch (Exception e){

                    System.out.println("Exception");
                }

            }
        }
    }

    private void saveAsFileHandler(){
        String text = textArea1.getText();
        RandomAccessFile raf = null;
        JFileChooser fileChooser = new JFileChooser();
        int result =  fileChooser.showSaveDialog(null);
        String path="";

        if (result==JFileChooser.APPROVE_OPTION){
            path =fileChooser.getSelectedFile().getAbsolutePath();
            String fileName = fileChooser.getSelectedFile().getName();
            this.setTitle(fileName);
            path+=".txt";
        }


        File file = new File(path);
        try{
            raf = new RandomAccessFile(file, "rwd");
            file.createNewFile();
            raf.writeBytes(text);
            raf.close();
        }
        catch (Exception e){
            System.out.println("IO Exception");
        }


    }

    private void closeFileHandler(){
       int option =  JOptionPane.showConfirmDialog(rootPane, "Are you sure?", "Closing file", JOptionPane.YES_NO_OPTION);
       if (option == 0){
           saveFileHandler();
           System.exit(0);}
    }

    private JPanel panel1;
    private JTextArea textArea1;
    private  JScrollPane scrollPane = new JScrollPane(textArea1);
    private JMenuBar menuBar = new JMenuBar();

    public static void main(String[] args) {
        new Notepad();
    }
}

