package models.situation;

import models.Model;

public class PersistentSituation extends Model {

    private br.ufes.inf.lprm.situation.model.Situation situation;

    public PersistentSituation() {

    }

    public PersistentSituation(Long id, br.ufes.inf.lprm.situation.model.Situation situation) {
        this.id = id;
        this.situation = situation;
    }

    public br.ufes.inf.lprm.situation.model.Situation getSituation() {
        return situation;
    }
}
