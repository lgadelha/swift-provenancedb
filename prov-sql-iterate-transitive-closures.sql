
-- based on http://coblitz.codeen.org:3125/citeseer.ist.psu.edu/cache/papers/cs/554/http:zSzzSzsdmc.krdl.org.sgzSzkleislizSzpsZzSzdlsw-ijit97-9.pdf/dong99maintaining.pdf

CREATE TEMPORARY TABLE transtemp
    (before char(128),
     after char(128),
CONSTRAINT no_duplicate_arcs_in_transtemp UNIQUE (before, after)
    );


-- existing path before existing arc
INSERT INTO transtemp SELECT DISTINCT transbase.before, trans.after FROM transbase, trans
    WHERE transbase.after = trans.before;

INSERT INTO trans SELECT transtemp.before, transtemp.after FROM transtemp
    WHERE NOT EXISTS (SELECT * FROM trans where trans.before = transtemp.before
    AND trans.after = transtemp.after);

SELECT COUNT(*) FROM trans;

-- existing arc before existing path


--INSERT INTO transbase SELECT execute_id AS before, dataset_id AS after FROM dataset_usage where direction='O';

--INSERT INTO transbase SELECT dataset_id AS before, execute_id AS after FROM dataset_usage where direction='I';

-- now seed the transitive closure table

-- INSERT into trans SELECT before, after from transbase;

