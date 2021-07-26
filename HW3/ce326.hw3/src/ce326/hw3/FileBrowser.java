/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ce326.hw3;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.RandomAccessFile;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
//import java.io.*;
import java.io.PrintWriter;
import javax.swing.*;
//import javax.swing.text.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
//import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.Collection;
//import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
//import java.util.logging.Level;
//import java.util.logging.Logger;
import java.util.regex.MatchResult;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
/* MenuLookDemo.java requires middle.gif. */
 
/*
 * This class exists solely to show you what menus look like.
 * It has no menu-related event handling.
 */
public class FileBrowser extends JFrame implements ListSelectionListener{
  JTextArea output;
  JScrollPane scrollPane;
  JScrollPane listScrollPane;
  JButton searchbutton;//tou ksexoristou panel
  JTextField searchfield;
  JMenuBar menuBar ;
  JPopupMenu popupm;
  JPopupMenu xpopup;
  JMenu fmenu;
  JMenu edmenu;
  JMenu searchbtn;//tou menu
  JMenuItem pmenuItem;
  JMenuItem pmenuItempopup;
  JMenuItem menuItem;
  JCheckBox canRead;
  JCheckBox canWrite;
  JCheckBox canEx;
  JLabel enteredFile;//to file pou einai panw tou o kersoras thn trexousa stigmh
  JLabel currentChosenFile;
  JLabel breadSeparator;
  JLabel labeltochangecolor;
  JPanel breadPanel;
  JPanel favPanel;
  JPanel workspPanel;
  JPanel searchPanel;
  JPanel searchandBreadPanel;
  JPanel searchPanel2;
  JPanel filesPanel;
  JPanel workspPanel2;
  ItemListener itemListener;
  String path;
  String name;
  String currentAbsPath;
  String favoritesPath;
  String substring;
  String xmlgroup;
  String initialLabel;
  String currenttoDelete;
  String icontype;
  String iconpath;//pou einai o fakelos me tis eikones
  String sizetype;
  File currentFile;
  File favoritesxml;
  File cf;//to file pou tha antigrapsw
  public LinkedList<String> bread;
  public LinkedList<String> namesList;
  long size; 
  boolean ex, rd, wr;
  boolean copyC;
  boolean searchedSomething;
  NodeList nList;
  DocumentBuilderFactory dbFactory;
  DocumentBuilder dBuilder;
  Document doc; 
  RandomAccessFile rf;
  public int offset;
  private JList<String> list;
  public Container container;
  private DefaultListModel<String> listModel;
  int startTagSize;
  float ypoloipo;
  Color labelBackgroundColor;
  Collection <String> uniques;
 
 public FileBrowser(){
        super();
        this.ex = false;
        this.rd = false;
        this.wr = false;
        copyC = false;
        cf = null;
        labeltochangecolor = new JLabel("***none***");
        icontype = null;
        searchedSomething = false;
        namesList = new LinkedList<String>();
        setMinimumSize(new Dimension(300,300));
        setPreferredSize(new Dimension(750, 600));
        setTitle("File Browser");
        setLayout(new BorderLayout(10,10));/*new GridLayout(1,2));*/
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10,10));
//         iconpath = System.getProperty("user.dir")+System.getProperty("file.separator")
//                +"src"+System.getProperty("file.separator")
//                +"ce326"+System.getProperty("file.separator")+"hw3";
        favPanel = new JPanel();
        favPanel.setLayout(new BoxLayout(favPanel, BoxLayout.Y_AXIS));
        JLabel fl = new JLabel("$>Your Favorite Files :)");
        fl.setForeground(Color.GREEN);//"<html><font color='red'>red</font></html>"
        fl.setFont(new Font("Courier New", Font.BOLD, 14));
        favPanel.add(fl);
        favPanel.add(Box.createVerticalStrut(15));
        favPanel.setBackground(Color.BLACK);
        breadSeparator = new JLabel(">");
        add(favPanel,BorderLayout.WEST);
        searchfield = new JTextField(15);
        searchfield.setSize(5,7);
        searchfield.setMaximumSize( 
        new Dimension(Integer.MAX_VALUE, searchfield.getPreferredSize().height) );
         searchbutton = new JButton("Search");
         //searchbutton.setForeground(Color.WHITE);
         searchbutton.setFont(new Font("Courier New", Font.BOLD, 12));
         SearchListener searchListener = new SearchListener(searchbutton);
        searchbutton.setActionCommand("Search");
        searchbutton.addActionListener(searchListener);
        searchbutton.setVisible(false);
        searchfield.addActionListener(searchListener);
        searchfield.getDocument().addDocumentListener(searchListener);
        searchfield.setVisible(false);
         searchPanel = new JPanel();
        searchPanel.setLayout(/*new FlowLayout()*/new BoxLayout(searchPanel, BoxLayout.LINE_AXIS));
        searchPanel.setBackground(Color.GREEN);
        searchPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        //searchPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(Box.createHorizontalBox());
        searchPanel.add(searchfield);
//        searchPanel.add(Box.createHorizontalBox());
//        searchPanel.add(Box.createHorizontalStrut(10));
        searchPanel.add(searchbutton);
             
        breadPanel = new JPanel();
        breadPanel.setLayout(new WrapLayout(FlowLayout.LEADING));//new BoxLayout(breadPanel, BoxLayout.X_AXIS));
        breadPanel.setBackground(Color.BLACK);
        
         filesPanel = new JPanel();
         //To wraplayout allazei dynamika grammh sto panel otan gemisei,
         //ypologizontas dynamika to trexwn prefferedsize tou
         //O theos na ton exei kala ton anthrwpo pou to skeftike
        filesPanel.setLayout(new WrapLayout(FlowLayout.LEADING));//FlowLayout(FlowLayout.LEADING));
        filesPanel.setMaximumSize(filesPanel.getPreferredSize()); 
        filesPanel.setBackground(Color.BLACK);
