import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;

@SuppressWarnings("ALL")
public class TextEditor extends JFrame {
    public static final int PREFERRED_HEIGHT = 600;
    public static final int PREFERRED_WIDTH  = 800;

    protected final Map<ActionType, EditorAction> actions;

    protected final JTextArea textArea;

    protected File    openFile;
    protected boolean modified = false;

    public TextEditor() throws HeadlessException {
        super();

        textArea = new JTextArea();
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        var areaScrollPane = new JScrollPane(textArea);
        areaScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        areaScrollPane.setPreferredSize(new Dimension(PREFERRED_WIDTH, PREFERRED_HEIGHT));

        textArea.setText("");
        textArea.getDocument().addDocumentListener(new EditorListener());

        this.add(areaScrollPane);

        actions = makeActions();

        // Links actions and key combinations
        String actionKey;
        for (var a : ActionType.values()) {
            actionKey = "actionMapKey" + a.name();
            this.textArea.getInputMap().put(a.getKeys(), actionKey);
            this.textArea.getActionMap().put(actionKey, this.getAction(a));
        }

        this.setJMenuBar(this.makeMenu());
    }

    public static void run() {
        //Create and set up the window.
        var frame = new TextEditor();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        run();
    }

    protected void setOpenFile(File newFile) {
        this.openFile = newFile;
        this.getAction(ActionType.SAVE).setEnabled(newFile != null);
    }

    protected void error(String message) {
        this.error(message, null);
    }

    protected void error(String message, Exception cause) {
        JOptionPane.showMessageDialog(TextEditor.this, message,
                                      cause != null ? cause.getClass().getName() : "Error",
                                      JOptionPane.ERROR_MESSAGE);
    }

    protected boolean confirm(String message) {
        return this.confirm(message, null);
    }

    protected boolean confirm(String message, String title) {
        return JOptionPane.OK_OPTION ==
               JOptionPane.showConfirmDialog(TextEditor.this, message,
                                             title != null ? title : "Please confirm",
                                             JOptionPane.OK_CANCEL_OPTION,
                                             JOptionPane.QUESTION_MESSAGE);
    }

    protected JMenuBar makeMenu() {
        var menuBar = new JMenuBar();

        JMenu tempMenu = new JMenu("File");
        tempMenu.setMnemonic(KeyEvent.VK_F);
        menuBar.add(tempMenu);

        JMenuItem tempItem = new JMenuItem(this.actions.get(ActionType.NEW));
        tempMenu.add(tempItem);

        tempMenu.addSeparator();

        tempItem = new JMenuItem(this.actions.get(ActionType.OPEN));
        tempMenu.add(tempItem);

        tempMenu.addSeparator();

        tempItem = new JMenuItem(this.actions.get(ActionType.SAVE));
        tempMenu.add(tempItem);
        tempItem = new JMenuItem(this.actions.get(ActionType.SAVE_AS));
        tempMenu.add(tempItem);

        tempMenu.addSeparator();

        tempItem = new JMenuItem(this.actions.get(ActionType.EXIT));
        tempMenu.add(tempItem);

        return menuBar;
    }

    protected Map<ActionType, EditorAction> makeActions() {
        Map<ActionType, EditorAction> actions = new HashMap<>();

        actions.put(ActionType.OPEN, new OpenAction());
        actions.put(ActionType.NEW, new NewAction());
        actions.put(ActionType.SAVE, new SaveAction());
        actions.put(ActionType.SAVE_AS, new SaveAsAction());
        actions.put(ActionType.EXIT, new ExitAction());

        return actions;
    }

    protected EditorAction getAction(ActionType type) {
        return this.actions.get(type);
    }

    protected void doAction(ActionType type, ActionEvent e) {
        this.actions.get(type).actionPerformed(e);
    }

    @SuppressWarnings("InnerClassMayBeStatic")
    private abstract class EditorAction extends AbstractAction {
        public EditorAction(ActionType type) {
            super(type.getName());
            this.putValue(Action.NAME, type.getName());
            this.putValue(Action.SHORT_DESCRIPTION, type.getDescription());
        }
    }

    private class NewAction extends EditorAction {
        public NewAction() {
            super(ActionType.NEW);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (!TextEditor.this.modified || TextEditor.this.modified
                                             && TextEditor.this.confirm(
                    "Your work is not saved. Are you sure you want to create a new text?",
                    "Please confirm new.")) {
                TextEditor.this.textArea.setText("");
                TextEditor.this.setOpenFile(null);
                TextEditor.this.modified = false;
            }
        }
    }

