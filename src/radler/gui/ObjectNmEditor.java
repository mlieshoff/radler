package radler.gui;

import radler.ApplicationFactory;
import radler.gui.actions.CancelAction;
import radler.gui.actions.SelectAction;

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
public class ObjectNmEditor extends JPanel implements ActionListener {

    private ApplicationFactory _applicationFactory = ApplicationFactory.getInstance();

    private MetaModel _nmMetaModel;

    private MetaModel _objectMetaModel;
    private MetaField _objectMetaField;

    private java.util.List<Object> _list;

    private JButton _buttonAdd;
    private JButton _buttonRemove;

    // nm
    private JTable _table;
    private ObjectTableModel _model;

    public ObjectNmEditor(MetaModel nmMetaModel, MetaModel objectMetaModel, MetaField objectMetaField, Object object) {
        _nmMetaModel = nmMetaModel;
        _objectMetaModel = objectMetaModel;
        _objectMetaField = objectMetaField;
        _list = (java.util.List<Object>) _objectMetaModel.getEditableFieldValue(objectMetaField, object);
        if (_list == null) {
            _list = new ArrayList<Object>();
            _objectMetaField.setValue(object, _list);
        }
        setName(String.format("%s.%s", _objectMetaModel.getTitle(), "***"));
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
        _buttonAdd = new JButton("Hinzufügen");
        _buttonAdd.addActionListener(this);
        _buttonRemove = new JButton("Entfernen");
        _buttonRemove.addActionListener(this);
        control.add(_buttonAdd);
        control.add(_buttonRemove);
        panel.add("East", control);
        return panel;
    }

    private Component createContent() {
        JPanel panel = new JPanel();
        _model = new ObjectTableModel(_list, _nmMetaModel);
        _table = new JTable(_model);
        JScrollPane scrollPane = new JScrollPane(_table);
        scrollPane.setWheelScrollingEnabled(true);
        panel.add(scrollPane);
        return panel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == _buttonAdd) {
            JDialog dialog = new JDialog() {{
                add(
                new ObjectSimpleSearch(_nmMetaModel,
                        _applicationFactory.getDataProvider().read(_nmMetaModel.getObjectClass()),
                        new CancelAction() {
                            @Override
                            public void onCancel(JComponent component) {
                                dispose();
                            }
                        },
                        new SelectAction() {
                            @Override
                            public void onSelect(Object object) {
                                _list.add(object);
                                _model.fireTableDataChanged();
                            }
                        }
                    ));
            }};
            dialog.setSize(640, 480);
            dialog.setVisible(true);
        } else if (e.getSource() == _buttonRemove) {
            if (_table.getSelectedRow() >= 0) {
                int index = _table.convertRowIndexToModel(_table.getSelectedRow());
                _list.remove(index);
                _model.fireTableDataChanged();
            }
        }
    }
}
