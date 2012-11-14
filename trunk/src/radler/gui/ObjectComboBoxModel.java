package radler.gui;

import javax.swing.*;
import javax.swing.event.ListDataListener;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: micha
 * Date: 14.11.12
 * Time: 22:17
 * To change this template use File | Settings | File Templates.
 */
public class ObjectComboBoxModel implements ComboBoxModel {

    private List<Object> objects;
    private UiClassResolver uiClassResolver;

    private Object selected;

    public ObjectComboBoxModel(List<Object> objects, UiClassResolver uiClassResolver) {
        this.objects = objects;
        this.uiClassResolver = uiClassResolver;
    }

    @Override
    public void setSelectedItem(Object anItem) {
        selected = anItem;
    }

    @Override
    public Object getSelectedItem() {
        return selected;
    }

    @Override
    public int getSize() {
        return objects.size();
    }

    @Override
    public Object getElementAt(int index) {
        return objects.get(index);
    }

    @Override
    public void addListDataListener(ListDataListener l) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void removeListDataListener(ListDataListener l) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
