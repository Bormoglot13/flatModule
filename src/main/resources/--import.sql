create sequence hibernate_sequence;
create extension ltree;
create table module (id bigint PRIMARY KEY, parent_id bigint, name varchar(100) not null, path ltree,
                     created timestamp with time zone default current_timestamp,
                     updated timestamp with time zone default current_timestamp,
                     constraint fk_module_parent_id foreign key(parent_id) references module(id) );
CREATE UNIQUE INDEX idx_module_path_btree_idx ON module USING btree(path);
CREATE INDEX idx_module_path_gist_idx ON module USING gist(path);
create table cost (id bigint PRIMARY KEY, PURCHASE_COST numeric(19,2), DELIVERY_COST numeric(19,2),
                    INSTALL_COST numeric(19,2),created timestamp with time zone default current_timestamp,
                    updated timestamp with time zone default current_timestamp,
                    constraint fk_cost_module foreign key(id) references module(id)
                    on delete set null);

CREATE OR REPLACE FUNCTION get_calc_module_path(id bigint)
  RETURNS ltree AS
$$
SELECT  CASE WHEN m.parent_id IS NULL THEN m.id::text::ltree
            ELSE get_calc_module_path(m.parent_id)  || m.id::text END
    FROM module as m
    WHERE m.id = $1;
$$
  LANGUAGE sql;

CREATE OR REPLACE FUNCTION trg_update_module_path_updated() RETURNS trigger AS
$$
BEGIN
  IF TG_OP = 'UPDATE' THEN
        IF (NEW.name != OLD.name) THEN
            UPDATE module SET updated = current_timestamp where module.id = NEW.id; 
        END IF;
        IF (COALESCE(OLD.parent_id,0) != COALESCE(NEW.parent_id,0)  OR  NEW.id != OLD.id) THEN
            -- update all nodes that are children of this one including this one
            UPDATE module SET path = get_calculated_module_path(id)
                WHERE OLD.path  @> module.path;
        END IF;
  ELSIF TG_OP = 'INSERT' THEN
        UPDATE module SET path = get_calc_module_path(NEW.id) WHERE module.id = NEW.id;
  END IF;

  RETURN NEW;
END
$$
LANGUAGE 'plpgsql' VOLATILE;


CREATE OR REPLACE FUNCTION trg_insert_module_constraints() RETURNS trigger AS
$$
DECLARE
    l_cnt int;
    l_recursion_level int;
    c_maxlevel constant int := 5;
    c_alllevel_maxcount constant int  := 20;
    c_lastlevel_maxcount constant int  := 10000;
BEGIN
  IF TG_OP = 'INSERT' THEN
        WITH RECURSIVE a (id,parent_id,recursion_level) AS (
            SELECT id, parent_id, 1::integer recursion_level
            FROM module
            WHERE 1=1
                  and id = NEW.PARENT_ID
            UNION ALL
            SELECT m.id, m.parent_id, a.recursion_level + 1
            FROM module m
            JOIN a ON a.parent_id = m.id
        )
        select max(recursion_level), count(id)
        into l_recursion_level, l_cnt
        from a
        group by recursion_level;
        IF l_recursion_level > c_maxlevel THEN
            RAISE EXCEPTION 'Current recursion_level --> % prohibited. Limit exceeded.', l_recursion_level
                  USING HINT = 'Достигнута максимальная вложенность элементов';
        ELSIF (l_recursion_level < c_maxlevel - 1 and  l_cnt + 1 > c_alllevel_maxcount) THEN
            RAISE EXCEPTION 'Limit exceeded. --> %', c_alllevel_maxcount
                  USING HINT = 'Превышено число элементов на данном уровне';
        ELSIF (l_recursion_level = c_maxlevel and  l_cnt + 1 > c_lastlevel_maxcount) THEN
                    RAISE EXCEPTION 'Limit exceeded. --> %', c_lastlevel_maxcount
                          USING HINT = 'Превышено число элементов на данном уровне';
        END IF;
  END IF;

  RETURN NEW;
END
$$
LANGUAGE 'plpgsql' VOLATILE;


