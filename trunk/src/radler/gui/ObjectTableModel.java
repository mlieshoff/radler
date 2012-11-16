package radler.gui;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import java.util.List;

public class ObjectTableModel extends AbstractTableModel implements TableModel {

    private List<?> _objects;
    private MetaModel _uiClassResolver;

    public ObjectTableModel(List<?> objects, MetaModel uiClassResolver) {
        _uiClassResolver = uiClassResolver;
        _objects = objects;
    }

    @Override
    public int getRowCount() {
        return _objects == null ? 0 : _objects.size();
    }

    @Override
    public String getColumnName(int column) {
        return _uiClassResolver.getSelectableFieldTitle(column);
    }

    @Override
    public int getColumnCount() {
        return _uiClassResolver.getNumberOfSelectableFields();
    }

    public Class getColumnClass(int columnIndex) {
        return _uiClassResolver.getSelectableValueType(columnIndex);
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return _uiClassResolver.getSelectableValue(_objects.get(rowIndex), columnIndex);
    }


}
