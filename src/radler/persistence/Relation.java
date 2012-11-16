package radler.persistence;

/**
 * This ...
 *
 * @author mlieshoff
 */
public class Relation {

    private Class<?> _to;
    private RelationType _relationType;
    private String _pattern;
    private String[] _members;

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

    public String getDisplayPattern() {
        return _pattern;
    }

    public void setDisplayPattern(String pattern) {
        _pattern = pattern;
    }

    public String[] getDisplayMembers() {
        return _members;
    }

    public void setDisplayMembers(String[] members) {
        _members = members;
    }
}
