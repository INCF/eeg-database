
package cz.zcu.kiv.eegdatabase.logic.highlighter;

import java.util.Arrays;

/**
 *
 * @author Honza
 */
public class Highlighter {

  private Fragment[] fragment;
  private String startMark;
  private String endMark;



    public void highlight(String searchedText) {
        String[] words = searchedText.split(" ");
        for (int i = 0; i < fragment.length; i++) {
            for (int j = 0; j < words.length; j++) {
                String word = ""+words[j];
                boolean starBefore = false;
                boolean starAfter = false;
                if ((word.equals(""))||(word.equals("*"))||(word.equals("**"))) {
                    return;
                }
                if (words[j].charAt(0) == '*') {
                    starBefore = true;
                    word = ""+word.substring(1);
                }
                if (words[j].charAt(words[j].length()-1) == '*') {
                    starAfter = true;
                    word = ""+word.substring(0, word.length() -1);
                }


                if ((starBefore)&&(starAfter)) {
                    //TODO
                }
                if ((!starBefore)&&(starAfter)) {
                    //TODO
                }
                if ((starBefore)&&(!starAfter)) {
                    //TODO
                }
                if ((!starBefore)&&(!starAfter)) {
                    highlightWithoutStars(word, fragment[i]);
                }
            }
        }
    }

    private void highlightWithoutStars(String word, Fragment fragment) {
        int index = -1;
        String fg = fragment.getFragment();
        do {
            index = fg.indexOf(word, index+1);
            if (index < 0) {
                break;
            }
            if ((index > 0)&&((Character.isLetter(fg.charAt(index-1)))||
                              (Character.isDigit(fg.charAt(index-1))))) {
                continue;
            }
            if ((index + word.length() < fg.length())&&
                    ((Character.isLetter(fg.charAt(index+word.length())))||
                       (Character.isDigit(fg.charAt(index+word.length()))))) {
                continue;
            }
            fg = fg.substring(0, index) + startMark + fg.substring(index, index+word.length())+
            endMark + fg.substring(index+word.length());
            index += startMark.length() + endMark.length();
            fragment.setScore(fragment.getScore() + 1);
        } while(index > -1);
        fragment.setFragment(fg);
    }


    public String getBestFragment() {
        Arrays.sort(fragment);
        return fragment[0].getFragment();
    }

    public Fragment[] getFragment() {
        return fragment;
    }

    public void setFragment(Fragment[] fragment) {
        this.fragment = fragment;
    }


    public String getStartMark() {
        return startMark;
    }

    public String getEndMark() {
        return endMark;
    }

    public void setHighlightMark(String startMark, String endMark) {
        this.startMark = startMark;
        this.endMark = endMark;
    }

}
