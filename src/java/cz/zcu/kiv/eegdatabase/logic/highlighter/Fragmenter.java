
package cz.zcu.kiv.eegdatabase.logic.highlighter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Honza
 */
public class Fragmenter {

   private boolean connectAttrs;

    public Fragment[] fragment(String[] attrs, int maxChar, Object object)
    throws IllegalArgumentException, IllegalAccessException  {
        Field[] fields = object.getClass().getDeclaredFields();
        Field[] field = new Field[attrs.length];
        int index = 0;
        for (int i = 0; i < fields.length; i++) {
            for (int j = 0; j < attrs.length; j++) {
                if (fields[i].getName().equals(attrs[j])) {
                    field[index] = fields[i];
                    index++;
                           break;
                }
            }
        }
        return fragment(field, maxChar, object);
    }


    public Fragment[] fragment(Field[] fields, int maxChar, Object object)
    throws IllegalArgumentException, IllegalAccessException {
        List<Fragment> list = new ArrayList<Fragment>();
        String s = "";
        for (int i = 0; i < fields.length; i++) {
            fields[i].setAccessible(true);
            if (connectAttrs) {
                if (s.length() == 0) {
                    s = ""+fields[i].get(object);
                }
                else {
                    s = s + " " + fields[i].get(object);
                }
            }
            else {
                String param = ""+fields[i].get(object);
                list =  createFragments(param, maxChar, list);
            }
        }
        if (connectAttrs) {
            list = createFragments(s, maxChar, list);
        }
        Fragment[] field = new Fragment[list.size()];
        field = list.toArray(field);
        return field;
    }


    public Fragment[] fragment(int maxChar, Object object)
    throws IllegalArgumentException, IllegalAccessException {
        Field[] fields = object.getClass().getDeclaredFields();
        return fragment(fields, maxChar, object);
    }

    private List<Fragment> createFragments(String word, int maxChar, List<Fragment> list) {
        String[] words = word.split(" ");
        String fragment = "";
        for (int k = 0; k < words.length; k++) {
            if (fragment.length() + words[k].length() <= maxChar) {
                if (fragment.length() == 0) {
                    fragment = words[k];
                } else {
                    fragment = fragment + " " + words[k];
                }
            }
            else {
                list.add(new Fragment(fragment));
                fragment = words[k];
            }
        }
        list.add(new Fragment(fragment));
        return list;
    }


    public boolean isConnectAttrs() {
        return connectAttrs;
    }


    public void setConnectAttrs(boolean connectAttrs) {
        this.connectAttrs = connectAttrs;
    }
}