//        filesPanel.setMinimumSize(filesPanel.getPreferredSize());
        workspPanel = new JPanel();
        workspPanel.setLayout(new BoxLayout(workspPanel, BoxLayout.Y_AXIS));//new FlowLayout());
        workspPanel.setBackground(Color.GREEN);
        currentAbsPath = System.getProperty("user.home");
        currentFile = new File(currentAbsPath);
        
        
        scrollPane = new JScrollPane(filesPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setBackground(Color.BLACK);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(new Dimension(200, 600));
//        filesPanel.setMaximumSize(filesPanel.getPreferredSize()); 
//        filesPanel.setMinimumSize(filesPanel.getPreferredSize());
        //scrollPane.setPreferredSize(new Dimension(200, 200));
        workspPanel.add(searchPanel);
        workspPanel.add(breadPanel);
        workspPanel.add(scrollPane, BorderLayout.CENTER);
        add(workspPanel, BorderLayout.CENTER);
        setBackground(Color.DARK_GRAY);
        xpopup = new JPopupMenu();
        uniques = new HashSet<String>();
        initialLabel = "*****initialLabel*********";
        enteredFile = new JLabel(initialLabel);
        this.substring = "";
//        breadPanel = new JPanel();
//        breadPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));//new BoxLayout(breadPanel, BoxLayout.X_AXIS));
        path = null;
        
        //ActionListener listListener = new ListSelectionListener();
        listModel = new DefaultListModel<String>();
        list = new JList<>(listModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setLayoutOrientation(JList.VERTICAL);
        list.setSelectedIndex(0);
        list.addListSelectionListener(this);
        list.setVisibleRowCount(7);
        list.setMaximumSize(new Dimension(list.getPreferredSize()));
        listScrollPane = new JScrollPane(list);
        listScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        listScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        listScrollPane.setPreferredSize(new Dimension(200, 600));
        favoritesPath = System.getProperty("user.home") + System.getProperty("file.separator")
                +".java-file-browser";
        //System.out.println("home "+System.getProperty("user.home")+" and file-ava-directory "+favoritesPath);
        favoritesxml = new File(favoritesPath+System.getProperty("file.separator")+"properties.xml");
       try{
           File temp = new File(favoritesPath);
            if(!temp.exists()){
                temp.mkdir();
            }
            if(!favoritesxml.exists()){
                PrintWriter lwr = new PrintWriter(favoritesxml);
               lwr.print("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<favorites>\n</favorites>");
               lwr.close();
                findOffset();
            }
            else{
                if(favoritesxml.length() == 0){try ( //an einai keno
                    PrintWriter lwr = new PrintWriter(favoritesxml)) {
                    lwr.print("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<favorites>\n</favorites>");
                    lwr.close();
                    }
                }
                findOffset();
                LoadXML();
            }
       }catch(IOException io){
           io.printStackTrace(); 
       }
        createPopupMenuXML();
        setJMenuBar(createMenuBar());
        CreateWorkspace(CurrentDirList(currentAbsPath));
        
        bread = new LinkedList<String>();
        
        Breadcrumb(System.getProperty("user.home"));//initialise breadcrumb with home dir
        //tha to allaksw wste na einai prosarmosimo
        
        searchPanel2 = new JPanel();
        searchPanel2.setLayout(new GridBagLayout());
        
        //GridBagLayout gridbag = (GridBagLayout)getLayout();
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 0;
        c.weightx = 1.0;
        c.fill = GridBagConstraints.HORIZONTAL;
        //add.setConstraints(searchPanel, c);
        //searchPanel2.add(searchPanel,c);
        itemListener = new ItemListener() {
        /* Listens to the check boxes. */
        @Override
        public void itemStateChanged(ItemEvent e) {
        Object source = e.getItemSelectable();

        if (source == canRead) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
               currentFile.setReadable(true);
               rd = true;
            }
            else{
                currentFile.setReadable(false);
                rd = false;
            }
            
        }
        else if (source == canWrite) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
               currentFile.setWritable(true);
               wr = true;
            }
            else{
                currentFile.setWritable(false);
                wr = false;
            }
        }
        else if (source == canEx) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
               currentFile.setExecutable(true);
               ex = true;
            }
            else{
                currentFile.setExecutable(false);
                ex = false;
            }
            
        }
        }
      };
        pack();
 }

 //Gia th lista tou search
    @Override
    public void valueChanged(ListSelectionEvent lse) {
    if (lse.getValueIsAdjusting() == false) {
 
      if (list.getSelectedIndex() == -1) {
      //No selection
        
 
      } else {
      //Selection
        String searchpath = listModel.getElementAt(lse.getFirstIndex());
        workspPanel.removeAll();
        workspPanel.add(searchPanel);
        workspPanel.add(breadPanel);
        workspPanel.add(scrollPane, BorderLayout.CENTER);
        
        breadPanel.revalidate();
        filesPanel.revalidate();
        favPanel.revalidate();
        scrollPane.revalidate();
        workspPanel.repaint();
        revalidate();
        breadPanel.repaint();
        filesPanel.repaint();
        favPanel.repaint();
        scrollPane.repaint();
        workspPanel.repaint();
        repaint();
        OpenFile(searchpath, new File(searchpath));
        
      }
    }
  }
    

 class PopupListener extends MouseAdapter {
    JPopupMenu tpopup;
    String str;
    //constructor for the favorites buttons
    PopupListener(JPopupMenu popupMenu, String pathtodelete) {
      tpopup = popupMenu;
      str = pathtodelete;
    }
    //constructor for the file labels in the workspace
    
    public void mousePressed(MouseEvent e) {
      maybeShowPopup(e);
    }
 
    public void mouseReleased(MouseEvent e) {
      maybeShowPopup(e);
        
      breadPanel.revalidate();
      filesPanel.revalidate();
      favPanel.revalidate();
      scrollPane.revalidate();
      workspPanel.repaint();
      revalidate();
      breadPanel.repaint();
      filesPanel.repaint();
      favPanel.repaint();
      scrollPane.repaint();
      workspPanel.repaint();
      repaint();
    }
 
    private void maybeShowPopup(MouseEvent e) {
      if (e.isPopupTrigger()) {
        tpopup.show(e.getComponent(),
               e.getX(), e.getY());
        
        currenttoDelete = str;
      
      }
    }
  }
 
 class SearchListener implements ActionListener, DocumentListener{
    public SearchListener(JButton btn){
        searchbutton = btn;
    }
    @Override
    public void actionPerformed(ActionEvent se){
        if("Search".equals(se.getActionCommand())){
            searchfield.requestFocusInWindow();
            searchfield.selectAll();
            searchbutton.setActionCommand("Stop");
            Search(searchfield.getText());
            searchbutton.setActionCommand("Search");
        }
        else if ("Stop".equals(se.getActionCommand())){
            displaySearchResults();
            searchbutton.setActionCommand("Search");
        }
    }

        @Override
        public void insertUpdate(DocumentEvent de) {
            
        }

        @Override
        public void removeUpdate(DocumentEvent de) {
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void changedUpdate(DocumentEvent de) {
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    }
  public JMenuBar createMenuBar() {
        
        menuBar = new JMenuBar();
        fmenu = new JMenu("File");
        popupm = new JPopupMenu();
        fmenu.setMnemonic(KeyEvent.VK_F);
        menuBar.add(fmenu);
    /////FOR THE POPUP MENU/////////////////////////////////////////
    
    
    //FOR THE MENU BAR//////////////////////////////////////////////
    menuItem = new JMenuItem("New Window",KeyEvent.VK_N);
    menuItem.setAccelerator(KeyStroke.getKeyStroke(
            KeyEvent.VK_N, ActionEvent.ALT_MASK));
    menuItem.addActionListener(new ActionListener(){
      @Override
      public void actionPerformed(ActionEvent e){
           javax.swing.SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        createAndShowGUI();
      }
        });
      }
    }
    );
    fmenu.add(menuItem);
    menuItem = new JMenuItem("Exit",KeyEvent.VK_E);
    menuItem.setAccelerator(KeyStroke.getKeyStroke(
            KeyEvent.VK_E, ActionEvent.ALT_MASK));
    menuItem.addActionListener(new ActionListener(){
      @Override
      public void actionPerformed(ActionEvent e){
         //find active window first
         Window window = KeyboardFocusManager.getCurrentKeyboardFocusManager().getActiveWindow();
        	if (window != null)
		{
                WindowEvent windowClosing = new WindowEvent(window, WindowEvent.WINDOW_CLOSING);
        	window.dispatchEvent(windowClosing);
                }
        }
    }
    );
    fmenu.add(menuItem);
    
    edmenu = new JMenu("Edit");
    edmenu.setEnabled(false);
    //edmenu.setMnemonic(KeyEvent.VK_E);
    menuBar.add(edmenu);
    
    ActionListener copyListener = new ActionListener(){
       @Override
        public void actionPerformed(ActionEvent ev){
            if(null != ev.getActionCommand())switch (ev.getActionCommand()) {
                case "copy":
                    pmenuItempopup.setEnabled(true);
                    pmenuItem.setEnabled(true);
                    if(currentAbsPath.contains(System.getProperty("file.separator"))){
                        int indx = currentAbsPath.lastIndexOf(System.getProperty("file.separator"));
                        if(indx ==currentAbsPath.length() - 1 && indx !=0){
                            currentAbsPath = currentAbsPath.substring(0, indx);
                        }
                    }
                    cf = new File(currentAbsPath +System.getProperty("file.separator")+currentChosenFile.getText());                    
                    copyC = true;
                    break;
                case "cut":
                    pmenuItempopup.setEnabled(true);
                    pmenuItem.setEnabled(true);
                    if(currentAbsPath.contains(System.getProperty("file.separator"))){
                        int indx = currentAbsPath.lastIndexOf(System.getProperty("file.separator"));
                        if(indx ==currentAbsPath.length() - 1 && indx !=0){
                            currentAbsPath = currentAbsPath.substring(0, indx);
                        }
                    }
                    cf = new File(currentAbsPath +System.getProperty("file.separator")+currentChosenFile.getText());  
                    //copyParentPath = (currentFile.getParentFile()).getAbsolutePath();
                    copyC = false;
                    break;
                case "paste":
                    //copies file
                    if(currentAbsPath.contains(System.getProperty("file.separator"))){
                        int indx = currentAbsPath.lastIndexOf(System.getProperty("file.separator"));
                        if(indx ==currentAbsPath.length() - 1 && indx!=0){
                            currentAbsPath = currentAbsPath.substring(0, indx);
                        }
                    }
                    File copyDest = new File(currentAbsPath + System.getProperty("file.separator")+ currentChosenFile.getText());
                    File newfile = new File( copyDest.getAbsolutePath()+ System.getProperty("file.separator")+cf.getName());
                        if(newfile.exists()){
                            int answer = JOptionPane.showConfirmDialog(workspPanel,
                                    "A file with same name already exists!\n"
                                            + "Are you sure you want to overwrite?",
                                    "Warning",
                                    JOptionPane.YES_NO_OPTION);
                            if(answer != JOptionPane.YES_OPTION){
                                //return;
                            }
                            else{
                                if( CheckFileType(newfile)== "dir" ){
                                    recursiveDelete(newfile.getAbsolutePath());
                                }
                                newfile.delete();
                            }
                        }
                        Path pth = copyDest.toPath();
                        Path sourcepth = cf.toPath();
                        if(CheckFileType(cf) == "dir" ){
                            if(cf.list().length>0){
                                    File nfile = new File(copyDest + System.getProperty("file.separator")+cf.getName());
                                    nfile.mkdir();
                                    copyDest = new File(nfile.getAbsolutePath());
                            }
                            recursiveCopy(cf.getAbsolutePath(), copyDest.getAbsolutePath());
                        }
                        else if(!cf.isDirectory()){
                            try {
                                Files.copy(sourcepth, pth.resolve(sourcepth.getFileName()), REPLACE_EXISTING);
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        }
                        if(copyC == false){
                            if(cf.isDirectory()){
                            //an exw kanei cut
                                if(DirSize(cf)>0){
                                    recursiveDelete(cf.getAbsolutePath());
                                }
                                cf.delete();
                            }
                            else{
                                cf.delete();
                            }
                        }
                    Breadcrumb(currentAbsPath);
                    CreateWorkspace(CurrentDirList(currentAbsPath)); 
                    pmenuItempopup.setEnabled(false);
                    pmenuItem.setEnabled(false);
                    
                    break;
                    default:
                    break;
            }
        }
    };
    ActionListener popupcopyListener = new ActionListener(){
        @Override
        public void actionPerformed(ActionEvent ev){
            if(null != ev.getActionCommand())switch (ev.getActionCommand()) {
                case "copy":
                    pmenuItempopup.setEnabled(true);
                    pmenuItem.setEnabled(true);
                    if(currentAbsPath.contains(System.getProperty("file.separator"))){
                        int indx = currentAbsPath.lastIndexOf(System.getProperty("file.separator"));
                        if(indx ==currentAbsPath.length() - 1&& indx !=0){
                            currentAbsPath = currentAbsPath.substring(0, indx);
                        }
                    }
                    cf = new File(currentAbsPath +System.getProperty("file.separator")+currentChosenFile.getText());                    
                    copyC = true;
                    break;
                case "cut":
                    pmenuItempopup.setEnabled(true);
                    pmenuItem.setEnabled(true);
                    if(currentAbsPath.contains(System.getProperty("file.separator"))){
                        int indx = currentAbsPath.lastIndexOf(System.getProperty("file.separator"));
                        if(indx ==currentAbsPath.length() - 1 && indx !=0){
                            currentAbsPath = currentAbsPath.substring(0, indx);
                        }
                    }
                    cf = new File(currentAbsPath +System.getProperty("file.separator")+currentChosenFile.getText());  
                    //copyParentPath = (currentFile.getParentFile()).getAbsolutePath();
                    copyC = false;
                    break;
                case "paste":
                    //copies file
                    if(currentAbsPath.contains(System.getProperty("file.separator"))){
                        int indx = currentAbsPath.lastIndexOf(System.getProperty("file.separator"));
                        if(indx ==currentAbsPath.length() - 1 && indx!=0){
                            currentAbsPath = currentAbsPath.substring(0, indx);
                        }
                    }
                    File copyDest = new File(currentAbsPath + System.getProperty("file.separator")+ currentChosenFile.getText());
                    File newfile = new File( copyDest.getAbsolutePath()+ System.getProperty("file.separator")+cf.getName());
                        if(newfile.exists()){
                            int answer = JOptionPane.showConfirmDialog(workspPanel,
                                    "A file with same name already exists!\n"
                                            + "Are you sure you want to overwrite?",
                                    "Warning",
                                    JOptionPane.YES_NO_OPTION);
                            if(answer != JOptionPane.YES_OPTION){
                                //return;
                            }
                            else{
                                if( CheckFileType(newfile)== "dir" && DirSize(newfile)>0){
                                    recursiveDelete(newfile.getAbsolutePath());
                                }
                                newfile.delete();
                            }
                        }
                        Path pth = copyDest.toPath();
                        Path sourcepth = cf.toPath();
                        if(CheckFileType(cf) == "dir" ){
                            if(cf.list().length>0){
                                    File nfile = new File(copyDest + System.getProperty("file.separator")+cf.getName());
                                    nfile.mkdir();
                                    copyDest = new File(nfile.getAbsolutePath());
                            }
                            recursiveCopy(cf.getAbsolutePath(), copyDest.getAbsolutePath());
                        }
                        else if(!cf.isDirectory()){
                            try {
                                Files.copy(sourcepth, pth.resolve(sourcepth.getFileName()), REPLACE_EXISTING);
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        }
                        if(copyC == false){
                            if(cf.isDirectory()){
                            //an exw kanei cut
                                if(DirSize(cf)>0){
                                    recursiveDelete(cf.getAbsolutePath());
                                }
                                cf.delete();
                            }
                            else{
                                cf.delete();
                            }
                        }
                    Breadcrumb(currentAbsPath);
                    CreateWorkspace(CurrentDirList(currentAbsPath)); 
                    pmenuItempopup.setEnabled(false);
                    pmenuItem.setEnabled(false);
                    
                    break;
                    default:
                    break;
            }
        }
    };
    JMenuItem edmenuItem = new JMenuItem("Cut",KeyEvent.VK_X);
    edmenuItem.setAccelerator(KeyStroke.getKeyStroke(
            KeyEvent.VK_X, ActionEvent.CTRL_MASK));
    edmenuItem.setActionCommand("cut");
    edmenuItem.addActionListener(copyListener);
    edmenu.add(edmenuItem);
    edmenuItem = new JMenuItem("Copy",KeyEvent.VK_C);
    edmenuItem.setAccelerator(KeyStroke.getKeyStroke(
            KeyEvent.VK_C, ActionEvent.CTRL_MASK));
    edmenuItem.setActionCommand("copy");
    edmenuItem.addActionListener(copyListener);
    edmenu.add(edmenuItem);
    pmenuItem = new JMenuItem("Paste",KeyEvent.VK_V);
    pmenuItem.setEnabled(false);
    pmenuItem.setAccelerator(KeyStroke.getKeyStroke(
            KeyEvent.VK_V, ActionEvent.CTRL_MASK));
    pmenuItem.setActionCommand("paste");
    pmenuItem.addActionListener(copyListener);
    edmenu.add(pmenuItem);
    
    edmenuItem = new JMenuItem("Cut",KeyEvent.VK_X);
    edmenuItem.setAccelerator(KeyStroke.getKeyStroke(
            KeyEvent.VK_X, ActionEvent.CTRL_MASK));
    edmenuItem.setActionCommand("cut");
    edmenuItem.addActionListener(popupcopyListener);
    popupm.add(edmenuItem);
    edmenuItem = new JMenuItem("Copy",KeyEvent.VK_C);
    edmenuItem.setAccelerator(KeyStroke.getKeyStroke(
            KeyEvent.VK_C, ActionEvent.CTRL_MASK));
    edmenuItem.setActionCommand("copy");
    edmenuItem.addActionListener(popupcopyListener);
    popupm.add(edmenuItem);
    pmenuItempopup = new JMenuItem("Paste",KeyEvent.VK_V);
    pmenuItempopup.setEnabled(false);
    pmenuItempopup.setAccelerator(KeyStroke.getKeyStroke(
            KeyEvent.VK_V, ActionEvent.CTRL_MASK));
    pmenuItempopup.setActionCommand("paste");
    pmenuItempopup.addActionListener(popupcopyListener);
    popupm.add(pmenuItempopup);
    
    edmenuItem = new JMenuItem("Delete",KeyEvent.VK_V);
    edmenuItem.setAccelerator(KeyStroke.getKeyStroke(
            KeyEvent.VK_D, ActionEvent.CTRL_MASK));
    edmenuItem.addActionListener(new ActionListener(){
      @Override
      public void actionPerformed(ActionEvent e){
            int answer = JOptionPane.showConfirmDialog(
                            menuBar, "File will be deleted!\n"
                                    + "Are you sure you want to procede?",
                            "Warning",
                            JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);
            if(answer != JOptionPane.YES_OPTION){
               return;
            }
            else{
                if(currentAbsPath.contains(System.getProperty("file.separator"))){
                    int indx = currentAbsPath.lastIndexOf(System.getProperty("file.separator"));
                    if(indx ==currentAbsPath.length() -1 && indx!=0){
                        currentAbsPath = currentAbsPath.substring(0, indx);
                    }
                }
                File file2 = new File(currentAbsPath +System.getProperty("file.separator")+currentChosenFile.getText());
                if( CheckFileType(file2)== "dir" && DirSize(file2)>0){
                    recursiveDelete(file2.getAbsolutePath());
                }
                file2.delete();
                Breadcrumb(currentAbsPath);
                CreateWorkspace(CurrentDirList(currentAbsPath));   
            }
      }
    }
    );
    edmenu.add(edmenuItem);
    edmenuItem = new JMenuItem("Delete",KeyEvent.VK_V);
    edmenuItem.setAccelerator(KeyStroke.getKeyStroke(
            KeyEvent.VK_D, ActionEvent.CTRL_MASK));
    edmenuItem.addActionListener(new ActionListener(){
      @Override
      public void actionPerformed(ActionEvent e){
            int answer = JOptionPane.showConfirmDialog(
                            menuBar, "File will be deleted!\n"
                                    + " Are you sure you want to procede?",
                            "Warning",
                            JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);
            if(answer != JOptionPane.YES_OPTION){
                return;
            }
            else{
                if(currentAbsPath.contains(System.getProperty("file.separator"))){
                    int indx = currentAbsPath.lastIndexOf(System.getProperty("file.separator"));
                    if(indx ==currentAbsPath.length()-1&& indx !=0){
                        currentAbsPath = currentAbsPath.substring(0, indx);
                    }
                }
                File file2 = new File(currentAbsPath +System.getProperty("file.separator")+currentChosenFile.getText());
                if( CheckFileType(file2)== "dir" && DirSize(file2)>0){
                    recursiveDelete(file2.getAbsolutePath());
                }
                file2.delete();
                Breadcrumb(currentAbsPath);
                CreateWorkspace(CurrentDirList(currentAbsPath));   
            }
      }
    }
    );
    edmenu.add(edmenuItem);
    popupm.add(edmenuItem);
    edmenuItem = new JMenuItem("Rename",KeyEvent.VK_R);
//    edmenuItem.setAccelerator(KeyStroke.getKeyStroke(
//            KeyEvent.VK_R, ActionEvent.ALT_MASK));
    edmenuItem.addActionListener(new ActionListener(){
      @Override
      public void actionPerformed(ActionEvent e){
          
        String newname = JOptionPane.showInputDialog( "Rename file to:" , "Rename File");
        if(currentAbsPath.contains(System.getProperty("file.separator"))){
            int indx = currentAbsPath.lastIndexOf(System.getProperty("file.separator"));
            if(indx ==currentAbsPath.length()-1 && indx !=0){
                currentAbsPath = currentAbsPath.substring(0, indx);
            }
        }
        File file2 = new File(currentAbsPath+System.getProperty("file.separator") + newname);
        if (file2.exists()){
            int answer = JOptionPane.showConfirmDialog(workspPanel,
                "A file with name " +"\""+newname+"\""+" already exists!\n"
                    + "Are you sure you want to overwrite?",
                "Warning",
                JOptionPane.YES_NO_OPTION);
            if(answer != JOptionPane.YES_OPTION){
              //return;
            }
            else{
                //an to arxeio einai non empty directory, tote to move tou files apotygxanei
                try {
                    if( CheckFileType(file2)== "dir" && DirSize(file2)>0){
                         recursiveDelete(file2.getAbsolutePath());
                    }
                    Path pth = currentFile.toPath();
                    Files.move(pth, pth.resolveSibling(newname), REPLACE_EXISTING);
                        //currentFile.re renameTo(new File(newname));
                    Breadcrumb(currentAbsPath);
                    CreateWorkspace(CurrentDirList(currentAbsPath));   
                }catch (IOException ex) {
                    Logger.getLogger(FileBrowser.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        else{
            try{
            Path pth = currentFile.toPath();
            Files.move(pth, pth.resolveSibling(newname), REPLACE_EXISTING);
                        //currentFile.re renameTo(new File(newname));
            Breadcrumb(currentAbsPath);
            CreateWorkspace(CurrentDirList(currentAbsPath));   
            }catch(IOException rfe){
                rfe.printStackTrace();
            }
        }
      }
    }
    );
    edmenu.add(edmenuItem);
    edmenuItem = new JMenuItem("Rename",KeyEvent.VK_R);
//    edmenuItem.setAccelerator(KeyStroke.getKeyStroke(
//            KeyEvent.VK_R, ActionEvent.ALT_MASK));
    edmenuItem.addActionListener(new ActionListener(){
      @Override
      public void actionPerformed(ActionEvent e){
          
        String newname = JOptionPane.showInputDialog( "Rename file to:" , "Rename File");
        if(currentAbsPath.contains(System.getProperty("file.separator"))){
            int indx = currentAbsPath.lastIndexOf(System.getProperty("file.separator"));
            if(indx ==currentAbsPath.length()-1 && indx !=0 ){
                currentAbsPath = currentAbsPath.substring(0, indx);
            }
        }
        File file2 = new File(currentAbsPath+System.getProperty("file.separator") + newname);
        if (file2.exists()){
            int answer = JOptionPane.showConfirmDialog(workspPanel,
                "A file with name " +"\""+newname+"\""+" already exists!\n"
                    + "Are you sure you want to overwrite?",
                "Warning",
                JOptionPane.YES_NO_OPTION);
            if(answer != JOptionPane.YES_OPTION){
              //return;
            }
            else{
                //an to arxeio einai non empty directory, tote to move tou files apotygxanei
                try {
                    if( CheckFileType(file2)== "dir" && DirSize(file2)>0){
                         recursiveDelete(file2.getAbsolutePath());
                    }
                    Path pth = currentFile.toPath();
                    Files.move(pth, pth.resolveSibling("newname"), REPLACE_EXISTING);
                        //currentFile.re renameTo(new File(newname));
                    Breadcrumb(currentAbsPath);
                    CreateWorkspace(CurrentDirList(currentAbsPath));   
                }catch (IOException ex) {
                    Logger.getLogger(FileBrowser.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        else{
            try{
            Path pth = currentFile.toPath();
            Files.move(pth, pth.resolveSibling(newname), REPLACE_EXISTING);
                        //currentFile.re renameTo(new File(newname));
            Breadcrumb(currentAbsPath);
            CreateWorkspace(CurrentDirList(currentAbsPath));   
            }catch(IOException rfe){
                rfe.printStackTrace();
            }
        }
      }
    }
    );
    popupm.add(edmenuItem);
    edmenuItem = new JMenuItem("Add to Favourites");
//    edmenuItem.setAccelerator(KeyStroke.getKeyStroke(
//            KeyEvent.VK_V, ActionEvent.ALT_MASK));
    edmenuItem.addActionListener(new ActionListener(){
      @Override
      public void actionPerformed(ActionEvent e){
          AddXMLEntry(currentFile.getAbsolutePath());
      }
    }
    );
    edmenu.add(edmenuItem);
    edmenuItem = new JMenuItem("Add to Favourites");
//    edmenuItem.setAccelerator(KeyStroke.getKeyStroke(
//            KeyEvent.VK_V, ActionEvent.ALT_MASK));
    edmenuItem.addActionListener(new ActionListener(){
      @Override
      public void actionPerformed(ActionEvent e){
          AddXMLEntry(currentFile.getAbsolutePath());
      }
    }
    );
    popupm.add(edmenuItem);
    edmenuItem = new JMenuItem("Properties");
//    edmenuItem.setAccelerator(KeyStroke.getKeyStroke(
//            KeyEvent.VK_V, ActionEvent.ALT_MASK));
    edmenuItem.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
          if("***currentdir***".equals(currentChosenFile.getText())){//an den exei epilexthei kapoio file
              //dinei properties tou current directory
             //.....................
             File curdir = new File(currentAbsPath);
             name = curdir.getName();
             path = curdir.getAbsolutePath();
             size = DirSize(new File(currentAbsPath));
             if(size >= 1024){
                 int kb = (int) (size/1024);//metra kilobytes
                 int mkb = (int) (size%1024); 
                 size = kb;
                 ypoloipo =(float) mkb/1024;
                 sizetype = "KB";
                 if(kb >= 1024 ){
                     int mb = (int) (kb/1024);//metra megabytes
                     int mmb = (int) (kb%1024);
                     size = mb;
                     ypoloipo =(float) mmb/1024;
                     sizetype = "MB";
                     if(mb >= 1024 ){
                        int gb = (int) (mb/1024);//metra gigabytes
                        int mgb = (int) (mb%1024);
                        size = gb;
                        ypoloipo =(float) mgb/1024;
                        sizetype = "GB";
                        if(gb >= 1024 ){
                            int tb = (int) (gb/1024);//metra gigabytes
                            int mtb = (int) (gb%1024); 
                            size = tb;
                            ypoloipo =(float) mtb/1024;
                            sizetype = "TB";
                         }
                     }
                 }
                 
             }
             File cdir = new File(currentAbsPath);
             if( cdir.canExecute() ){ ex = true;}
             if(cdir.canRead() ){ rd = true;}
             if(cdir.canWrite()){ wr = true;}
          }
          else{
              // dinei properties tou epilegmenou file
              //.......................
              //File curdir = new File(".");
              name = currentFile.getName();
              path = currentFile.getAbsolutePath();
              //+ System.getProperty("file.separator")+currentChosenFile.getText();
              //File curfile = new File(path);
              size = DirSize(currentFile);
              if(size >= 1024){
                 int kb = (int) (size/1024);//metra kilobytes
                 int mkb = (int) (size%1024); 
                 size = kb;
                 ypoloipo =(float) mkb/1024;
                 sizetype = "KB";
                 if(kb >= 1024 ){
                     int mb = (int) (kb/1024);//metra megabytes
                     int mmb = (int) (kb%1024);
                     size = mb;
                     ypoloipo = (float)mmb/1024;
                     sizetype = "MB";
                     if(mb >= 1024 ){
                        int gb = (int) (mb/1024);//metra gigabytes
                        int mgb = (int) (mb%1024);
                        size = gb;
                        ypoloipo = (float)mgb/1024;
                        sizetype = "GB";
                        if(gb >= 1024 ){
                            int tb = (int) (gb/1024);//metra gigabytes
                            int mtb = (int) (gb%1024); 
                            size = tb;
                            ypoloipo = (float)mtb/1024;
                            sizetype = "TB";
                         }
                     }
                 }
                 
             }
                if( currentFile.canExecute() ){
                    ex = true;}
                if(currentFile.canRead() ){ 
                    rd = true;}
                if(currentFile.canWrite()){ 
                    wr = true;}
          }
          String namestr = "Name: "+name;
          String pathstr = "Path: "+path;
          String s = String.format ("%.3f", size + ypoloipo);
          String sizestr = "Size: "+s+ sizetype;
          
          
          canRead = new JCheckBox("Can read");
          canWrite = new JCheckBox("Can write");
          canEx = new JCheckBox("Can execute");
          if(CheckFileOwner(currentFile) == false){
             canRead.setEnabled(false);
             canWrite.setEnabled(false);
             canEx.setEnabled(false);
          }
          canRead.setSelected(rd);
          canWrite.setSelected(wr);
          canEx.setSelected(ex);
          
          
          canRead.addItemListener(itemListener);
          canWrite.addItemListener(itemListener);
          canEx.addItemListener(itemListener);
          //popupm.add(cmenuItem);
        Object[] inputs = {namestr,pathstr,sizestr,canRead, canWrite, canEx};
        int result = JOptionPane.showConfirmDialog(currentChosenFile, inputs,
                "Properties", JOptionPane.PLAIN_MESSAGE);
      }
    }
    );
    edmenu.add(edmenuItem);
    edmenuItem = new JMenuItem("Properties");
//    edmenuItem.setAccelerator(KeyStroke.getKeyStroke(
//            KeyEvent.VK_V, ActionEvent.ALT_MASK));
    edmenuItem.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
          if("***currentdir***".equals(currentChosenFile.getText())){//an den exei epilexthei kapoio file
              //dinei properties tou current directory
             //.....................
             File curdir = new File(currentAbsPath);
             name = curdir.getName();
             path = curdir.getAbsolutePath();
             size = DirSize(new File(currentAbsPath));
             if(size >= 1024){
                 int kb = (int) (size/1024);//metra kilobytes
                 int mkb = (int) (size%1024); 
                 size = kb;
                 ypoloipo = (float)mkb/1024;
                 sizetype = "KB";
                 if(kb >= 1024 ){
                     int mb = (int) (kb/1024);//metra megabytes
                     int mmb = (int) (kb%1024);
                     size = mb;
                     ypoloipo =(float) mmb/1024;
                     sizetype = "MB";
                     if(mb >= 1024 ){
                        int gb = (int) (mb/1024);//metra gigabytes
                        int mgb = (int) (mb%1024);
                        size = gb;
                        ypoloipo = (float)mgb/1024;
                        sizetype = "GB";
                        if(gb >= 1024 ){
                            int tb = (int) (gb/1024);//metra gigabytes
                            int mtb = (int) (gb%1024); 
                            size = tb;
                            ypoloipo = (float)mtb/1024;
                            sizetype = "TB";
                         }
                     }
                 }
                 
             }
             File cdir = new File(currentAbsPath);
             if( cdir.canExecute() ){ ex = true;}
             if(cdir.canRead() ){ rd = true;}
             if(cdir.canWrite()){ wr = true;}
          }
          else{
              // dinei properties tou epilegmenou file
              //.......................
              //File curdir = new File(".");
              name = currentFile.getName();
              path = currentFile.getAbsolutePath();
              //+ System.getProperty("file.separator")+currentChosenFile.getText();
              //File curfile = new File(path);
              size = DirSize(currentFile);
              if(size >= 1024){
                 int kb = (int) (size/1024);//metra kilobytes
                 int mkb = (int) (size%1024); 
                 size = kb;
                 ypoloipo = (float)mkb/1024;
                 sizetype = "KB";
                 if(kb >= 1024 ){
                     int mb = (int) (kb/1024);//metra megabytes
                     int mmb = (int) (kb%1024);
                     size = mb;
                     ypoloipo = (float)mmb/1024;
                     sizetype = "MB";
                     if(mb >= 1024 ){
                        int gb = (int) (mb/1024);//metra gigabytes
                        int mgb = (int) (mb%1024);
                        size = gb;
                        ypoloipo = (float)mgb/1024;
                        sizetype = "GB";
                        if(gb >= 1024 ){
                            int tb = (int) (gb/1024);//metra gigabytes
                            int mtb = (int) (gb%1024); 
                            size = tb;
                            ypoloipo = (float)mtb/1024;
                            sizetype = "TB";
                         }
                     }
                 }
                 
             }
                if( currentFile.canExecute() ){ ex = true;}
                if(currentFile.canRead() ){ rd = true;}
                if(currentFile.canWrite()){ wr = true;}
          }
          String namestr = "Name: "+name;
          String pathstr = "Path: "+path;
          String s = String.format ("%.3f", size + ypoloipo);
          String sizestr = "Size: "+s+ sizetype;
          
          
          canRead = new JCheckBox("Can read");
          canWrite = new JCheckBox("Can write");
          canEx = new JCheckBox("Can execute");
          if(CheckFileOwner(currentFile) == false){
             canRead.setEnabled(false);
             canWrite.setEnabled(false);
             canEx.setEnabled(false);
          }
          canRead.setSelected(rd);
          canWrite.setSelected(wr);
          canEx.setSelected(ex);
          
          
          canRead.addItemListener(itemListener);
          canWrite.addItemListener(itemListener);
          canEx.addItemListener(itemListener);
          
          //popupm.add(cmenuItem);
        Object[] inputs = {namestr,pathstr,sizestr,canRead, canWrite, canEx};
        int result = JOptionPane.showConfirmDialog(currentChosenFile, inputs,
                "Properties", JOptionPane.PLAIN_MESSAGE);
      }
    }
    );
    popupm.add(edmenuItem);
    searchbtn = new JMenu("Search");
    searchbtn.addMenuListener(new MenuListener(){
        @Override
        public void menuSelected(MenuEvent me) {
          if(searchbutton.isVisible()){
            searchbutton.setVisible(false);
            searchfield.setVisible(false);
          }
          else{ 
          searchbutton.setVisible(true);
          searchfield.setVisible(true);
          }
        }

        @Override
        public void menuDeselected(MenuEvent me) {
            
        }

        @Override
        public void menuCanceled(MenuEvent me) {
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    }
    );
    menuBar.add(searchbtn);
    /////////////////////////////
    return menuBar;
  }
 
public void findOffset(){
    //to parakatw vriskei to <favorites>
    //epishs pio aplos tropos anti gia randomfile kai seek, an de douleuei to allazw
    try{
    Scanner sc = new Scanner(favoritesxml);
    if(sc.hasNextLine()){
        String line = sc.nextLine();
        if(line.contains("<?xml")==true){
//elegxei an h prwth grammh einai apla to header, an isxuei, tot to <favorites> tha einai sthn epomenh grammh
            if(sc.hasNextLine()){
                //vres to xml group
                sc.findInLine("<(\\w+)>");
                MatchResult result = sc.match();
                line = sc.nextLine();
                //vres to 2o xml group
               // sc.findInLine("<(w+)>");		
                
                xmlgroup = result.group(1);
                startTagSize = xmlgroup.length();
            }
        }
    }
    //tha vrw to offset apo opou mporw na prosthetw nees eggrafes
    while(sc.hasNextLine()){
        String r = sc.findInLine("</"+xmlgroup+">");//
//        MatchResult result = sc.match();
        if(r != null){
            MatchResult result = sc.match();
            offset = result.start();
        }
        else{
        sc.nextLine();
        }
    }
    sc.close();
    }catch(FileNotFoundException fnf){
        System.out.println("Sorry XML file not found");
    }
}
private void RemoveXML(String  pathOfFile){
    try{
        nList = doc.getElementsByTagName("dir");
        for(int iter = 0; iter < nList.getLength(); iter++){
             Node node = nList.item(iter);
            
            // remove the specific node
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                if(element.getElementsByTagName("path").item(0).getTextContent() == pathOfFile){
                    element = (Element) doc.getElementsByTagName("dir").item(iter);
                    element.getParentNode().removeChild(element);
                }
            }
        }
        PrintWriter nwr = new PrintWriter(favoritesxml);
        nwr.print("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<"+xmlgroup+">");
        nList = doc.getElementsByTagName("dir");
        for(int iter = 0; iter < nList.getLength(); iter++){
             Node node = nList.item(iter);
                 // remove the specific node
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                nwr.print("\n<dir>\n\t<name>"+element.getElementsByTagName("name").item(0).getTextContent()
                        +"</name>\n\t"+"<path>"+element.getElementsByTagName("path").item(0).getTextContent()
                        +"</path>\n</dir>");
            }
        }
        nwr.print("\n</"+xmlgroup+">");
        nwr.close();
    } catch (FileNotFoundException ex) {
          Logger.getLogger(FileBrowser.class.getName()).log(Level.SEVERE, null, ex);
      }/*catch(TransformerConfigurationException te){
        te.printStackTrace();
    } catch (TransformerException ex) {
          Logger.getLogger(FileBrowser.class.getName()).log(Level.SEVERE, null, ex);
      }*/
}

 public void createPopupMenuXML() {
    JMenuItem xmenuItem;
 
    //Create the popup menu.
    xmenuItem = new JMenuItem("Remove from Favorites");
    xmenuItem.addActionListener(new ActionListener(){
        @Override
        public void actionPerformed(ActionEvent ae) {
            RemoveXML(currenttoDelete);
            LoadXML();
        }
    });
    xpopup.add(xmenuItem);  
}  

private void LoadXML(){
    //allFavPathnames.clear();
    try{
        favPanel.removeAll();
//        favPanel.add(new JLabel("<html><font color='white'>Favorites Files or Directories</font></html>"));
        JLabel fl = new JLabel("$>Your Favorite Files :)");
        fl.setForeground(Color.GREEN);//"<html><font color='red'>red</font></html>"
        fl.setFont(new Font("Courier New", Font.BOLD, 14));
        favPanel.add(fl);
        favPanel.add(Box.createVerticalStrut(15));
        dbFactory = DocumentBuilderFactory.newInstance();
	dBuilder = dbFactory.newDocumentBuilder();
	doc = dBuilder.parse(favoritesxml);
	doc.getDocumentElement().normalize();
        nList = doc.getElementsByTagName("dir");
        for(int iter = 0; iter < nList.getLength(); iter++){
            Node node = nList.item(iter);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) node;
                JButton favb = new JButton(eElement.getElementsByTagName("name").item(0).getTextContent());
                favb.setActionCommand(eElement.getElementsByTagName("path").item(0).getTextContent());
                favb.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent ae){
                        GotoFavorite(ae.getActionCommand());
                    }
                });
                Dimension dmnsn = favb.getPreferredSize();
                dmnsn.width = 70; 
                favb.setPreferredSize(dmnsn);
                favb.setToolTipText(favb.getText());
                MouseListener popupListener = new PopupListener(xpopup, eElement.getElementsByTagName("path").item(0).getTextContent());
                favb.addMouseListener(popupListener);
                favb.setBorder(BorderFactory.createRaisedBevelBorder());
                favb.setBorder(BorderFactory.createMatteBorder(1, 5, 1, 1, Color.green));
                favb.setForeground(Color.GREEN);
                favb.setBackground(Color.BLACK);
                uniques.add(eElement.getElementsByTagName("path").item(0).getTextContent());
                favPanel.add(favb);
                favPanel.add(Box.createVerticalStrut(7));
                favPanel.revalidate();
                favPanel.repaint();
                breadPanel.revalidate();
                filesPanel.revalidate();
                workspPanel.repaint();
                revalidate();
                breadPanel.repaint();
                filesPanel.repaint();
                workspPanel.repaint();
                repaint();
            }
        }
    }catch(ParserConfigurationException pe){
        
    }
    catch(SAXException se){
        
    }
    catch(IOException ie){
        
    }   
}

