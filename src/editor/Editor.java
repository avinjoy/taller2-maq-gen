package editor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import javax.swing.text.LayeredHighlighter;
import javax.swing.text.Highlighter.HighlightPainter;
import javax.swing.undo.UndoManager;

import compiler.ParserAssembler;
import compiler.Scanner;

import domain.Console;
import domain.MachineState;
import editor.TextLineNumber.Mode;
import editor.res.Conversor;

/**
 * Clase principal donde se construye la GUI del editor.
 *
 * @author Dark[byte]
 */
public class Editor implements ActionListener {

    public enum LanguajeType {

        MACHINE, ASSEMBLER
    };

    private LanguajeType languajeType = LanguajeType.ASSEMBLER; // 0 codigo maquina, 1 assembler
    private Dimension pantalla;

    private JFrame jFrame; // instancia de JFrame (ventana principal)
    private JMenuBar jMenuBar; // instancia de JMenuBar (barra de menú)
    private JToolBar jToolBar; // instancia de JToolBar (barra de herramientas)
    private JPopupMenu jPopupMenu; // instancia de JPopupMenu (menú emergente)
    private JPanel statusBar; // instancia de JPanel (barra de estado)

    private int indextest = 0;
    private boolean in_debug = false;

    private JSplitPane splitPaneMain;
    private JSplitPane splitPaneDebug;
    private ConsolePanel consolePanel;
    private RegisterPanel registerPanel;
    private PortPanel portPanel;
    private MemoryPanel memoryPanel;
    private AluPanel aluPanel;
    public ErrorPanel errorPanel;

    //private JSplitPane problemPanel;
    private JTextArea jTextArea; // instancia de JTextArea (Área de edición)
    private JTextArea jTextAreaTranslate; // instancia de JTextArea para la traduccion(Área de edición)
    private JTextArea jTextAreaProblems;
    private JLabel labeltitle;
    private JLabel labeltitleTraslate;
    private TextLineNumber columnLineCounter;
    private TextLineNumber columnLineCounterTranslate;
    private JScrollPane scrollPane;
    private JScrollPane scrollPaneTranslate;
    //private JScrollPane scrollPaneProblems;

    private JCheckBoxMenuItem itemLineWrap; // instancias de algunos items de menú que necesitan ser accesibles
    private JCheckBoxMenuItem itemShowToolBar;
    private JCheckBoxMenuItem itemFixedToolBar;
    private JCheckBoxMenuItem itemShowStatusBar;

    private JMenuItem mbItemUndo;
    private JMenuItem mbItemRedo;
    private JMenuItem mpItemUndo;
    private JMenuItem mpItemRedo;

    private JButton buttonUndo; // instancias de algunos botones que necesitan ser accesibles
    private JButton buttonRedo;

    private JToggleButton buttonTraducir;

    private JLabel sbFilePath; // etiqueta que muestra la ubicación del archivo actual
    private JLabel sbFileSize; // etiqueta que muestra el tamaño del archivo actual
    private JLabel sbCaretPos; // etiqueta que muestra la posición del cursor en el Área de edición

    private boolean hasChanged = false; // el estado del documento actual, no modificado por defecto
    private File currentFile = null; // el archivo actual, ninguno por defecto 

    private final EventHandler eventHandler; // instancia de EventHandler (la clase que maneja eventos) 
    private final ActionPerformer actionPerformer; // instancia de ActionPerformer (la clase que ejecuta acciones)
    private final UndoManager undoManager; // instancia de UndoManager (administrador de edición)
    private ArrayList<String> words;
    private AutoSuggestor autoSuggestor;
    private BufferedReader reader;
    private int lastdividerPosition;
    //private int lastproblemPanelLocation;
    private ErrorParser errorParser;
    private MachineState currMachineState = null;

    //conversorHexa
    JTextField textFieldConvHexa;
    JLabel resultConvHexa;
    JLabel textFieldLabel;
    
    //Program Counter
    JLabel valuePCLabel;
    JLabel pcLabel;

