package radler.gui;

import javax.swing.*;
import java.awt.*;

public class ComboBoxRenderer extends JLabel implements ListCellRenderer {

    private MetaField metaField;
    private MetaModel foreignModel;

    public ComboBoxRenderer(MetaModel foreignModel, MetaField metaField) {
        this.foreignModel = foreignModel;
        this.metaField = metaField;
    }

    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }
        if (value != null) {
            setText(metaField.displayRelationString(foreignModel, value));
        }
        setFont(list.getFont());
        return this;
    }
}