private void AddXMLEntry(String path){
    try{
        File fdir = new File(path);
        String s;
        s = "\n<dir>\n\t<name>"+fdir.getName()+"</name>\n\t"+"<path>"+path+"</path>\n</dir>\n</"+xmlgroup+">";
        if(!uniques.contains(path)){
            byte[] buffer = s.getBytes();
            //System.out.println("s.length:  "+s.length()+" file length "+(favoritesxml.length() )+" tagsize "+startTagSize);
            findOffset();
            //lwr.write(s,offset,s.length());
            RandomAccessFile rf = new RandomAccessFile(favoritesxml.getAbsolutePath(), "rw");
            rf.seek(offset-1);//paei sthn teleytaia eggrafh prin to </favorites>
            rf.write(buffer, 0,s.length());


            findOffset();
            //lwr.close();
            rf.close();
            LoadXML();
        }
    }catch(IOException ioe){
        
    }
    
}

public void GotoFavorite(String favoriteFolderPath){
    File ff = new File(favoriteFolderPath);
    if(searchedSomething == true){
                   workspPanel.removeAll();
                   workspPanel.add(searchPanel);
                   workspPanel.add(breadPanel);
                   workspPanel.add(scrollPane);
                   searchedSomething = false;
        }
    OpenFile(ff.getAbsolutePath(),ff);
    filesPanel.revalidate();
      favPanel.revalidate();
      workspPanel.repaint();
      revalidate();
      breadPanel.repaint();
      filesPanel.repaint();
      favPanel.repaint();
      workspPanel.repaint();
      repaint();

}