CREATE OR REPLACE FUNCTION trg_update_cost_updated() RETURNS trigger AS
$$
BEGIN
  IF TG_OP = 'UPDATE' THEN
        IF (NEW.purchase_cost != OLD.purchase_cost or NEW.purchase_cost != OLD.install_cost
            or NEW.delivery_cost != OLD.delivery_cost ) THEN
            UPDATE cost SET updated = current_timestamp where cost.id = NEW.id; 
        END IF;
  END IF;

  RETURN NEW;
END
$$
LANGUAGE 'plpgsql' VOLATILE;


CREATE TRIGGER trg01_trg_insert_module_constraints AFTER INSERT OR UPDATE OF id, parent_id
   ON module FOR EACH ROW
   EXECUTE PROCEDURE trg_insert_module_constraints();

CREATE TRIGGER trg02_update_module_path_name AFTER INSERT OR UPDATE OF id, parent_id, name
   ON module FOR EACH ROW
   EXECUTE PROCEDURE trg_update_module_path_updated();
   
CREATE TRIGGER trg01_update_cost_updated AFTER UPDATE OF purchase_cost,install_cost,delivery_cost
   ON cost FOR EACH ROW
   EXECUTE PROCEDURE trg_update_cost_updated();

INSERT INTO module (ID, PARENT_ID, NAME, CREATED, UPDATED) VALUES(1, null, 'Здание', TO_TIMESTAMP('2018-03-21 01:00:00', 'YYYY-MM-DD HH:MI:SS'), TO_TIMESTAMP('2018-03-21', 'YYYY-MM-DD'));
INSERT INTO module (ID, PARENT_ID, NAME, CREATED, UPDATED) VALUES(2, 1,'Окна', TO_TIMESTAMP('2018-04-22', 'YYYY-MM-DD'), TO_TIMESTAMP('2018-04-22', 'YYYY-MM-DD'));
INSERT INTO module (ID, PARENT_ID, NAME, CREATED, UPDATED) VALUES(3, 1,'Двери', TO_TIMESTAMP('2018-05-23', 'YYYY-MM-DD'), TO_TIMESTAMP('2018-05-23', 'YYYY-MM-DD'));
INSERT INTO module (ID, PARENT_ID, NAME, CREATED, UPDATED) VALUES(4, 2,'Профильные', TO_TIMESTAMP('2018-04-22', 'YYYY-MM-DD'), TO_TIMESTAMP('2018-04-22', 'YYYY-MM-DD'));
INSERT INTO module (ID, PARENT_ID, NAME, CREATED, UPDATED) VALUES(5, 3,'Пожарные', TO_TIMESTAMP('2018-04-22', 'YYYY-MM-DD'), TO_TIMESTAMP('2018-04-22', 'YYYY-MM-DD'));
INSERT INTO module (ID, PARENT_ID, NAME, CREATED, UPDATED) VALUES(6, 4,'500x600', TO_TIMESTAMP('2018-04-22', 'YYYY-MM-DD'), TO_TIMESTAMP('2018-04-22', 'YYYY-MM-DD'));
INSERT INTO module (ID, PARENT_ID, NAME, CREATED, UPDATED) VALUES(7, 4,'800x700', TO_TIMESTAMP('2018-04-22', 'YYYY-MM-DD'), TO_TIMESTAMP('2018-04-22', 'YYYY-MM-DD'));
INSERT INTO module (ID, PARENT_ID, NAME, CREATED, UPDATED) VALUES(8, 5,'120x230', TO_TIMESTAMP('2018-04-22', 'YYYY-MM-DD'), TO_TIMESTAMP('2018-04-22', 'YYYY-MM-DD'));
INSERT INTO module (ID, PARENT_ID, NAME, CREATED, UPDATED) VALUES(9, 6,'Окно 1251', TO_TIMESTAMP('2018-04-22', 'YYYY-MM-DD'), TO_TIMESTAMP('2018-04-22', 'YYYY-MM-DD'));
INSERT INTO module (ID, PARENT_ID, NAME, CREATED, UPDATED) VALUES(10, 6,'Окно 1252', TO_TIMESTAMP('2018-04-22', 'YYYY-MM-DD'), TO_TIMESTAMP('2018-04-22', 'YYYY-MM-DD'));
INSERT INTO module (ID, PARENT_ID, NAME, CREATED, UPDATED) VALUES(11, 7,'Окно 756', TO_TIMESTAMP('2018-04-22', 'YYYY-MM-DD'), TO_TIMESTAMP('2018-04-22', 'YYYY-MM-DD'));
INSERT INTO module (ID, PARENT_ID, NAME, CREATED, UPDATED) VALUES(12, 7,'Окно 757', TO_TIMESTAMP('2018-04-22', 'YYYY-MM-DD'), TO_TIMESTAMP('2018-04-22', 'YYYY-MM-DD'));
INSERT INTO module (ID, PARENT_ID, NAME, CREATED, UPDATED) VALUES(13, 8,'Дверь пожарная 1734', TO_TIMESTAMP('2018-04-22', 'YYYY-MM-DD'), TO_TIMESTAMP('2018-04-22', 'YYYY-MM-DD'));
INSERT INTO module (ID, PARENT_ID, NAME, CREATED, UPDATED) VALUES(14, 8,'Дверь пожарная 1735', TO_TIMESTAMP('2018-04-22', 'YYYY-MM-DD'), TO_TIMESTAMP('2018-04-22', 'YYYY-MM-DD'));

