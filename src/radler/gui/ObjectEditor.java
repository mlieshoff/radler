package radler.gui;

import net.miginfocom.swing.MigLayout;
import radler.persistence.DataProvider;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * This ...
 *
 * @author mlieshoff
 */
public class ObjectEditor extends JPanel implements ActionListener {

    private UiClassResolver _uiClassResolver;
    private Object _object;

    private DataProvider<Object, Object> _dataProvider;

    private JButton _buttonNew;
    private JButton _buttonReset;
    private JButton _buttonSave;
    private JButton _buttonCancel;

    private CloseAction _closeAction;
    private SaveAction _saveAction;

    private java.util.List<JComponent> _inputs = new ArrayList<JComponent>();

    public ObjectEditor(UiClassResolver uiClassResolver, DataProvider<Object, Object> dataProvider, Object object, CloseAction closeAction, SaveAction saveAction) {
        _uiClassResolver = uiClassResolver;
        _object = object;
        _dataProvider = dataProvider;
        _closeAction = closeAction;
        _saveAction = saveAction;
        setName(String.format("%s.%s", _uiClassResolver.getTitle(), _uiClassResolver.getKey(object)));
        setSize(new Dimension(320, 200));
        init();
        setVisible(true);
    }

    private void init() {
        setLayout(new BorderLayout());
        add("Center", createContent());
        add("South", createControls());
    }

    private Component createControls() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        JPanel control = new JPanel();
        _buttonNew = new JButton("Neu");
        _buttonNew.addActionListener(this);
        _buttonReset = new JButton("Verwerfen");
        _buttonReset.addActionListener(this);
        _buttonSave = new JButton("Speichern");
        _buttonSave.addActionListener(this);
        _buttonCancel = new JButton("Abbruch");
        _buttonCancel.addActionListener(this);
        control.add(_buttonNew);
        control.add(_buttonReset);
        control.add(_buttonSave);
        control.add(_buttonCancel);
        panel.add("East", control);
        return panel;
    }

    private Component createContent() {
        JPanel master = new JPanel(new BorderLayout());
        JPanel panel = new JPanel(new MigLayout());
        master.add("North", panel);
        for (int i = 0, n = _uiClassResolver.getNumberOfEditableFields(); i < n; i ++) {
            MetaField metaField = _uiClassResolver.getEditableMetaField(i);
            JComponent component;
            if (_uiClassResolver.getEditableType(i) == InputType.TEXTFIELD) {
                JTextField textField = new JTextField(100);
                textField.setText(_uiClassResolver.displayString(_uiClassResolver.getEditableFieldValue(metaField, _object)));
                component = textField;
            } else if (_uiClassResolver.getEditableType(i) == InputType.CHECKBOX) {
                JCheckBox checkBox = new JCheckBox();
                checkBox.setSelected(((Boolean) _uiClassResolver.getEditableFieldValue(metaField, _object)).booleanValue());
                component = checkBox;
            } else if (_uiClassResolver.getEditableType(i) == InputType.COMBOBOX) {
                JComboBox comboBox = new JComboBox(new ObjectComboBoxModel(_dataProvider.read(metaField.getWrappedType()), _uiClassResolver));
                component = comboBox;
            } else {
                component = null;
            }
            if (component != null) {
                panel.add(new JLabel(_uiClassResolver.getEditableFieldTitle(metaField)), "gap para");
                panel.add(component, "span, growx, wrap para");
                _inputs.add(component);
            }
        }
        JTabbedPane tabbedPane = new JTabbedPane();
        for (int i = 0, n = _uiClassResolver.getNumberOfEditableFields(); i < n; i ++) {
            MetaField metaField = _uiClassResolver.getEditableMetaField(i);
            if (_uiClassResolver.getEditableType(i) == InputType.TAB) {
                tabbedPane.add(metaField.getName(), new JLabel(_uiClassResolver.getTitle()));
            }
        }
        if (tabbedPane.getTabCount() > 0) {
            master.add("Center", tabbedPane);
        }
        return master;
    }

    private JTabbedPane createTabs() {
        JTabbedPane tabbedPane = new JTabbedPane();
        for (int i = 0, n = _uiClassResolver.getNumberOfEditableFields(); i < n; i ++) {
            MetaField metaField = _uiClassResolver.getEditableMetaField(i);
            if (_uiClassResolver.getEditableType(i) == InputType.TAB) {
                tabbedPane.add(metaField.getName(), new JLabel(_uiClassResolver.getTitle()));
            }

        }
        return tabbedPane;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == _buttonCancel) {
            _closeAction.close(this);
        } else if (e.getSource() == _buttonReset) {
            _object = _dataProvider.get(_uiClassResolver.getObjectClass(), _uiClassResolver.getKey(_object));
        } else if (e.getSource() == _buttonSave) {
            for (int i = 0, n = _inputs.size(); i < n; i ++) {
                JComponent component = _inputs.get(i);
                Object value = null;
                if (_uiClassResolver.getEditableType(i) == InputType.TEXTFIELD) {
                    JTextField textField = (JTextField) component;
                    value = textField.getText();
                } else if (_uiClassResolver.getEditableType(i) == InputType.CHECKBOX) {
                    JCheckBox checkBox = (JCheckBox) component;
                    value = checkBox.isSelected();
                }
                if (value != null) {
                    _uiClassResolver.setEditableValue(i, _object, value);
                }
            }
            _dataProvider.write(_object);
            _saveAction.save(this);
            _closeAction.close(this);
        } else if (e.getSource() == _buttonNew) {
            _dataProvider.create(_uiClassResolver.getObjectClass());
        }
    }
}
