import javax.swing.*;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import static javax.swing.JOptionPane.showMessageDialog;

public class Notepad extends JFrame {
    public  Notepad(){
        this.setVisible(true);
        this.setSize(500,600);
        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        this.setBounds((Toolkit.getDefaultToolkit().getScreenSize().width-this.getWidth())/2,(Toolkit.getDefaultToolkit().getScreenSize().height-this.getHeight())/2,this.getWidth(),this.getHeight());
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                saveFileHandler();
                System.exit(0);
            }
        });



        this.setLayout(new GridLayout(1,1));
        this.getContentPane().add(scrollPane);


        initComponents();


    }

    private void initComponents(){

        this.setJMenuBar(menuBar);
        JMenu file= menuBar.add(new JMenu("File"));
        JMenu function = menuBar.add(new JMenu("Function"));

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


        JMenuItem findSeq = function.add(new JMenuItem("Find"));
        findSeq.setToolTipText("Find in text chars sequence ");
        findSeq.setAccelerator(KeyStroke.getKeyStroke("ctrl F"));
        findSeq.setMnemonic('F');
        function.addSeparator();
        JMenuItem replaceSeq = function.add(new JMenuItem("Find and Replace"));
        replaceSeq.setToolTipText("Find in text chars sequence and replace them");
        replaceSeq.setMnemonic('R');
        replaceSeq.setAccelerator(KeyStroke.getKeyStroke("ctrl R"));







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

        findSeq.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isFindSelected)
                    findSeqHandler();
            }
        });

        replaceSeq.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!isReplaceSelected)
                    replaceSeqHandler();
            }
        });



    }

    private void newFileHandler(){
        saveFileHandler();
        textArea1.setText("");
        this.setTitle("");
        this.path="";

    }

    private void openFileHandler(){
        if (!textArea1.getText().equals(""))
            newFileHandler();
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.isDirectory()||f.getName().toLowerCase().endsWith(".txt");
            }

            @Override
            public String getDescription() {
                return "Text document";
            }
        });
        int result = fileChooser.showOpenDialog(rootPane);
        RandomAccessFile raf = null;
        if (result==JFileChooser.APPROVE_OPTION){
            try{
                File file = new File(path=fileChooser.getSelectedFile().getAbsolutePath());
                String title = fileChooser.getName(file);
                title=title.replace(".txt","");
                this.setTitle(title);
                raf = new RandomAccessFile(file, "rwd");
                String line = "";
                while ((line=raf.readLine())!=null){
                    textArea1.setText(textArea1.getText()+line+"\n");

                }


            }catch (Exception e){
                System.out.println("Exception");

            }

        }

    }

    private void saveFileHandler(){
        int option = JOptionPane.showConfirmDialog(rootPane,"Do you want save changes?","Saving changes",JOptionPane.YES_NO_OPTION);
        if (option == 0) {
            if (path.equals("")) {
                saveAsFileHandler();
            } else {
               // path = getTitle()+".txt";
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
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.isDirectory()||f.getName().toLowerCase().endsWith(".txt");
            }

            @Override
            public String getDescription() {
                return ".txt";
            }
        });
        int result =  fileChooser.showSaveDialog(rootPane);
        path="";

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

    private void findSeqHandler(){
        isFindSelected=true;


        internalFrameFind = new JInternalFrame("Find",false,true);
        JPanel buttonPanel = new JPanel();
        JButton findButton = new JButton("Find");
        JLabel label = new JLabel("What are you looking for?");
        internalFrameFind.setVisible(true);
        internalFrameFind.setSize(new Dimension(200,140));
        internalFrameFind.setLayout(new GridLayout(3,1));

        textFind.setSize(new Dimension(100,7));
        buttonPanel.add(findButton);
        internalFrameFind.add(label);
        internalFrameFind.getContentPane().add(textFind);
        internalFrameFind.getContentPane().add(buttonPanel);


        findButton.addActionListener(new FindSequence());

        this.getContentPane().add(desktopPane);
        desktopPane.add(internalFrameFind);
        internalFrameFind.addInternalFrameListener(new InternalFrameAdapter() {
            @Override
            public void internalFrameClosed(InternalFrameEvent e) {
                if (!isReplaceSelected)
                    getContentPane().remove(desktopPane);
                isFindSelected=false;
                super.internalFrameClosed(e);
            }

        });
    }

    private void replaceSeqHandler(){
        isReplaceSelected=true;
        internalFrameReplace = new JInternalFrame("Find and Replace",false,true);
        JPanel buttonPanel = new JPanel();
        JButton findButton = new JButton("Replace");
        JLabel label = new JLabel("What do you want replace?");
        internalFrameReplace.setVisible(true);
        internalFrameReplace.setSize(new Dimension(200,200));
        internalFrameReplace.setLayout(new GridLayout(5,1));

        textFindReplace.setSize(new Dimension(100,7));
        buttonPanel.add(findButton);
        internalFrameReplace.add(label);
        internalFrameReplace.getContentPane().add(textFindReplace);
        internalFrameReplace.getContentPane().add(new JLabel("New sequence:"));
        internalFrameReplace.getContentPane().add(textReplace);
        internalFrameReplace.getContentPane().add(buttonPanel);


        findButton.addActionListener(new ReplaceSequence());

        this.getContentPane().add(desktopPane);
        desktopPane.add(internalFrameReplace);
        internalFrameReplace.addInternalFrameListener(new InternalFrameAdapter() {
            @Override
            public void internalFrameClosed(InternalFrameEvent e) {

                if (!isFindSelected)
                    getContentPane().remove(desktopPane);

                isReplaceSelected=false;

                super.internalFrameClosed(e);
            }
        });

    }

    private class FindSequence implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {



            if(firstIndex==-1||firstIndex==0)
                firstIndex = textArea1.getText().indexOf(textFind.getText());

            if (firstIndex>=0&&textFind.getText().length()>0){

                textArea1.requestFocus();
                textArea1.select(firstIndex,firstIndex+textFind.getText().length());
                firstIndex = textArea1.getText().indexOf(textFind.getText(),firstIndex+textFind.getText().length());
            }
        }
        private int firstIndex = 0;

    }

    private class ReplaceSequence implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {



            if(firstIndexReplace==-1||firstIndexReplace==0)
                firstIndexReplace = textArea1.getText().indexOf(textFindReplace.getText());

            if (firstIndexReplace>=0&&textFindReplace.getText().length()>0){

                textArea1.requestFocus();
                textArea1.select(firstIndexReplace,firstIndexReplace+textFindReplace.getText().length());
                textArea1.replaceSelection(textReplace.getText());
                firstIndexReplace = textArea1.getText().indexOf(textFindReplace.getText(),firstIndexReplace+textFindReplace.getText().length());
            }
        }
        private int firstIndexReplace = 0;

    }





    private JDesktopPane desktopPane = new JDesktopPane();
    private JTextArea textArea1 = new JTextArea();
    private  JScrollPane scrollPane = new JScrollPane(textArea1);
    private JMenuBar menuBar = new JMenuBar();
    private JTextField textFind = new JTextField();
    private JTextField textFindReplace = new JTextField();
    private JTextField textReplace = new JTextField();
    private  JInternalFrame internalFrameFind;
    private  JInternalFrame internalFrameReplace;
    private boolean isFindSelected = false;
    private boolean isReplaceSelected = false;
    private String path="";


    public static void main(String[] args) {
        new Notepad();
    }
}

