package radler.gui;

import radler.ApplicationFactory;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import java.util.List;

public class ObjectTableModel extends AbstractTableModel implements TableModel {

    private ApplicationFactory _applicationFactory = ApplicationFactory.getInstance();
    private List<?> _objects;
    private MetaModel _metaModel;

    public ObjectTableModel(List<?> objects, MetaModel metaModel) {
        _metaModel = metaModel;
        _objects = objects;
    }

    @Override
    public int getRowCount() {
        return _objects == null ? 0 : _objects.size();
    }

    @Override
    public String getColumnName(int column) {
        return _applicationFactory.getResource(_metaModel.getSelectableFieldTitle(column));
    }

    @Override
    public int getColumnCount() {
        return _metaModel.getNumberOfSelectableFields();
    }

    public Class getColumnClass(int columnIndex) {
        return _metaModel.getSelectableValueType(columnIndex);
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return _metaModel.getSelectableValue(_objects.get(rowIndex), columnIndex);
    }


}
