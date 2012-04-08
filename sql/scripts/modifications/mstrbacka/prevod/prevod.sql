/* pridani laterality */
UPDATE PERSON SET LATERALITY='X'; 
ALTER TABLE PERSON MODIFY LATERALITY NOT NULL;

/* pridani education level */
INSERT INTO EDUCATION_LEVEL (EDUCATION_LEVEL_ID, "TITLE", IS_DEFAULT) VALUES (1, 'NotKnown', 0);
UPDATE PERSON SET EDUCATION_LEVEL_ID=1;
ALTER TABLE PERSON MODIFY EDUCATION_LEVEL_ID NOT NULL;

/* prejmenovani weathernote */
ALTER TABLE EXPERIMENT RETITLE COLUMN WEATHERNOTE TO ENVIRONMENT_NOTE;

/* pridani default artefact_id */
INSERT INTO ARTEFACT (ARTEFACT_ID, COMPENSATION, REJECT_CONDITION) VALUES (1, 'NotKnown', 'NotKnown');
UPDATE EXPERIMENT SET ARTEFACT_ID=1;
ALTER TABLE EXPERIMENT MODIFY ARTEFACT_ID NOT NULL;

/* pridani default group */
INSERT INTO SUBJECT_GROUP (SUBJECT_GROUP_ID, "TITLE", "DESCRIPTION") VALUES (1, 'Default', 'Default');
UPDATE EXPERIMENT SET SUBJECT_GROUP_ID=1;
ALTER TABLE EXPERIMENT MODIFY SUBJECT_GROUP_ID NOT NULL;

/* pridani electrode_conf */
INSERT INTO ELECTRODE_CONF (ELECTRODE_CONF_ID, IMPEDANCE, ELECTRODE_LOCATION_ID, ELECTRODE_SYSTEM_ID) VALUES (1, -1, 1, 1);
INSERT INTO ELECTRODE_LOCATION (ELECTRODE_LOCATION_ID, "TITLE", SHORTCUT, "DESCRIPTION", ELECTRODE_TYPE_ID, ELECTRODE_FIX_ID, IS_DEFAULT) VALUES (1, 'None', 'None', 'None', 1, 1, 0);
INSERT INTO ELECTRODE_LOCATION_REL (ELECTRODE_LOCATION_ID, ELECTRODE_CONF_ID) VALUES (1, 1);
INSERT INTO ELECTRODE_TYPE (ELECTRODE_TYPE_ID, "TITLE", "DESCRIPTION",IS_DEFAULT) VALUES (1, 'None', 'None',0);
INSERT INTO ELECTRODE_FIX (ELECTRODE_FIX_ID, "TITLE", "DESCRIPTION",IS_DEFAULT) VALUES (1, 'None', 'None',0);
INSERT INTO ELECTRODE_SYSTEM (ELECTRODE_SYSTEM_ID, "TITLE", "DESCRIPTION", IS_DEFAULT) VALUES (1, 'None', 'None',0);
UPDATE EXPERIMENT SET ELECTRODE_CONF_ID=1;
ALTER TABLE EXPERIMENT MODIFY ELECTRODE_CONF_ID NOT NULL;

/* prevod sampling_rate */
CREATE SEQUENCE DIGITIZATION_ID_SEQ MINVALUE 1 MAXVALUE 999999999999999999999999999 START WITH 1 INCREMENT BY 1 CACHE 20;
ALTER TABLE DIGITIZATION MODIFY GAIN FLOAT NULL;
ALTER TABLE DIGITIZATION MODIFY "FILTER" VARCHAR2(300) NULL;
ALTER TABLE DIGITIZATION MODIFY SAMPLING_RATE FLOAT NULL;
CREATE OR REPLACE TRIGGER test_trigger BEFORE INSERT ON DIGITIZATION REFERENCING NEW AS NEW FOR EACH ROW
BEGIN
SELECT DIGITIZATION_ID_SEQ.nextval INTO :NEW.DIGITIZATION_ID FROM dual;
END;
/
INSERT INTO DIGITIZATION(SAMPLING_RATE) VALUES (-1);
INSERT INTO DIGITIZATION(SAMPLING_RATE) SELECT DISTINCT SAMPLING_RATE FROM DATA_FILE; 
UPDATE DIGITIZATION SET GAIN=-1, "FILTER"='NotKnown';
DECLARE
  CURSOR experiment_cur IS SELECT * FROM experiment;
  experiment_row EXPERIMENT%ROWTYPE;
  sampling_rate_type DATA_FILE.SAMPLING_RATE%TYPE;
  sr_count INTEGER;
BEGIN
  OPEN experiment_cur;
  LOOP  
    FETCH experiment_cur INTO experiment_row;
    EXIT WHEN experiment_cur%NOTFOUND;
    SELECT count(d.SAMPLING_RATE) INTO sr_count FROM DATA_FILE d WHERE d.EXPERIMENT_ID=experiment_row.EXPERIMENT_ID AND ROWNUM <= 1;
    if sr_count > 0 then
      SELECT d.SAMPLING_RATE INTO sampling_rate_type FROM DATA_FILE d WHERE d.EXPERIMENT_ID=experiment_row.EXPERIMENT_ID AND ROWNUM <= 1;
      UPDATE EXPERIMENT e SET DIGITIZATION_ID=(SELECT DIGITIZATION_ID FROM digitization dig WHERE dig.SAMPLING_RATE=sampling_rate_type) WHERE e.experiment_id=experiment_row.EXPERIMENT_ID; 
    else
      UPDATE EXPERIMENT e SET DIGITIZATION_ID=(SELECT digitization_id from digitization where sampling_rate=-1) WHERE e.experiment_id=experiment_row.EXPERIMENT_ID;
    end if;
  END LOOP;
