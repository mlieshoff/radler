package radler.persistence.db;

import radler.gui.MetaField;
import radler.gui.MetaModel;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * This ...
 *
 * @author mlieshoff
 */
public class DbUpdater extends JdbcTemplate {

    private DbMode dbMode;
    private DatabaseMetaData databaseMetaData;

    private String catalog;
    private String schema;
    private Collection<MetaModel> metaModels;


    public DbUpdater(String catalog, String schema, Connection connection, Collection<MetaModel> metaModels) {
        this.catalog = catalog;
        this.schema = schema;
        this.connection = connection;
        this.metaModels = metaModels;
    }

    public void update() throws SQLException {
        if (metaModels == null || metaModels.size() == 0) {
            return;
        }
        databaseMetaData = connection.getMetaData();
        dbMode = DbMode.fromConnectionUrl(databaseMetaData.getURL());
        List<String> existingTables = getExistingTables();
        System.out.println("tables in db: " + existingTables);
        List<MetaModel> tablesToAdd = getTablesToAdd(metaModels, existingTables);
        System.out.println("tables to add: " + tablesToAdd);
        addTables(tablesToAdd);
        // check modified tables
        for (MetaModel metaModel : metaModels) {
            if (!tablesToAdd.contains(metaModel)) {
                System.out.println("modify table: " + metaModel.getObjectClass().getSimpleName());
                // columns changed OR columns added
                List<String> sqlForModifiedColumns = getModifiedColumnsSql(metaModel);
                for (String sql : sqlForModifiedColumns) {
                    updateInJdbc(sql);
                }
            }
        }
    }

    private List<String> getModifiedColumnsSql(MetaModel metaModel) throws SQLException {
        List<String> sql = new ArrayList<String>();
        ResultSet columns = databaseMetaData.getColumns(catalog, schema, metaModel.getObjectClass().getSimpleName().toUpperCase(), null);
        Set<String> detectedColumns = new HashSet<String>();
        for (MetaField metaField : metaModel.getMetaFields()) {
            detectedColumns.add(metaField.getName().toLowerCase());
        }
        // modify columns
        while (columns.next()) {
            String columnName = columns.getString(4);
            detectedColumns.remove(columnName.toLowerCase());
            MetaField metaField = metaModel.getMetaField(columnName);
            if (metaField != null) {
                int type = columns.getInt(5);
                if (type != SqlUtil.getSqlType(dbMode, metaField)) {
                    // alter column
                    sql.add(SqlUtil.createAlterTableAlterColumn(dbMode, metaModel, metaField));
                }
            } else {
                System.out.println("ignore column: " + columnName);
            }
        }
        // add columns
        for (String columnName : detectedColumns) {
            System.out.println("+ " + columnName);
            MetaField metaField = metaModel.getMetaField(columnName);
            sql.add(SqlUtil.createAlterTableAddColumn(dbMode, metaModel, metaField));
        }
        columns.close();
        return sql;
    }

    private void addTables(List<MetaModel> tablesToAdd) throws SQLException {
        for (MetaModel metaModel : tablesToAdd) {
            updateInJdbc(SqlUtil.createTableSql(dbMode, metaModel));
        }
    }

    private List<String> getExistingTables() throws SQLException {
        List<String> list = new ArrayList<String>();
        ResultSet resultSet = databaseMetaData.getTables(catalog, schema, "%", null);
        while (resultSet.next()) {
            list.add(resultSet.getString(3));
        }
        return list;
    }

    private List<MetaModel> getTablesToAdd(Collection<MetaModel> metaModels, List<String> existingTables) {
        List<MetaModel> list = new ArrayList<MetaModel>();
        for (MetaModel metaModel : metaModels) {
            boolean found = false;
            for (String existingTablename : existingTables) {
                if (existingTablename.toLowerCase().equals(metaModel.getObjectClass().getSimpleName().toLowerCase())) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                list.add(metaModel);
            }
        }
        return list;
    }


}
