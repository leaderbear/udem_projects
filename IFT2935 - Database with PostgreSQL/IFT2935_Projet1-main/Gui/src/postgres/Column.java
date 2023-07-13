package postgres;

/**
 * Une structure pour garder les noms des colonnes et leur valeurs
 */
public class Column {
    private String name;
    private Object value;

    public Column(String name, Object value){
        this.setName(name);
        this.setValue(value);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
