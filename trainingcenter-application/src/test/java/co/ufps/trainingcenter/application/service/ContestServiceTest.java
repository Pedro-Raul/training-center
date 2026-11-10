package co.ufps.trainingcenter.application.service;

import co.ufps.trainingcenter.application.port.out.ContestRepository;
import co.ufps.trainingcenter.contest.Contest;
import co.ufps.trainingcenter.contest.ContestId;
import co.ufps.trainingcenter.contest.Status;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContestServiceTest {

    @Mock
    private ContestRepository contestRepository;

    @InjectMocks
    private ContestService contestService;

    @Test
    void shouldReturnActiveContests() {
        when(contestRepository.findByStatus(Status.ACTIVE))
                .thenReturn(List.of(mock(Contest.class)));

        var result = contestService.getActiveContests();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(contestRepository).findByStatus(Status.ACTIVE);
    }

    @Test
    void shouldReturnEmptyListWhenNoActiveContests() {
        when(contestRepository.findByStatus(Status.ACTIVE)).thenReturn(List.of());

        var result = contestService.getActiveContests();

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldUpdateStatuses() {
        Contest upcoming = mock(Contest.class);
        Contest active = mock(Contest.class);

        when(contestRepository.findByStatus(Status.UPCOMING)).thenReturn(List.of(upcoming));
        when(contestRepository.findByStatus(Status.ACTIVE)).thenReturn(List.of(active));

        contestService.updateStatuses();

        verify(upcoming).updateStatusBasedOnTime(any());
        verify(active).updateStatusBasedOnTime(any());
        verify(contestRepository).save(upcoming);
        verify(contestRepository).save(active);
    }
}
