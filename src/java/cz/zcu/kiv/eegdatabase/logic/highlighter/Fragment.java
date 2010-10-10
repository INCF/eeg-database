
package cz.zcu.kiv.eegdatabase.logic.highlighter;

/**
 *
 * @author Honza
 */
public class Fragment implements Comparable<Fragment> {

  private String fragment;
  private float score;


  public Fragment(String text) {
        fragment = text;
        score = 0;
    }


  public String getFragment() {
        return fragment;
    }


    public float getScore() {
        return score;
    }


    public void setFragment(String fragment) {
        this.fragment = fragment;
    }


    public void setScore(float score) {
        this.score = score;
    }

    @Override
    public int compareTo(Fragment o) {
        if (this.score < o.score) return +1;
        if (this.score > o.score) return -1;
        return 0;
    }
}
