package uk.co.serin.thule.people;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;

import uk.co.serin.thule.people.domain.DomainModel;
import uk.co.serin.thule.utils.utils.ClassUtils;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;

@Configuration
public class IdExposingRepositoryRestConfigurer extends RepositoryRestConfigurerAdapter {
    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
        // Identify all classes that need to have their ids exposed
        ClassPathScanningCandidateComponentProvider componentProvider = new ClassPathScanningCandidateComponentProvider(false);
        componentProvider.addIncludeFilter(new AnnotationTypeFilter(Entity.class));
        componentProvider.addIncludeFilter(new AnnotationTypeFilter(MappedSuperclass.class));

        Set<BeanDefinition> domainModelBeanDefinitions = componentProvider.findCandidateComponents(DomainModel.class.getPackage().getName());
        Class[] classes = domainModelBeanDefinitions.stream().map(
                domainModelBeanDefinition -> ClassUtils.forName(domainModelBeanDefinition.getBeanClassName())).toArray(Class[]::new);

        // Expose ids for all domain model classes. By default, ids are not exposed by Spring Data Rest
        config.exposeIdsFor(classes);
    }
}
