package radler.sample;

import radler.gui.annotation.Editables;
import radler.gui.annotation.Selectables;
import radler.persistence.annotation.Id;
import radler.persistence.annotation.OneToMany;

import java.util.List;

/**
 * This ...
 *
 * @author mlieshoff
 */
@Selectables(columns = {"_sku", "_title", "_description", "_active", "_productType"})
@Editables(columns = {"_sku", "_title", "_description", "_active", "_productType"})
public class Product {

    @Id
    private String _sku;
    private String _title;
    private String _description;
    private boolean _active;

    @OneToMany
    private ProductType _productType;

    private List<SubProduct> _subProducts;

    public void setSku(String sku) {
        _sku = sku;
    }

    public void setSubProducts(List<SubProduct> subProducts) {
        _subProducts = subProducts;
    }

    public String getSku() {
        return _sku;
    }

    public List<SubProduct> getSubProducts() {
        return _subProducts;
    }
}
