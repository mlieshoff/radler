package radler.gui;

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

    private MetaModel _uiClassResolver;
    private DataProvider<Object, Object> _dataProvider;

    private JTable _table;
    private java.util.List<Object> _objects;
    private ObjectTableModel _model;

    private JButton _buttonNew;
    private JButton _buttonSelect;
    private JButton _buttonCancel;

    private OpenAction _openAction;
    private CloseAction _closeAction;

    public ObjectSearch(MetaModel uiClassResolver, DataProvider<Object, Object> dataProvider, OpenAction openAction, CloseAction closeAction) {
        _uiClassResolver = uiClassResolver;
        _dataProvider = dataProvider;
        _openAction = openAction;
        _closeAction = closeAction;
        setSize(new Dimension(320, 200));
        init();
        setVisible(true);
    }

    private void init() {
        _objects = _dataProvider.read(_uiClassResolver.getObjectClass());
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
        _model = new ObjectTableModel(_objects, _uiClassResolver);
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
            _openAction.open(new ObjectEditor(_uiClassResolver, _dataProvider, object, new CloseAction() {
                @Override
                public void close(JComponent component) {
                    _closeAction.close(component);
                }
            }, new SaveAction() {
                @Override
                public void save(JComponent component) {
                    //To change body of implemented methods use File | Settings | File Templates.
                }
            }));
        } else if (e.getSource() == _buttonNew) {
            _openAction.open(new ObjectEditor(_uiClassResolver, _dataProvider, _dataProvider.create(_uiClassResolver.getObjectClass()), new CloseAction() {
                @Override
                public void close(JComponent component) {
                    _closeAction.close(component);
                    _objects.clear();
                    _objects.addAll(_dataProvider.read(_uiClassResolver.getObjectClass()));
                    _model.fireTableDataChanged();
                }
            }, new SaveAction() {
                @Override
                public void save(JComponent component) {
                    _objects.clear();
                    _objects.addAll(_dataProvider.read(_uiClassResolver.getObjectClass()));
                    _model.fireTableDataChanged();
                }
            }));
        } else if (e.getSource() == _buttonCancel) {
            _closeAction.close(this);
        }
    }

}
