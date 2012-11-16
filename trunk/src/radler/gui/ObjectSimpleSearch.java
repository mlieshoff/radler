package radler.gui;

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
public class ObjectSimpleSearch extends JPanel implements ActionListener {

    private MetaModel _metaModel;

    private JTable _table;
    private java.util.List<Object> _objects = new ArrayList<Object>();
    private ObjectTableModel _model;

    private JButton _buttonSelect;
    private JButton _buttonCancel;

    private CancelAction _cancelAction;
    private SelectAction _selectAction;

    public ObjectSimpleSearch(MetaModel metaModel, java.util.List<Object> objects, CancelAction cancelAction,
            SelectAction selectAction) {
        _metaModel = metaModel;
        _cancelAction = cancelAction;
        _selectAction = selectAction;
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
        _buttonSelect = new JButton("Auswählen");
        _buttonSelect.addActionListener(this);
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
            int index = _table.convertRowIndexToModel(_table.getSelectedRow());
            _selectAction.onSelect(_objects.get(index));
            _cancelAction.onCancel(this);
        } else if (e.getSource() == _buttonCancel) {
            _cancelAction.onCancel(this);
        }
    }

}
