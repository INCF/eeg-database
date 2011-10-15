create table sync_changes(
table_name varchar(40) not null,
last_change timestamp default SYSTIMESTAMP not null
);

insert into sync_changes values('person', SYSTIMESTAMP);
insert into sync_changes values('scenario', SYSTIMESTAMP);
insert into sync_changes values('experiment', SYSTIMESTAMP);
insert into sync_changes values('research_group', SYSTIMESTAMP);
insert into sync_changes values('data_file', SYSTIMESTAMP);
insert into sync_changes values('weather', SYSTIMESTAMP);

create or replace trigger person_insert
after insert
on person
begin
  update sync_changes set last_change = SYSTIMESTAMP where table_name='person';
end person_insert;

create or replace trigger person_update
after update
on person
begin
  update sync_changes set last_change = SYSTIMESTAMP where table_name='person';
end person_update;

create or replace trigger person_delete
after delete
on person
begin
  update sync_changes set last_change = SYSTIMESTAMP where table_name='person';
end person_delete;

create or replace trigger scenario_insert
after insert
on scenario
begin
  update sync_changes set last_change = SYSTIMESTAMP where table_name='scenario';
end scenario_insert;

create or replace trigger scenario_update
after update
on scenario
begin
  update sync_changes set last_change = SYSTIMESTAMP where table_name='scenario';
end scenario_update;

create or replace trigger scenario_delete
after delete
on scenario
begin
  update sync_changes set last_change = SYSTIMESTAMP where table_name='scenario';
end scenario_delete;

create or replace trigger experiment_insert
after insert
on experiment
begin
  update sync_changes set last_change = SYSTIMESTAMP where table_name='experiment';
end experiment_insert;

create or replace trigger experiment_update
after update
on experiment
begin
  update sync_changes set last_change = SYSTIMESTAMP where table_name='experiment';
end experiment_update;

create or replace trigger experiment_delete
after delete
on experiment
begin
  update sync_changes set last_change = SYSTIMESTAMP where table_name='experiment';
end experiment_delete;

create or replace trigger research_group_insert
after insert
on research_group
begin
  update sync_changes set last_change = SYSTIMESTAMP where table_name='research_group';
end research_group_insert;

create or replace trigger research_group_update
after update
on research_group
begin
  update sync_changes set last_change = SYSTIMESTAMP where table_name='research_group';
end research_group_update;

create or replace trigger research_group_delete
after delete
on research_group
begin
  update sync_changes set last_change = SYSTIMESTAMP where table_name='research_group';
end research_group_delete;

create or replace trigger data_file_insert
after insert
on data_file
begin
  update sync_changes set last_change = SYSTIMESTAMP where table_name='data_file';
end data_file_insert;

create or replace trigger data_file_update
after update
on data_file
begin
  update sync_changes set last_change = SYSTIMESTAMP where table_name='data_file';
end data_file_update;

create or replace trigger data_file_delete
after delete
on data_file
begin
  update sync_changes set last_change = SYSTIMESTAMP where table_name='data_file';
end data_file_delete;

create or replace trigger weather_insert
after insert
on weather
begin
  update sync_changes set last_change = SYSTIMESTAMP where table_name='weather';
end weather_insert;

create or replace trigger weather_update
after update
on weather
begin
  update sync_changes set last_change = SYSTIMESTAMP where table_name='weather';
end weather_update;

create or replace trigger weather_delete
after delete
on weather
begin
  update sync_changes set last_change = SYSTIMESTAMP where table_name='weather';
end weather_delete;