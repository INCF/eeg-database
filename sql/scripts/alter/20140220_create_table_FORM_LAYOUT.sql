CREATE TABLE form_layout (
	form_layout_id INTEGER PRIMARY KEY NOT NULL,
	form_name VARCHAR(50) NOT NULL,
	layout_name VARCHAR(50) NOT NULL,
	content BYTEA NOT NULL,
	person_id INTEGER
);

ALTER TABLE form_layout ADD CONSTRAINT form_layout_unique_idx UNIQUE (form_name, layout_name);

ALTER TABLE form_layout ADD CONSTRAINT form_layout_person_fk FOREIGN KEY (person_id) REFERENCES person (person_id) ON DELETE SET NULL;