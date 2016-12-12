package fxtrader.com.app.lrececlerview.interfaces;

public interface OnSwipeMenuItemClickListener {

    /**
     * Invoke when the menu item is clicked.
     *
     * @param closeable       closeable.
     * @param adapterPosition adapterPosition.
     * @param menuPosition    menuPosition.
     */
    void onItemClick(Closeable closeable, int adapterPosition, int menuPosition, int direction);

}