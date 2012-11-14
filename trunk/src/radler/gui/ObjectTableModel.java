package radler.gui;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import java.util.List;

public class ObjectTableModel extends AbstractTableModel implements TableModel {

    private List<?> _objects;
    private UiClassResolver _uiClassResolver;

    public ObjectTableModel(List<?> objects, UiClassResolver uiClassResolver) {
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
        System.out.println("--> " + columnIndex + "    " + _uiClassResolver.getSelectableValueType(columnIndex));
        return _uiClassResolver.getSelectableValueType(columnIndex);
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return _uiClassResolver.getSelectableValue(_objects.get(rowIndex), columnIndex);
    }


}
