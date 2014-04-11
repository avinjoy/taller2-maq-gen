/**
 * ActionPerformer.java
 */
 
package editor;
 
import java.awt.Color;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Pattern;

import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import editor.TextLineNumber.Mode;
 
/**
 * Clase que ejecuta las operaciones solicitadas.
 * 
 * @author Dark[byte]
 */
public class ActionPerformer {
 
    private final Editor tpEditor;    //instancia de TPEditor (la clase principal)
    private String lastSearch = "";     //la ï¿½ltima bï¿½squeda de texto realizada, por defecto no contiene nada
 
    /**
     * Constructor de la clase.
     * 
     * @param tpEditor clase principal
     */
    public ActionPerformer(Editor tpEditor) {
        this.tpEditor = tpEditor;    //guarda la instancia de la clase TPEditor
    }
 
    /**
     * Opciï¿½n seleccionada: "Nuevo".
     * 
     * Reemplaza el documento actual por uno nuevo vacï¿½o.
     */
    public void actionNew() {
        if (tpEditor.documentHasChanged() == true) {    //si el documento esta marcado como modificado
            //le ofrece al usuario guardar los cambios
            int option = JOptionPane.showConfirmDialog(tpEditor.getJFrame(), "¿Desea guardar los cambios?");
 
            switch (option) {
                case JOptionPane.YES_OPTION:       //si elige que si
                    actionSave();                  //guarda el archivo
                    break;
                case JOptionPane.CANCEL_OPTION:    //si elige cancelar
                    return;                        //cancela esta operaciï¿½n
                //en otro caso se continï¿½a con la operaciï¿½n y no se guarda el documento actual
            }
        }
 
        tpEditor.getJFrame().setTitle("Generic Sim - Sin Tï¿½tulo");    //nuevo tï¿½tulo de la ventana
 
        //limpia el contenido del area de ediciï¿½n
        tpEditor.getJTextArea().setText("");
        tpEditor.getJTextAreaTranslate().setText("");

        if (tpEditor.getLanguajeType() == 0) {
        	tpEditor.getColumnLineCounter().setMode(Mode.DECIMAL);
        	tpEditor.getColumnLineCounterTranslate().setMode(Mode.DECIMAL);
        	tpEditor.getLabeltitle().setText("Codigo Maquina");
        	tpEditor.getLabeltitleTraslate().setText("Assembler");
        } else {
        	tpEditor.getColumnLineCounter().setMode(Mode.DECIMAL);
        	tpEditor.getColumnLineCounterTranslate().setMode(Mode.DECIMAL);
        	tpEditor.getLabeltitle().setText("Assembler");
        	tpEditor.getLabeltitleTraslate().setText("Codigo Maquina");
        }
        
        //limpia el contenido de las etiquetas en la barra de estado
        tpEditor.getJLabelFilePath().setText("");
        tpEditor.getJLabelFileSize().setText("");
 
        tpEditor.getUndoManager().die();    //limpia el buffer del administrador de ediciï¿½n
        tpEditor.updateControls();          //actualiza el estado de las opciones "Deshacer" y "Rehacer"
 
        //el archivo asociado al documento actual se establece como null
        tpEditor.setCurrentFile(null);
        //marca el estado del documento como no modificado
        tpEditor.setDocumentChanged(false);
    }
 
    /**
     * Opciï¿½n seleccionada: "Abrir".
     * 
     * Le permite al usuario elegir un archivo para cargar en el ï¿½rea de ediciï¿½n.
     */
    public void actionOpen() {
        if (tpEditor.documentHasChanged() == true) {    //si el documento esta marcado como modificado
            //le ofrece al usuario guardar los cambios
            int option = JOptionPane.showConfirmDialog(tpEditor.getJFrame(), "¿Desea guardar los cambios?");
 
            switch (option) {
                case JOptionPane.YES_OPTION:     //si elige que si
                    actionSave();               //guarda el archivo
                    break;
                case JOptionPane.CANCEL_OPTION:  //si elige cancelar
                    return;                      //cancela esta operaciï¿½n
                //en otro caso se continï¿½a con la operaciï¿½n y no se guarda el documento actual
            }
        }
 
        JFileChooser fc = getJFileChooser();    //obtiene un JFileChooser
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "ASM & MAQ Files", "asm", "maq");
        fc.setFileFilter(filter);
        fc.setAcceptAllFileFilterUsed(false);
        