    private class OpenAction extends EditorAction {
        public OpenAction() {
            super(ActionType.OPEN);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (!TextEditor.this.modified || TextEditor.this.modified
                                             && TextEditor.this.confirm(
                    "Your work is not saved. Are you sure you want to open a new text?",
                    "Please confirm open.")) {
                var jfc = new JFileChooser(".");
                jfc.setDialogTitle("Choose text file");
                jfc.setAcceptAllFileFilterUsed(false);
                var filter = new FileNameExtensionFilter("Text files", "txt");
                jfc.addChoosableFileFilter(filter);

                int returnValue = jfc.showOpenDialog(TextEditor.this);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File tempFile = jfc.getSelectedFile();

                    try (var in = new BufferedReader(new FileReader(tempFile))) {
                        StringWriter newText = new StringWriter();
                        in.lines().forEachOrdered(l -> {newText.write(l); newText.append('\n');});

                        TextEditor.this.textArea.setText(newText.toString());

                        TextEditor.this.modified = false;
                    } catch (FileNotFoundException fnfe) {
                        TextEditor.this.error("Could not locate file.", fnfe);
                    } catch (IOException ioe) {
                        TextEditor.this.error("Could not open file.", ioe);
                    }

                    TextEditor.this.setOpenFile(tempFile);
                }
            }
        }
    }

    private class SaveAsAction extends EditorAction {
        public SaveAsAction() {
            super(ActionType.SAVE_AS);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            var jfc = new JFileChooser(".");
            jfc.setDialogTitle("Choose save file");
            jfc.setAcceptAllFileFilterUsed(false);
            var filter = new FileNameExtensionFilter("Text files", "txt");
            jfc.addChoosableFileFilter(filter);

            int returnValue = jfc.showSaveDialog(TextEditor.this);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                setOpenFile(jfc.getSelectedFile());
                TextEditor.this.doAction(ActionType.SAVE, e);
            }
        }
    }

    private class SaveAction extends EditorAction {
        protected SaveAction() {
            super(ActionType.SAVE);
            this.setEnabled(TextEditor.this.openFile != null);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            File outFile = TextEditor.this.openFile;
            if (outFile != null) {
                try (var out = new BufferedWriter(new FileWriter(outFile, false))) {
                    String text;
                    synchronized (textArea) {
                        text = textArea.getText();
                    }
                    out.write(text);
                    out.flush();
                    TextEditor.this.modified = false;
                } catch (FileNotFoundException fnfe) {
                    TextEditor.this.error("Could not locate file.", fnfe);
                } catch (IOException ioe) {
                    TextEditor.this.error("Could not save to file.", ioe);
                }
            }
        }
    }

    private class ExitAction extends EditorAction {
        public ExitAction() {
            super(ActionType.EXIT);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (!TextEditor.this.modified || TextEditor.this.modified
                                             && TextEditor.this.confirm(
                    "Your work is not saved. Are you sure you want to exit?",
                    "Please confirm exit.")) {
                System.exit(0);
            }
        }
    }

    protected enum ActionType {
        OPEN("Open", "Open a new text.", KeyStroke.getKeyStroke(KeyEvent.VK_O,
                                                               java.awt.event.InputEvent.CTRL_DOWN_MASK),
            NewAction.class),
        NEW("New", "Create a new text.", KeyStroke.getKeyStroke(KeyEvent.VK_N,
                                                               java.awt.event.InputEvent.CTRL_DOWN_MASK),
            NewAction.class),
        SAVE("Save", "Saves the current text.", KeyStroke.getKeyStroke(KeyEvent.VK_S,
                                                                          java.awt.event.InputEvent.CTRL_DOWN_MASK),
                SaveAction.class),
        SAVE_AS("Save As", "Saves the current text to a new file.",
             KeyStroke.getKeyStroke(KeyEvent.VK_S,
                                    java.awt.event.InputEvent.CTRL_DOWN_MASK
                                    | java.awt.event.InputEvent.SHIFT_DOWN_MASK),
             SaveAsAction.class),
        EXIT("Exit", "Exits the program.", KeyStroke.getKeyStroke(KeyEvent.VK_X,
                                                                  java.awt.event.InputEvent.CTRL_DOWN_MASK),
             ExitAction.class);

        private final String    name;
        private final String    description;
        private final KeyStroke keys;
        private final Class     actionClass;

        ActionType(String name, String description, KeyStroke keys, Class actionClass) {
            this.name = name;
            this.description = description;
            this.keys = keys;
            this.actionClass = actionClass;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public KeyStroke getKeys() {
            return keys;
        }

        public Class getActionClass() {
            return actionClass;
        }
    }

    private class EditorListener implements DocumentListener {
        @Override
        public void insertUpdate(DocumentEvent e) {
            TextEditor.this.modified = true;

        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            TextEditor.this.modified = true;

        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            TextEditor.this.modified = true;

        }
    }
}
