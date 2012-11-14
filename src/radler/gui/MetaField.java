package radler.gui;

import java.lang.reflect.Field;
import java.util.List;

/**
 * This ...
 *
 * @author mlieshoff
 */
public class MetaField {

    private Field field;
    private boolean editable;
    private boolean selectable;
    private boolean identifier;
    private boolean oneToOne;
    private boolean oneToMany;
    private boolean manyToOne;
    private boolean manyToMany;
    private String name;


    private String displayFormat;
    private List<Field> displayFields;

    public MetaField(Field field) {
        this.field = field;
        this.name = field.getName();
    }

    public Object getValue(Object object) {
        try {
            Object value;
            if (!field.isAccessible()) {
                field.setAccessible(true);
                value = field.get(object);
                field.setAccessible(false);
            } else {
                value = field.get(object);
            }
            return value;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public void setEditableValue(Object object, Object value) {
        try {
            if (!field.isAccessible()) {
                field.setAccessible(true);
                field.set(object, value);
                field.setAccessible(false);
            } else {
                field.set(object, value);
            }
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }

    public Class<?> getWrappedType() {
        Class<?> clazz = field.getType();
        if (clazz == boolean.class) {
            return Boolean.class;
        }
        return clazz;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public boolean isSelectable() {
        return selectable;
    }

    public void setSelectable(boolean selectable) {
        this.selectable = selectable;
    }

    public boolean isIdentifier() {
        return identifier;
    }

    public void setIdentifier(boolean identifier) {
        this.identifier = identifier;
    }

    public boolean isOneToMany() {
        return oneToMany;
    }

    public void setOneToMany(boolean oneToMany) {
        this.oneToMany = oneToMany;
    }

    public boolean isManyToOne() {
        return manyToOne;
    }

    public void setManyToOne(boolean manyToOne) {
        this.manyToOne = manyToOne;
    }

    public boolean isManyToMany() {
        return manyToMany;
    }

    public void setManyToMany(boolean manyToMany) {
        this.manyToMany = manyToMany;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayFormat() {
        return displayFormat;
    }

    public void setDisplayFormat(String displayFormat) {
        this.displayFormat = displayFormat;
    }

    public List<Field> getDisplayFields() {
        return displayFields;
    }

    public void setDisplayFields(List<Field> displayFields) {
        this.displayFields = displayFields;
    }

    public boolean isOneToOne() {
        return oneToOne;
    }

    public void setOneToOne(boolean oneToOne) {
        this.oneToOne = oneToOne;
    }
}
