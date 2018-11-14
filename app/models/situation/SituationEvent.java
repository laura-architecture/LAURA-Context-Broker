package models.situation;

public class SituationEvent {

    private SituationEventType type;
    private Long timestamp;
    private PersistentSituation situation;

    public SituationEvent() {

    }

    public SituationEvent(SituationEventType type, Long timestamp, PersistentSituation situation) {
        this.setType(type);
        this.setTimestamp(timestamp);
        this.setSituation(situation);
    }


    public SituationEventType getType() {
        return type;
    }

    public void setType(SituationEventType type) {
        this.type = type;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public PersistentSituation getSituation() {
        return situation;
    }

    public void setSituation(PersistentSituation situation) {
        this.situation = situation;
    }
}
