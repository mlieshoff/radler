package radler.persistence.db;

import radler.gui.MetaField;
import radler.gui.MetaModel;

import java.sql.Types;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * This ...
 *
 * @author mlieshoff
 */
public class SqlUtil {

    public static String createTableSql(DbMode dbMode, MetaModel metaModel) {
        String sql = "create table %s (%s)";
        StringBuilder attributes = new StringBuilder();

        List<MetaField> list = new ArrayList<MetaField>(metaModel.getMetaFields());
        List<MetaField> primaries = new ArrayList<MetaField>();
        for (int i = list.size() -1 ; i >= 0; i --) {
            MetaField metaField = list.get(i);
            if (metaField.isIdentifier()) {
                primaries.add(list.get(i));
            }
        }
        for (int i = 0, n = list.size(); i < n; i ++) {
            MetaField metaField = list.get(i);
            attributes.append(metaField.getName());
            attributes.append(" ");
            attributes.append(getSqlType(getSqlType(dbMode, metaField)));
            if (primaries.size() == 1 && metaField.isIdentifier() && metaField.getWrappedType() == Integer.class) {
                attributes.append(" identity");
            }
            if (i < n - 1) {
                attributes.append(", ");
            }
        }
        if (primaries.size() > 0) {
            attributes.append(", primary key(");
            for (int i = 0, n = primaries.size(); i < n; i ++) {
                MetaField metaField = primaries.get(i);
                attributes.append(metaField.getName());
                if (i < n - 1) {
                    attributes.append(", ");
                }
            }
            attributes.append(")");
        }
        return String.format(sql, metaModel.getObjectClass().getSimpleName(), attributes);
    }

    private static String getSqlType(int type) {
        if (type == Types.BOOLEAN) {
            return "boolean";
        } else if (type == Types.VARCHAR) {
            return "varchar";
        } else if (type == Types.TINYINT) {
            return "tinyint";
        } else if (type == Types.SMALLINT) {
            return "smallint";
        } else if (type == Types.INTEGER) {
            return "int";
        } else if (type == Types.REAL) {
            return "real";
        } else if (type == Types.BIGINT) {
            return "bigint";
        } else if (type == Types.DOUBLE) {
            return "double";
        }
        throw new UnsupportedOperationException("not supported type: " + type);
    }

    public static int getSqlType(DbMode dbMode, MetaField metaField) {
        Class<?> wrappedType = metaField.getWrappedType();
        if (wrappedType == Boolean.class) {
            return Types.BOOLEAN;
        } else if (wrappedType == String.class) {
            return Types.VARCHAR;
        } else if (wrappedType == Byte.class) {
            return Types.TINYINT;
        } else if (wrappedType == Short.class) {
            return Types.SMALLINT;
        } else if (wrappedType == Integer.class) {
            return Types.INTEGER;
        } else if (wrappedType == Float.class) {
            return Types.REAL;
        } else if (wrappedType == Long.class) {
            return Types.BIGINT;
        } else if (wrappedType == Double.class) {
            return Types.DOUBLE;
        } else {
            return Types.INTEGER;
        }
    }

    public static String createAlterTableAlterColumn(DbMode dbMode, MetaModel metaModel, MetaField metaField) {
        StringBuilder s = new StringBuilder();
        s.append("alter table %s alter column %s ");
        s.append(getSqlType(getSqlType(dbMode, metaField)));
        return String.format(s.toString(), metaModel.getObjectClass().getSimpleName(), metaField.getName());
    }

    public static String createAlterTableAddColumn(DbMode dbMode, MetaModel metaModel, MetaField metaField) {
        StringBuilder s = new StringBuilder();
        s.append("alter table %s add column %s ");
        s.append(getSqlType(getSqlType(dbMode, metaField)));
        return String.format(s.toString(), metaModel.getObjectClass().getSimpleName(), metaField.getName());
    }

    public static String createInsertSql(DbMode dbMode, MetaModel metaModel, Object object) {
        StringBuilder fields = new StringBuilder();
        StringBuilder values = new StringBuilder();
        List<MetaField> metaFields = new ArrayList<MetaField>(metaModel.getMetaFields());
        List<MetaField> keys = metaModel.getKeyFields();
        if (keys.size() == 1) {
            MetaField key = keys.get(0);
            if (key.getWrappedType() == Integer.class) {
                metaFields.remove(key);
            }
        }
        for (int i = 0, n = metaFields.size(); i < n; i ++) {
            MetaField field = metaFields.get(i);
            Object value = field.getValue(object);
            fields.append(field.getName());
            if (field.getWrappedType() == String.class) {
                values.append(String.format("'%s'", value));
            } else {
                values.append(String.format("%s", value));
            }
            if (i < n - 1) {
                values.append(", ");
                fields.append(", ");
            }
        }
        return String.format("insert into %s (%s) values(%s)", metaModel.getObjectClass().getSimpleName(), fields, values);
    }

    public static String createSelectSql(DbMode dbMode, MetaModel metaModel, Object id) {
        StringBuilder fields = new StringBuilder();
        StringBuilder where = new StringBuilder();
        List<MetaField> metaFields = new ArrayList<MetaField>(metaModel.getMetaFields());
        for (int i = 0, n = metaFields.size(); i < n; i ++) {
            MetaField field = metaFields.get(i);
            fields.append(field.getName());
            if (i < n - 1) {
                fields.append(", ");
            }
        }
        Map<MetaField, Object> keys = (Map<MetaField, Object>) id;
        for (Iterator<MetaField> iterator = keys.keySet().iterator(); iterator.hasNext(); ) {
            MetaField metaField = iterator.next();
            Object value = keys.get(metaField);
            where.append(metaField.getName());
            where.append("=");
            if (metaField.getWrappedType() == String.class) {
                where.append(String.format("'%s'", value));
            } else {
                where.append(String.format("%s", value));
            }
            if (iterator.hasNext()) {
                where.append(" and ");
            }
        }
        return String.format("select %s from %s where %s", fields, metaModel.getObjectClass().getSimpleName(), where);
    }

    public static String createSelectSql(DbMode dbMode, MetaModel metaModel) {
        StringBuilder fields = new StringBuilder();
        List<MetaField> metaFields = new ArrayList<MetaField>(metaModel.getMetaFields());
        for (int i = 0, n = metaFields.size(); i < n; i ++) {
            MetaField field = metaFields.get(i);
            fields.append(field.getName());
            if (i < n - 1) {
                fields.append(", ");
            }
        }
        return String.format("select %s from %s", fields, metaModel.getObjectClass().getSimpleName());
    }

}
