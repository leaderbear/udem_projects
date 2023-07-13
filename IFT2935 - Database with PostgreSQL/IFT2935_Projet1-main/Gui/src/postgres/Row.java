package postgres;

import java.util.ArrayList;


/**
 * Une structure pour garder une liste de colonnes
 */
public class Row {
    private ArrayList<Column> cols;

    public Row(ArrayList<Column> cols){
        this.setCols(cols);
    }

    public ArrayList<Column> getCols() {
        return cols;
    }

    public void setCols(ArrayList<Column> cols) {
        this.cols = cols;
    }
}