END;
/
ALTER TABLE DIGITIZATION MODIFY GAIN FLOAT NOT NULL;
ALTER TABLE DIGITIZATION MODIFY "FILTER" VARCHAR2(300) NOT NULL;
ALTER TABLE DIGITIZATION MODIFY SAMPLING_RATE FLOAT NOT NULL;  
ALTER TABLE EXPERIMENT MODIFY DIGITIZATION_ID NOT NULL;
DROP SEQUENCE DIGITIZATION_ID_SEQ;
DROP TRIGGER test_trigger;
ALTER TABLE DATA_FILE DROP COLUMN SAMPLING_RATE; 

/* DISEASE */
CREATE SEQUENCE DISEASE_ID_SEQ MINVALUE 1 MAXVALUE 999999999999999999999999999 START WITH 1 INCREMENT BY 1 CACHE 20;
CREATE OR REPLACE TRIGGER test_trigger BEFORE INSERT ON DISEASE REFERENCING NEW AS NEW FOR EACH ROW 
BEGIN 
	SELECT DISEASE_ID_SEQ.nextval INTO :NEW.DISEASE_ID FROM dual; 
END;
/

INSERT INTO DISEASE("TITLE", "DESCRIPTION") SELECT DISTINCT DESCRIPTION, DESCRIPTION FROM VISUAL_IMPAIRMENT;
INSERT INTO DISEASE("TITLE", "DESCRIPTION") SELECT DISTINCT DESCRIPTION, DESCRIPTION FROM HEARING_IMPAIRMENT;

DELETE FROM disease A WHERE ROWID > (
     SELECT min(rowid) FROM disease B
     WHERE A.title = B.title);

DECLARE
  CURSOR person_cur IS SELECT * FROM person;
  cursor hearing_rel_cur (p_id in integer) is SELECT * from hearing_impairment_rel hr where hr.person_id=p_id;
  cursor visual_rel_cur (p_id in integer) is SELECT * from visual_impairment_rel vr where vr.person_id=p_id;
  cursor experiment_cur (p_id in integer) is SELECT experiment_id from experiment where subject_person_id=p_id;
  person_row PERSON%ROWTYPE;
  hearing_rel_row HEARING_IMPAIRMENT_REL%ROWTYPE;
  visual_rel_row VISUAL_IMPAIRMENT_REL%ROWTYPE;
  visual_imp_row VISUAL_IMPAIRMENT%ROWTYPE;
  hearing_imp_row HEARING_IMPAIRMENT%ROWTYPE;
  disease_id_type DISEASE.DISEASE_ID%TYPE;
  experiment_id_type EXPERIMENT.EXPERIMENT_ID%TYPE;
  tmp integer;
BEGIN
  OPEN person_cur;
  LOOP  
    FETCH person_cur INTO person_row;
    EXIT WHEN person_cur%NOTFOUND;
    OPEN hearing_rel_cur (person_row.person_id);
    LOOP
      fetch hearing_rel_cur into hearing_rel_row;
      EXIT WHEN hearing_rel_cur%NOTFOUND;
      SELECT * into hearing_imp_row from hearing_impairment where hearing_impairment_id=hearing_rel_row.hearing_impairment_id;
      SELECT d.DISEASE_ID into disease_id_type from disease d where d.title=hearing_imp_row.description;
      open experiment_cur (person_row.person_id);
      loop
        fetch experiment_cur into experiment_id_type;
        EXIT WHEN experiment_cur%NOTFOUND;
        INSERT into disease_rel(disease_id, experiment_id) VALUES (disease_id_type, experiment_id_type);
      end loop;
      close experiment_cur;
    end loop;
    close hearing_rel_cur;
    
    OPEN visual_rel_cur (person_row.person_id);
    loop
      fetch visual_rel_cur into visual_rel_row;
      EXIT WHEN visual_rel_cur%NOTFOUND;
      SELECT * into visual_imp_row from visual_impairment where visual_impairment_id=visual_rel_row.visual_impairment_id;
      SELECT d.DISEASE_ID into disease_id_type from disease d where d.title=visual_imp_row.description;
      open experiment_cur (person_row.person_id);
      loop
        fetch experiment_cur into experiment_id_type;
        EXIT WHEN experiment_cur%NOTFOUND;
        select count(*) into tmp from disease_rel where disease_id=disease_id_type and experiment_id=experiment_id_type;
        if tmp = 0 then
          INSERT into disease_rel(disease_id, experiment_id) VALUES (disease_id_type, experiment_id_type);
        end if;
      end loop;
      close experiment_cur;
    end loop;
    close visual_rel_cur;
    
  END LOOP;
  close person_cur;
END;
/

drop table visual_impairment_rel;
drop table visual_impairment_group_rel;
drop table hearing_impairment_rel;
drop table hearing_impairment_group_rel;
drop table visual_impairment cascade constraints;
drop table hearing_impairment cascade constraints;
drop table visual_impairment_group_rel cascade constraints;
drop table hearing_impairment_group_rel cascade constraints;
DROP SEQUENCE DISEASE_ID_SEQ;
drop trigger test_trigger;



