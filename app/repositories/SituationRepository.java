package repositories;

import models.situation.PersistentSituation;

import javax.inject.Singleton;
import java.util.*;
import java.util.stream.Collectors;

@Singleton
public class SituationRepository {

    private Map<Long, PersistentSituation> situations = new HashMap<Long, PersistentSituation>();
    private Map<Long, PersistentSituation> ongoing = new HashMap<Long, PersistentSituation>();

    public PersistentSituation save(br.ufes.inf.lprm.situation.model.Situation sit) {
        PersistentSituation situation = new PersistentSituation((long) (situations.size() + 1), sit );
        situations.put(situation.getId(), situation);
        ongoing.put(situation.getSituation().getUID(), situation);
        return situation;
    }

    public Optional<PersistentSituation> getById(long id) {
        return Optional.ofNullable(situations.get(id));
    }

    public Optional<PersistentSituation> getByOngoingId(long oid) {
        return Optional.ofNullable(ongoing.get(oid));
    }

    public void removeOngoing(long oid) {
        ongoing.remove(oid);
    }

    public List<PersistentSituation> getAll() {
        return new ArrayList<>(situations.values());
    }
}
