package jkmau5.alternativeenergy.network.synchronisation;

import java.util.List;

/**
 * No description given
 *
 * @author jk-5
 */
public interface ISynchronisationHandler {

    public SynchronsiationMap<?> getSyncMap();

    public void onSynced(List<ISynchronized> changes);
}
