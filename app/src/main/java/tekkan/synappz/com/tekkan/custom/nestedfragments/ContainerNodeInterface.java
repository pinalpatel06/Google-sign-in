package tekkan.synappz.com.tekkan.custom.nestedfragments;

/**
 * Created by Tejas Sherdiwala on 26/04/17.
 */

public interface ContainerNodeInterface extends CommonNodeInterface {
    boolean onBackPressed();
    void setChild(CommonNodeInterface fragment);
    int getContainerId();
    FragmentChangeCallback getChangeCallback();
}