    /**
     * Punto de entrada del programa.
     *
     * Instanciamos esta clase para construir la GUI y hacerla visible.
     *
     * @param args argumentos de la línea de comandos.
     */
    public static void main(String[] args) {
        // construye la GUI en el EDT (Event Dispatch Thread)
        javax.swing.SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                new Editor().jFrame.setVisible(true); // hace visible la GUI
                // creada por la clase
                // TPEditor
            }
        });
    }

    /**
     * Constructor de la clase.
     *
     * Se construye la GUI del editor, y se instancian clases importantes.
     */
    public Editor() {
        try { // LookAndFeel nativo
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            System.err.println(ex);
        }

        pantalla = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension minimumSize = new Dimension(100, 50);
        Dimension minimumSize2 = new Dimension(300, 100);

        // construye un JFrame con título
        jFrame = new JFrame("Gerenic Sim - Sin Título");
        jFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        jFrame.setExtendedState(Frame.MAXIMIZED_BOTH);
        jFrame.setLocationRelativeTo(null);

        // asigna un manejador de eventos la visualizacion del JFrame
        jFrame.addComponentListener(new ComponentAdapter() {

            public void componentShown(ComponentEvent evt) {
                lastdividerPosition = pantalla.width / 2;
                enableTraslate();
                //Component c = (Component) evt.getSource();
                //System.out.println("Component is now visible");
            }
        });

        // asigna un manejador de eventos para el cierre del JFrame
        jFrame.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent we) {
                actionPerformer.actionExit(); // invoca el método actionExit()
            }
        });

        eventHandler = new EventHandler(); // construye una instancia de EventHandler
        actionPerformer = new ActionPerformer(this); // construye una instancia de ActionPerformer
        undoManager = new UndoManager(); // construye una instancia de UndoManager
        undoManager.setLimit(50); // le asigna un límite al buffer de ediciones

        buildMenuBar(); // construye la barra de menú
        buildToolBar(); // construye la barra de herramientas
        buildStatusBar(); // construye la barra de estado
        buildPopupMenu(); // construye el menú emergente
        loadLanguageWords();

        Container c = jFrame.getContentPane(); // obtiene el contendor principal

        // añade los componentes de la parte superior de la pantalla
        c.add(jToolBar, BorderLayout.NORTH); // añade la barra de herramientas,
        jFrame.setJMenuBar(jMenuBar); // designa la barra de menú del JFrame

        // añade los compponentes a la derecha de la pantalla
        //frame.setLayout(new BoxLayout(frame,BoxLayout.X_AXIS));
        JPanel leftPanel = new JPanel(new BorderLayout());
        registerPanel = new RegisterPanel();
        portPanel = new PortPanel();
        leftPanel.add(registerPanel, BorderLayout.NORTH);
        leftPanel.add(portPanel, BorderLayout.CENTER);
        aluPanel = new AluPanel();
        leftPanel.add(aluPanel, BorderLayout.SOUTH);
        //leftPanel.add(portPanel, BorderLayout.PAGE_END);

        // añade los componentes del pie de la pantalla
        JPanel bottonPanel = new JPanel(new BorderLayout());
        consolePanel = new ConsolePanel();
        memoryPanel = new MemoryPanel();
        errorPanel = new ErrorPanel();
        consolePanel.setMinimumSize(minimumSize2);
        memoryPanel.setMinimumSize(minimumSize2);

        //Create a split pane with the two scroll panes in it.
        splitPaneDebug = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, memoryPanel, consolePanel);
        splitPaneDebug.setOneTouchExpandable(true);
        splitPaneDebug.setDividerLocation(350);
        bottonPanel.add(errorPanel, BorderLayout.PAGE_START);
        bottonPanel.add(splitPaneDebug, BorderLayout.CENTER);
        bottonPanel.add(statusBar, BorderLayout.PAGE_END);

        // añade los componentes al centro de la pantalla
        JPanel mainPanel = new JPanel(new BorderLayout());

        jTextArea = new JTextArea();
        buildTextArea(jTextArea, 16777215, true); // construye el Área de edición, es importante que esta
        // sea la primera parte en construirse

        jTextAreaTranslate = new JTextArea();
        jTextAreaTranslate.setEditable(false);
        buildTextArea(jTextAreaTranslate, 15122687, false); // construye el Área de edición, es importante que esta
        // sea la primera parte en construirse

        jTextAreaProblems = new JTextArea();
        jTextAreaProblems.setEditable(false);
        buildTextArea(jTextAreaProblems, 16777215, false); // construye el Área de edición, es importante que esta
        // sea la primera parte en construirse

        autoSuggestor = newAutoSuggestor();

        // contendor
        scrollPane = new JScrollPane(jTextArea);
        scrollPaneTranslate = new JScrollPane(jTextAreaTranslate);
        //c.add(scrollPane, BorderLayout.WEST); // añade el area de edición en

        // el CENTRO
        labeltitle = new JLabel("Assembler");
        buidTitlePanel(scrollPane, labeltitle);
        columnLineCounter = new TextLineNumber(jTextArea);
        buildColumnLineCounter(columnLineCounter, scrollPane);

        labeltitleTraslate = new JLabel("Codigo Máquina");
        buidTitlePanel(scrollPaneTranslate, labeltitleTraslate);
        columnLineCounterTranslate = new TextLineNumber(jTextArea);
        buildColumnLineCounter(columnLineCounterTranslate, scrollPaneTranslate);

        //Provide minimum sizes for the two components in the split pane
        scrollPane.setMinimumSize(minimumSize);
        scrollPaneTranslate.setMinimumSize(minimumSize);

        //Create a split pane with the two scroll panes in it.
        splitPaneMain = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollPane, scrollPaneTranslate);
        splitPaneMain.setOneTouchExpandable(true);
        splitPaneMain.setDividerLocation(0.5);

        //Set invisible
        buttonTraducir.setSelected(true);

        //this.enableTraslate();
        mainPanel.add(splitPaneMain, BorderLayout.CENTER);

        /*scrollPaneProblems = new JScrollPane(jTextAreaProblems);
         scrollPaneProblems.setMinimumSize(minimumSize);
        
        
         lastproblemPanelLocation = pantalla.height / 2;
         problemPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT, scrollPane, scrollPaneProblems);
         problemPanel.setMinimumSize(minimumSize);
         problemPanel.setOneTouchExpandable(true);
         problemPanel.setDividerLocation(1000000);
         problemPanel.setDividerSize(0);
        
         c.add(problemPanel, BorderLayout.CENTER);*/
        c.add(mainPanel, BorderLayout.CENTER);
        c.add(leftPanel, BorderLayout.LINE_END);
        c.add(bottonPanel, BorderLayout.PAGE_END);

        buildTimer();

        this.in_debug = false;
        enableDebug();
        enableError(false);
    }

    private void buildTimer() {

        Timer timer = new Timer(2, this);
        timer.setRepeats(true);
        timer.start();
        errorParser = new NataliusErrorParser(jTextArea);
    }

    /**
     * Construye el Áea de edición.
     */
    private void buildTextArea(JTextArea jTextArea, int color, boolean principal) {
        //jTextArea = new JTextArea(); // construye un JTextArea
        jTextArea.setBackground(new Color(color));

        // se configura por defecto para que se ajusten las líneas al tamaño del
        // Área de texto ...
        jTextArea.setLineWrap(true);
        // ... y que se respete la integridad de las palaras en el ajuste
        jTextArea.setWrapStyleWord(true);

        if (principal) {
            // asigna el manejador de eventos para el cursor
            jTextArea.addCaretListener(eventHandler);
            // asigna el manejador de eventos para el ratón
            jTextArea.addMouseListener(eventHandler);
            // asigna el manejador de eventos para registrar los cambios sobre el
            // documento
            jTextArea.getDocument().addUndoableEditListener(eventHandler);

            jTextArea.addMouseMotionListener(eventHandler);

            // asigna el manejador de eventos para el teclado
            jTextArea.addKeyListener(eventHandler);
            // remueve las posibles combinaciones de teclas asociadas por defecto
            // con el JTextArea
            jTextArea.getInputMap().put(
                    KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK),
                    "none"); // remueve CTRL + X ("Cortar")
            jTextArea.getInputMap().put(
                    KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK),
                    "none"); // remueve CTRL + C ("Copiar")
            jTextArea.getInputMap().put(
                    KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK),
                    "none"); // remueve CTRL + V ("Pegar")
        }
    }

    /**
     * Construye la barra de menú.
     */
    private void buildMenuBar() {
        jMenuBar = new JMenuBar(); // construye un JMenuBar

        // construye el menú "Archivo", a continuación se construyen los items
        // para este menú
        JMenu menuFile = new JMenu("Archivo");
        // construye el item "Nuevo"
        JMenu itemNew = new JMenu("Nuevo");

        JMenuItem itemNewMaq = new JMenuItem("Código Máquina");
        itemNewMaq.setActionCommand("cmd_new_maq");
        // le asigna una combinación de teclas
        itemNewMaq.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M,
                InputEvent.CTRL_MASK));

        JMenuItem itemNewAsm = new JMenuItem("Código Assembler");
        itemNewAsm.setActionCommand("cmd_new_asm");
        itemNewAsm.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A,
                InputEvent.CTRL_MASK));

        JMenuItem itemOpen = new JMenuItem("Abrir");
        itemOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,
                InputEvent.CTRL_MASK));
        itemOpen.setActionCommand("cmd_open");

        JMenuItem itemSave = new JMenuItem("Guardar");
        itemSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
                InputEvent.CTRL_MASK));
        itemSave.setActionCommand("cmd_save");

        JMenuItem itemSaveAs = new JMenuItem("Guardar como...");
        itemSaveAs.setActionCommand("cmd_saveas");
        itemSaveAs.addActionListener(eventHandler);

        JMenuItem itemPrint = new JMenuItem("Imprimir");
        itemPrint.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P,
                InputEvent.CTRL_MASK));
        itemPrint.setActionCommand("cmd_print");

        JMenuItem itemExit = new JMenuItem("Salir");
        itemExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q,
                InputEvent.CTRL_MASK));
        itemExit.setActionCommand("cmd_exit");

        itemNew.add(itemNewMaq);
        itemNew.add(itemNewAsm);
        menuFile.add(itemNew); // se añaden los items al menú "Archivo"
        menuFile.add(itemOpen);
        menuFile.add(itemSave);
        menuFile.add(itemSaveAs);
        menuFile.addSeparator();
        menuFile.add(itemPrint);
        menuFile.addSeparator();
        menuFile.add(itemExit);
		// ----------------------------------------------

        // construye el menú "Editar", a continuación se construyen los items
        // para este menú
        JMenu menuEdit = new JMenu("Editar");

        mbItemUndo = new JMenuItem("Deshacer");
        mbItemUndo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z,
                ActionEvent.CTRL_MASK));
        mbItemUndo.setEnabled(false);
        mbItemUndo.setActionCommand("cmd_undo");

        mbItemRedo = new JMenuItem("Rehacer");
        mbItemRedo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y,
                ActionEvent.CTRL_MASK));
        mbItemRedo.setEnabled(false);
        mbItemRedo.setActionCommand("cmd_redo");

        JMenuItem itemCut = new JMenuItem("Cortar");
        itemCut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,
                ActionEvent.CTRL_MASK));
        itemCut.setActionCommand("cmd_cut");

        JMenuItem itemCopy = new JMenuItem("Copiar");
        itemCopy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,
                ActionEvent.CTRL_MASK));
        itemCopy.setActionCommand("cmd_copy");

        JMenuItem itemPaste = new JMenuItem("Pegar");
        itemPaste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V,
                ActionEvent.CTRL_MASK));
        itemPaste.setActionCommand("cmd_paste");

        JMenuItem itemSearch = new JMenuItem("Buscar");
        itemSearch.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F,
                ActionEvent.CTRL_MASK));
        itemSearch.setActionCommand("cmd_search");

        JMenuItem itemSearchNext = new JMenuItem("Buscar siguiente");
        itemSearchNext
                .setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0));
        itemSearchNext.setActionCommand("cmd_searchnext");

        JMenuItem itemGotoLine = new JMenuItem("Ir a la línea...");
        itemGotoLine.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T,
                ActionEvent.CTRL_MASK));
        itemGotoLine.setActionCommand("cmd_gotoline");

        JMenuItem itemSelectAll = new JMenuItem("Seleccionar todo");
        itemSelectAll.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E,
                ActionEvent.CTRL_MASK));
        itemSelectAll.setActionCommand("cmd_selectall");

        menuEdit.add(mbItemUndo); // se añaden los items al menú "Editar"
        menuEdit.add(mbItemRedo);
        menuEdit.addSeparator(); // añade separadores entre algunos items
        menuEdit.add(itemCut);
        menuEdit.add(itemCopy);
        menuEdit.add(itemPaste);
        menuEdit.addSeparator();
        menuEdit.add(itemSearch);
        menuEdit.add(itemSearchNext);
        menuEdit.add(itemGotoLine);
        menuEdit.addSeparator();
        menuEdit.add(itemSelectAll);
		// ----------------------------------------------

        // construye el menú "Opciones", a continuación se construyen los items
        // para este menú
        JMenu menuTools = new JMenu("Opciones");

        itemLineWrap = new JCheckBoxMenuItem("Ajuste de línea");
        itemLineWrap.setSelected(true);
        itemLineWrap.setActionCommand("cmd_linewrap");

        itemShowToolBar = new JCheckBoxMenuItem("Ver barra de herramientas");
        itemShowToolBar.setSelected(true);
        itemShowToolBar.setActionCommand("cmd_showtoolbar");

        itemFixedToolBar = new JCheckBoxMenuItem("Fijar barra de herramientas");
        itemFixedToolBar.setSelected(true);
        itemFixedToolBar.setActionCommand("cmd_fixedtoolbar");

        itemShowStatusBar = new JCheckBoxMenuItem("Ver barra de estado");
        itemShowStatusBar.setSelected(true);
        itemShowStatusBar.setActionCommand("cmd_showstatusbar");

        JMenuItem itemFont = new JMenuItem("Fuente de letra");
        itemFont.setActionCommand("cmd_font");

        JMenuItem itemFontColor = new JMenuItem("Color de letra");
        itemFontColor.setActionCommand("cmd_fontcolor");

        JMenuItem itemBackgroundColor = new JMenuItem("Color de fondo");
        itemBackgroundColor.setActionCommand("cmd_backgroundcolor");

        menuTools.add(itemLineWrap); // se añaden los items al menú "Opciones"
        menuTools.add(itemShowToolBar);
        menuTools.add(itemFixedToolBar);
        menuTools.add(itemShowStatusBar);
        menuTools.addSeparator();
        menuTools.add(itemFont);
        menuTools.add(itemFontColor);
        menuTools.add(itemBackgroundColor);

        // construye el menú "Ayuda", a continuación se construye el único item
        // para este menú
        JMenu menuHelp = new JMenu("Ayuda");

        JMenuItem itemAbout = new JMenuItem("Acerca de");
        itemAbout.setActionCommand("cmd_about");

        menuHelp.add(itemAbout); // se añade el único item al menú "Ayuda"
        // ----------------------------------------------

        jMenuBar.add(menuFile); // se añaden los menúes construidos a la barra
        // de menú
        jMenuBar.add(Box.createHorizontalStrut(5)); // añade espacios entre cada
        // menú
        jMenuBar.add(menuEdit);
        jMenuBar.add(Box.createHorizontalStrut(5));
        jMenuBar.add(menuTools);
        jMenuBar.add(Box.createHorizontalStrut(5));
        jMenuBar.add(menuHelp);

        /**
         * itera sobre todos los componentes de la barra de menú, se les asigna
         * el mismo manejador de eventos a todos excepto a los separadores
         */
        for (Component c1 : jMenuBar.getComponents()) {
            // si el componente es un menú
            if (c1.getClass().equals(javax.swing.JMenu.class)) {
                // itera sobre los componentes del menú
                for (Component c2 : ((JMenu) c1).getMenuComponents()) {
                    if (c2.getClass().equals(javax.swing.JMenu.class)) {
                        for (Component c3 : ((JMenu) c2).getMenuComponents()) {
                            // si el componente no es un separador
                            if (!c3.getClass().equals(
                                    javax.swing.JPopupMenu.Separator.class)) {
                                ((JMenuItem) c3)
                                        .addActionListener(eventHandler);
                            }
                        }
                    } else {
                        if (!c2.getClass().equals(
                                javax.swing.JPopupMenu.Separator.class)) {
                            ((JMenuItem) c2).addActionListener(eventHandler);
                        }
                    }
                }
            }
        }
    }

    /**
     * Construye la barra de herramientas.
     */
    private void buildToolBar() {
        jToolBar = new JToolBar(); // construye un JToolBar
        jToolBar.setFloatable(false); // se configura por defecto como barra
        // fija

        // construye el botón "Nuevo"
        JButton buttonNew = new JButton();
        // le asigna una etiqueta flotante
        buttonNew.setToolTipText("Nuevo");
        // le asigna una imagen ubicada en los recursos del proyecto
        buttonNew.setIcon(new ImageIcon(getClass().getResource(
                "/editor/res/tp_new.png")));
        // le asigna un nombre de comando
        buttonNew.setActionCommand("cmd_new");

        JButton buttonOpen = new JButton();
        buttonOpen.setToolTipText("Abrir");
        buttonOpen.setIcon(new ImageIcon(getClass().getResource(
                "/editor/res/tp_open.png")));
        buttonOpen.setActionCommand("cmd_open");

        JButton buttonSave = new JButton();
        buttonSave.setToolTipText("Guardar");
        buttonSave.setIcon(new ImageIcon(getClass().getResource(
                "/editor/res/tp_save.png")));
        buttonSave.setActionCommand("cmd_save");

        JButton buttonSaveAs = new JButton();
        buttonSaveAs.setToolTipText("Guardar como...");
        buttonSaveAs.setIcon(new ImageIcon(getClass().getResource(
                "/editor/res/tp_saveas.png")));
        buttonSaveAs.setActionCommand("cmd_saveas");

        JButton buttonPrint = new JButton();
        buttonPrint.setToolTipText("Imprimir");
        buttonPrint.setIcon(new ImageIcon(getClass().getResource(
                "/editor/res/tp_print.png")));
        buttonPrint.setActionCommand("cmd_print");

        buttonUndo = new JButton();
        buttonUndo.setEnabled(false);
        buttonUndo.setToolTipText("Deshacer");
        buttonUndo.setIcon(new ImageIcon(getClass().getResource(
                "/editor/res/tp_undo.png")));
        buttonUndo.setActionCommand("cmd_undo");

        buttonRedo = new JButton();
        buttonRedo.setEnabled(false);
        buttonRedo.setToolTipText("Rehacer");
        buttonRedo.setIcon(new ImageIcon(getClass().getResource(
                "/editor/res/tp_redo.png")));
        buttonRedo.setActionCommand("cmd_redo");

        JButton buttonCut = new JButton();
        buttonCut.setToolTipText("Cortar");
        buttonCut.setIcon(new ImageIcon(getClass().getResource(
                "/editor/res/tp_cut.png")));
        buttonCut.setActionCommand("cmd_cut");

        JButton buttonCopy = new JButton();
        buttonCopy.setToolTipText("Copiar");
        buttonCopy.setIcon(new ImageIcon(getClass().getResource(
                "/editor/res/tp_copy.png")));
        buttonCopy.setActionCommand("cmd_copy");

        JButton buttonPaste = new JButton();
        buttonPaste.setToolTipText("Pegar");
        buttonPaste.setIcon(new ImageIcon(getClass().getResource(
                "/editor/res/tp_paste.png")));
        buttonPaste.setActionCommand("cmd_paste");

        JButton buttonCompile = new JButton();
        buttonCompile.setToolTipText("Compilar");
        buttonCompile.setIcon(new ImageIcon(getClass().getResource(
                "/editor/res/compile.png")));
        buttonCompile.setActionCommand("cmd_compile");

        buttonTraducir = new JToggleButton();
        buttonTraducir.setToolTipText("Traduccion");
        buttonTraducir.setIcon(new ImageIcon(getClass().getResource(
                "/editor/res/traslate.png")));
        buttonTraducir.setActionCommand("cmd_translate");

        JButton buttonExecute = new JButton();
        buttonExecute.setToolTipText("Ejecutar");
        buttonExecute.setIcon(new ImageIcon(getClass().getResource(
                "/editor/res/execute.png")));
        buttonExecute.setActionCommand("cmd_execute");

        JButton buttonDebug = new JButton();
        buttonDebug.setToolTipText("Debug");
        buttonDebug.setIcon(new ImageIcon(getClass().getResource(
                "/editor/res/debug.png")));
        buttonDebug.setActionCommand("cmd_debug");
        buttonDebug.setMnemonic('d');
        
        JButton buttonReset = new JButton();
        buttonReset.setToolTipText("Restart");
        buttonReset.setIcon(new ImageIcon(getClass().getResource(
                "/editor/res/restart.png")));
        buttonReset.setActionCommand("cmd_reset");
        
        
        textFieldLabel = new JLabel("Conversor Dec-Hexa");
        textFieldConvHexa = new JTextField();
        resultConvHexa = new JLabel();
        resultConvHexa.setMaximumSize(new Dimension(300, 100));
        textFieldConvHexa.setMaximumSize(new Dimension(100, 100));
        textFieldConvHexa.addActionListener(eventHandler);
        textFieldConvHexa.addFocusListener(new FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {

                String value = textFieldConvHexa.getText();
                Conversor c = new Conversor();
                try {

                    resultConvHexa.setText(c.decToHexa(value));
                } catch (NumberFormatException e) {

                    resultConvHexa.setText(e.getMessage());
                }

            }
        });
        
        
        pcLabel = new JLabel("Program Counter: ");
        valuePCLabel = new JLabel("1");

        
        jToolBar.add(buttonNew); // se añaden los botones construidos a la barra
        // de herramientas
        jToolBar.add(buttonOpen);
        jToolBar.add(buttonSave);
        jToolBar.add(buttonSaveAs);
        jToolBar.addSeparator(); // añade separadores entre algunos botones
        jToolBar.add(buttonPrint);
        jToolBar.addSeparator();
        jToolBar.add(buttonUndo);
        jToolBar.add(buttonRedo);
        jToolBar.addSeparator();
        jToolBar.add(buttonCut);
        jToolBar.add(buttonCopy);
        jToolBar.add(buttonPaste);
        jToolBar.addSeparator();
        jToolBar.add(buttonCompile);
        jToolBar.addSeparator();
        jToolBar.add(buttonTraducir);
        jToolBar.add(buttonExecute);
        jToolBar.add(buttonDebug);
        jToolBar.add(buttonReset);
        jToolBar.addSeparator();
        jToolBar.add(textFieldLabel);
        jToolBar.add(textFieldConvHexa);
        jToolBar.add(resultConvHexa);
        jToolBar.addSeparator();
        jToolBar.add(pcLabel);
        jToolBar.add(valuePCLabel);

        /**
         * itera sobre todos los componentes de la barra de herramientas, se les
         * asigna el mismo margen y el mismo manejador de eventos unicamente a
         * los botones
         */
        for (Component c : jToolBar.getComponents()) {
            // si el componente es un botón
            if (c.getClass().equals(javax.swing.JButton.class)) {
                JButton jb = (JButton) c;
                jb.setMargin(new Insets(0, 0, 0, 0));
                jb.addActionListener(eventHandler);
            } else if (c.getClass().equals(javax.swing.JToggleButton.class)) {
                JToggleButton jb = (JToggleButton) c;
                jb.setMargin(new Insets(0, 0, 0, 0));
                jb.addActionListener(eventHandler);
            }
        }
    }

    /**
     * Construye la barra de estado.
     */
    private void buildStatusBar() {
        statusBar = new JPanel(); // construye un JPanel
        // se configura con un BoxLayout
        statusBar.setLayout(new BoxLayout(statusBar, BoxLayout.LINE_AXIS));
        // le añade un borde compuesto
        statusBar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLoweredBevelBorder(),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        // construye la etiqueta para mostrar la ubicación del archivo actual
        sbFilePath = new JLabel("...");
        // construye la etiqueta para mostrar el tamaño del archivo actual
        sbFileSize = new JLabel("");
        // construye la etiqueta para mostrar la posición del cursor en el
        // documento actual
        sbCaretPos = new JLabel("...");

        /**
         * se añaden las etiquetas construidas al JPanel, el resultado es un
         * panel similar a una barra de estado
         */
        statusBar.add(sbFilePath);
        statusBar.add(Box.createRigidArea(new Dimension(10, 0)));
        statusBar.add(sbFileSize);
        statusBar.add(Box.createRigidArea(new Dimension(10, 0)));
        statusBar.add(Box.createHorizontalGlue());
        statusBar.add(sbCaretPos);
    }

    /**
     * Construye el menú emergente.
     */
    private void buildPopupMenu() {
        jPopupMenu = new JPopupMenu(); // construye un JPopupMenu

        // construye el item "Deshacer"
        mpItemUndo = new JMenuItem("Deshacer");
        // se configura desactivado por defecto
        mpItemUndo.setEnabled(false);
        // le asigna un nombre de comando
        mpItemUndo.setActionCommand("cmd_undo");

        mpItemRedo = new JMenuItem("Rehacer");
        mpItemRedo.setEnabled(false);
        mpItemRedo.setActionCommand("cmd_redo");

        JMenuItem itemCut = new JMenuItem("Cortar");
        itemCut.setActionCommand("cmd_cut");

        JMenuItem itemCopy = new JMenuItem("Copiar");
        itemCopy.setActionCommand("cmd_copy");

        JMenuItem itemPaste = new JMenuItem("Pegar");
        itemPaste.setActionCommand("cmd_paste");

        JMenuItem itemGoto = new JMenuItem("Ir a...");
        itemGoto.setActionCommand("cmd_gotoline");

        JMenuItem itemSearch = new JMenuItem("Buscar");
        itemSearch.setActionCommand("cmd_search");

        JMenuItem itemSearchNext = new JMenuItem("Buscar siguiente");
        itemSearchNext.setActionCommand("cmd_searchnext");

        JMenuItem itemSelectAll = new JMenuItem("Seleccionar todo");
        itemSelectAll.setActionCommand("cmd_selectall");

        jPopupMenu.add(mpItemUndo); // se añaden los items al menú emergente
        jPopupMenu.add(mpItemRedo);
        jPopupMenu.addSeparator(); // añade separadores entre algunos items
        jPopupMenu.add(itemCut);
        jPopupMenu.add(itemCopy);
        jPopupMenu.add(itemPaste);
        jPopupMenu.addSeparator();
        jPopupMenu.add(itemGoto);
        jPopupMenu.add(itemSearch);
        jPopupMenu.add(itemSearchNext);
        jPopupMenu.addSeparator();
        jPopupMenu.add(itemSelectAll);

        /**
         * itera sobre todos los componentes del menú emergente, se les asigna
         * el mismo manejador de eventos a todos excepto a los separadores
         */
        for (Component c : jPopupMenu.getComponents()) {
            // si el componente es un item
            if (c.getClass().equals(javax.swing.JMenuItem.class)) {
                ((JMenuItem) c).addActionListener(eventHandler);
            }
        }
    }

    private void buidTitlePanel(JScrollPane scrollPane, JLabel labeltitle) {
        labeltitle.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLoweredBevelBorder(),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        scrollPane.setColumnHeaderView(labeltitle);
    }

    /**
     * Construye la barra de estado.
     */
    private void buildColumnLineCounter(TextLineNumber columnLineCounter, JScrollPane scrollPane) {

        // se configura con un BoxLayout
        columnLineCounter.setLayout(new BoxLayout(columnLineCounter,
                BoxLayout.PAGE_AXIS));
        // le añade un borde compuesto
        columnLineCounter.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLoweredBevelBorder(),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        scrollPane.setRowHeaderView(columnLineCounter);

        /**
         * se añaden las etiquetas construidas al JPanel, el resultado es un
         * panel similar a una barra de estado
         */
        columnLineCounter.add(Box.createRigidArea(new Dimension(10, 0)));
        columnLineCounter.add(Box.createRigidArea(new Dimension(10, 0)));
        columnLineCounter.add(Box.createHorizontalGlue());
    }
    
    private void buildHighlighterDebug(int programcounter) {
        try {
        	int linecounter = programcounter - 1;
        	int startOffset = jTextArea.getLineStartOffset(linecounter);
        	int endOffset = jTextArea.getLineEndOffset(linecounter);
        	
        	Highlighter hilit = new DefaultHighlighter();
        	jTextArea.setHighlighter(hilit);
        	jTextArea.getHighlighter().removeAllHighlights();
			jTextArea.getHighlighter().addHighlight(startOffset, endOffset, new DefaultHighlighter.DefaultHighlightPainter(Color.ORANGE));
			jTextArea.setCaretPosition(startOffset);
			jTextArea.repaint();
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    /**
     * Hace visible el menú emergente.
     *
     * @param me evento del ratón
     */
    private void showPopupMenu(MouseEvent me) {
        if (me.isPopupTrigger() == true) { // si el evento es el desencadenador
            // de menú emergente
            // hace visible el menú emergente en las coordenadas actuales del
            // ratón
            jPopupMenu.show(me.getComponent(), me.getX(), me.getY());
        }
    }

    /**
     * Actualiza el estado de las opciones "Deshacer" y "Rehacer".
     */
    void updateControls() {
        // averigua si se pueden deshacer los cambios en el documento actual
        boolean canUndo = undoManager.canUndo();
        // averigua si se pueden rehacer los cambios en el documento actual
        boolean canRedo = undoManager.canRedo();

        // activa o desactiva las opciones en la barra de menú
        mbItemUndo.setEnabled(canUndo);
        mbItemRedo.setEnabled(canRedo);

        // activa o desactiva las opciones en la barra de herramientas
        buttonUndo.setEnabled(canUndo);
        buttonRedo.setEnabled(canRedo);

        // activa o desactiva las opciones en el menú emergente
        mpItemUndo.setEnabled(canUndo);
        mpItemRedo.setEnabled(canRedo);
    }

    private void enableTraslate() {
        if (buttonTraducir.isSelected()) {
            splitPaneMain.setDividerLocation(lastdividerPosition);
            splitPaneMain.setDividerSize(10);
            doTraslate();
        } else {
            lastdividerPosition = splitPaneMain.getDividerLocation();
            splitPaneMain.setDividerLocation(1000000);
            splitPaneMain.setDividerSize(0);
        }
    }
 
    public void enableDebug() {
        if (in_debug) {
            splitPaneDebug.setVisible(true);
            portPanel.setVisible(true);
            registerPanel.setVisible(true);
            aluPanel.setVisible(true);
        } 
        else if (!in_debug) {
            splitPaneDebug.setVisible(false);
            registerPanel.setVisible(false);
            portPanel.setVisible(false);
            aluPanel.setVisible(false);
        }

    }
    
    public void resetDebug() {
        consolePanel.clear();
        memoryPanel.resetValor("BASURA");
        registerPanel.resetValor("BASURA");
        portPanel.resetValor("BASURA", "NN");
        aluPanel.resetValor();
    }
        

    public void enableError(boolean enable) {
        if (enable) {
            errorPanel.setVisible(true);
        } else {
            errorPanel.setVisible(false);
        }
    }

    private void doTraslate() {
        try {
            String text = jTextArea.getDocument().getText(0, jTextArea.getDocument().getLength());
            ParserAssembler parser = new ParserAssembler(new Scanner(text).getTokens());
            jTextAreaTranslate.getDocument().remove(0, jTextAreaTranslate.getDocument().getLength());
            jTextAreaTranslate.setText(parser.translate());
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retorna la instancia de EventHandler, la clase interna que maneja
     * eventos.
     *
     * @return el manejador de eventos.
     */
    EventHandler getEventHandler() {
        return eventHandler;
    }

    /**
     * Retorna la instancia de UndoManager, la cual administra las ediciones
     * sobre el documento en el Área de texto.
     *
     * @return el administrador de edición.
     */
    UndoManager getUndoManager() {
        return undoManager;
    }

    /**
     * Retorna el estado del documento actual.
     *
     * @return true si ah sido modificado, false en caso contrario
     */
    boolean documentHasChanged() {
        return hasChanged;
    }

    public LanguajeType getLanguajeType() {
        return languajeType;
    }

    public void setLanguajeType(LanguajeType languajeType) {
        this.languajeType = languajeType;
        if (languajeType == LanguajeType.MACHINE) {
            errorParser = new MachineErrorParser(jTextArea);
            this.columnLineCounter.setMode(Mode.HEXADECIMAL);
        } else if (languajeType == LanguajeType.ASSEMBLER) {
            errorParser = new NataliusErrorParser(jTextArea);
            this.columnLineCounter.setMode(Mode.DECIMAL);
        }
    }

    public TextLineNumber getColumnLineCounter() {
        return columnLineCounter;
    }

    public TextLineNumber getColumnLineCounterTranslate() {
        return columnLineCounterTranslate;
    }

    public JLabel getLabeltitle() {
        return labeltitle;
    }

    public JLabel getLabeltitleTraslate() {
        return labeltitleTraslate;
    }

    /**
     * Establece el estado del documento actual.
     *
     * @param hasChanged true si ah sido modificado, false en caso contrario
     */
    void setDocumentChanged(boolean hasChanged) {
        this.hasChanged = hasChanged;
    }

    /**
     * Retorna la instancia de JTextArea, el Área de edición.
     *
     * @return retorna el Área de edición.
     */
    JTextArea getJTextArea() {
        return jTextArea;
    }

    JTextArea getJTextAreaTranslate() {
        return jTextAreaTranslate;
    }

    /**
     * Retorna la instancia de JFrame, la ventana principal del editor.
     *
     * @return la ventana principal del editor.
     */
    JFrame getJFrame() {
        return jFrame;
    }

    /**
     * Retorna la instancia de File, el archivo actual.
     *
     * @return el archivo actual
     */
    File getCurrentFile() {
        return currentFile;
    }

    /**
     * Establece el archivo actual.
     *
     * @param currentFile el archivo actual
     */
    void setCurrentFile(File currentFile) {
        this.currentFile = currentFile;
    }

    /**
     * Retorna la instancia de la etiqueta sbFilePath, donde se muestra la
     * ubicación del archivo actual.
     *
     * @return la instancia de la etiqueta sbFilePath
     */
    JLabel getJLabelFilePath() {
        return sbFilePath;
    }

    /**
     * Retorna la instancia de la etiqueta sbFileSize, donde se muestra el
     * tamaño del archivo actual
     *
     * @return la instancia de la etiqueta sbFileSize
     */
    JLabel getJLabelFileSize() {
        return sbFileSize;
    }

    /**
     * @return
     */
    private AutoSuggestor newAutoSuggestor() {
        return new AutoSuggestor(jTextArea, jFrame, words,
                Color.WHITE.brighter(), Color.BLUE, Color.RED, 0.75f) {
                    @Override
                    boolean wordTyped(String typedWord) {
                        System.out.println(typedWord);
                        return super.wordTyped(typedWord);// checks for a match in
                        // dictionary and returns
                        // true or false if found or
                        // not
                    }
                };
    }

    private void loadLanguageWords() {
        words = new ArrayList<String>();
        try {
            reader = new BufferedReader(new FileReader(
                    "./src/editor/res/reservedWords.properties"));
            String linea = reader.readLine();
            while (linea != null) {
                if (!linea.isEmpty()) {
                    words.add(linea.trim());
                }

                linea = reader.readLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
    	if (!in_debug)
    		errorParser.checkError();
    }

    /**
     * Clase interna que extiende e implementa las clases e interfaces
     * necesarias para atender y manejar los eventos sobre la GUI principal del
     * editor.
     */
    class EventHandler extends MouseAdapter implements ActionListener,
            CaretListener, UndoableEditListener, KeyListener {

        private boolean keytyped = false;

        /**
         * Atiende y maneja los eventos de acción.
         *
         * @param ae evento de acción
         */
        @Override
        public void actionPerformed(ActionEvent ae) {
            String ac = ae.getActionCommand(); // se obtiene el nombre del
            // comando ejecutado

            if (ac.equals("cmd_new_asm") == true) { // opción seleccionada:
                // "Nuevo"
                setLanguajeType(LanguajeType.ASSEMBLER);
                //jTextArea.setColumns(4);
                errorParser = new NataliusErrorParser(jTextArea);
                actionPerformer.actionNew();
            } else if (ac.equals("cmd_new_maq") == true) { // opción
                // seleccionada:
                // "Nuevo"
                setLanguajeType(LanguajeType.MACHINE);
                errorParser = new MachineErrorParser(jTextArea);
                actionPerformer.actionNew();
            } else if (ac.equals("cmd_open") == true) { // opción seleccionada:
                // "Abrir"
                actionPerformer.actionOpen();
                enableTraslate();
            } else if (ac.equals("cmd_save") == true) { // opción seleccionada:
                // "Guardar"
                actionPerformer.actionSave();
            } else if (ac.equals("cmd_saveas") == true) { // opción
                // seleccionada:
                // "Guardar como"
                actionPerformer.actionSaveAs();
            } else if (ac.equals("cmd_print") == true) { // opción seleccionada:
                // "Imprimir"
                actionPerformer.actionPrint();
            } else if (ac.equals("cmd_exit") == true) { // opción seleccionada:
                // "Salir"
                actionPerformer.actionExit();
            } else if (ac.equals("cmd_undo") == true) { // opción seleccionada:
                // "Deshacer"
                actionPerformer.actionUndo();
            } else if (ac.equals("cmd_redo") == true) { // opción seleccionada:
                // "Rehacer"
                actionPerformer.actionRedo();
            } else if (ac.equals("cmd_cut") == true) { // opción seleccionada:
                // "Cortar"
                // corta el texto seleccionado en el documento
                jTextArea.cut();
            } else if (ac.equals("cmd_copy") == true) { // opción seleccionada:
                // "Copiar"
                // copia el texto seleccionado en el documento
                jTextArea.copy();
            } else if (ac.equals("cmd_paste") == true) { // opción seleccionada:
                // "Pegar"
                // pega en el documento el texto del portapapeles
                jTextArea.paste();
            } else if (ac.equals("cmd_gotoline") == true) { // opción
                // seleccionada:
                // "Ir a la línea..."
                actionPerformer.actionGoToLine();
            } else if (ac.equals("cmd_search") == true) { // opción
                // seleccionada:
                // "Buscar"
                actionPerformer.actionSearch();
            } else if (ac.equals("cmd_searchnext") == true) { // opción
                // seleccionada:
                // "Buscar siguiente"
                actionPerformer.actionSearchNext();
            } else if (ac.equals("cmd_selectall") == true) { // opción
                // seleccionada:
                // "Seleccionar todo"
                jTextArea.selectAll();
            } else if (ac.equals("cmd_linewrap") == true) { // opción
                // seleccionada:
                // "Ajuste de línea"
                // si esta propiedad esta activada se desactiva, o lo inverso
                jTextArea.setLineWrap(!jTextArea.getLineWrap());
                jTextArea.setWrapStyleWord(!jTextArea.getWrapStyleWord());
            } else if (ac.equals("cmd_showtoolbar") == true) { // opción
                // seleccionada:
                // "Ver barra de herramientas"
                // si la barra de herramientas esta visible se oculta, o lo
                // inverso
                jToolBar.setVisible(!jToolBar.isVisible());
            } else if (ac.equals("cmd_fixedtoolbar") == true) { // opción
                // seleccionada:
                // "Fijar barra de herramientas"
                // si esta propiedad esta activada se desactiva, o lo inverso
                jToolBar.setFloatable(!jToolBar.isFloatable());
            } else if (ac.equals("cmd_showstatusbar") == true) { // opción
                // seleccionada:
                // "Ver barra de estado"
                // si la barra de estado esta visible se oculta, o lo inverso
                statusBar.setVisible(!statusBar.isVisible());
            } else if (ac.equals("cmd_font") == true) { // opción seleccionada:
                // "Fuente de letra"
                actionPerformer.actionSelectFont();
            } else if (ac.equals("cmd_fontcolor") == true) { // opción
                // seleccionada:
                // "Color de letra"
                actionPerformer.actionSelectFontColor();
            } else if (ac.equals("cmd_backgroundcolor") == true) { // opción
                // seleccionada:
                // "Color de fondo"
                actionPerformer.actionSelectBackgroundColor();
            } else if (ac.equals("cmd_about") == true) { // opción seleccionada:
                // "Acerca de"
                // presenta un dialogo modal con alguna informacion
                JOptionPane.showMessageDialog(jFrame, "Generic Sim",
                        "Acerca de", JOptionPane.INFORMATION_MESSAGE);
            } else if (ac.equals("cmd_compile") == true) { // opción
                // seleccionada:
                // "Compilar"
                // presenta un dialogo modal con alguna informacion
                actionPerformer.actionCompile();
                /*problemPanel.setDividerLocation(lastproblemPanelLocation);
                 problemPanel.setDividerSize(10);*/

            } else if (ac.equals("cmd_translate") == true) {	// opción
                enableTraslate();
            } else if (ac.equals("cmd_execute") == true) {	// opción
            	in_debug = true;
            	resetDebug();
                enableDebug();
                enableError(false);
                
                currMachineState = actionPerformer.actionExecute();
                registerPanel.loadRegisterValues(currMachineState.getRegControl().getRegisterState());
                memoryPanel.loadMemoryValues(currMachineState.getMemControl().getMemoryState());
                portPanel.setValorAndMark(Integer.toHexString(currMachineState.getMemControl().getPortValue(253)), currMachineState.getMemControl().getValue(252).toString(), 0);
                portPanel.setValorAndMark(Integer.toHexString(currMachineState.getMemControl().getPortValue(255)), currMachineState.getMemControl().getValue(254).toString(), 1);
                aluPanel.setValorAndMark(Integer.toHexString(currMachineState.getAlu().getCarry()),"3",0);
                aluPanel.setValorAndMark(Integer.toHexString(currMachineState.getAlu().getOverflow()),"4",1);
                aluPanel.setValorAndMark(Integer.toHexString(currMachineState.getAlu().getPresicion()),"5",2);
                valuePCLabel.setText(currMachineState.getProgramCounter().toString());
                valuePCLabel.repaint();
                
                in_debug = false;
            } else if (ac.equals("cmd_debug") == true) {// opción
            	if (!in_debug) {
            		in_debug = true;
            		resetDebug();
            	}
            	enableDebug();
                enableError(false);
                
                currMachineState = actionPerformer.actionDebug();
                registerPanel.loadRegisterValues(currMachineState.getRegControl().getRegisterState());
                memoryPanel.loadMemoryValues(currMachineState.getMemControl().getMemoryState());
                  portPanel.setValorAndMark(Integer.toHexString(currMachineState.getMemControl().getPortValue(253)), currMachineState.getMemControl().getValue(252).toString(), 0);
                portPanel.setValorAndMark(Integer.toHexString(currMachineState.getMemControl().getPortValue(255)), currMachineState.getMemControl().getValue(254).toString(), 1);
                aluPanel.setValorAndMark(Integer.toHexString(currMachineState.getAlu().getCarry()),"3",0);
                aluPanel.setValorAndMark(Integer.toHexString(currMachineState.getAlu().getOverflow()),"4",1);
                aluPanel.setValorAndMark(Integer.toHexString(currMachineState.getAlu().getPresicion()),"5",1);
                valuePCLabel.setText(currMachineState.getProgramCounter().toString());
                valuePCLabel.repaint();
                
                buildHighlighterDebug(currMachineState.getProgramCounter());
                
                if (currMachineState.getProgramCounter() > currMachineState.getTotalProgramInsturctions()) {
                	in_debug = false;
                }
            } else if (ac.equals("cmd_reset") == true) {	// opción
            	in_debug = true;
            	resetDebug();
                enableDebug();
                enableError(false);
                
                currMachineState = actionPerformer.actionRestart();
                registerPanel.loadRegisterValues(currMachineState.getRegControl().getRegisterState());
                memoryPanel.loadMemoryValues(currMachineState.getMemControl().getMemoryState());
                valuePCLabel.setText(currMachineState.getProgramCounter().toString());
                valuePCLabel.repaint();
                in_debug = false;
            }
        }

        /**
         * Atiende y maneja los eventos del cursor.
         *
         * @param ce evento del cursor
         */
        @Override
        public void caretUpdate(CaretEvent e) {
            final int caretPos; // valor de la posición del cursor sin
            // inicializar
            int y = 1; // valor de la línea inicialmente en 1
            int x = 1; // valor de la columna inicialmente en 1

            try {
                // obtiene la posición del cursor con respecto al inicio del
                // JTextArea (Área de edición)
                caretPos = jTextArea.getCaretPosition();
                // sabiendo lo anterior se obtiene el valor de la línea actual
                // (se cuenta desde 0)
                y = jTextArea.getLineOfOffset(caretPos);

                /**
                 * a la posición del cursor se le resta la posición del inicio
                 * de la línea para determinar el valor de la columna actual
                 */
                x = caretPos - jTextArea.getLineStartOffset(y);

                // al valor de la línea actual se le suma 1 porque estas
                // comienzan contÃ¡ndose desde 0
                y += 1;
            } catch (BadLocationException ex) { // en caso de que ocurra una
                // excepción
                System.err.println(ex);
            }

            /**
             * muestra la información recolectada en la etiqueta sbCaretPos de
             * la barra de estado, también se incluye el número total de lineas
             */
            sbCaretPos.setText("Ln: " + jTextArea.getLineCount() + " - Col: "
                    + x);
        }

        /**
         * Atiende y maneja los eventos sobre el documento en el Área de
         * edición.
         *
         * @param uee evento de edición
         */
        @Override
        public void undoableEditHappened(UndoableEditEvent uee) {
            /**
             * el cambio realizado en el Área de edición se guarda en el buffer
             * del administrador de edición
             */
            undoManager.addEdit(uee.getEdit());
            updateControls(); // actualiza el estado de las opciones "Deshacer"
            // y "Rehacer"

            hasChanged = true;
        }

        /**
         * Atiende y maneja los eventos sobre el ratón cuando este es
         * presionado.
         *
         * @param me evento del ratón
         */
        @Override
        public void mousePressed(MouseEvent me) {
            showPopupMenu(me);
        }

        @Override
        public void mouseMoved(java.awt.event.MouseEvent evt) {

            Highlighter.Highlight[] highlights = jTextArea.getHighlighter().getHighlights();

            int offset = jTextArea.viewToModel(evt.getPoint());

            jTextArea.setToolTipText(null);

            for (int i = 0; i < highlights.length; i++) {

                if (highlights[i].getStartOffset() < offset && offset < highlights[i].getEndOffset()) {

                    jTextArea.setToolTipText(highlights[i].getPainter().toString());

                    break;
                }

            }

        }

        /**
         * Atiende y maneja los eventos sobre el ratón cuando este es liberado.
         *
         * @param me evento del ratón
         */
        @Override
        public void mouseReleased(MouseEvent me) {
            showPopupMenu(me);
        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == 38 || e.getKeyCode() == 40) {
                if (buttonTraducir.isSelected() && keytyped) {
                    doTraslate();
                }

                keytyped = false;
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            // TODO Auto-generated method stub
        }

        @Override
        public void keyTyped(KeyEvent e) {
            keytyped = true;

            char c = e.getKeyChar();
            if (c == '\n') {
                // AcÃ¡ se buscarían en la base de datos de conocimientos para la
                // ayuda en línea

                if (buttonTraducir.isSelected()) {
                    // traduce el nuevo texto
                    doTraslate();
                }
            }
            // Detecta el evento Ctrol+Space
            if ((e.getModifiers() & ActionEvent.CTRL_MASK) == ActionEvent.CTRL_MASK
                    && e.getKeyChar() == ' ') {
                autoSuggestor.checkForAndShowSuggestions();
            }
        }
    }

    public Console getConsole() {

        return consolePanel;
    }

}
