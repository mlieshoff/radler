package radler.gui;

import radler.gui.annotation.Display;
import radler.gui.annotation.Editables;
import radler.gui.annotation.Selectables;
import radler.persistence.Relation;
import radler.persistence.RelationType;
import radler.persistence.annotation.*;

import java.lang.reflect.Field;
import java.util.*;

/**
 * This ...
 *
 * @author mlieshoff
 */
public class MetaModel {
    private String _title;
    private Class<?> _clazz;
    private List<MetaField> _keyFields = new ArrayList<MetaField>();
    private Map<String, MetaField> _fieldsByName = new HashMap<String, MetaField>();
    private Map<Integer, MetaField> _selectableFields = new TreeMap<Integer, MetaField>();
    private Map<Integer, MetaField> _editableFields = new TreeMap<Integer, MetaField>();

    public MetaModel(Class<?> clazz) {
        _clazz = clazz;
        _title = _clazz.getSimpleName();

        for (Field field : _clazz.getDeclaredFields()) {
            MetaField metaField = new MetaField(field);
            _fieldsByName.put(field.getName(), metaField);
            if (field.isAnnotationPresent(Id.class)) {
                _keyFields.add(metaField);
                metaField.setIdentifier(true);
                System.out.println(clazz.getName() + "    key: " + metaField.getName());
            }

            Relation relation = new Relation();
            if (field.isAnnotationPresent(OneToMany.class)) {
                OneToMany annotation = (OneToMany) field.getAnnotation(OneToMany.class);
                relation.setRelationType(RelationType.ONE_TO_MANY);
                relation.setDisplayPattern(annotation.displayPattern());
                relation.setDisplayMembers(annotation.displayFields());
                metaField.setRelation(relation);
            }
            if (field.isAnnotationPresent(OneToOne.class)) {
                relation.setRelationType(RelationType.ONE_TO_ONE);
                metaField.setRelation(relation);
            }
            if (field.isAnnotationPresent(ManyToOne.class)) {
                ManyToOne annotation = (ManyToOne) field.getAnnotation(ManyToOne.class);
                relation.setRelationType(RelationType.MANY_TO_ONE);
                relation.setTo(annotation.to());
                metaField.setRelation(relation);
            }
            if (field.isAnnotationPresent(ManyToMany.class)) {
                ManyToMany annotation = (ManyToMany) field.getAnnotation(ManyToMany.class);
                relation.setRelationType(RelationType.MANY_TO_MANY);
                relation.setTo(annotation.to());
                metaField.setRelation(relation);
            }
        }

        if (clazz.isAnnotationPresent(Display.class)) {
            Display annotation = clazz.getAnnotation(Display.class);
            for (int i = 0; i < annotation.columns().length; i ++) {
                MetaField metaField = _fieldsByName.get(annotation.columns()[i]);
                // TODO
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

    public MetaField getMetaField(String key) {
        return _fieldsByName.get(key);
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

    public Map<MetaField, Object> getKeys(Object object) {
        Map<MetaField, Object> map = new HashMap<MetaField, Object>();
        for (MetaField metaField : _keyFields) {
            map.put(metaField, metaField.getValue(object));
        }
        return map;
    }

    public List<MetaField> getKeyFields() {
        return _keyFields;
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
        Relation relation = field.getRelation();
        if (clazz == String.class) {
            return InputType.TEXTFIELD;
        } else if (clazz == Boolean.class) {
            return InputType.CHECKBOX;
        } else if (relation != null) {
            RelationType relationType = relation.getRelationType();
            if (relationType == RelationType.ONE_TO_MANY || relationType == RelationType.ONE_TO_ONE) {
                return InputType.COMBOBOX;
            } else if (relationType == RelationType.MANY_TO_MANY || relationType == RelationType.MANY_TO_ONE) {
                return InputType.TAB;
            }
        }
        return InputType.TEXTFIELD;
    }

    public String displayString(Object object) {
        return object != null ? object.toString() : "";
    }

    public void setEditableValue(int index, Object object, Object value) {
        MetaField field = _editableFields.get(index);
        if (value != null) {
            Class<?> valueClass = value.getClass();
            Class<?> fieldClass = field.getWrappedType();
            if (valueClass != fieldClass) {
                // from text fields
                if (valueClass == String.class) {
                    if (fieldClass == Integer.class) {
                        value = Integer.valueOf(value.toString());
                    }
                }
            }
        }
        field.setValue(object, value);
    }

    public MetaField getEditableMetaField(int index) {
        return _editableFields.get(index);
    }

}
