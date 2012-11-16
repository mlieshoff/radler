package radler.gui;

import radler.gui.actions.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * This ...
 *
 * @author mlieshoff
 */
public class ObjectSearch extends JPanel implements ActionListener {

    private MetaModel _metaModel;

    private JTable _table;
    private java.util.List<Object> _objects = new ArrayList<Object>();
    private ObjectTableModel _model;

    private JButton _buttonNew;
    private JButton _buttonSelect;
    private JButton _buttonCancel;

    private OpenAction _openAction;
    private CancelAction _cancelAction;
    private ReadAction _readAction;
    private SaveAction _saveAction;
    private NewAction _newAction;
    private ResetAction _resetAction;

    public ObjectSearch(MetaModel metaModel, java.util.List<Object> objects, OpenAction openAction,
            CancelAction cancelAction, NewAction newAction, ReadAction readAction, SaveAction saveAction,
            ResetAction resetAction) {
        _metaModel = metaModel;
        _openAction = openAction;
        _cancelAction = cancelAction;
        _newAction = newAction;
        _readAction = readAction;
        _saveAction = saveAction;
        _resetAction = resetAction;
        if (objects != null && objects.size() > 0) {
            _objects.addAll(objects);
        }
        setSize(new Dimension(640, 480));
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
        _buttonCancel = new JButton("Abbruch");
        _buttonCancel.addActionListener(this);
        _buttonNew = new JButton("Neu");
        _buttonNew.addActionListener(this);
        _buttonSelect = new JButton("Auswählen");
        _buttonSelect.addActionListener(this);
        control.add(_buttonNew);
        control.add(_buttonSelect);
        control.add(_buttonCancel);
        panel.add("East", control);
        return panel;
    }

    private Component createContent() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        _model = new ObjectTableModel(_objects, _metaModel);
        _table = new JTable(_model);
        JScrollPane scrollPane = new JScrollPane(_table);
        scrollPane.setWheelScrollingEnabled(true);
        panel.add(scrollPane);
        return panel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == _buttonSelect && _table.getSelectedRow() >= 0) {
            final Object object = _objects.get(_table.getSelectedRow());
            _openAction.open(new ObjectEditor("..", _metaModel, object,
                    new CloseAction() {
                        @Override
                        public void close(JComponent component) {
                            _cancelAction.onCancel(component);
                        }
                    }, new SaveAction() {
                        @Override
                        public void save(Object object) {
                            _saveAction.save(object);
                        }
                    }, new ReadAction() {
                        @Override
                        public java.util.List<Object> read(Class<?> clazz) {
                            return _readAction.read(clazz);
                        }
                    }, new ResetAction() {
                        @Override
                        public Object reset(Object objectToReset) {
                            return _resetAction.reset(objectToReset);
                        }
                    }, new NewAction() {
                        @Override
                        public Object doNew() {
                            return _newAction.doNew();
                        }
                    }
            ));
        } else if (e.getSource() == _buttonNew) {
            final Object object = _newAction.doNew();
            _openAction.open(new ObjectEditor("*", _metaModel, object,
                    new CloseAction() {
                        @Override
                        public void close(JComponent component) {
                            _cancelAction.onCancel(component);
                            _objects.clear();
                            _objects.addAll(_readAction.read(_metaModel.getObjectClass()));
                            _model.fireTableDataChanged();
                        }
                    }, new SaveAction() {
                        @Override
                        public void save(Object object) {
                            _saveAction.save(object);
                            _objects.clear();
                            _objects.addAll(_readAction.read(_metaModel.getObjectClass()));
                            _model.fireTableDataChanged();
                        }
                    }, new ReadAction() {
                        @Override
                        public List<Object> read(Class<?> clazz) {
                            return _readAction.read(clazz);
                        }
                    }, new ResetAction() {
                        @Override
                        public Object reset(Object objectToReset) {
                            return _resetAction.reset(objectToReset);
                        }
                    }, new NewAction() {
                        @Override
                        public Object doNew() {
                            return _newAction.doNew();
                        }
                    }
            ));
        } else if (e.getSource() == _buttonCancel) {
            _cancelAction.onCancel(this);
        }
    }

}
