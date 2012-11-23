package radler.persistence.db;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import radler.gui.MetaModel;
import radler.persistence.annotation.Id;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This ...
 *
 * @author mlieshoff
 */
public class DbUpdaterTest extends JdbcTemplate {

    @Before
    public void setUp() throws SQLException {
        connection = DriverManager.getConnection("jdbc:h2:~/unittest");
        // prepare
        updateInJdbc("create table if not exists tmp (id integer primary key)");
    }

    @After
    public void tearDown() throws SQLException {
        // check tmp not dropped
        queryInJdbc("select * from tmp");
        // drop all
        updateInJdbc("drop table if exists tmp");
        updateInJdbc("drop table if exists a");
        connection.close();
    }

    @Test
    public void shouldDontDropTable() throws SQLException {
        // exercise
        new DbUpdater("UNITTEST", "PUBLIC", connection, new ArrayList<MetaModel>()).update();
        // check tmp not dropped
        queryInJdbc("select * from tmp");
    }

    @Test
    public void shouldAddTable() throws SQLException {
        List<MetaModel> metaModels = Arrays.asList(new MetaModel(A.class));
        // exercise
        new DbUpdater("UNITTEST", "PUBLIC", connection, metaModels).update();
        // check a created
        queryInJdbc("select * from a");
    }

    @Test
    public void shouldDontDropColumn() throws SQLException {
        // prepare
        updateInJdbc("create table if not exists a (id integer primary key, dontdropme boolean)");
        List<MetaModel> metaModels = Arrays.asList(new MetaModel(A.class));
        // exercise
        new DbUpdater("UNITTEST", "PUBLIC", connection, metaModels).update();
        // check column not dropped
        queryInJdbc("select dontdropme from a");
        // check a column added
        queryInJdbc("select stringField from a");
    }

    @Test
    public void shouldChangeColumn() throws SQLException {
        // prepare
        updateInJdbc("create table if not exists a (id integer primary key, longField varchar(50))");
        List<MetaModel> metaModels = Arrays.asList(new MetaModel(A.class));
        // exercise
        new DbUpdater("UNITTEST", "PUBLIC", connection, metaModels).update();
        // check a column type changed
        queryInJdbc("select sum(longField) from a");
    }

    @Test
    public void shouldAddColumn() throws SQLException {
        // prepare
        updateInJdbc("create table if not exists a (id integer primary key)");
        List<MetaModel> metaModels = Arrays.asList(new MetaModel(A.class));
        // exercise
        new DbUpdater("UNITTEST", "PUBLIC", connection, metaModels).update();
        // check a column added
        queryInJdbc("select stringField from a");
    }

    static class A {
        @Id
        private int id;
        private String stringField;
        private long longField;
    }

}
