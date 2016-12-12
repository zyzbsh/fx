package fxtrader.com.app.lrececlerview.swipe.touch;

import android.support.v7.widget.helper.ItemTouchHelper;

public class CompatItemTouchHelper extends ItemTouchHelper {

    private Callback mCallback;

    public CompatItemTouchHelper(ItemTouchHelper.Callback callback) {
        super(callback);
        mCallback = callback;
    }

    /**
     * Developer callback which controls the behavior of ItemTouchHelper.
     *
     * @return {@link Callback}
     */
    protected ItemTouchHelper.Callback getCallback() {
        return mCallback;
    }
}
