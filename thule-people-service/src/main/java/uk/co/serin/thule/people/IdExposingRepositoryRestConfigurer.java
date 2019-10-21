package uk.co.serin.thule.people;

import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;

import uk.co.serin.thule.people.domain.entity.AuditEntity;
import uk.co.serin.thule.utils.utils.ClassUtils;

import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;

@Configuration
public class IdExposingRepositoryRestConfigurer implements RepositoryRestConfigurer {
    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
        // Identify all classes that need to have their ids exposed
        var componentProvider = new ClassPathScanningCandidateComponentProvider(false);
        componentProvider.addIncludeFilter(new AnnotationTypeFilter(Entity.class));
        componentProvider.addIncludeFilter(new AnnotationTypeFilter(MappedSuperclass.class));

        var beanDefinitions = componentProvider.findCandidateComponents(AuditEntity.class.getPackage().getName());
        var classes = beanDefinitions.stream().map(beanDefinition -> ClassUtils.forName(beanDefinition.getBeanClassName())).toArray(Class[]::new);

        // Expose ids for all domain model classes. By default, ids are not exposed by Spring Data Rest
        config.exposeIdsFor(classes);
    }
}
