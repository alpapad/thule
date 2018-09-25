CREATE TABLE standard_table_name_entity (
  id VARCHAR(50) NOT NULL,

  PRIMARY KEY (id)
);

CREATE TABLE Some_customTable_Name12_3 (
  id VARCHAR(50) NOT NULL,

  PRIMARY KEY (id)
);

CREATE TABLE standard_column_name_entity (
  id VARCHAR(50) NOT NULL,
  some_property VARCHAR(50),

  PRIMARY KEY (id)
);

CREATE TABLE custom_column_name_entity (
  id VARCHAR(50) NOT NULL,
  custom_columnName VARCHAR(50),

  PRIMARY KEY (id)
);

CREATE TABLE standard_join_column_referenced_entity (
  id VARCHAR(50) NOT NULL,

  PRIMARY KEY (id)
);

CREATE TABLE standard_join_column_owner_entity (
  id VARCHAR(50) NOT NULL,
  referenced_entity_id VARCHAR(50) NOT NULL,

  PRIMARY KEY (id),
  FOREIGN KEY (referenced_entity_id) REFERENCES standard_join_column_referenced_entity(id)
);

CREATE TABLE custom_join_column_referenced_entity (
  id VARCHAR(50) NOT NULL,

  PRIMARY KEY (id)
);

CREATE TABLE custom_join_column_owner_entity (
  id VARCHAR(50) NOT NULL,
  customForeign_key_id VARCHAR(50) NOT NULL,

  PRIMARY KEY (id),
  FOREIGN KEY (customForeign_key_id) REFERENCES custom_join_column_referenced_entity(id)
);
