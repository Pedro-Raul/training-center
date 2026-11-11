package co.ufps.trainingcenter.infrastructure.persistence.contest;

import co.ufps.trainingcenter.domain.contest.Contest;
import co.ufps.trainingcenter.domain.contest.ContestId;
import co.ufps.trainingcenter.infrastructure.persistence.contest.ContestJpa;

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
