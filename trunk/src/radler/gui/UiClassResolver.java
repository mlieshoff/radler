package radler.gui;

import radler.gui.annotation.Display;
import radler.gui.annotation.Editables;
import radler.gui.annotation.Selectables;
import radler.persistence.annotation.*;

import java.awt.*;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * This ...
 *
 * @author mlieshoff
 */
public class UiClassResolver {
    private String _title;
    private Class<?> _clazz;
    private Field _keyField;
    private Map<String, MetaField> _fieldsByName = new HashMap<String, MetaField>();
    private Map<Integer, MetaField> _selectableFields = new TreeMap<Integer, MetaField>();
    private Map<Integer, MetaField> _editableFields = new TreeMap<Integer, MetaField>();

    public UiClassResolver(Class<?> clazz) {
        _clazz = clazz;
        _title = _clazz.getSimpleName();

        for (Field field : _clazz.getDeclaredFields()) {
            MetaField metaField = new MetaField(field);
            _fieldsByName.put(field.getName(), metaField);
            if (field.isAnnotationPresent(Id.class)) {
                _keyField = field;
                metaField.setIdentifier(true);
                System.out.println(clazz.getName() + "    key: " + metaField.getName());
            }

            if (field.isAnnotationPresent(OneToMany.class)) {
                metaField.setOneToMany(true);
            }
            if (field.isAnnotationPresent(OneToOne.class)) {
                metaField.setOneToOne(true);
            }
            if (field.isAnnotationPresent(ManyToOne.class)) {
                metaField.setManyToOne(true);
            }
            if (field.isAnnotationPresent(ManyToMany.class)) {
                metaField.setManyToMany(true);
            }
        }

        if (clazz.isAnnotationPresent(Display.class)) {
            Display annotation = clazz.getAnnotation(Display.class);
            for (int i = 0; i < annotation.columns().length; i ++) {
                MetaField metaField = _fieldsByName.get(annotation.columns()[i]);
                metaField.setDisplayFormat(annotation.format());
            }
        }

        if (clazz.isAnnotationPresent(Selectables.class)) {
            Selectables annotation = clazz.getAnnotation(Selectables.class);
            for (int i = 0; i < annotation.columns().length; i ++) {
                MetaField metaField = _fieldsByName.get(annotation.columns()[i]);
                metaField.setSelectable(true);
                _selectableFields.put(i, metaField);
                System.out.println(clazz.getName() + "    select: " + metaField.getName());
            }
        }

        if (clazz.isAnnotationPresent(Editables.class)) {
            Editables annotation = clazz.getAnnotation(Editables.class);
            for (int i = 0; i < annotation.columns().length; i ++) {
                MetaField metaField = _fieldsByName.get(annotation.columns()[i]);
                metaField.setEditable(true);
                _editableFields.put(i, metaField);
                System.out.println(clazz.getName() + "    edit: " + metaField.getName());
            }
        }

    }

    public String getTitle() {
        return _title;
    }

    public String getFieldTitle(MetaField field) {
        return field.getName();
    }

    public Object createObject() {
        try {
            return _clazz.newInstance();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public Object getKey(Object object) {
        MetaField field = _fieldsByName.get(_keyField.getName());
        return field.getValue(object);
    }

    public Class<?> getObjectClass() {
        return _clazz;
    }

    // selectable

    public Object getSelectableValue(Object object, int columnIndex) {
        MetaField field = _selectableFields.get(columnIndex);
        return field.getValue(object);
    }

    public Class<?> getSelectableValueType(int columnIndex) {
        MetaField field = _selectableFields.get(columnIndex);
        return field.getWrappedType();
    }

    public int getNumberOfSelectableFields() {
        return _selectableFields.size();
    }

    public String getSelectableFieldTitle(int index) {
        return getFieldTitle(_selectableFields.get(index));
    }

    // editable

    public int getNumberOfEditableFields() {
        return _editableFields.size();
    }

    public String getEditableFieldTitle(MetaField metaField) {
        return getFieldTitle(_fieldsByName.get(metaField.getName()));
    }

    public Object getEditableFieldValue(MetaField metaField, Object object) {
        MetaField field = _fieldsByName.get(metaField.getName());
        return field.getValue(object);
    }

    public InputType getEditableType(int index) {
        MetaField field = _editableFields.get(index);
        Class<?> clazz = field.getWrappedType();
        if (clazz == String.class) {
            return InputType.TEXTFIELD;
        } else if (clazz == Boolean.class) {
            return InputType.CHECKBOX;
        } else if (field.isOneToMany() || field.isOneToOne()) {
            return InputType.COMBOBOX;
        } else if (field.isManyToMany() || field.isManyToOne()) {
            return InputType.TAB;
        }
        return InputType.TEXTFIELD;
    }

    public String displayString(Object object) {
        return object != null ? object.toString() : "";
    }

    public void setEditableValue(int index, Object object, Object value) {
        MetaField field = _editableFields.get(index);
        field.setEditableValue(object, value);
    }

    public MetaField getEditableMetaField(int index) {
        return _editableFields.get(index);
    }

    public Component displayObject(Object value) {
        return null;  //To change body of created methods use File | Settings | File Templates.
    }
}
