-- this is the schema definition used for the main relational provenance
-- implementation (in both sqlite3 and postgres)

drop table annot_dataset_num cascade;
drop table annot_dataset_text cascade;
drop table ds cascade;

-- ds stores all dataset identifiers.

create table ds (
      uid    serial,
      id	 varchar(256) primary key
);

create table annot_dataset_num ( 
       -- dataset_id varchar(256) references ds (id) on delete cascade, 
       dataset_id varchar(256), -- on delete cascade, 
       name  varchar(256),
       value numeric,
       uid   serial primary key
       -- primary key (dataset_id, name)
);

create table annot_dataset_text( 
       -- dataset_id varchar(256) references ds (id) on delete cascade, 
       dataset_id varchar(256), -- on delete cascade, 
       name  varchar(256),
       value varchar(2048),
       uid   serial primary key
       -- primary key (dataset_id, name)
);

create view annotation as
       select dataset_id, name, NULL as numeric_value, value as text_value from annot_dataset_text
       union all
       select dataset_id, name, value as numeric_value, NULL as text_value from annot_dataset_num;
