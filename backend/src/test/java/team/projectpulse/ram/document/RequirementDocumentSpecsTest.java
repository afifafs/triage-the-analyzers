package team.projectpulse.ram.document;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.data.jpa.domain.Specification;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RequirementDocumentSpecsTest {

    @Test
    void hasTitleLike_usesLocaleIndependentCaseConversion() {
        Locale originalLocale = Locale.getDefault();
        try {
            Locale.setDefault(Locale.forLanguageTag("tr-TR"));

            @SuppressWarnings("unchecked")
            Root<RequirementDocument> root = mock(Root.class);
            @SuppressWarnings("unchecked")
            Path<String> title = mock(Path.class);
            CriteriaBuilder criteriaBuilder = mock(CriteriaBuilder.class);
            Predicate expectedPredicate = mock(Predicate.class);
            ArgumentCaptor<String> titlePattern = ArgumentCaptor.forClass(String.class);

            when(root.<String>get("title")).thenReturn(title);
            when(criteriaBuilder.lower(title)).thenReturn(title);
            when(criteriaBuilder.like(eq(title), anyString())).thenReturn(expectedPredicate);

            Specification<RequirementDocument> specification = RequirementDocumentSpecs.hasTitleLike("IVAN");

            assertThat(specification.toPredicate(root, mock(CriteriaQuery.class), criteriaBuilder))
                    .isSameAs(expectedPredicate);
            verify(criteriaBuilder).like(eq(title), titlePattern.capture());
            assertThat(titlePattern.getValue()).isEqualTo("%ivan%");
        } finally {
            Locale.setDefault(originalLocale);
        }
    }
}
