ALTER TABLE service_result DROP COLUMN figure;

ALTER TABLE service_result ADD COLUMN content bytea;
ALTER TABLE service_result ALTER COLUMN content SET NOT NULL;