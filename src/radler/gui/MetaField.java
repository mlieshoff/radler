package radler.gui;

import radler.persistence.Relation;

import java.lang.reflect.Field;

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
    private String name;
    private Relation relation;

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

    public void setValue(Object object, Object value) {
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
        if (clazz == short.class) {
            return Short.class;
        } else if (clazz == int.class) {
            return Integer.class;
        } else if (clazz == float.class) {
            return Float.class;
        } else if (clazz == long.class) {
            return Long.class;
        } else if (clazz == double.class) {
            return Double.class;
        } else if (clazz == boolean.class) {
            return Boolean.class;
        } else if (clazz == char.class) {
            return String.class;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Relation getRelation() {
        return relation;
    }

    public void setRelation(Relation relation) {
        this.relation = relation;
    }

    public String displayRelationString(MetaModel foreignModel, Object value) {
        if (relation != null) {
            String[] members = relation.getDisplayMembers();
            Object[] values = new String[members.length];
            for (int i = 0; i < members.length; i ++) {
                String member = members[i];
                MetaField field = foreignModel.getMetaField(member);
                values[i] = field.getValue(value);
            }
            return String.format(relation.getDisplayPattern(), values);
        }
        return null;
    }
}