INSERT INTO cost (ID, PURCHASE_COST, DELIVERY_COST, INSTALL_COST) VALUES(9,30,30,40);
INSERT INTO cost (ID, PURCHASE_COST, DELIVERY_COST, INSTALL_COST) VALUES(10,30,30,40);
INSERT INTO cost (ID, PURCHASE_COST, DELIVERY_COST, INSTALL_COST) VALUES(11,50,50,50);
INSERT INTO cost (ID, PURCHASE_COST, DELIVERY_COST, INSTALL_COST) VALUES(12,50,50,50);
INSERT INTO cost (ID, PURCHASE_COST, DELIVERY_COST, INSTALL_COST) VALUES(13,130,150,120);
INSERT INTO cost (ID, PURCHASE_COST, DELIVERY_COST, INSTALL_COST) VALUES(14,130,150,120);



CREATE OR REPLACE VIEW v_tree_data
as
WITH RECURSIVE a (id,parent_id,recursion_level,name,path) AS (
SELECT id, parent_id, 1::integer recursion_level, name, path
FROM module
WHERE 1=1 -- and id = :id
      and parent_id IS NULL
UNION ALL
SELECT m.id, m.parent_id, a.recursion_level + 1, m.name, m.path
FROM module m
JOIN a ON a.id = m.parent_id -- and m.recursion_level < :flatModuleMaxLevel
),
cost_agg as (select    m.path, sum(purchase_cost+delivery_cost+install_cost) as cost,
                       sum(purchase_cost) as purchase_cost,
                       sum(delivery_cost) as delivery_cost,
                       sum(install_cost) as install_cost
             from      module m left join module sub on sub.path <@ m.path
                                left join cost c on c.id = sub.id
             group by  m.path),
nounpivot_data as (
SELECT  a.id,a.parent_id,
        name || case when recursion_level = 1
                     then ' '||a.id
                     else ''
                 end as name,
        c.cost,c.purchase_cost,c.delivery_cost,
        c.install_cost,
        /*name || case when recursion_level = 1
                     then ' (id:'||a.id||')'
                     when recursion_level = 5
                     then ' (Стоимость: '||purchase_cost+delivery_cost+install_cost||')'
                     else ''
                 end as line,*/ recursion_level,
        cast(ltree2text(subpath(a.path,0,1)) as bigint) root , a.path
FROM a left join cost_agg c on a.path=c.path
WHERE 1=1
)
select n.name,t.prop,t.value,n.path,n.root,n.id,n.parent_id from nounpivot_data n
cross join lateral(values
                    (n.cost,'Стоимость'),(n.purchase_cost,'Цена закупки'),
                    (n.delivery_cost,'Стоимость поставки'),
                    (n.install_cost,'Стоимость монтажа')
                 ) as t(value,prop);

--docker exec -it db psql -U compose-postgres -c '\x' -c "select 1;"


