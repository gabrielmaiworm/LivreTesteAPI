package com.livre.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.livre.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AparelhoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Aparelho.class);
        Aparelho aparelho1 = new Aparelho();
        aparelho1.setId(1L);
        Aparelho aparelho2 = new Aparelho();
        aparelho2.setId(aparelho1.getId());
        assertThat(aparelho1).isEqualTo(aparelho2);
        aparelho2.setId(2L);
        assertThat(aparelho1).isNotEqualTo(aparelho2);
        aparelho1.setId(null);
        assertThat(aparelho1).isNotEqualTo(aparelho2);
    }
}
