package org.broadinstitute.sting.oneoffprojects.walkers.association.modules.casecontrol;

import org.broadinstitute.sting.utils.pileup.PileupElement;
import org.broadinstitute.sting.utils.pileup.ReadBackedPileup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author chartl
 */
public class InsertSizeDistribution extends ValueTest {
    private final int MAPQ_THRESHOLD = 5;

    public boolean usePreviouslySeenReads() { return false; }

    public Collection<Number> map(ReadBackedPileup pileup) {
        List<Integer> insertSizes = new ArrayList<Integer>(pileup.size());
        for ( PileupElement e : pileup ) {
            if ( e.getMappingQual() >= MAPQ_THRESHOLD ) {
                insertSizes.add(Math.abs(e.getRead().getInferredInsertSize()));
            }
        }

        return (Collection) insertSizes;
    }

}
