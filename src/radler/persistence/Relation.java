package radler.persistence;

/**
 * This ...
 *
 * @author mlieshoff
 */
public class Relation {

    private Class<?> _to;
    private RelationType _relationType;

    public Class<?> getTo() {
        return _to;
    }

    public void setTo(Class<?> to) {
        _to = to;
    }

    public void setRelationType(RelationType relationType) {
        _relationType = relationType;
    }

    public RelationType getRelationType() {
        return _relationType;
    }
}
