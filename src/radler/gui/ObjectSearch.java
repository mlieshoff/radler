package radler.gui;

import radler.gui.actions.CancelAction;
import radler.gui.actions.CloseAction;
import radler.gui.actions.OpenAction;
import radler.gui.actions.SaveAction;
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
public class ObjectSearch extends JPanel implements ActionListener {

    private MetaModel _metaModel;
    private DataProvider<Object, Object> _dataProvider;

    private JTable _table;
    private java.util.List<Object> _objects;
    private ObjectTableModel _model;

    private JButton _buttonNew;
    private JButton _buttonSelect;
    private JButton _buttonCancel;

    private OpenAction _openAction;
    private CancelAction _cancelAction;

    public ObjectSearch(MetaModel metaModel, DataProvider<Object, Object> dataProvider, OpenAction openAction,
                        CancelAction cancelAction) {
        _metaModel = metaModel;
        _dataProvider = dataProvider;
        _openAction = openAction;
        _cancelAction = cancelAction;
        setSize(new Dimension(640, 480));
        init();
        setVisible(true);
    }

    private void init() {
        _objects = _dataProvider.read(_metaModel.getObjectClass());
        if (_objects == null) {
            _objects = new ArrayList<Object>();
        }
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
        System.out.println(e.getSource());
        if (e.getSource() == _buttonSelect && _table.getSelectedRow() >= 0) {
            Object object = _objects.get(_table.getSelectedRow());
            _openAction.open(new ObjectEditor(_metaModel, _dataProvider, object, new CloseAction() {
                @Override
                public void close(JComponent component) {
                    _cancelAction.onCancel(component);
                }
            }, new SaveAction() {
                @Override
                public void save(JComponent component) {
                    //To change body of implemented methods use File | Settings | File Templates.
                }
            }));
        } else if (e.getSource() == _buttonNew) {
            _openAction.open(new ObjectEditor(_metaModel, _dataProvider, _dataProvider.create(_metaModel.getObjectClass()), new CloseAction() {
                @Override
                public void close(JComponent component) {
                    _cancelAction.onCancel(component);
                    _objects.clear();
                    _objects.addAll(_dataProvider.read(_metaModel.getObjectClass()));
                    _model.fireTableDataChanged();
                }
            }, new SaveAction() {
                @Override
                public void save(JComponent component) {
                    _objects.clear();
                    _objects.addAll(_dataProvider.read(_metaModel.getObjectClass()));
                    _model.fireTableDataChanged();
                }
            }));
        } else if (e.getSource() == _buttonCancel) {
            _cancelAction.onCancel(this);
        }
    }

}
