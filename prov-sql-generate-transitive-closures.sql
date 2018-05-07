
-- based on http://coblitz.codeen.org:3125/citeseer.ist.psu.edu/cache/papers/cs/554/http:zSzzSzsdmc.krdl.org.sgzSzkleislizSzpsZzSzdlsw-ijit97-9.pdf/dong99maintaining.pdf

DROP TABLE trans;
DROP TABLE transbase;

-- general transitive relation table. this should be refined some.

-- maybe should have uniqueness constraints to stop multiple occurence
-- of same?

CREATE TABLE trans 
    (before varchar(2048),
     after varchar(2048),
    CONSTRAINT no_duplicate_arcs_in_trans UNIQUE (before, after));

CREATE TABLE transbase
    (before varchar(2048),
     after varchar(2048),
     CONSTRAINT no_duplicate_arcs_in_transbase UNIQUE (before, after)
    );

CREATE INDEX transindex ON trans (before);
-- CREATE INDEX transbaseindex ON transbase (before);

INSERT INTO transbase SELECT inner_dataset_id AS before, outer_dataset_id AS after FROM dataset_containment;

INSERT INTO transbase SELECT process_id AS before, dataset_id AS after FROM dataset_usage where direction='O';

INSERT INTO transbase SELECT dataset_id AS before, process_id AS after FROM dataset_usage where direction='I';

-- now seed the transitive closure table
INSERT into trans SELECT before, after from transbase;

