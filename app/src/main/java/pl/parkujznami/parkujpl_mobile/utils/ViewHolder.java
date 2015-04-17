package pl.parkujznami.parkujpl_mobile.utils;

import android.util.SparseArray;
import android.view.View;

/**
 * Created by Marcin on 2015-04-14
 * <p/>
 * Allows for convenient use of viewholder pattern.
 *
 * @see <a href="http://www.piwai.info/android-adapter-good-practices">piwai.info/android-adapter-good-practices</a>
 */
public class ViewHolder {
    //added a generic return type to reduce the casting noise in client code
    @SuppressWarnings("unchecked")
    public static <T extends View> T get(View view, int id) {
        SparseArray<View> viewHolder = (SparseArray<View>) view.getTag();
        if (viewHolder == null) {
            viewHolder = new SparseArray<View>();
            view.setTag(viewHolder);
        }
        View childView = viewHolder.get(id);
        if (childView == null) {
            childView = view.findViewById(id);
            viewHolder.put(id, childView);
        }
        return (T) childView;
    }
}