public void OpenFile(String filepath, File f){
try{  
    //File file = new File(filepath);
    int indx = filepath.lastIndexOf(System.getProperty("file.separator"));
    if(indx ==filepath.length()-1 && indx !=0 ){
                filepath = filepath.substring(0, indx);
            }
    if(!Desktop.isDesktopSupported())//check if Desktop is supported by Platform or not  
    {  
        System.out.println("not supported"); 
        System.exit(0);
    }  
    Desktop desktop = Desktop.getDesktop();  
    Path papth = f.toPath();
    boolean h= Files.isHidden(papth);
    ///////////////////// DEN AFHNEI NA MPEI STON FAKELO USERS
   if(!Desktop.isDesktopSupported())//check if Desktop is supported by Platform or not  
    {  
        System.out.println("not supported"); 
        try {
            //gia linux
            //Desktop desktop = Desktop.getDesktop();
            desktop.edit(f);
          } catch (IOException e){
              
          }
        System.exit(0);
    }  
        if(!f.isDirectory()){ 
            try{
           // if(f.canExecute()== true){
                if(CheckFileType(f)== "exe"){
                Runtime runTime = Runtime.getRuntime();
                String executablePath = f.getAbsolutePath();
                Process process = runTime.exec(executablePath);
                currentFile = f;
                }
                else{
                     desktop.open(f);   //opens the specified file 
                     currentFile = f;
                }
           // }
           // else{
            //    JOptionPane.showMessageDialog(scrollPane,"ERROR: Selected file is not executable",
            //            "Error Message",JOptionPane.ERROR_MESSAGE);
            //}
            }catch(UnsupportedOperationException uo){
                
            }
        }
        else{
            currentAbsPath = f.getAbsolutePath();
            currentFile = f;
            Breadcrumb(currentAbsPath);
            File [] dfiles = CurrentDirList(currentAbsPath);
            CreateWorkspace(dfiles);//filepath + System.getProperty("file.separator")+f.getName()));
        }
}
catch(IOException e){  
    e.printStackTrace();  
 }
}
  
