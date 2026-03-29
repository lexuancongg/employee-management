CREATE EXTENSION IF NOT EXISTS unaccent;
CREATE EXTENSION IF NOT EXISTS pg_trgm;

CREATE INDEX idx_branch_name_trgm
    ON branch USING GIN (name gin_trgm_ops);


CREATE INDEX idx_country_name_trgm
    ON country USING GIN (name gin_trgm_ops);

CREATE INDEX idx_department_name_trgm
    ON department USING GIN (name gin_trgm_ops);

CREATE INDEX idx_district_name_trgm
    ON district USING GIN (name gin_trgm_ops);


CREATE INDEX idx_employee_name_trgm
    ON employee USING GIN (name gin_trgm_ops);

CREATE INDEX idx_position_name_trgm
    ON position USING GIN (name gin_trgm_ops);

CREATE INDEX idx_province_name_trgm
    ON province USING GIN (name gin_trgm_ops);