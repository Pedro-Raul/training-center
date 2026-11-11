package co.ufps.trainingcenter.persistence.contest;

import co.ufps.trainingcenter.contest.Contest;
import co.ufps.trainingcenter.contest.ContestId;

public class ContestJpaMapper {

    public static Contest toDomain(ContestJpa jpa) {
        if (jpa == null) return null;
        return new Contest(
                ContestId.of(jpa.getContestId()),
                jpa.getTitle(),
                jpa.getPlatform(),
                jpa.getUrl(),
                jpa.getStartTime(),
                jpa.getDurationSeconds(),
                jpa.getStatus(),
                jpa.isCustom()
        );
    }

    public static ContestJpa toJpa(Contest domain) {
        if (domain == null) return null;
        ContestJpa jpa = new ContestJpa();
        jpa.setContestId(domain.getId().value());
        jpa.setTitle(domain.getTitle());
        jpa.setPlatform(domain.getPlatform());
        jpa.setUrl(domain.getUrl());
        jpa.setStartTime(domain.getStartTime());
        jpa.setDurationSeconds(domain.getDurationSeconds());
        jpa.setStatus(domain.getStatus());
        jpa.setCustom(domain.isCustom());
        return jpa;
    }
}