public boolean CheckFileOwner(File f){
    String file_owner = null, system_owner;
    try{
        //vriskei ton owner tou arxeiou f
              file_owner =  Files.getOwner(f.toPath()).getName();
            }
            catch(IOException ex){
                System.out.println("not supported");
            }
    if(file_owner.contains(System.getProperty("file.separator"))){
        int indx = file_owner.lastIndexOf(System.getProperty("file.separator"));
        if(indx !=-1){
            file_owner = file_owner.substring(indx+1, file_owner.length());
        }
    }
    //kai elegxei an sympiptei me ton owner tou system
    system_owner = System.getProperty("user.name");
    if(system_owner.equals(file_owner)){
        return(true);
    }
    return(false);
}

  public final File[] CurrentDirList(String curPath){
   
    File currentDir = new File(curPath);
    try{
    File files[] = currentDir.listFiles();
    if(files !=null){
        if(files.length > 0){
            ArrayList<File> dirList = new ArrayList<>();//periexei alphabitika tous fakelous mono
            ArrayList<File> filesList = new ArrayList<>();//periexei alphabitka ta arxeia mono
            for(File f: files){
                if(f.isDirectory()){
                    dirList.add(f);
                }
                else{
                    filesList.add(f);
                }
            }
            
            
            ArrayList<File> allFiles = new ArrayList<>();
            if(!dirList.isEmpty()){
                Collections.sort(dirList);
                allFiles.addAll(dirList);
            }
            if(!filesList.isEmpty()){
                Collections.sort(filesList);
                allFiles.addAll(filesList);
            }
            files = allFiles.toArray(files);
        }
    }
    return (files);
    }catch(SecurityException se){
        System.out.println("SECURITY EXCEPTION");
    }
   // }
    return null;
  }
  
  public long DirSize(File directory) {
    long length = 0;
    File []files;
    files = CurrentDirList(directory.getAbsolutePath());
    if(files !=null){
        for (File file : directory.listFiles()) {
            if (file.isFile())
                length += file.length();
            else
                length += DirSize(file);
        }
    }
    else{
        return directory.length();
    }
    return length;
}
  
  public void Search(String stringtosearch){
      String type = "all";
      namesList.clear();
      searchedSomething = true;
      StringBuilder strf = new StringBuilder();
      String str;
      //check if string has "type: " first and the actual type substring is returned
      //........
      int check = stringtosearch.lastIndexOf("type:");
      if(check!=-1){//an uparxei h leksh type:
         type = stringtosearch.substring(check+5);//+5 giati einai to length tou type:,thelw na diavasei meta apo auto
      }
      Scanner sc = new Scanner(stringtosearch);
      while(sc.hasNext()){
          str = sc.next();
          if(str.contains("type:")==false ){
              strf.append(str+" ");
          }
          else { break;}
      }
      substring = strf.toString(); 
      //searches recursively from current dir 
      int indx = currentAbsPath.lastIndexOf(System.getProperty("file.separator")); 
      //an uparxei / sto telos tou path
      if(indx == currentAbsPath.length() - 1 && indx !=0){
          currentAbsPath = currentAbsPath.substring(0, indx);
      }
      
        indx = substring.lastIndexOf(" "); 
        //an uparxei keno sto telos tou path
        if(indx == substring.length() - 1){
            substring = substring.substring(0, indx);
        }
      recursiveSearch( type, currentAbsPath);
      displaySearchResults();
  }
  
  public void displaySearchResults(){
      workspPanel.removeAll();
      ListIterator<String> it = namesList.listIterator();
      listModel.removeAllElements();
      while(it.hasNext()){
          listModel.addElement(it.next());
      }
        //list = new JList<>(listModel);
        workspPanel.add(searchPanel);
        workspPanel.add(breadPanel);
        workspPanel.add(listScrollPane);
        favPanel.revalidate();
        favPanel.repaint();
        breadPanel.revalidate();
        filesPanel.revalidate();
        workspPanel.repaint();
        revalidate();
        breadPanel.repaint();
        filesPanel.repaint();
        workspPanel.repaint();
        repaint();
  }
  
  public void recursiveCopy( String sourcedir, String destdir){
       File [] files;
    files = CurrentDirList(sourcedir); 
    //an files == null, tote to arxiko file einai arxeio 'h kenos fakelos
    if(files !=null){
    for(File f: files){
        
            if(CheckFileType(f) == "dir" ){
                if(f.list().length>0){
                        File nfile = new File(destdir + System.getProperty("file.separator")+f.getName());
                        nfile.mkdir();
                        recursiveCopy(f.getAbsolutePath(),nfile.getAbsolutePath());
                    
                }
                else if(f.list().length == 0){
                    try {
                        File destd = new File(destdir);
                        Path pth = destd.toPath();
                        Path sourcepth = f.toPath();
                        Files.copy(sourcepth, pth.resolve(sourcepth.getFileName()), REPLACE_EXISTING);
                    } catch (IOException ex) {
                        Logger.getLogger(FileBrowser.class.getName()).log(Level.SEVERE, null, ex);
                        ex.printStackTrace();
                    }
                }
            }
            else{
                try {
                        File destd = new File(destdir);
                        Path pth = destd.toPath();
                        Path sourcepth = f.toPath();
                        Files.copy(sourcepth, pth.resolve(sourcepth.getFileName()), REPLACE_EXISTING);
                    } catch (IOException ex) {
                        Logger.getLogger(FileBrowser.class.getName()).log(Level.SEVERE, null, ex);
                        ex.printStackTrace();
                    }
            }
    }
    //sto telos ths antigrafhs, epistrefei true an yphrkse sfalma me kapoio fakelo xwris permission
    //kai h kalousa methodos me bash auto bgazei ena modal window anaferontas to lathos 
    //kai epishmainontas oti o sygkekrimenos fakelos den antigrafhke
    }
}
  
  
  public void recursiveSearch( String type, String path){
      File [] files;
      String pathf;
      String newpath = null;
      //searches recursively from current dir and returns a list of
      //all pathnames that matched the requested substring
      files = CurrentDirList(path);
      if(files !=null){
          
      for(File f: files){
               if(((f.getName().toLowerCase()). contains(substring.toLowerCase()))==true){
                       pathf = f.getAbsolutePath();
                       if(type == "all")
                        namesList.add(pathf);
                       else{
                           if(CheckFileType(f) !=null && CheckFileType(f).equals(type)){
                               namesList.add(pathf);
                           }
                       }
                }
               //gia na mhn mpei se fakelo pou den exw prosvash,to elegxw prwta
               try{
                    
                if(f.isDirectory()){
                    if(f.list()!=null && f.list().length >0){        
                        newpath = path +System.getProperty("file.separator")+ f.getName();
                        recursiveSearch(type, newpath);
                    }
                }
               }catch(NullPointerException np){
                   System.out.println("ERROR AT FILE "+f.getAbsolutePath()+" "+newpath);
                   np.printStackTrace();
                   System.exit(1);
               }
           // }
      //}
      }
      }
      //namesList = recursiveSearch(type);
//      return(namesList);
  }
  
