package com.gohenry.utils.jpa;

import com.gohenry.utils.jpa.repository.CustomColumnNameRepository;
import com.gohenry.utils.jpa.repository.CustomJoinColumnReferencedRepository;
import com.gohenry.utils.jpa.repository.CustomTableNameRepository;
import com.gohenry.utils.jpa.repository.StandardColumnNameRepository;
import com.gohenry.utils.jpa.repository.StandardJoinColumnReferencedRepository;
import com.gohenry.utils.jpa.repository.StandardTableNameRepository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ImplicitNamingStrategyIntTest {

    @Autowired
    private StandardTableNameRepository standardTableNameRepository;
    @Autowired
    private StandardColumnNameRepository standardColumnNameRepository;
    @Autowired
    private CustomTableNameRepository customTableNameRepository;
    @Autowired
    private CustomColumnNameRepository customColumnNameRepository;
    @Autowired
    private StandardJoinColumnReferencedRepository standardJoinColumnReferencedRepository;
    @Autowired
    private CustomJoinColumnReferencedRepository customJoinColumnReferencedRepository;

    @Test
    public void when_querying_conventionally_named_entity_table_then_repository_maps_names_to_snake_case() {

        //When
        var fromDb = standardTableNameRepository.findById("standardTableEntityId");

        //Then
        assertThat(fromDb).isPresent();

    }

    @Test
    public void when_querying_custom_named_entity_table_then_repository_maps_name_from_annotation() {

        //When
        var fromDb = customTableNameRepository.findById("customTableEntityId");

        //Then
        assertThat(fromDb).isPresent();

    }

    @Test
    public void when_querying_conventionally_named_entity_column_then_repository_maps_names_to_snake_case() {

        //When
        var fromDb = standardColumnNameRepository.findBySomeProperty("standardPropertyValue");

        //Then
        assertThat(fromDb).isNotNull();
        assertThat(fromDb.getSomeProperty()).isEqualTo("standardPropertyValue");

    }

    @Test
    public void when_querying_custom_named_entity_column_then_repository_maps_name_from_annotation() {

        //When
        var fromDb = customColumnNameRepository.findBySomeProperty("customPropertyValue");

        //Then
        assertThat(fromDb).isNotNull();
        assertThat(fromDb.getSomeProperty()).isEqualTo("customPropertyValue");

    }

    @Test
    public void when_querying_conventionally_named_many_to_one_relationship_then_repositary_main_foreign_key_with_snake_case() {

        //When
        var fromDb = standardJoinColumnReferencedRepository.findById("standardJoinColumnReferencedEntityId");

        //Then
        assertThat(fromDb).isPresent();
        assertThat(fromDb.get().getOwnerEntities()).hasSize(1);

    }

    @Test
    public void when_querying_custom_named_many_to_one_relationship_then_repositary_maps_foreign_key_from_annotation() {

        //When
        var fromDb = customJoinColumnReferencedRepository.findById("customJoinColumnReferencedEntityId");

        //Then
        assertThat(fromDb).isPresent();
        assertThat(fromDb.get().getOwnerEntities()).hasSize(1);

    }

}