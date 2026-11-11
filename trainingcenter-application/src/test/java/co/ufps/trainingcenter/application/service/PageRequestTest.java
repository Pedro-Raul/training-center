package co.ufps.trainingcenter.application.service;

import co.ufps.trainingcenter.DomainException;
import co.ufps.trainingcenter.application.query.PageRequest;
import co.ufps.trainingcenter.application.query.PageResult;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PageRequestTest {

    @Test
    void shouldComputeOffset() {
        assertEquals(40, new PageRequest(3, 20, null, null).offset());
    }

    @Test
    void shouldRejectPageLessThanOne() {
        assertThrows(DomainException.class, () -> new PageRequest(0, 20, null, null));
    }

    @Test
    void shouldRejectSizeAboveMax() {
        assertThrows(DomainException.class, () -> new PageRequest(1, 101, null, null));
    }

    @Test
    void shouldComputeTotalPages() {
        PageResult<String> result = new PageResult<>(List.of("a"), 1, 10, 25);
        assertEquals(3, result.totalPages());
    }
}