public void recursiveDelete(String currentdir){
    File [] files;
    //searches recursively from current dir and returns a list of
    //all pathnames that matched the requested substring
      
    files = CurrentDirList(currentdir);  
    if(files != null){
    for(File f: files){
            if(CheckFileType(f) == "dir" ){
                if(f.list().length>0){
                    recursiveDelete(f.getAbsolutePath());
                }
                if(f.list().length == 0){
                    f.delete();
                }
            }
            else{
                f.delete();
            }
        
    }
    }
}
  
  public final void CreateWorkspace(File [] files){
      filesPanel.removeAll();
      if(files != null){      
      int i =0;
      JLabel [] label = new JLabel [files.length];
      
      currentChosenFile = new JLabel("***currentdir***");
      for(File f : files) {
          String type;
          String icon;
          type = CheckFileType(f);
          icon = GetFileTypeImage(type, currentAbsPath);
          ImageIcon fileIcon = new ImageIcon(icon);
          if(fileIcon !=null){
          label[i] = new JLabel(f.getName(),
                                fileIcon ,
                                JLabel.CENTER);
          label[i].setBackground(Color.DARK_GRAY);
          label[i].setForeground(Color.GREEN);
          label[i].setFont(new Font(("Courier New"),Font.PLAIN,11));
                //Set the position of the text, relative to the icon:
          label[i].setVerticalTextPosition(JLabel.BOTTOM);
          label[i].setHorizontalTextPosition(JLabel.CENTER);
          label[i].setOpaque(true);
          label[i].setToolTipText(f.getName());
          label[i].setPreferredSize(new Dimension(80, 85));
          }
      MyMouseListener ml = new MyMouseListener(label[i]);
      label[i].addMouseListener(ml); 
      labelBackgroundColor = label[i].getBackground();
      filesPanel.add(label[i]);
    }  
      if(currentChosenFile.getText() == "***currentdir***"){
         edmenu.setEnabled(false);
      }
      }
      breadPanel.revalidate();
      filesPanel.revalidate();
      favPanel.revalidate();
      
      workspPanel.repaint();
      revalidate();
      breadPanel.repaint();
      filesPanel.repaint();
      favPanel.repaint();
      
      workspPanel.repaint();
      repaint();
      
  }
  
  public String CheckFileType(File f){
      if(!f.isDirectory()){
        int i = f.getName().lastIndexOf('.');
          if (i > -1) {
              String extension;
              extension = f.getName().substring(i+1);
              return extension;
         }
      }
      else{
          return "dir";
      }
        return null;
  }
  
  public String GetFileTypeImage(String type, String currentPath){
        File [] files;
        files =CurrentDirList("."+System.getProperty("file.separator")+"icons");
//an to kanw etsi, tote trexwntas to apo terminal to jar, 
//pairnei to icons fakelo pou brisketai ston trexon katalogo poy einai to terminal
///////PWS TO ALLAZW WSTE NA TA DIAVAZEI APO TO .jar  ARXEIO POU HDH EINAI EKEI??????
        
        if(type != null){
            
            
            for(File f : files){
                if(type !="dir"){
                int i = f.getName().lastIndexOf('.');
                    if (i > 0) {
                        icontype = f.getName().substring(0,i);
                    }
                    if(icontype.equals(type)){
                        return "."+System.getProperty("file.separator")+"icons"+System.getProperty("file.separator")+f.getName();
                    }
                    
                }
                else{
                    return "."+System.getProperty("file.separator")+"icons"+System.getProperty("file.separator")+"folder.png";
                }
            }
           
            return "."+System.getProperty("file.separator")+"icons"+System.getProperty("file.separator")+"question.png";
        }
        else
        //sb = sb.append(iconpath).append(System.getProperty("file.separator")).append("icons").append(System.getProperty("file.separator")).append("question");
        return "."+System.getProperty("file.separator")+"icons"+System.getProperty("file.separator")+"question.png";
  }
  
  public class MyMouseListener implements MouseListener{
      JLabel l;
      int mousecntr;
      public MyMouseListener(JLabel l){
      this.l = l;
      mousecntr = 0;
      }
            public void mouseClicked(MouseEvent e) {
                //an den exei epilegei kanena arxeio akoma 'h an exei epilegei to idio me prin(dld 2plo click)
                
                if(e.getButton() == MouseEvent.BUTTON1){//Left mouse click
                    if(labeltochangecolor.getText() != "***none***")
                        labeltochangecolor.setBackground(labelBackgroundColor);
                    l.setBackground(Color.BLUE);
                    mousecntr++;
                    //System.out.println("l "+l.getText()+" chosen "+currentChosenFile.getText());
                    if((l.getText() != currentChosenFile.getText())){
                        currentChosenFile.setBackground(labelBackgroundColor);
                        currentFile = new File(currentAbsPath + System.getProperty("file.separator")+l.getText());
                        currentChosenFile=l;
                        currentFile = new File(currentAbsPath + System.getProperty("file.separator")+currentChosenFile.getText());
                        mousecntr = 1;
                    }
                    edmenu.setEnabled(true);
                    if(mousecntr >= 2){
                        //System.out.println("Clicked "+e.getClickCount()+" times. Openning file "+l.getText());
                        
                        OpenFile(currentAbsPath, new File(currentAbsPath +System.getProperty("file.separator")+ currentChosenFile.getText()));
                        l.setBackground(Color.LIGHT_GRAY);
                        mousecntr = 0;
                    }
                }
            }
           
            public void mouseEntered(MouseEvent e) {
            }
            public void mouseExited(MouseEvent e) {
            }
            public void mousePressed(MouseEvent e) {
      maybeShowPopup(e);
    }
 
    public void mouseReleased(MouseEvent e) {
      maybeShowPopup(e);
    }
 
    private void maybeShowPopup(MouseEvent e) {
      if (e.isPopupTrigger()) {
        labeltochangecolor = currentChosenFile;
        currentChosenFile = l;
        currentFile = new File(currentAbsPath + System.getProperty("file.separator")+l.getText());
        popupm.show(e.getComponent(),
               e.getX(), e.getY());
        
                breadPanel.revalidate();
                filesPanel.revalidate();
                favPanel.revalidate();
                scrollPane.revalidate();
                workspPanel.repaint();
                revalidate();
                breadPanel.repaint();
                filesPanel.repaint();
                favPanel.repaint();
                scrollPane.repaint();
                workspPanel.repaint();
                repaint();
                }
    }
}
  
  public final void Breadcrumb(String abspath){
      String directory;
      String path = null, folder;
      int indx = abspath.lastIndexOf(System.getProperty("file.separator")); 
      //an uparxei / sto telos tou path
      if(indx == abspath.length() - 1 && indx !=0){
          abspath = abspath.substring(0, indx);
      }
      ActionListener breadListener = new ActionListener(){
          @Override
          public void actionPerformed(ActionEvent be) {
              currentFile = new File(be.getActionCommand());
              currentAbsPath = be.getActionCommand();
              Breadcrumb(currentAbsPath);
              
              breadPanel.repaint();
               breadPanel.revalidate();
        if(searchedSomething == true){
                   workspPanel.removeAll();
                   workspPanel.add(searchPanel);
                   workspPanel.add(breadPanel);
                   workspPanel.add(scrollPane);
                   searchedSomething = false;
        }
      filesPanel.revalidate();
      favPanel.revalidate();
      workspPanel.repaint();
      revalidate();
      breadPanel.repaint();
      filesPanel.repaint();
      favPanel.repaint();
      workspPanel.repaint();
      repaint();
              //workspPanel.revalidate();
              CreateWorkspace(CurrentDirList(be.getActionCommand()));
          }
      };
      Path pt = Paths.get(abspath);
      int ind = 1;
      for(int i = -1;i<pt.getNameCount(); i++){
          if(i == -1){
              directory = (pt.getRoot()).toString();
              //An einai windows, h riza einai c://, egwthelw na vgalw ta //
                String low = directory;
                int sepindx = low.lastIndexOf(System.getProperty("file.separator"));

                while(sepindx == low.length() - 1 && sepindx != 0){
                    low = low.substring(0,sepindx);
                    sepindx = low.lastIndexOf(System.getProperty("file.separator"));
                }
                directory = low;
          }
          else{
            directory = (pt.getName(i)).toString();
          }
          //tha elegxei an uparxei hdh kapoios katalogos sto breadcrumb
           if(ind<=bread.size()){
               if(!(bread.get(ind-1)).equals(directory)){
                   bread.set(ind-1, directory);//diagrafei palio, vazei nea timh
               }
           }
           else{
               bread.add(directory);
           }
           ind++;
      }
      if(ind<bread.size()+1){
        int initialsize = bread.size();
        while(ind<=initialsize){
            bread.removeLast();
            ind++;
        }
      }
      ListIterator<String> it = bread.listIterator();
      
        breadPanel.removeAll();
        if(it.hasNext()){
            folder = it.next();
            path = folder + System.getProperty("file.separator");
            JButton brdbut = new JButton(folder);
            brdbut.setFont(new Font(("Courier New"),Font.PLAIN,14));
            brdbut.setBorder(BorderFactory.createLineBorder(Color.CYAN));
           brdbut.setForeground(Color.CYAN);
           brdbut.setBackground(Color.BLACK);
           //brdbut.setBorder(new LineBorder(Color.CYAN));
            brdbut.addActionListener(breadListener);
            brdbut.setActionCommand(path);
            breadPanel.repaint();
            breadPanel.revalidate();
            filesPanel.revalidate();
            favPanel.revalidate();
            workspPanel.repaint();
            revalidate();
            breadPanel.repaint();
            filesPanel.repaint();
            favPanel.repaint();
            workspPanel.repaint();
            repaint();
           //breadPanel.removeAll();
           breadPanel.add(brdbut);
      }
      while(it.hasNext()){
           folder = it.next();
           path = path + folder + System.getProperty("file.separator");
           JButton brdbut = new JButton(folder);
           brdbut.setFont(new Font(("Courier New"),Font.PLAIN,14));
           brdbut.setBorder(BorderFactory.createLineBorder(Color.CYAN));
           brdbut.setForeground(Color.CYAN);
           brdbut.setBackground(Color.BLACK);
           brdbut.addActionListener(breadListener);
           brdbut.setActionCommand(path);
           brdbut.setBorder(BorderFactory.createLineBorder(Color.CYAN));
           JLabel tip =new JLabel(">");
           tip.setForeground(Color.GREEN);
           breadPanel.add(tip);
           breadPanel.add(brdbut);
      }
  }
  
  
  public Container createContentPane() {
    //Create the content-pane-to-be.
    JPanel contentPane = new JPanel(new BorderLayout());
    add(favPanel, BorderLayout.WEST);
    add(workspPanel, BorderLayout.CENTER);
    return contentPane;
  }

  /**
   * Create the GUI and show it.  For thread safety,
   * this method should be invoked from the
   * event-dispatching thread.
   */
  private static void createAndShowGUI() {
    FileBrowser fb = new FileBrowser();
    fb.pack();
    fb.setVisible(true);
  }
 
  public static void main(String[] args) {
    //Schedule a job for the event-dispatching thread:
    //creating and showing this application's GUI.
    javax.swing.SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        createAndShowGUI();
      }
    });
  }
}