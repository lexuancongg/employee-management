CREATE EXTENSION IF NOT EXISTS unaccent;
CREATE EXTENSION IF NOT EXISTS pg_trgm;
-- Branch
CREATE INDEX idx_branch_name_trgm
    ON branch USING GIN (unaccent(lower(name)) gin_trgm_ops);

-- Country
CREATE INDEX idx_country_name_trgm
    ON country USING GIN (unaccent(lower(name)) gin_trgm_ops);

-- Department
CREATE INDEX idx_department_name_trgm
    ON department USING GIN (unaccent(lower(name)) gin_trgm_ops);

-- District
CREATE INDEX idx_district_name_trgm
    ON district USING GIN (unaccent(lower(name)) gin_trgm_ops);

-- Employee
CREATE INDEX idx_employee_name_trgm
    ON employee USING GIN (unaccent(lower(name)) gin_trgm_ops);

-- Position
CREATE INDEX idx_position_name_trgm
    ON position USING GIN (unaccent(lower(name)) gin_trgm_ops);

-- Province
CREATE INDEX idx_province_name_trgm
    ON province USING GIN (unaccent(lower(name)) gin_trgm_ops);