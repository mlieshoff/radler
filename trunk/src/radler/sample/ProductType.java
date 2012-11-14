package radler.sample;

import radler.gui.annotation.Editables;
import radler.gui.annotation.Selectables;
import radler.persistence.annotation.Id;

/**
 * This ...
 *
 * @author mlieshoff
 */
@Selectables(columns = {"_name"})
@Editables(columns = {"_name"})
public class ProductType {

    @Id
    private String _name;

}
