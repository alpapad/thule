INSERT INTO standard_table_name_entity (id) VALUES('standardTableEntityId');

INSERT INTO Some_customTable_Name12_3 (id) VALUES('customTableEntityId');

INSERT INTO standard_column_name_entity (id, some_property) VALUES('standardColumnEntityId', 'standardPropertyValue');

INSERT INTO custom_column_name_entity (id, custom_columnName) VALUES('customColumnEntityId', 'customPropertyValue');

INSERT INTO standard_join_column_referenced_entity (id) VALUES ('standardJoinColumnReferencedEntityId');

INSERT INTO standard_join_column_owner_entity (
  id,
  referenced_entity_id
) VALUES (
  'standardJoinColumnOwnerEntityId',
  'standardJoinColumnReferencedEntityId'
);

INSERT INTO custom_join_column_referenced_entity (id) VALUES ('customJoinColumnReferencedEntityId');

INSERT INTO custom_join_column_owner_entity (
  id,
  customForeign_key_id
) VALUES (
  'customJoinColumnOwnerEntityId',
  'customJoinColumnReferencedEntityId'
);