        //presenta un dialogo modal para que el usuario seleccione un archivo
        int state = fc.showOpenDialog(tpEditor.getJFrame());
 
        if (state == JFileChooser.APPROVE_OPTION) {    //si elige abrir el archivo
            File f = fc.getSelectedFile();    //obtiene el archivo seleccionado
 
            try {
                //abre un flujo de datos desde el archivo seleccionado
                BufferedReader br = new BufferedReader(new FileReader(f));
                //lee desde el flujo de datos hacia el area de ediciï¿½n
                tpEditor.getJTextArea().read(br, null);
                br.close();    //cierra el flujo
 
                tpEditor.getJTextArea().getDocument().addUndoableEditListener(tpEditor.getEventHandler());
 
                tpEditor.getUndoManager().die();    //se limpia el buffer del administrador de ediciï¿½n
                tpEditor.updateControls();          //se actualiza el estado de las opciones "Deshacer" y "Rehacer"
 
                //nuevo tï¿½tulo de la ventana con el nombre del archivo cargado
                tpEditor.getJFrame().setTitle("Generic Sim - " + f.getName());
 
                //muestra la ubicaciï¿½n del archivo actual
                tpEditor.getJLabelFilePath().setText(shortPathName(f.getAbsolutePath()));
                //muestra el tamaï¿½o del archivo actual
                tpEditor.getJLabelFileSize().setText(roundFileSize(f.length()));
 
                //establece el archivo cargado como el archivo actual
                tpEditor.setCurrentFile(f);
                //marca el estado del documento como no modificado
                tpEditor.setDocumentChanged(false);
            } catch (IOException ex) {    //en caso de que ocurra una excepciï¿½n
                //presenta un dialogo modal con alguna informaciï¿½n de la excepciï¿½n
                JOptionPane.showMessageDialog(tpEditor.getJFrame(),
                                              ex.getMessage(),
                                              ex.toString(),
                                              JOptionPane.ERROR_MESSAGE);
            }
        }
    }
 
    /**
     * Opciï¿½n seleccionada: "Guardar".
     * 
     * Guarda el documento actual en el archivo asociado actualmente.
     */
    public void actionSave() {
        if (tpEditor.getCurrentFile() == null) {    //si no hay un archivo asociado al documento actual
            actionSaveAs();    //invoca el mï¿½todo actionSaveAs()
        } else if (tpEditor.documentHasChanged() == true) {    //si el documento esta marcado como modificado
            try {
                //abre un flujo de datos hacia el archivo asociado al documento actual
                BufferedWriter bw = new BufferedWriter(new FileWriter(tpEditor.getCurrentFile()));
                //escribe desde el flujo de datos hacia el archivo
                tpEditor.getJTextArea().write(bw);
                bw.close();    //cierra el flujo
 
                //marca el estado del documento como no modificado
                tpEditor.setDocumentChanged(false);
            } catch (IOException ex) {    //en caso de que ocurra una excepciï¿½n
                //presenta un dialogo modal con alguna informaciï¿½n de la excepciï¿½n
                JOptionPane.showMessageDialog(tpEditor.getJFrame(),
                                              ex.getMessage(),
                                              ex.toString(),
                                              JOptionPane.ERROR_MESSAGE);
            }
        }
    }
 
    /**
     * Opciï¿½n seleccionada: "Guardar como".
     * 
     * Le permite al usuario elegir la ubicaciï¿½n donde se guardarï¿½ el documento actual.
     */
    public void actionSaveAs() {
        JFileChooser fc = getJFileChooser();    //obtiene un JFileChooser
 
        //presenta un dialogo modal para que el usuario seleccione un archivo
        int state = fc.showSaveDialog(tpEditor.getJFrame());
        if (state == JFileChooser.APPROVE_OPTION) {    //si elige guardar en el archivo
            File f = fc.getSelectedFile();    //obtiene el archivo seleccionado
 
            try {
                //abre un flujo de datos hacia el archivo asociado seleccionado
                BufferedWriter bw = new BufferedWriter(new FileWriter(f));
                //escribe desde el flujo de datos hacia el archivo
                tpEditor.getJTextArea().write(bw);
                bw.close();    //cierra el flujo
 
                //nuevo tï¿½tulo de la ventana con el nombre del archivo guardado
                tpEditor.getJFrame().setTitle("Generic Sim - " + f.getName());
 
                //muestra la ubicaciï¿½n del archivo guardado
                tpEditor.getJLabelFilePath().setText(shortPathName(f.getAbsolutePath()));
                //muestra el tamaï¿½o del archivo guardado
                tpEditor.getJLabelFileSize().setText(roundFileSize(f.length()));
 
                //establece el archivo guardado como el archivo actual
                tpEditor.setCurrentFile(f);
                //marca el estado del documento como no modificado
                tpEditor.setDocumentChanged(false);
            } catch (IOException ex) {    //en caso de que ocurra una excepciï¿½n
                //presenta un dialogo modal con alguna informaciï¿½n de la excepciï¿½n
                JOptionPane.showMessageDialog(tpEditor.getJFrame(),
                                              ex.getMessage(),
                                              ex.toString(),
                                              JOptionPane.ERROR_MESSAGE);
            }
        }
    }
 
    /**
     * Opciï¿½n seleccionada: "Imprimir".
     * 
     * Imprime el documento actual.
     */
    @SuppressWarnings("unused")
	public void actionPrint() {
        boolean result = false;    //resultado de la impresiï¿½n, por defecto es false
 
        //si el documento actual no esta vacï¿½o
        if (tpEditor.getJTextArea().getText().trim().equals("") == false) {
            //invoca nuestra la clase PrintAction para presentar el dialogo de impresiï¿½n
            result = PrintAction.print(tpEditor.getJTextArea(), tpEditor.getJFrame());
        }
    }
 
    /**
     * Opciï¿½n seleccionada: "Salir".
     * 
     * Finaliza el programa.
     */
    public void actionExit() {
        if (tpEditor.documentHasChanged() == true) {    //si el documento esta marcado como modificado
            //le ofrece al usuario guardar los cambios
            int option = JOptionPane.showConfirmDialog(tpEditor.getJFrame(), "¿Desea guardar los cambios?");
 
            switch (option) {
                case JOptionPane.YES_OPTION:     //si elige que si
                    actionSave();                //guarda el archivo
                    break;
                case JOptionPane.CANCEL_OPTION:  //si elige cancelar
                    return;                      //cancela esta operaciï¿½n
                //en otro caso se continï¿½a con la operaciï¿½n y no se guarda el documento actual
            }
        }
 
 
        System.exit(0);    //finaliza el programa con el cï¿½digo 0 (sin errores)
    }
 
    /**
     * Opciï¿½n seleccionada: "Deshacer".
     * 
     * Deshace el ï¿½ltimo cambio realizado en el documento actual.
     */
    public void actionUndo() {
        try {
            //deshace el ï¿½ltimo cambio realizado sobre el documento en el ï¿½rea de ediciï¿½n
            tpEditor.getUndoManager().undo();
        } catch (CannotUndoException ex) {    //en caso de que ocurra una excepciï¿½n
            System.err.println(ex);
        }
 
        //actualiza el estado de las opciones "Deshacer" y "Rehacer"
        tpEditor.updateControls();
    }
 
    /**
     * Opciï¿½n seleccionada: "Rehacer".
     * 
     * Rehace el ï¿½ltimo cambio realizado en el documento actual.
     */
    public void actionRedo() {
        try {
            //rehace el ï¿½ltimo cambio realizado sobre el documento en el ï¿½rea de ediciï¿½n
            tpEditor.getUndoManager().redo();
        } catch (CannotRedoException ex) {    //en caso de que ocurra una excepciï¿½n
            System.err.println(ex);
        }
 
        //actualiza el estado de las opciones "Deshacer" y "Rehacer"
        tpEditor.updateControls();
    }
 
    /**
     * Opciï¿½n seleccionada: "Buscar".
     * 
     * Busca un texto especificado por el usuario en el documento actual. El texto queda 
     * guardado para bï¿½squedas siguientes.
     */
    public void actionSearch() {
        //solicita al usuario que introduzca el texto a buscar
        String text = JOptionPane.showInputDialog(
                tpEditor.getJFrame(),
                "Texto:",
                "Generic Sim - Buscar",
                JOptionPane.QUESTION_MESSAGE);
 
        if (text != null) {    //si se introdujo texto (puede ser una cadena vacï¿½a)
            String textAreaContent = tpEditor.getJTextArea().getText();    //obtiene todo el contenido del ï¿½rea de ediciï¿½n
            int pos = textAreaContent.indexOf(text);    //obtiene la posiciï¿½n de la primera ocurrencia del texto
 
            if (pos > -1) {    //si la posiciï¿½n es mayor a -1 significa que la bï¿½squeda fue positiva
                //selecciona el texto en el ï¿½rea de ediciï¿½n para resaltarlo
                tpEditor.getJTextArea().select(pos, pos + text.length());
            }
 
            //establece el texto buscado como el texto de la ï¿½ltima bï¿½squeda realizada
            lastSearch = text;
        }
    }
 
    /**
     * Opciï¿½n seleccionada: "Buscar siguiente".
     * 
     * Busca el texto de la ï¿½ltima bï¿½squeda en el documento actual.
     */
    public void actionSearchNext() {
        if (lastSearch.length() > 0) {    //si la ï¿½ltima bï¿½squeda contiene texto
            String textAreaContent = tpEditor.getJTextArea().getText();    //se obtiene todo el contenido del ï¿½rea de ediciï¿½n
            int pos = tpEditor.getJTextArea().getCaretPosition();    //se obtiene la posiciï¿½n del cursor sobre el ï¿½rea de ediciï¿½n
            //buscando a partir desde la posiciï¿½n del cursor, se obtiene la posiciï¿½n de la primera ocurrencia del texto
            pos = textAreaContent.indexOf(lastSearch, pos);
 
            if (pos > -1) {    //si la posiciï¿½n es mayor a -1 significa que la bï¿½squeda fue positiva
                //selecciona el texto en el ï¿½rea de ediciï¿½n para resaltarlo
                tpEditor.getJTextArea().select(pos, pos + lastSearch.length());
            }
        } else {    //si la ï¿½ltima bï¿½squeda no contiene nada
            actionSearch();    //invoca el mï¿½todo actionSearch()
        }
    }
 
    /**
     * Opciï¿½n seleccionada: "Ir a la lï¿½nea...".
     * 
     * Posiciona el cursor en el inicio de una lï¿½nea especificada por el usuario.
     */
    public void actionGoToLine() {
        //solicita al usuario que introduzca el nï¿½mero de lï¿½nea
        String line = JOptionPane.showInputDialog(
                tpEditor.getJFrame(),
                "Nï¿½mero:",
                "Generic Sim - Ir a la lï¿½nea...",
                JOptionPane.QUESTION_MESSAGE);
 
        if (line != null && line.length() > 0) {    //si se introdujo un dato
            try {
                int pos = Integer.parseInt(line);    //el dato introducido se convierte en entero
 
                //si el nï¿½mero de lï¿½nea esta dentro de los lï¿½mites del ï¿½rea de texto
                if (pos >= 0 && pos <= tpEditor.getJTextArea().getLineCount()) {
                    //posiciona el cursor en el inicio de la lï¿½nea
                    tpEditor.getJTextArea().setCaretPosition(tpEditor.getJTextArea().getLineStartOffset(pos));
                }
            } catch (NumberFormatException ex) {    //en caso de que ocurran excepciones
                System.err.println(ex);
            } catch (BadLocationException ex) {
                System.err.println(ex);
            }
        }
    }
 
    /**
     * Opciï¿½n seleccionada: "Fuente de letra".
     * 
     * Le permite al usuario elegir la fuente para la letra en el ï¿½rea de ediciï¿½n.
     */
    public void actionSelectFont() {
        //presenta el dialogo de selecciï¿½n de fuentes
        Font font = JFontChooser.showDialog(tpEditor.getJFrame(),
                                            "Generic Sim - Fuente de letra:",
                                            tpEditor.getJTextArea().getFont());
        if (font != null) {    //si un fuente fue seleccionado
            //se establece como fuente del area de ediciï¿½n
            tpEditor.getJTextArea().setFont(font);
        }
    }
 
    /**
     * Opciï¿½n seleccionada: "Color de letra".
     * 
     * Le permite al usuario elegir el color para la letra en el ï¿½rea de ediciï¿½n.
     */
    public void actionSelectFontColor() {
        //presenta el dialogo de selecciï¿½n de colores
        Color color = JColorChooser.showDialog(tpEditor.getJFrame(),
                                               "Generic Sim - Color de letra:",
                                               tpEditor.getJTextArea().getForeground());
        if (color != null) {    //si un color fue seleccionado
            //se establece como color del fuente y cursor
            tpEditor.getJTextArea().setForeground(color);
            tpEditor.getJTextArea().setCaretColor(color);
        }
    }
 
    /**
     * Opciï¿½n seleccionada: "Color de fondo".
     * 
     * Le permite al usuario elegir el color para el fondo del ï¿½rea de ediciï¿½n.
     */
    public void actionSelectBackgroundColor() {
        //presenta el dialogo de selecciï¿½n de colores
        Color color = JColorChooser.showDialog(tpEditor.getJFrame(),
                                               "Generic Sim - Color de fondo:",
                                               tpEditor.getJTextArea().getForeground());
        if (color != null) {    //si un color fue seleccionado
            //se establece como color de fondo
            tpEditor.getJTextArea().setBackground(color);
        }
    }
 
    /**
     * Retorna la instancia de un JFileChooser, con el cual se muestra un dialogo que permite
     * seleccionar un archivo.
     * 
     * @return un dialogo para seleccionar un archivo.
     */
    private static JFileChooser getJFileChooser() {
        JFileChooser fc = new JFileChooser();                     //construye un JFileChooser
        fc.setDialogTitle("Generic Sim - Elige un archivo:");    //se le establece un tï¿½tulo
        fc.setMultiSelectionEnabled(false);                       //desactiva la multi-selecciï¿½n
        fc.setFileFilter(textFileFilter);                         //aplica un filtro de extensiones
        return fc;    //retorna el JFileChooser
    }
 
    /**
     * Clase anï¿½nima interna que extiende la clase javax.swing.filechooser.FileFilter para 
     * establecer un filtro de archivos en el JFileChooser.
     */
    private static FileFilter textFileFilter = new FileFilter() {
 
        @Override
        public boolean accept(File f) {
            //acepta directorios y archivos de extensiï¿½n .txt
            return f.isDirectory() || f.getName().toLowerCase().endsWith("txt");
        }
 
        @Override
        public String getDescription() {
            //la descripciï¿½n del tipo de archivo aceptado
            return "Text Files";
        }
    };
 
    /**
     * Retorna la ruta de la ubicaciï¿½n de un archivo en forma reducida.
     * 
     * @param longpath la ruta de un archivo
     * @return la ruta reducida del archivo
     */
    private static String shortPathName(String longPath) {
        //construye un arreglo de cadenas, donde cada una es un nombre de directorio
        String[] tokens = longPath.split(Pattern.quote(File.separator));
 
        //construye un StringBuilder donde se aï¿½adirï¿½ el resultado
        StringBuilder shortpath = new StringBuilder();
 
        //itera sobre el arreglo de cadenas
        for (int i = 0 ; i < tokens.length ; i++) {
            if (i == tokens.length - 1) {              //si la cadena actual es la ï¿½ltima, es el nombre del archivo
                shortpath.append(tokens[i]);    //aï¿½ade al resultado sin separador
                break;                          //termina el bucle
            } else if (tokens[i].length() >= 10) {     //si la cadena actual tiene 10 o mï¿½s caracteres
                //se toman los primeros 3 caracteres y se aï¿½ade al resultado con un separador
                shortpath.append(tokens[i].substring(0, 3)).append("...").append(File.separator);
            } else {                                   //si la cadena actual tiene menos de 10 caracteres
                //aï¿½ade al resultado con un separador
                shortpath.append(tokens[i]).append(File.separator);
            }
        }
 
        return shortpath.toString();    //retorna la cadena resultante
    }
 
    /**
     * Redondea la longitud de un archivo en KiloBytes si es necesario.
     * 
     * @param length longitud de un archivo
     * @return el tamaï¿½o redondeado  
     */
    private static String roundFileSize(long length) {
        //retorna el tamaï¿½o del archivo redondeado
        return (length < 1024) ? length + " bytes" : (length / 1024) + " Kbytes";
    }
}